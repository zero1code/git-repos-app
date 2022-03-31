package br.com.mpsystems.cpmtracking.gitrepos.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.mpsystems.cpmtracking.gitrepos.R

class DividerItemDecoration() : RecyclerView.ItemDecoration() {
    private var mDivider: Drawable? = null

    constructor(context: Context):this() {
        mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)
    }

    override fun onDrawOver(c: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {

        val left = 44
        val right = recyclerView.width - 44

        val childCount = recyclerView.childCount
        for (i in 0 until childCount) {

            val child = recyclerView.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)

        }
    }
}