package hbs.com.snackb.ui.main

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager
        val total = layoutManager!!.itemCount
        val current = layoutManager.getPosition(view)

        with(outRect) {
            if (current == total - 1) {
                top = spaceHeight/2
            } else {
                top = spaceHeight/2
                right = spaceHeight
            }
        }
    }
}