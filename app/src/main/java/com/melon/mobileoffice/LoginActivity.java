package com.melon.mobileoffice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.melon.mobileoffice.model.User;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

public class LoginActivity extends AppCompatActivity {

    private Button login_btn;
    private EditText user_name;
    private EditText user_password;

    final FinalDb db = FinalDb.create(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        user_name = (EditText) findViewById(R.id.name_edit);
        user_password = (EditText) findViewById(R.id.password_edit);
        login_btn = (Button) findViewById(R.id.login_sure);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = user_name.getText().toString();
                String password = user_password.getText().toString();
                if (!name.equals("") && !password.equals("")) {
                    CheckUser(name, password);
                }
            }
        });
    }

    private void CheckUser(String name, String password) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/login?name=" + name + "&password=" + password, new AjaxCallBack() {

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (o != null) {
                    User user = (User) o;
                    db.save(user);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onStart() {
                //开始http请求的时候回调
            }

        });

    }

}
