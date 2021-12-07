package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import java.lang.RuntimeException
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BookListFragment : Fragment() {
    private var books: BookList? = null
    var parentActivity: BookSelectedInterface? = null
    val recycle: RecyclerView? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)

        /*
         This fragment needs to communicate with its parent activity
         so we verify that the activity implemented our defined interface
         */parentActivity = if (context is BookSelectedInterface) {
            context
        } else {
            throw RuntimeException("Please implement the required interface(s)")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            books = requireArguments().getParcelable(BOOK_LIST_KEY)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val recycle = view.findViewById<RecyclerView>(R.id.rcvFragView)
        val size = books?.size()
        Log.d("SIZE", size.toString())
        if (size != null){
            recycle.adapter = BookAdapter(books!!) {
                parentActivity!!.bookSelected(it)
            }
            recycle.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }

    }

    public interface BookSelectedInterface {
        fun bookSelected(book: Book)
    }

    companion object {
        private const val BOOK_LIST_KEY = "booklist"
        @JvmStatic
        fun newInstance(books: BookList?): BookListFragment {
            val fragment = BookListFragment()
            val args = Bundle()
            args.putParcelable(BOOK_LIST_KEY, books)
            fragment.arguments = args
            return fragment
        }
    }

}