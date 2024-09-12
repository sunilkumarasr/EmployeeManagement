package com.royalitpark.employeemanagement.customviews

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class VerticalSpaceItemDecoration(val verticalSpaceHeight:Int): RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
    }



    /*fun onDraw(c: Canvas?, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom: Int = top + divider.getIntrinsicHeight()
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
        super.onDraw(c, parent, state)
    }*/

}