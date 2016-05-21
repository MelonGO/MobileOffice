package com.melon.mobileoffice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.melon.mobileoffice.model.User;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button login_btn;
    private EditText user_name;
    private EditText user_password;

    private FinalDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = FinalDb.create(this);

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
                String message = o.toString();
                if (!message.equals("error")) {
                    String userInfo[] = message.split(";");
                    int userid = Integer.parseInt(userInfo[0]);
                    String username = userInfo[1];
                    String userpassword = userInfo[2];
                    String gender = userInfo[3];
                    int age = Integer.parseInt(userInfo[4]);
                    String address = userInfo[5];

                    User user = new User();
                    user.setUserid(userid);
                    user.setUsername(username);
                    user.setUserpassword(userpassword);
                    user.setGender(gender);
                    user.setAge(age);
                    user.setAddress(address);

                    List<User> userList = db.findAll(User.class);
                    if (userList.size() > 0) {
                        db.deleteAll(User.class);//删除以前的用户信息

                    }
                    db.save(user);

                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
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
