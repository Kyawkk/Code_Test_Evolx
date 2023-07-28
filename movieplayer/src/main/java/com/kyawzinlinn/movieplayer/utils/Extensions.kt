package com.kyawzinlinn.movieplayer.utils

import android.app.Activity
import android.content.Intent

fun Activity.openDirectory(){
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY)
}