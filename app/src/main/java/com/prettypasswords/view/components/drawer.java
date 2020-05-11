package com.prettypasswords.view.components;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.DrawerPopupView;
import com.prettypasswords.R;

public class drawer extends DrawerPopupView {
    public drawer(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.add_tag_dialogue;
    }
    @Override
    protected void onCreate() {
        super.onCreate();
    }
}