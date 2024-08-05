package com.worksy.hr.art.views.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class CustomViewPager : ViewPager {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec :Int
        var biggestHeightMeasureSpec = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child != null) {
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val h = child.measuredHeight
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY)
                if (heightMeasureSpec > biggestHeightMeasureSpec) biggestHeightMeasureSpec =
                    heightMeasureSpec
            }
        }
        super.onMeasure(widthMeasureSpec, biggestHeightMeasureSpec)
    }
}