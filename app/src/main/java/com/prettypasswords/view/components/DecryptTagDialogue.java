package com.prettypasswords.view.components;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.prettypasswords.PrettyManager;
import com.prettypasswords.controller.ContentManager;

import com.lxj.xpopup.core.CenterPopupView;
import com.prettypasswords.R;
import com.prettypasswords.model.Body;
import com.prettypasswords.model.Tag;

import java.util.ArrayList;
import com.prettypasswords.utilities.PopupKt;
import com.prettypasswords.view.activities.EntriesListActivity;


// Xpop up custom center pop up
// https://github.com/li-xiaojun/XPopup/blob/master/library/src/main/java/com/lxj/xpopup/core/CenterPopupView.java
public class DecryptTagDialogue extends CenterPopupView {

    int tagPosition;
    Context context;


    TextView errorText;


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

    public DecryptTagDialogue(@NonNull Context context, int tagPosition) {
        super(context);
        this.context = context;
        this.tagPosition = tagPosition;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.decrypt_tag_dialogue;
    }


    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();

        errorText = findViewById(R.id.errorLabel);


        final EditText tag_password = findViewById(R.id.tag_password);
        tag_password.addTextChangedListener(textWatcher);

        TextView closeButton = findViewById(R.id.decrypt_tag_close);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // 关闭弹窗
            }
        });

        TextView submitButton = findViewById(R.id.decrypt_tag_submit);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {



                String tagPassword = tag_password.getText().toString();

                if (tagPassword.isEmpty()){

                    errorText.setText("Input cannot be empty");
                    errorText.setVisibility(VISIBLE);

                }else{

                    ContentManager cm = PrettyManager.INSTANCE.getCm();
                    Body body = cm.getBody();
                    ArrayList<Tag> tags = body.getTags();

                    Tag tag = tags.get(tagPosition);


                    boolean decryptSuccess = tag.decrypt(tagPassword);

                    if (decryptSuccess){


                        Intent intent = new Intent(context, EntriesListActivity.class);
                        intent.putExtra("clickedTag", tagPosition);
                        context.startActivity(intent);

                        Toast.makeText(getContext(), "Decrypted tag success", Toast.LENGTH_LONG).show();

                        delayDismiss(500); // 关闭弹窗

                    }else{

                        errorText.setText("Incorrect Password");
                        errorText.setVisibility(VISIBLE);
                    }


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