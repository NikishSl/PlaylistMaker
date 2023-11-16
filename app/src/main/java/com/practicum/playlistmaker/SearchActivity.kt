package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.searchRecyclerPack.ITunesApiService
import com.practicum.playlistmaker.searchRecyclerPack.RecyclerSearchAdapter
import com.practicum.playlistmaker.searchRecyclerPack.TrackResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_TEXT_KEY = "searchText"
        private const val BASE_URL = "https://itunes.apple.com"
    }

    private var searchText: String = ""
    private lateinit var recycler: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var noResultsFrame: FrameLayout
    private lateinit var connectTrouble: FrameLayout
    private lateinit var search_history_ll: LinearLayout

    private lateinit var searchHistoryManager: SearchHistoryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchHistoryManager = SearchHistoryManager(this)
        search_history_ll = findViewById(R.id.search_history_ll)

        initRecycler()
        initHistoryRecycler()

        val searchBackButton = findViewById<ImageButton>(R.id.search_back_button)
        val inputSearchText = findViewById<EditText>(R.id.input_search_text)
        val searchClearButton = findViewById<ImageView>(R.id.clear_icon)
        val updateButton = findViewById<Button>(R.id.search_update_bt)
        val clearHistoryButton = findViewById<Button>(R.id.clear_history_button)

        noResultsFrame = findViewById(R.id.search_frame_nothing)
        connectTrouble = findViewById(R.id.connect_trouble)

        inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = inputSearchText.text.toString()
                searchTracks(searchText)
                hideKeyboard()
                true
            } else {
                false
            }
        }

        inputSearchText.setOnFocusChangeListener{ _, hasFocus ->
            if (hasFocus && searchText.isEmpty()) {
                search_history_ll.visibility = if (searchHistoryManager.getSearchHistory().isNotEmpty()) View.VISIBLE else View.GONE
            } else {
                search_history_ll.visibility = View.GONE
            }
        }

        searchClearButton.setOnClickListener {
            inputSearchText.setText("")
            hideKeyboard()
        }

        clearHistoryButton.setOnClickListener {
            searchHistoryManager.clearSearchHistory()
            search_history_ll.visibility = View.GONE
        }

        updateButton.setOnClickListener {
            searchTracks(searchText)
        }

        searchBackButton.setOnClickListener {
            finish()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                searchClearButton.visibility = clearButtonVisibility(s)
                searchTracks(searchText)
                if (searchText.isEmpty()) {
                    search_history_ll.visibility = if (searchHistoryManager.getSearchHistory().isNotEmpty()) View.VISIBLE else View.GONE
                } else {
                    search_history_ll.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputSearchText.addTextChangedListener(simpleTextWatcher)
    }

    private fun initRecycler() {
        recycler = findViewById(R.id.search_recycler)
        recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun initHistoryRecycler() {
        historyRecyclerView = findViewById(R.id.history_recycler_view)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        val searchHistory = searchHistoryManager.getSearchHistory().toMutableList() // Преобразование в MutableList
        val historyAdapter = RecyclerSearchAdapter(this, searchHistory, searchHistoryManager)
        historyRecyclerView.adapter = historyAdapter
        searchHistoryManager.historyAdapter = historyAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputSearchText = findViewById<EditText>(R.id.input_search_text)
        searchText = savedInstanceState.getString(SEARCH_TEXT_KEY, "")
        inputSearchText.setText(searchText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun searchTracks(searchText: String) {
        if (searchText.isEmpty()) {
            recycler.visibility = View.GONE
            noResultsFrame.visibility = View.GONE
            connectTrouble.visibility = View.GONE
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val iTunesApiService = retrofit.create(ITunesApiService::class.java)
        val call = iTunesApiService.search(searchText)

        call.enqueue(object : Callback<TrackResult> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TrackResult>, response: Response<TrackResult>) {
                if (response.isSuccessful) {
                    val trackResult = response.body()
                    if (trackResult != null) {
                        val tracks = trackResult.results.toMutableList()
                        if (tracks.isNotEmpty()) {
                            val adapter = RecyclerSearchAdapter(this@SearchActivity, tracks, searchHistoryManager)
                            recycler.adapter = adapter
                            recycler.visibility = View.VISIBLE
                            noResultsFrame.visibility = View.GONE
                            connectTrouble.visibility = View.GONE
                            adapter.notifyDataSetChanged()
                        } else {
                            recycler.visibility = View.GONE
                            noResultsFrame.visibility = View.VISIBLE
                            connectTrouble.visibility = View.GONE
                            search_history_ll.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onFailure(call: Call<TrackResult>, t: Throwable) {
                recycler.visibility = View.GONE
                noResultsFrame.visibility = View.GONE
                connectTrouble.visibility = View.VISIBLE
                search_history_ll.visibility = View.GONE
            }
        })
    }
}
