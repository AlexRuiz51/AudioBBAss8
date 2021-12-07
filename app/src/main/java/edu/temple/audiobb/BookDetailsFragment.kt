package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


private const val BOOK_KEY = "book"


/**
 * A simple [Fragment] subclass.
 * Use the [BookDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookDetailsFragment : Fragment() {
    private var book: Book? = null
    var titleTextView: TextView? = null
    var authorTextView: TextView? = null
    var coverImageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            book = requireArguments().getParcelable(BOOK_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_book_details, container, false)
        titleTextView = v.findViewById<TextView>(R.id.titleTextView)
        authorTextView = v.findViewById<TextView>(R.id.authorTextView)
        coverImageView = v.findViewById<ImageView>(R.id.imgBook)


         if (book != null)
         {
             displayBook(book!!)
         }

        return v
    }

    /*
    This method is used both internally and externally (from the activity)
    to display a book
     */
    fun displayBook(book: Book) {
        titleTextView!!.text = book.title
        authorTextView!!.text = book.author
        Picasso.get().load((book.coverUrl)).into(coverImageView)
    }

    companion object {
        fun newInstance(book: Book?): BookDetailsFragment {
            val fragment = BookDetailsFragment()
            val args = Bundle()

            args.putParcelable(BOOK_KEY, book)
            fragment.arguments = args
            return fragment
        }
    }
}