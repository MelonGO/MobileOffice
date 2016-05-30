package com.melon.mobileoffice.work.customer;

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
import android.widget.Toast;

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

public class CustomerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private FinalDb db;

    private List<Map<String, Object>> mSampleList;
    public static final String KEY_CONTENT = "content";

    private ListView customerList;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);

        db = FinalDb.create(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.customer_toolbar);
        TextView title = (TextView) findViewById(R.id.customer_toolbar_title);
        title.setText("客户管理");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerActivity.this.finish();
            }
        });

        btn_add = (Button) findViewById(R.id.customer_add_btn);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CustomerActivity.this, CustomerInputActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<User> userList = db.findAll(User.class);
        int userid = userList.get(userList.size() - 1).getUserid();
        initCustomerList(userid);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "click customer", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final TextView cname = (TextView) view.findViewById(R.id.customer_name);

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("删除客户")
                .setContentText("确认删除该客户？")
                .setConfirmText("Delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        List<User> userList = db.findAll(User.class);
                        int userid = userList.get(userList.size() - 1).getUserid();
                        deleteCustomer(userid, cname.getText().toString());
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
                convertView = mInflater.inflate(R.layout.customer_list_item, parent, false);
                viewHolder.customerTextView = (TextView) convertView.findViewById(R.id.customer_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.customerTextView.setText((String) mData.get(position).get(KEY_CONTENT));

            return convertView;
        }

        class ViewHolder {
            TextView customerTextView;
        }

    }

    private void initCustomerList(int host) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/customer?type=all&host=" + host, new AjaxCallBack() {

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String message = o.toString();
                mSampleList = new ArrayList<>();
                Map<String, Object> map;
                if (!message.equals("noCustomer")) {
                    String[] cname = message.split(";");
                    for (int i = 0; i < cname.length; i++) {
                        map = new HashMap<>();
                        map.put(KEY_CONTENT, cname[i]);
                        mSampleList.add(map);
                    }
                    customerList = (ListView) findViewById(R.id.customer_list);
                    customerList.setAdapter(new SampleAdapter(getApplicationContext(), R.layout.customer_list_item, mSampleList));
                    customerList.setOnItemClickListener(CustomerActivity.this);
                    customerList.setOnItemLongClickListener(CustomerActivity.this);
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

    private void deleteCustomer(int host, String cname) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/customer?type=delete&host=" + host + "&cname=" + cname, new AjaxCallBack() {

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
                    initCustomerList(userid);
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
