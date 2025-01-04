package com.example.composepdfkit.generator.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.drawToBitmap

internal class ComposeViewUtils(private val context: Context) {
    data class ViewMeasurements(
        val headerHeight: Int?,
        val bodyHeight: Int,
        val footerHeight: Int?,
        val headerBitmap: Bitmap?,
        val bodyBitmap: Bitmap,
        val footerBitmap: Bitmap?
    )

    fun createComposeView(content: @Composable () -> Unit): ComposeView {
        return ComposeView(context).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent(content)
        }
    }

    fun measureViews(
        headerView: ComposeView?,
        bodyView: ComposeView,
        footerView: ComposeView?
    ): ViewMeasurements {
        // Remove views from any parent
        headerView?.parent?.let { (it as ViewGroup).removeView(headerView) }
        bodyView.parent?.let { (it as ViewGroup).removeView(bodyView) }
        footerView?.parent?.let { (it as ViewGroup).removeView(footerView) }

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            headerView?.let { addView(it) }
            addView(bodyView)
            footerView?.let { addView(it) }
        }

        val measurements = measureLayout(container)
        cleanup(container)

        return measurements
    }

    private fun measureLayout(container: LinearLayout): ViewMeasurements {
        val tempLayout = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addView(container)
        }

        (context as Activity).window.decorView.let { decorView ->
            (decorView as ViewGroup).addView(tempLayout)
            tempLayout.measure(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            tempLayout.layout(0, 0, tempLayout.measuredWidth, tempLayout.measuredHeight)
            decorView.removeView(tempLayout)
        }

        val headerView = container.getChildAt(0) as? ComposeView
        val bodyView = container.getChildAt(if (headerView != null) 1 else 0) as ComposeView
        val footerView = container.getChildAt(container.childCount - 1) as? ComposeView

        return ViewMeasurements(
            headerHeight = headerView?.measuredHeight,
            bodyHeight = bodyView.measuredHeight,
            footerHeight = footerView?.measuredHeight,
            headerBitmap = headerView?.drawToBitmap(),
            bodyBitmap = bodyView.drawToBitmap(),
            footerBitmap = footerView?.drawToBitmap()
        )
    }

    private fun cleanup(container: ViewGroup) {
        for (i in 0 until container.childCount) {
            val view = container.getChildAt(i)
            if (view is ComposeView) {
                view.disposeComposition()
            }
        }
        (container.parent as? ViewGroup)?.removeView(container)
    }

    fun cleanupView(view: ComposeView) {
        (view.parent as? ViewGroup)?.removeView(view)
        view.disposeComposition()
    }
}