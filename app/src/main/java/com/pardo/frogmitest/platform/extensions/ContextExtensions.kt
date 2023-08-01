package com.pardo.frogmitest.platform.extensions

import android.R
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView

fun Activity.getActivityRootFrame(): FrameLayout {
    return findViewById<View>(R.id.content) as FrameLayout
}
fun ComponentActivity.getComposeRoot(): ComposeView {
    return (getActivityRootFrame().getChildAt(0) as ComposeView)
}