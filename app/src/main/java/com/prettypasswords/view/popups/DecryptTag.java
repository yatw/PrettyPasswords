package com.prettypasswords.view.popups;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.prettypasswords.PrettyManager;
import com.prettypasswords.controller.ContentManager;

import com.lxj.xpopup.core.CenterPopupView;
import com.prettypasswords.R;
import com.prettypasswords.model.Body;
import com.prettypasswords.model.Tag;

import java.util.ArrayList;
import com.prettypasswords.view.activities.EntriesListActivity;
import com.prettypasswords.view.fragments.HomeFragment;


// Xpop up custom center pop up
// https://github.com/li-xiaojun/XPopup/blob/master/library/src/main/java/com/lxj/xpopup/core/CenterPopupView.java
public class DecryptTag extends CenterPopupView {

    int tagPosition;
    Context context;
    HomeFragment fragment;
    Tag tag;

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

    public DecryptTag(@NonNull Context context, int tagPosition, HomeFragment fragment) {
        super(context);
        this.context = context;
        this.fragment = fragment;
        this.tagPosition = tagPosition;

        ContentManager cm = PrettyManager.INSTANCE.getCm();
        Body body = cm.getBody();
        ArrayList<Tag> tags = body.getTags();

        tag = tags.get(tagPosition);

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_decrypt_tag;
    }


    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();

        errorText = findViewById(R.id.errorLabel);


        TextView tv_name = findViewById(R.id.tv_name);
        tv_name.setText("Decrypt " + tag.getTagName());

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


                    boolean decryptSuccess = tag.decrypt(tagPassword);

                    if (decryptSuccess){


                        // notify the list to update ui
                        Intent updateIntent = new Intent("tagStatusChanged");
                        updateIntent.putExtra("clickedTag", tagPosition);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);

                        Intent intent = new Intent(context, EntriesListActivity.class);
                        intent.putExtra("clickedTag", tagPosition);
                        fragment.startActivityForResult(intent, 10);

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