package com.practicum.playlistmaker.search.presentation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.data.SearchHistoryManager

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var recycler: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var noResultsFrame: FrameLayout
    private lateinit var connectTrouble: FrameLayout
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var searchHistoryManager: SearchHistoryManager

    private var searchText: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { viewModel.searchTracks(searchText) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchHistoryManager = SearchHistoryManager(this)
        searchHistoryLayout = findViewById(R.id.search_history_ll)
        progressBar = findViewById(R.id.progressBar)
        noResultsFrame = findViewById(R.id.search_frame_nothing)
        connectTrouble = findViewById(R.id.connect_trouble)

        initRecycler()
        initHistoryRecycler()
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        val searchBackButton = findViewById<ImageButton>(R.id.search_back_button)
        val inputSearchText = findViewById<EditText>(R.id.input_search_text)
        val searchClearButton = findViewById<ImageView>(R.id.clear_icon)
        val updateButton = findViewById<Button>(R.id.search_update_bt)
        val clearHistoryButton = findViewById<Button>(R.id.clear_history_button)

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

        searchBackButton.setOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        viewModel.tracks.observe(this) { tracks ->
            if (tracks.isNotEmpty()) {
                updateSearchResults(tracks)
                noResultsFrame.visibility = View.GONE
            } else {
                recycler.visibility = View.GONE
                noResultsFrame.visibility = if (searchText.isNotEmpty()) View.VISIBLE else View.GONE
            }
            connectTrouble.visibility = View.GONE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun initRecycler() {
        recycler = findViewById(R.id.search_recycler)
        recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun initHistoryRecycler() {
        historyRecyclerView = findViewById(R.id.history_recycler_view)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        val searchHistory = searchHistoryManager.getSearchHistory().toMutableList()
        val historyAdapter = RecyclerSearchAdapter(this, searchHistory, searchHistoryManager)
        historyRecyclerView.adapter = historyAdapter
        searchHistoryManager.historyAdapter = historyAdapter
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun updateHistoryVisibility(isEmptySearchText: Boolean) {
        searchHistoryLayout.visibility = if (isEmptySearchText && searchHistoryManager.getSearchHistory().isNotEmpty())
            View.VISIBLE
        else
            View.GONE
    }

    private fun updateSearchResults(tracks: List<Track>) {
        val adapter = RecyclerSearchAdapter(this, tracks.toMutableList(), searchHistoryManager)
        recycler.adapter = adapter
        recycler.visibility = View.VISIBLE
    }
}
