package com.kyawzinlinn.movieplayer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kyawzinlinn.movieplayer.adapter.VideoAdapter
import com.kyawzinlinn.movieplayer.databinding.ActivityMainBinding
import com.kyawzinlinn.movieplayer.model.Video
import com.kyawzinlinn.movieplayer.utils.READ_EXTERNAL_STORAGE_REQUEST_CODE
import com.kyawzinlinn.movieplayer.utils.REQUEST_CODE_OPEN_DIRECTORY
import com.kyawzinlinn.movieplayer.utils.REQUEST_PERMISSION_MANAGE_EXTERNAL_STORAGE
import com.kyawzinlinn.movieplayer.utils.VIDEO_PATH_INTENT_EXTRA
import com.kyawzinlinn.movieplayer.utils.VideoViewType
import com.kyawzinlinn.movieplayer.utils.openDirectory
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isGrid = true
    private lateinit var adapter: VideoAdapter
    private var videoList = mutableListOf<Video>()
    private var videoViewType = VideoViewType.GRID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpClickListeners()
        setUpVideoRecyclerView(videoViewType)
    }

    private fun setUpClickListeners() {
        binding.apply {
            ivVideoViewType.setOnClickListener { toggleVideoView() }
            ivFolder.setOnClickListener { checkManageExternalStoragePermission() }
            ivAddLink.setOnClickListener { showAddLinkVideoDialog() }
        }
    }

    private fun showAddLinkVideoDialog() {
        val inputEditText = EditText(this)
        inputEditText.hint = "Enter video link"

        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.setTitle("Add Video Link")
            .setView(inputEditText)
            .setPositiveButton("Play"){dilog, _ ->
                val url = inputEditText.text.toString()

                if (url.trim().isNotEmpty()) startVideoPlayActivity(url)
                dilog.dismiss()
            }.show()

    }

    private fun toggleVideoView() {
        isGrid =!isGrid
        if (!isGrid) {
            binding.ivVideoViewType.setImageResource(R.drawable.round_grid_view_24)
            videoViewType = VideoViewType.LIST
        }
        else {
            binding.ivVideoViewType.setImageResource(R.drawable.round_view_list_24)
            videoViewType = VideoViewType.GRID
        }
        setUpVideoRecyclerView(videoViewType)
        adapter.submitList(videoList)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY || requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            data?.data?.apply {
                loadVideosFromFolder(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openDirectory()
            }
        }
    }

    private fun checkManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                // Permission already granted, proceed to load videos
                openDirectory()
            } else {
                // Request permission to access all files on external storage
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, REQUEST_PERMISSION_MANAGE_EXTERNAL_STORAGE)
            }
        } else {
            // For devices running below Android 11, no runtime permission required
            openDirectory()
        }
    }

    private fun loadVideosFromFolder(uri: Uri) {

        videoList.clear()

        val pathSegments = uri.path?.split(":")
        val folderPath = "${Environment.getExternalStorageDirectory()}/${pathSegments?.get(1)}"
        val folder = File(folderPath)

        for (file in folder.listFiles()){
            if (file.name.endsWith(".mp4")){
                val video = Video(
                    file.name,
                    file.path
                )
                videoList.add(video)
            }
        }

        setUpVideoRecyclerView(videoViewType)
        adapter.submitList(videoList)
    }

    private fun setUpVideoRecyclerView(videoViewType: VideoViewType) {
        adapter = VideoAdapter(videoViewType){
            startVideoPlayActivity(it.path!!)
        }
        binding.rvVideos.setHasFixedSize(true)
        binding.rvVideos.layoutManager = if (isGrid) GridLayoutManager(this,2) else LinearLayoutManager(this)
        binding.rvVideos.adapter = adapter
    }

    private fun startVideoPlayActivity(url: String) {
        val intent = Intent(this,PlayerActivity::class.java)
        intent.putExtra(VIDEO_PATH_INTENT_EXTRA,url)
        startActivity(intent)
    }
}