package com.etsproject.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeController(private val callback: SwipeCallback) : ItemTouchHelper.Callback() {

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 1000000000F
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {
            val paint = Paint()
            paint.color = Color.RED
            c.drawRect(
                itemView.right.toFloat() + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat(),
                paint
            )
            paint.color = Color.WHITE
            paint.textSize = 40F
            c.drawText(
                "Sil",
                itemView.right.toFloat() - 100.0f,
                (itemView.top.toFloat() + itemView.bottom) / 2 - (paint.descent() + paint.ascent()) / 2,
                paint
            )

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback.onSwipe(viewHolder.adapterPosition)
    }
}