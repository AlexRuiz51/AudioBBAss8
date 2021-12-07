package edu.temple.audiobb

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import edu.temple.audiobb.BookListFragment.BookSelectedInterface
import edu.temple.audiobb.AudioPlayerFragment.PlayerFragmentInterface
import edu.temple.audiobb.BookListFragment.Companion.newInstance
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.*
import android.widget.SeekBar
import edu.temple.audlibplayer.PlayerService


class MainActivity : AppCompatActivity(), BookSelectedInterface, PlayerFragmentInterface
{

    lateinit var binder: PlayerService.MediaControlBinder
    lateinit  var fm: FragmentManager
    lateinit var seekBar: SeekBar
    var twoPane = false
    var bookDetailsFragment: BookDetailsFragment? = null
    var audioPlayerFragment: AudioPlayerFragment? = null
    var selectedBook: Book? = null
    var bookListData: BookList? = null
    var currentProgress: Int =0
    var connect= false


    private val KEY_SELECTED_BOOK = "selectedBook"
    private val KEY_BOOK_LIST = "bookList"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Bind Service
        bindService(Intent(this, PlayerService::class.java), serviceConnection, BIND_AUTO_CREATE)

        //bookListData = testBooks
        findViewById<Button>(R.id.buttonSecondActivity).setOnClickListener {
            openSomeActivityForResult()
        }

        //Fetch selected book if there was one
        if (savedInstanceState != null) {
            selectedBook = savedInstanceState.getParcelable(KEY_SELECTED_BOOK)
            bookListData = savedInstanceState.getParcelable(KEY_BOOK_LIST)
            twoPane = true

        }

        //Checking landscape
        twoPane = findViewById<View?>(R.id.container2) != null

        fm = supportFragmentManager
        val fragment1: Fragment? = fm.findFragmentById(R.id.container1)
        fm.beginTransaction()
            .replace(R.id.container3, AudioPlayerFragment())
            .commit()

        //BookListFragment be displayed in fragment container 1
        if (fragment1 is BookDetailsFragment) {
            fm.popBackStack()
        } else if (fragment1 !is BookListFragment) {
            fm.beginTransaction()
                .add(R.id.container1, newInstance(bookListData))
                .commit()
        }

        bookDetailsFragment =
            if (selectedBook == null)
                BookDetailsFragment()
            else
                BookDetailsFragment.newInstance(selectedBook)


        audioPlayerFragment =
            if (selectedBook == null)
                AudioPlayerFragment()
            else
                AudioPlayerFragment.newInstance(selectedBook)



        //Rotation
        if (twoPane) {
            fm.beginTransaction()
                .replace(R.id.container2, bookDetailsFragment!!)
                .replace(R.id.container3,audioPlayerFragment!!)
                .commit()
        } else if (selectedBook != null) {
            fm.beginTransaction()
                .replace(R.id.container1, bookDetailsFragment!!)
                .replace(R.id.container3,audioPlayerFragment!!)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_SELECTED_BOOK, selectedBook)
        outState.putParcelable(KEY_BOOK_LIST, bookListData)
    }

    override fun onBackPressed() {
        // If the user hits the back button, clear the selected book
        selectedBook = null
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> if (result.resultCode == Activity.RESULT_OK)
        { val data: Intent? = result.data
            val books = data?.getParcelableExtra<Parcelable>("SearchBooks") as BookList
            Log.i(
                "MainACTIVITY, Working",
                "new book object with url: " + books[0]?.author
            )
            bookListData = books
            if (bookListData != null) {
                Log.i(
                    "MainACTIVITY, Working",
                    "new book object with url: " + bookListData?.get(0)?.author
                )
                fm = supportFragmentManager
                fm.beginTransaction()
                    .replace(R.id.container1, newInstance(bookListData))
                    .commit()
            }
            if (twoPane) {
                fm.beginTransaction()
                    .replace(R.id.container2, bookDetailsFragment!!)
                    .replace(R.id.container3,audioPlayerFragment!!)
                    .replace(R.id.container1, newInstance(bookListData))
                    .commit()
            }
        }
    }


   //Generate a list of "books" for testing
    private val testBooks: BookList
        get() {
            val books = BookList()
            for (i in 1..10) {
                books.add(Book(i, "Book$i", "Author$i", "url$i", i));
            }
            return books
        }




    private fun openSomeActivityForResult() {
        val intent = Intent(this, BookSearchActivity::class.java)
        resultLauncher.launch(intent)
    }

    val progressHandler = Handler(Looper.getMainLooper()){

            if(it.obj != null) {
                val bookProgress = it.obj as PlayerService.BookProgress

                currentProgress = bookProgress.progress
                Log.i(
                    "PROGRESS HANDLER",
                    "progress time ?  $currentProgress",
                )
                seekBar = findViewById(R.id.seekBar)
                seekBar.progress = currentProgress

            }
            true
    }

    val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            connect = true
            binder = service as PlayerService.MediaControlBinder
            binder.setProgressHandler(progressHandler)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            connect = false
        }

    }

    override fun bookSelected(book: Book) {
        selectedBook = book
        if (twoPane){
            bookDetailsFragment!!.displayBook(selectedBook!!)
            audioPlayerFragment!!.book = selectedBook

        }
        else {
            //Display book using new fragment
            fm.beginTransaction()
                .replace(R.id.container1, BookDetailsFragment.newInstance(selectedBook))
                .replace(R.id.container3, AudioPlayerFragment.newInstance(selectedBook))
                .addToBackStack(null)
                .commit()
        }
        Log.i(
            "Main Activity-bookSelected",
            "Book Selected:" + selectedBook?.title + ", duration: " + selectedBook?.duration
        )
    }


    override fun play() {
        var book = selectedBook
        if(book != null&&connect)

            binder.play(book.id)
            //binder.seekTo(currentProgress)
            Log.i("Main Activity-Play",
                "Book currently playing"+selectedBook.toString())
        }

    override fun pause() {
            if(connect && binder.isBinderAlive) {
                binder.pause()
            }
        }

    override fun stop() {
            if(connect && binder.isBinderAlive) {
                binder.stop()
            }
        }

    override fun seekbarChange(p:Int) {
        if(connect && binder.isBinderAlive) {
            binder.seekTo(p)

        }
    }
    }