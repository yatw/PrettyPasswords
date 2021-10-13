package com.yatw.prettypasswords.view.popups;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.impl.FullScreenPopupView;
import com.yatw.prettypasswords.R;
import com.yatw.prettypasswords.model.Password;

public class AddEntry extends FullScreenPopupView {

    private Context context;
    private SubmitCallback submitCallback;

    public interface SubmitCallback{
        void onSubmit(Password password);
    }

    public AddEntry(@NonNull Context context, SubmitCallback callback) {
        super(context);
        this.context = context;
        this.submitCallback = callback;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_add_entry;
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

                String siteName = siteNameInput.getText().toString().trim();
                String username = usernameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (siteName.isEmpty() || password.isEmpty()){
                    PopUpDialogueKt.showAlert(getContext(), "Warning", "Please fill in all required fields");
                }else{

                    Password p = new Password(siteName, username, email, password, null, "");
                    submitCallback.onSubmit(p);
                    dismiss(); // 关闭弹窗
                }

            }
        });
    }

}