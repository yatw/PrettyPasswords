package com.prettypasswords.view.popups;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.prettypasswords.R;
import com.prettypasswords.model.Tag;
import com.prettypasswords.utilities.PopupKt;


// Description: 带有输入框的Bottom弹窗

public class ChangeTagPassword extends BottomPopupView {

    Tag tag;
    TextView errorText;
    EditText currentPasswordInput;
    EditText newPasswordInput;
    EditText confirmPasswordInput;


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            errorText.setVisibility(GONE);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public ChangeTagPassword(@NonNull Context context, Tag tag) {
        super(context);
        this.tag = tag;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_change_tag_password;
    }


    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onShow() {
        super.onShow();

        currentPasswordInput = findViewById(R.id.currentPasswordInput);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        currentPasswordInput.addTextChangedListener(textWatcher);
        newPasswordInput.addTextChangedListener(textWatcher);
        confirmPasswordInput.addTextChangedListener(textWatcher);

        errorText = findViewById(R.id.errorLabel);

        Button submitButton = findViewById(R.id.btn_submit);


        submitButton.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {


                String curPw = currentPasswordInput.getText().toString();
                String newPw = newPasswordInput.getText().toString();
                String newPw2 = confirmPasswordInput.getText().toString();

                if (curPw.isEmpty() || newPw.isEmpty() || newPw2.isEmpty()){
                    errorText.setText("Input cannot be empty");
                    errorText.setVisibility(VISIBLE);
                    return;
                }

                if (!newPw.equals(newPw2)){
                    errorText.setText("New Passwords do not match");
                    errorText.setVisibility(VISIBLE);
                    return;
                }

                if (curPw.equals(newPw2)){
                    PopupKt.showAlert(getContext(), "What's the point?", "Current password and new password are the same");
                    return;
                }

                if (!tag.vertifyPassword(curPw)){
                    PopupKt.showAlert(getContext(), "Incorrect Password", "");
                    return;
                }

                tag.changePassword(getContext(), newPw);
                dismiss();

            }
        });
    }


    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.50f);  // the pop up's height will not exceed more than half of the screen's height, if more, scroll
    }
}
