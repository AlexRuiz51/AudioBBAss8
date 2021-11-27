package edu.temple.audiobb
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.squareup.picasso.Picasso
class BookDetailsFragment : Fragment() {

    private lateinit var bookTitle: TextView
    private lateinit var bookAuthor: TextView
    private lateinit var bookCover: ImageView
    private val viewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_book_details, container, false)

        bookTitle = view.findViewById(R.id.titleTextView2)
        bookAuthor = view.findViewById(R.id.authorTextView2)
        bookCover = view.findViewById(R.id.bookCoverImageView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSelectedBook().observe(requireActivity(), {updateBook(it)})
    }

    private fun updateBook(book:BookModel?){
        book?.run {
            bookTitle.text = title
            bookAuthor.text = author
            Picasso.get().load(cover_url).into(bookCover)
        }
    }
}