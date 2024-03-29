package com.yatw.prettypasswords.view.popups


import android.content.Context
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
        ) {
            //Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show(
        }
        .show()
}