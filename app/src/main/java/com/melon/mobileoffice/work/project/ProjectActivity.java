package com.melon.mobileoffice.work.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.melon.mobileoffice.R;
import com.melon.mobileoffice.model.User;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProjectActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private FinalDb db;

    private List<Map<String, Object>> mSampleList;
    public static final String KEY_CONTENT = "content";

    private ListView projectList;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project);

        db = FinalDb.create(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.project_toolbar);
        TextView title = (TextView) findViewById(R.id.project_toolbar_title);
        title.setText("项目管理");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectActivity.this.finish();
            }
        });

        btn_add = (Button) findViewById(R.id.project_add_btn);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ProjectActivity.this, ProjectInputActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<User> userList = db.findAll(User.class);
        int userid = userList.get(userList.size() - 1).getUserid();
        initProjectList(userid);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final TextView pname = (TextView) view.findViewById(R.id.project_name);

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("删除项目")
                .setContentText("确认删除该项目？")
                .setConfirmText("Delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        List<User> userList = db.findAll(User.class);
                        int userid = userList.get(userList.size() - 1).getUserid();
                        deleteProject(userid, pname.getText().toString());
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelText("No")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

        return true;
    }

    class SampleAdapter extends ArrayAdapter<Map<String, Object>> {

        public static final String KEY_CONTENT = "content";

        private final LayoutInflater mInflater;
        private final List<Map<String, Object>> mData;

        public SampleAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data) {
            super(context, layoutResourceId, data);
            mData = data;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.project_list_item, parent, false);
                viewHolder.projectTextView = (TextView) convertView.findViewById(R.id.project_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.projectTextView.setText((String) mData.get(position).get(KEY_CONTENT));

            return convertView;
        }

        class ViewHolder {
            TextView projectTextView;
        }

    }

    private void initProjectList(int host) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/project?type=all&host=" + host, new AjaxCallBack() {

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String message = o.toString();
                mSampleList = new ArrayList<>();
                Map<String, Object> map;
                if (!message.equals("noProject")) {
                    String[] pname = message.split(";");
                    for (int i = 0; i < pname.length; i++) {
                        map = new HashMap<>();
                        map.put(KEY_CONTENT, pname[i]);
                        mSampleList.add(map);
                    }
                    projectList = (ListView) findViewById(R.id.project_list);
                    projectList.setAdapter(new SampleAdapter(getApplicationContext(), R.layout.project_list_item, mSampleList));
                    projectList.setOnItemClickListener(ProjectActivity.this);
                    projectList.setOnItemLongClickListener(ProjectActivity.this);
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

    private void deleteProject(int host, String pname) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/project?type=delete&host=" + host + "&pname=" + pname, new AjaxCallBack() {

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String message = o.toString();
                if (!message.equals("error")) {
                    List<User> userList = db.findAll(User.class);
                    int userid = userList.get(userList.size() - 1).getUserid();
                    initProjectList(userid);
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
