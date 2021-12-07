package edu.temple.audiobb


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class BookSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)
        val urlEditText = findViewById<EditText>(R.id.searchEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val progressBar = findViewById<ContentLoadingProgressBar>(R.id.progressBar)
        val requestQueue = Volley.newRequestQueue(this)
        progressBar.hide()
        searchButton.setOnClickListener(View.OnClickListener {
            progressBar.show()
            val url = "https://kamorris.com/lab/cis3515/search.php?term=" + urlEditText.text.toString()
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    progressBar.hide()
                    val intent = Intent()
                    val returnBook: BookList = jsonToBookList(response)
                    Log.i(
                        "Search Activity",
                        "new book object with url: " + returnBook[0]?.author
                    )
                    intent.putExtra("result", response.toString())
                    intent.putExtra("SearchBooks",returnBook)

                    setResult(RESULT_OK, intent)
                    finish()

                }
            ) { error ->
                Toast.makeText(
                    this@BookSearchActivity,
                    "ERROR" + error.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
            requestQueue.add(jsonArrayRequest)
        })
        cancelButton.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        })

    }
    private fun jsonToBookList(jsonArray: JSONArray): BookList {
        val bookList = BookList()
        var tempBook: JSONObject
        for (i in 0 until jsonArray.length()) {
            try {
                tempBook = jsonArray.getJSONObject(i)
                val temp = Book(
                    tempBook.getInt("id"),
                    tempBook.getString("title"),
                    tempBook.getString("author"),
                    tempBook.getString("cover_url"),
                    tempBook.getInt("duration")
                )
                bookList.add(temp)
                Log.i(
                    "Search Activity, json to book list",
                    "book info from json:" + " id: " + temp.id + "; title: " + temp.title+ "; author: " + temp.author + "; url: " + temp.coverUrl
                    )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return bookList
    }
}