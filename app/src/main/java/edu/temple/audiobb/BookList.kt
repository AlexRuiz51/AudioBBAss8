package edu.temple.audiobb

import android.os.Parcelable
import java.util.ArrayList
import android.os.Parcel
import android.os.Parcelable.Creator

class BookList : Parcelable {
    private var books: ArrayList<Book?>?

    constructor() {
        books = ArrayList()
    }

    constructor(`in`: Parcel) {
        books = `in`.createTypedArrayList(Book.CREATOR)
    }

    fun clear() {
        books!!.clear()
    }

    fun addAll(books: BookList) {
        for (i in 0 until books.size()) {
            this.books!!.add(books[i])
        }
    }

    fun add(book: Book) {
        books!!.add(book)
    }

    operator fun get(position: Int): Book? {
        return books!![position]
    }

    fun size(): Int {
        return books!!.size
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeTypedList(books)
    }


    companion object CREATOR : Creator<BookList> {
        override fun createFromParcel(parcel: Parcel): BookList {
            return BookList(parcel)
        }

        override fun newArray(size: Int): Array<BookList?> {
            return arrayOfNulls(size)
        }
    }

}