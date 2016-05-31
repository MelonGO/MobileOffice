package com.melon.mobileoffice.work.employee;

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

public class EmployeeInputActivity extends AppCompatActivity {
    private FinalDb db;
    private EditText employeeName;
    private Button btn_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_input);

        db = FinalDb.create(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.employee_input_toolbar);
        TextView title = (TextView) findViewById(R.id.employee_input_toolbar_title);
        title.setText("新增员工");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeInputActivity.this.finish();
            }
        });

        employeeName = (EditText) findViewById(R.id.employee_input_name);
        btn_sure = (Button) findViewById(R.id.employee_add_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ename = employeeName.getText().toString();
                List<User> userList = db.findAll(User.class);
                int userid = userList.get(userList.size() - 1).getUserid();
                AddEmployee(userid, ename);
            }
        });

    }

    private void AddEmployee(final int host, String ename) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/employee?type=add&host=" + host + "&ename=" + ename, new AjaxCallBack() {

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String message = o.toString();
                if (message.equals("success")) {
                    EmployeeInputActivity.this.finish();
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
