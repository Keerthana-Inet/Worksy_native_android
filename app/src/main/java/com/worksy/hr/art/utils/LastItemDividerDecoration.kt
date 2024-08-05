import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R

class LastItemDividerDecoration(context: Context, orientation: Int) : DividerItemDecoration(context, orientation) {
    var divider: Drawable? = ContextCompat.getDrawable(context, R.drawable.divider_drawable) // Replace with your divider drawable
    private val isVertical: Boolean = orientation == VERTICAL

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (isVertical) {
            drawVerticalDivider(canvas, parent)
        } else {
            super.onDraw(canvas, parent, state) // Handle HORIZONTAL orientation if needed
        }
    }

    private fun drawVerticalDivider(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0 until childCount - 1) { // Exclude divider after the last item
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + (divider?.intrinsicHeight ?: 0)

            divider?.setBounds(left, top, right, bottom)
            divider?.draw(canvas)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        if (itemPosition < itemCount - 1) { // Exclude divider after the last item
            if (isVertical) {
                outRect.set(0, 0, 0, divider?.intrinsicHeight ?: 0)
            } else {
                // Handle HORIZONTAL orientation if needed
                super.getItemOffsets(outRect, view, parent, state)
            }
        } else {
            outRect.setEmpty()
        }
    }
}
