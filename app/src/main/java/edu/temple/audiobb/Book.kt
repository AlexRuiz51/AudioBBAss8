package edu.temple.audiobb
import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator

class Book : Parcelable {
    var id: Int
    var title: String?
    var author: String?
    var coverUrl: String?
    var duration: Int

    constructor(id: Int, title: String?, author: String?, coverUrl: String?,duration:Int) {
        this.id = id
        this.title = title
        this.author = author
        this.coverUrl = coverUrl
        this.duration = duration

    }

    protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        title = `in`.readString()
        author = `in`.readString()
        coverUrl = `in`.readString()
        duration = `in`.readInt()

    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(coverUrl)
        parcel.writeInt(duration)

    }
    override fun toString(): String {
        return "Book ID: "+this.id + " Title: "+this.title + " Author: "+this.author
    }

    companion object {
        @JvmField
        val CREATOR: Creator<Book?> = object : Creator<Book?> {
            override fun createFromParcel(`in`: Parcel): Book? {
                return Book(`in`)
            }

            override fun newArray(size: Int): Array<Book?> {
                return arrayOfNulls(size)
            }
        }
    }
}