package edu.temple.audiobb

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.SeekBar


private const val PLAYER = "player"

/**
 * A simple [Fragment] subclass.
 * Use the [AudioPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AudioPlayerFragment : Fragment() {

    var book: Book? = null
    lateinit var nowPlaying: TextView
    lateinit var play:Button
    lateinit var pause:Button
    lateinit var stop:Button
    lateinit var bar: SeekBar
    var playerInterface: PlayerFragmentInterface? = null



    fun playerFragment() {
        // Required empty public constructor
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            book = requireArguments().getParcelable(PLAYER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(R.layout.fragment_audio_player,container,false)
        pause = v.findViewById<Button>(R.id.pauseButton)
        play = v.findViewById<Button>(R.id.playButton)
        stop = v.findViewById<Button>(R.id.stopButton)
        bar = v.findViewById(R.id.seekBar)
        nowPlaying = v.findViewById<TextView>(R.id.nowPlaying)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        play.setOnClickListener(View.OnClickListener {
            playerInterface!!.play()
            if (book != null) {
                nowPlaying.text = ("Now Playing:" + book!!.title)
                bar.max = book!!.duration
            }

        })
        pause.setOnClickListener(View.OnClickListener { playerInterface!!.pause()

        })
        stop.setOnClickListener(View.OnClickListener { playerInterface!!.stop() })

        bar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                playerInterface!!.seekbarChange(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            }
        )

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerInterface = if (context is PlayerFragmentInterface) {
            context
        } else {
            throw RuntimeException("Implement playerFragmentInterface")
        }
    }

    interface PlayerFragmentInterface {
        fun play()
        fun pause()
        fun stop()
        fun seekbarChange(p:Int)
    }
    companion object {
        fun newInstance(book: Book?): AudioPlayerFragment {
            val fragment = AudioPlayerFragment()
            val args = Bundle()
            args.putParcelable(PLAYER, book)
            fragment.arguments = args
            return fragment
        }
    }
}


