package com.melon.mobileoffice.work.project;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.melon.mobileoffice.R;
import com.melon.mobileoffice.model.User;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.List;

public class ProjectInputActivity extends AppCompatActivity {
    private FinalDb db;
    private EditText projectName;
    private Button btn_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_input);

        db = FinalDb.create(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.project_input_toolbar);
        TextView title = (TextView) findViewById(R.id.project_input_toolbar_title);
        title.setText("新建项目");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectInputActivity.this.finish();
            }
        });

        projectName = (EditText) findViewById(R.id.project_input_name);
        btn_sure = (Button) findViewById(R.id.project_add_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pname = projectName.getText().toString();
                List<User> userList = db.findAll(User.class);
                int userid = userList.get(userList.size() - 1).getUserid();
                AddProject(userid, pname);
            }
        });

    }

    private void AddProject(final int host, String pname) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/project?type=add&host=" + host + "&pname=" + pname, new AjaxCallBack() {

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String message = o.toString();
                if (message.equals("success")) {
                    ProjectInputActivity.this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
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
