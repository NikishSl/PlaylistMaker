package com.practicum.playlistmaker.search.presentation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.Track
import com.practicum.playlistmaker.search.data.SearchHistoryManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var recycler: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var noResultsFrame: FrameLayout
    private lateinit var connectTrouble: FrameLayout
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    private val searchHistoryManager: SearchHistoryManager by inject()

    private var searchText: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { viewModel.searchTracks(searchText) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchHistoryLayout = view.findViewById(R.id.search_history_ll)
        progressBar = view.findViewById(R.id.progressBar)
        noResultsFrame = view.findViewById(R.id.search_frame_nothing)
        connectTrouble = view.findViewById(R.id.connect_trouble)

        initRecycler(view)
        initHistoryRecycler(view)
        setupUI(view)
        setupObservers()

        updateHistoryVisibility(searchText.isEmpty())
    }

    private fun setupUI(view: View) {
        val inputSearchText = view.findViewById<EditText>(R.id.input_search_text)
        val searchClearButton = view.findViewById<ImageView>(R.id.clear_icon)
        val updateButton = view.findViewById<Button>(R.id.search_update_bt)
        val clearHistoryButton = view.findViewById<Button>(R.id.clear_history_button)

        inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchText = inputSearchText.text.toString()
                viewModel.searchTracks(searchText)
                hideKeyboard()
                true
            } else {
                false
            }
        }

        inputSearchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                searchClearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                searchDebounce()
                updateHistoryVisibility(searchText.isEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        inputSearchText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                updateHistoryVisibility(searchText.isEmpty())
            } else {
                searchHistoryLayout.visibility = View.GONE
            }
        }

        searchClearButton.setOnClickListener {
            inputSearchText.setText("")
            hideKeyboard()
            updateHistoryVisibility(true)
        }

        clearHistoryButton.setOnClickListener {
            searchHistoryManager.clearSearchHistory()
            updateHistoryVisibility(true)
        }

        updateButton.setOnClickListener {
            viewModel.searchTracks(searchText)
        }
    }

    private fun setupObservers() {
        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            if (tracks.isNotEmpty()) {
                updateSearchResults(tracks)
                noResultsFrame.visibility = View.GONE
            } else {
                recycler.visibility = View.GONE
                noResultsFrame.visibility = if (searchText.isNotEmpty()) View.VISIBLE else View.GONE
            }
            connectTrouble.visibility = View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun initRecycler(view: View) {
        recycler = view.findViewById(R.id.search_recycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initHistoryRecycler(view: View) {
        historyRecyclerView = view.findViewById(R.id.history_recycler_view)
        historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val searchHistory = searchHistoryManager.getSearchHistory().toMutableList()
        val historyAdapter = RecyclerSearchAdapter(requireContext(), searchHistory, searchHistoryManager)
        historyRecyclerView.adapter = historyAdapter
        searchHistoryManager.historyAdapter = historyAdapter
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        requireActivity().currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    private fun updateHistoryVisibility(isEmptySearchText: Boolean) {
        searchHistoryLayout.visibility = if (isEmptySearchText && searchHistoryManager.getSearchHistory().isNotEmpty())
            View.VISIBLE
        else
            View.GONE
    }

    private fun updateSearchResults(tracks: List<Track>) {
        val adapter = RecyclerSearchAdapter(requireContext(), tracks.toMutableList(), searchHistoryManager)
        recycler.adapter = adapter
        recycler.visibility = View.VISIBLE
    }
}