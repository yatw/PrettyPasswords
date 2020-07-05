package com.prettypasswords.view.components;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.lxj.xpopup.core.CenterPopupView;
import com.prettypasswords.R;
import com.prettypasswords.model.Tag;
import com.prettypasswords.utilities.PopupKt;

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


                EditText siteNameInput = findViewById(R.id.siteNameInput);
                EditText usernameInput = findViewById(R.id.usernameInput);
                EditText emailInput = findViewById(R.id.emailInput);
                EditText passwordInput = findViewById(R.id.passwordInput);
                EditText othersInput = findViewById(R.id.othersInput);

                String siteName = siteNameInput.getText().toString().trim();
                String username = usernameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String others = othersInput.getText().toString().trim();

                if (siteName.isEmpty() || password.isEmpty()){
                    PopupKt.showAlert(getContext(), "Warning", "Please fill in all required fields");
                }else{
                    tag.addEntry(context,siteName,username,password,email,others);
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