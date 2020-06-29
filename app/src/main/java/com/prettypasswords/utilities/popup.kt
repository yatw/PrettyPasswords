package com.prettypasswords.utilities

import android.content.Context
import android.widget.Toast
import com.lxj.xpopup.XPopup


fun showAlert(
    context: Context?,
    title: String?,
    message: String?
) {
    XPopup.Builder(context)
        .dismissOnTouchOutside(false)
        .asConfirm(
            title, message
        ) { Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show() }
        .show()
}