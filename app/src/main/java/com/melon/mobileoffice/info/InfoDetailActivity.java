package com.melon.mobileoffice.info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.melon.mobileoffice.R;
import com.melon.mobileoffice.model.User;

import net.tsz.afinal.FinalDb;

import java.util.List;

public class InfoDetailActivity extends AppCompatActivity {

    private FinalDb db;

    private TextView name;
    private TextView gender;
    private TextView age;
    private TextView address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_detail);

        db = FinalDb.create(this);

        name = (TextView) findViewById(R.id.user_name);
        gender = (TextView) findViewById(R.id.user_gender);
        age = (TextView) findViewById(R.id.user_age);
        address = (TextView) findViewById(R.id.user_address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.info_detail_toolbar);
        TextView title = (TextView) findViewById(R.id.info_toolbar_title);
        title.setText("个人信息");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDetailActivity.this.finish();
            }
        });

        initUserInfo();
    }

    private void initUserInfo() {

        List<User> userList = db.findAll(User.class);
        User user = userList.get(userList.size() - 1);
        name.setText(user.getUsername() + "");
        gender.setText(user.getGender() + "");
        age.setText(user.getAge() + "");
        address.setText(user.getAddress() + "");
    }

}
