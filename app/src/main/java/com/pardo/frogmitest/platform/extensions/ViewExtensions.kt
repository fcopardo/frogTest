package com.pardo.frogmitest.platform.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

fun View.fillFrameParameters() {
    val layoutParams = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    this.layoutParams = layoutParams
}

fun View.wrapWithFrame(wrapper : FrameLayout) {
    fillFrameParameters()
    wrapper.addView(this)
}