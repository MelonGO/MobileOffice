package com.melon.mobileoffice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.melon.mobileoffice.model.Friend;
import com.melon.mobileoffice.model.User;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.InputStream;
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

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.login_bg);
        Bitmap bitmap = readBitMap(this, R.drawable.bg_image);
        Drawable drawable = bitmapToDrawble(bitmap, this);
        rl.setBackground(drawable);

        user_name = (EditText) findViewById(R.id.name_edit);
        user_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    user_name.setHint(null);
                } else {
                    user_name.setHint("用户名");
                }
            }
        });

        user_password = (EditText) findViewById(R.id.password_edit);
        user_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    user_password.setHint(null);
                } else {
                    user_password.setHint("密码");
                }
            }
        });

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
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/login?type=login&name=" + name + "&password=" + password, new AjaxCallBack() {

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

                    User new_user = new User();
                    new_user.setUserid(userid);
                    new_user.setUsername(username);
                    new_user.setUserpassword(userpassword);
                    new_user.setGender(gender);
                    new_user.setAge(age);
                    new_user.setAddress(address);
                    db.save(new_user);

                    GetFriendList(userid);
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

    private void GetFriendList(int userid) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/login?type=friend&userid=" + userid, new AjaxCallBack() {

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);

                List friendList = db.findAll(Friend.class);
                if (friendList.size() > 0) {
                    db.deleteAll(Friend.class);
                }

                String message = o.toString();
                if (!message.equals("")) {
                    String[] infoList = message.split("&");
                    for (int i = 0; i < infoList.length; i++) {
                        String[] info = infoList[i].split(";");
                        Friend friend = new Friend();
                        friend.setFriendid(info[0]);
                        friend.setFriendname(info[1]);
                        db.save(friend);
                    }
                }
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
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

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static Drawable bitmapToDrawble(Bitmap bitmap, Context mcontext) {
        Drawable drawable = new BitmapDrawable(mcontext.getResources(), bitmap);
        return drawable;
    }

}
