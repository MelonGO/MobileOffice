package com.melon.mobileoffice.contact;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.melon.mobileoffice.R;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddFriendActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText find_name;
    private Button find_btn;
    private ListView listView;

    private List<Map<String, Object>> mSampleList;
    public static final String KEY_ICON = "icon";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_friend_toolbar);
        TextView title = (TextView) findViewById(R.id.add_friend_toolbar_title);
        title.setText("添加朋友");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendActivity.this.finish();
            }
        });

        find_name = (EditText) findViewById(R.id.find_name);
        find_btn = (Button) findViewById(R.id.find_btn);
        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = find_name.getText().toString();
                if (!name.equals("")) {
                    FindFriend(name);
                }
            }
        });

    }

    private void FindFriend(String name) {
        FinalHttp fh = new FinalHttp();
        fh.get("http://10.0.3.2:8080/MobileOfficeWeb/addFriend?type=find&name=" + name, new AjaxCallBack() {

            @Override
            public void onLoading(long count, long current) { //每1秒钟自动被回调一次

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String message = o.toString();
                if (!message.equals("notFound")) {
                    HashMap<Integer, String> findList = new HashMap<Integer, String>();
                    String[] s1 = message.split("&");
                    for (int i = 0; i < s1.length; i++) {
                        String[] s2 = s1[i].split(";");
                        int userid = Integer.parseInt(s2[0]);
                        String username = s2[1];
                        findList.put(userid, username);
                    }
                    initFindFriendList(findList);
                    listView = (ListView) findViewById(R.id.find_list);
                    listView.setAdapter(new SampleAdapter(AddFriendActivity.this, R.layout.find_friend_list_item, mSampleList));
                    listView.setOnItemClickListener(AddFriendActivity.this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView friendName = (TextView) findViewById(R.id.find_friend_name);
        TextView friendId = (TextView) findViewById(R.id.find_friend_id);

        String fname = friendName.getText().toString();
        String fid = friendId.getText().toString();

        Intent intent = new Intent();
        intent.setClass(AddFriendActivity.this, FriendInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fname", fname);
        bundle.putString("fid", fid);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class SampleAdapter extends ArrayAdapter<Map<String, Object>> {

        public static final String KEY_ICON = "icon";
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";

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
                convertView = mInflater.inflate(R.layout.find_friend_list_item, parent, false);
                viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.find_friend_image);
                viewHolder.friendTextView = (TextView) convertView.findViewById(R.id.find_friend_name);
                viewHolder.friendId = (TextView) convertView.findViewById(R.id.find_friend_id);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.imageViewIcon.setImageResource((int) mData.get(position).get(KEY_ICON));
            viewHolder.friendTextView.setText((String) mData.get(position).get(KEY_NAME));
            viewHolder.friendId.setText(mData.get(position).get(KEY_ID) + "");

            return convertView;
        }

        class ViewHolder {
            ImageView imageViewIcon;
            TextView friendId;
            TextView friendTextView;
        }

    }

    private void initFindFriendList(HashMap<Integer, String> findList) {
        Map<String, Object> map;
        mSampleList = new ArrayList<>();

        Iterator iter = findList.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int id = (int) entry.getKey();
            String name = (String) entry.getValue();

            map = new HashMap<>();
            map.put(KEY_ICON, R.drawable.icon_1);
            map.put(KEY_ID, id);
            map.put(KEY_NAME, name);
            mSampleList.add(map);
        }
    }

}
