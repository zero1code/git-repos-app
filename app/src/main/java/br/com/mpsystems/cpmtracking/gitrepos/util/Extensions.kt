package br.com.mpsystems.cpmtracking.gitrepos.util

import android.animation.Animator
import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar

fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showSnackBar(message: String, snackColor: String): Snackbar {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackbar.setBackgroundTint(Color.parseColor(snackColor))
    return snackbar
}

fun circularSearchAnimation(
    viewToAnimate: View?,
    viewToGetPositions: View,
    root: View?,
    isReverse: Boolean
): Animator? {
    val animator = ViewAnimationUtils.createCircularReveal(
        viewToAnimate,
        (viewToGetPositions.right + viewToGetPositions.left) / 2,
        (viewToGetPositions.top + viewToGetPositions.bottom) / 2,
        if (isReverse) root!!.width.toFloat() else 0f,
        if (isReverse) 0f else root!!.width.toFloat()
    )
    animator.duration = 300
    return animator
}