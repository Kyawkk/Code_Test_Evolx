package com.kyawzinlinn.movieplayer.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CenterGridItemDecoration(private val spanCount: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        // Calculate horizontal offset to center items
        val horizontalOffset = (parent.width - spanCount * view.layoutParams.width) / (spanCount + 1)
        val left = horizontalOffset * (column + 1) - view.layoutParams.width * column
        val right = view.layoutParams.width - left

        // Apply the calculated offsets to the item
        outRect.set(left, 0, right, 0)
    }
}
