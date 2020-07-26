package com.prettypasswords.view.popups

import android.content.Context
import android.widget.TextView
import com.lxj.xpopup.core.PositionPopupView
import com.prettypasswords.R

class PopUpAlert(context: Context, text: String?) :
    PositionPopupView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.layout_qq_popup
    }

    init {
        val tv = findViewById<TextView>(R.id.popup_text)
        tv.text = text
    }
}