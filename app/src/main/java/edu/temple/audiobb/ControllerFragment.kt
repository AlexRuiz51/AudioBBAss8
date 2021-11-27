package edu.temple.audiobb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

private const val ARG_PARAM1 = "param1"


class ControllerFragment : Fragment() {

    lateinit var nowPlayingText: TextView
    lateinit var playButton: Button
    lateinit var pauseButton: Button
    lateinit var stopButton: Button
    lateinit var seekBar: SeekBar
    lateinit var progressText: TextView
    var progressTime: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout =  inflater.inflate(R.layout.fragment_controller, container, false)

        nowPlayingText = layout.findViewById(R.id.nowPlayingText)
        playButton = layout.findViewById(R.id.playButton)
        pauseButton = layout.findViewById(R.id.pauseButton)
        stopButton = layout.findViewById(R.id.stopButton)
        seekBar = layout.findViewById(R.id.seekBar)
        progressText = layout.findViewById(R.id.progressText)

        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedBookViewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        playButton.setOnClickListener {

            val currentBook = selectedBookViewModel.getSelectedBook().value

            if (currentBook != null) {
                nowPlayingText.text = "Now Playing: " + currentBook.title
                Log.d("Playing:", currentBook.toString())
                Log.d("Duration:", currentBook.duration.toString())
            }
            (activity as ControlsClickedInterface).playClicked(progressTime)
        }

        pauseButton.setOnClickListener {

            val currentBook = selectedBookViewModel.getSelectedBook().value

            if (currentBook != null) {
                progressTime = (seekBar.progress * currentBook.duration / 100)
            }

            (activity as ControlsClickedInterface).pauseClicked()
        }

        stopButton.setOnClickListener { (activity as ControlsClickedInterface).stopClicked() }

        seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {

                val currentBook = selectedBookViewModel.getSelectedBook().value

                if (currentBook != null) {
                    progressText.text = (seekBar.progress * currentBook.duration / 100).toString()
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

                val currentBook = selectedBookViewModel.getSelectedBook().value

                if(currentBook != null){
                    progressTime = (seekBar.progress * currentBook.duration / 100)
                    progressText.text = progressTime.toString()

                    (activity as ControlsClickedInterface).playClicked(progressTime)
                }

            }
        })

    }

    interface ControlsClickedInterface {
        fun playClicked(progressTime: Int)
        fun pauseClicked()
        fun stopClicked()
        fun seekBarClicked()
    }
}