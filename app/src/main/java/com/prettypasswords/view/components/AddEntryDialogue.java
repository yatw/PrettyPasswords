package com.prettypasswords.view.components;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prettypasswords.PrettyManager;
import com.prettypasswords.controller.ContentManager;

import com.lxj.xpopup.core.CenterPopupView;
import com.prettypasswords.R;
import com.prettypasswords.model.Tag;

// Xpop up custom center pop up
// https://github.com/li-xiaojun/XPopup/blob/master/library/src/main/java/com/lxj/xpopup/core/CenterPopupView.java
public class AddEntryDialogue extends CenterPopupView {

    Context context;
    Tag tag;

    public AddEntryDialogue(@NonNull Context context, Tag tag) {
        super(context);
        this.context = context;
        this.tag = tag;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.add_entry_dialogue;
    }


    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();

        TextView closeButton = findViewById(R.id.create_entry_close);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // 关闭弹窗
            }
        });

        TextView submitButton = findViewById(R.id.create_entry_submit);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {



                EditText tag_name_input = findViewById(R.id.entry_name_input);

                String entryName = tag_name_input.getText().toString();

                if (entryName.isEmpty()){
                    PopupKt.showAlert(getContext(), "Input cannot be empty", "");
                }else{

                    tag.addEntry(context,entryName);

                    dismiss(); // 关闭弹窗
                }



            }
        });
    }

    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }

    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }

}