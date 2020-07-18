package com.prettypasswords.view.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prettypasswords.PrettyManager;
import com.prettypasswords.controller.ContentManager;

import com.lxj.xpopup.core.CenterPopupView;
import com.prettypasswords.R;


// Xpop up custom center pop up
// https://github.com/li-xiaojun/XPopup/blob/master/library/src/main/java/com/lxj/xpopup/core/CenterPopupView.java
public class AddTagDialogue extends CenterPopupView {

    TextView errorText;
    EditText tag_name_input;
    EditText tag_password;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            errorText.setVisibility(INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    public AddTagDialogue(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.add_tag_dialogue;
    }


    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();

        errorText = findViewById(R.id.errorLabel);
        tag_name_input = findViewById(R.id.tag_name_input);
        tag_password = findViewById(R.id.tag_password);

        tag_name_input.addTextChangedListener(textWatcher);
        tag_password.addTextChangedListener(textWatcher);

        TextView closeButton = findViewById(R.id.create_tag_close);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // 关闭弹窗
            }
        });

        TextView submitButton = findViewById(R.id.create_tag_submit);
        submitButton.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {


                String tagName = tag_name_input.getText().toString();
                String tagPassword = tag_password.getText().toString();

                if (tagName.isEmpty() || tagPassword.isEmpty()){
                    errorText.setText("Input cannot be empty");
                    errorText.setVisibility(VISIBLE);

                }else{
                    ContentManager cm = PrettyManager.INSTANCE.getCm();
                    cm.getBody().addTag(getContext(), tagName, tagPassword);

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