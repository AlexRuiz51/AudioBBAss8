package edu.temple.audiobb
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class BookListAdapter(_library :BookList, _onClick: (BookModel) -> Unit): RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {
    private val library = _library
    private val onClick = _onClick

    class BookViewHolder(itemView: View, onClick: (BookModel) -> Unit):RecyclerView.ViewHolder(itemView){
        val titleTextView : TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        lateinit var book: BookModel
        init {
            titleTextView.setOnClickListener {
                onClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.library_item, parent, false),
            onClick
        )
    }

    override fun getItemCount(): Int {
        return library.size()
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.titleTextView.text = library[position].title
        holder.authorTextView.text = library[position].author
        holder.book = library[position]
    }
}