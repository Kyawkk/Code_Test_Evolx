package com.kyawzinlinn.movieplayer

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.kyawzinlinn.movieplayer.databinding.ActivityPlayerBinding
import com.kyawzinlinn.movieplayer.utils.VIDEO_PATH_INTENT_EXTRA

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var player: SimpleExoPlayer? = null
    private var isFullScreen = false
    private lateinit var btnFullscreen: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnFullscreen = findViewById(R.id.bt_full_screen)

        initializePlayer()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        btnFullscreen.setOnClickListener { toggleFullScreen() }
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(this))
            .setLoadControl(DefaultLoadControl())
            .setTrackSelector(DefaultTrackSelector(this))
            .build()

        player?.addListener(getPlayerEventListener())

        val videoUri = intent?.extras?.getString(VIDEO_PATH_INTENT_EXTRA).toString()

        binding.playerView.player = player
        val mediaItem = MediaItem.fromUri(videoUri)
        player?.apply {
            setMediaItem(mediaItem)
            setAudioAttributes(getPlayerAudioAttributes(),true)
            volume = 0.5f
            prepare()
            play()
        }
    }

    private fun toggleFullScreen(){
        isFullScreen = !isFullScreen

        if(!isFullScreen){

            btnFullscreen.setImageResource(R.drawable.ic_exo_icon_full_screen_enter)

            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            supportActionBar?.show()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }else{
            btnFullscreen.setImageResource(R.drawable.ic_exo_icon_full_screen_exit)

            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            supportActionBar?.hide()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    private fun getPlayerEventListener(): Player.Listener{
        return object : Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

                when (playbackState){
                    Player.STATE_BUFFERING -> {
                        binding.loadingProgessBar.visibility = View.VISIBLE
                        binding.playbackErrorLayout.root.visibility = View.GONE
                    }
                    Player.STATE_READY -> {
                        binding.loadingProgessBar.visibility = View.GONE
                        binding.playbackErrorLayout.root.visibility = View.GONE
                    }
                    Player.STATE_ENDED -> {}
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                binding.playbackErrorLayout.root.visibility = View.VISIBLE
                binding.loadingProgessBar.visibility = View.GONE
            }
        }
    }

    private fun getPlayerAudioAttributes(): AudioAttributes{
        return AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MOVIE)
            .build()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onPause() {
        super.onPause()
        player?.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}