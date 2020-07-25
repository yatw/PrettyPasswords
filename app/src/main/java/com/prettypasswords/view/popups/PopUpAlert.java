package com.prettypasswords.view.popups;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.PositionPopupView;
import com.prettypasswords.R;

public class PopUpAlert extends PositionPopupView{


    public PopUpAlert(@NonNull Context context, String text) {
        super(context);

        TextView tv = findViewById(R.id.popup_text);
        tv.setText(text);

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_qq_popup;
    }
}
