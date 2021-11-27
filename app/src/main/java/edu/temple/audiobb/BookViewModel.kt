package edu.temple.audiobb
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class BookViewModel : ViewModel(){
    private val book: MutableLiveData<BookModel> by lazy {
        MutableLiveData()
    }

    fun getSelectedBook(): LiveData<BookModel> {
        return book
    }

    fun setSelectedBook(selectedBook: BookModel?) {
        this.book.value = selectedBook
    }
}