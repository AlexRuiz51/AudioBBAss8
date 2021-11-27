package edu.temple.audiobb
import java.io.Serializable
object BookList : Serializable{
    private val bookList : MutableList<BookModel> by lazy {
        ArrayList()
    }

    fun add(book: BookModel) {
        bookList.add(book)
    }

    fun addLibrary(library: List<BookModel>){
        for(book in library){
            add(book)
        }
    }

    fun remove(book: BookModel){
        bookList.remove(book)
    }

    fun clear(){
        bookList.clear()
    }

    operator fun get(index: Int) = bookList[index]

    fun size() = bookList.size



}