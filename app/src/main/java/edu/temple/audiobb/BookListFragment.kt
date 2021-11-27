package edu.temple.audiobb
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
private const val BOOKS = "BOOKS"
class BookListFragment : Fragment() {

    private val viewModel: BookViewModel by activityViewModels()

    interface BookSelectedInterface {
        fun bookSelected()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onClick : (BookModel) -> Unit = {
            //Updating view model on book selection
            book: BookModel -> viewModel.setSelectedBook(book)
            //Informing activity to prevent replay of event when it restarts
            (activity as BookSelectedInterface).bookSelected()
        }
        val manager = LinearLayoutManager(activity)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView?.layoutManager = manager
        recyclerView?.adapter = BookListAdapter(BookList, onClick)
    }
}