package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBackButton = findViewById<ImageButton>(R.id.search_back_button)
        val inputSearchText = findViewById<EditText>(R.id.input_search_text)
        val searchClearButton = findViewById<ImageView>(R.id.clear_icon)

        searchClearButton.setOnClickListener {
            inputSearchText.setText("")
            hideKeyboard()
        }

        searchBackButton.setOnClickListener {
            finish()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inputSearchText.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputSearchText = findViewById<EditText>(R.id.input_search_text)
        val searchText = inputSearchText.text.toString()
        outState.putString("searchText", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputSearchText = findViewById<EditText>(R.id.input_search_text)
        val searchText = savedInstanceState.getString("searchText", "")
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
        val hideK = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideK.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

}
