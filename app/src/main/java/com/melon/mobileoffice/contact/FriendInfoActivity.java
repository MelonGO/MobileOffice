package com.melon.mobileoffice.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.melon.mobileoffice.R;
import com.melon.mobileoffice.model.Friend;
import com.melon.mobileoffice.model.User;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.List;

public class FriendInfoActivity extends AppCompatActivity {

    private TextView friend_name;
    private TextView friend_gender;
    private TextView friend_age;
    private TextView friend_address;
    private Button friend_add;

    private FinalDb db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_friend);

        db = FinalDb.create(this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String fid = bundle.getString("fid");
        String fname = bundle.getString("fname");

        Toolbar toolbar = (Toolbar) findViewById(R.id.info_friend_toolbar);
        TextView title = (TextView) findViewById(R.id.info_friend_toolbar_title);
        title.setText(fname);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendInfoActivity.this.finish();
            }
        });

        FindFriendInfo(fid);

    }

    private void FindFriendInfo(String id) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/addFriend?type=info&id=" + id, new AjaxCallBack() {

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String message = o.toString();
                if (!message.equals("error")) {
                    String userInfo[] = message.split(";");
                    final int friendid = Integer.parseInt(userInfo[0]);
                    String username = userInfo[1];
//                    String userpassword = userInfo[2];
                    String gender = userInfo[3];
                    int age = Integer.parseInt(userInfo[4]);
                    String address = userInfo[5];

                    friend_name = (TextView) findViewById(R.id.friend_name);
                    friend_gender = (TextView) findViewById(R.id.friend_gender);
                    friend_age = (TextView) findViewById(R.id.friend_age);
                    friend_address = (TextView) findViewById(R.id.friend_address);
                    friend_add = (Button) findViewById(R.id.friend_add);

                    friend_name.setText(username);
                    friend_gender.setText(gender);
                    friend_age.setText(age + "");
                    friend_address.setText(address);
                    friend_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<User> userList = db.findAll(User.class);
                            int userid = userList.get(userList.size() - 1).getUserid();
                            if (userid != friendid) {
                                AddFriend(userid, friendid);
                            } else {
                                Toast.makeText(getApplicationContext(), "添加失败！", Toast.LENGTH_SHORT).show();
                                FriendInfoActivity.this.finish();
                            }

                        }
                    });
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

    private void AddFriend(int userId, int friendId) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/addFriend?type=add&userId=" + userId + "&friendId=" + friendId, new AjaxCallBack() {

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String message = o.toString();
                if (!message.equals("error")) {
                    String[] info = message.split(";");
                    String fid = info[0];
                    String fname = info[1];
                    Friend friend = new Friend();
                    friend.setFriendid(fid);
                    friend.setFriendname(fname);
                    db.save(friend);

                    Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
                    FriendInfoActivity.this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), "添加失败！", Toast.LENGTH_SHORT).show();
                    FriendInfoActivity.this.finish();
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
