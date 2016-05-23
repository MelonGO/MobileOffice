package com.melon.mobileoffice.contact;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.melon.mobileoffice.BaseFragment;
import com.melon.mobileoffice.R;
import com.melon.mobileoffice.model.Friend;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactFragment extends BaseFragment {
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;

    private int[] icon = {R.drawable.contact_1, R.drawable.contact_2,
            R.drawable.contact_3};

    private String[] iconName = {"联系人", "群组", "特别关注"};

    private List<Map<String, Object>> mSampleList;
    public static final String KEY_ICON = "icon";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_ID = "id";

    private FinalDb db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_contact, container, false);

        db = FinalDb.create(getActivity());

        gview = (GridView) view.findViewById(R.id.contact_grid_view);
        data_list = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"contact_image", "contact_text"};
        int[] to = {R.id.contact_image, R.id.contact_text};
        sim_adapter = new SimpleAdapter(this.getContext(), data_list, R.layout.contact_list_item, from, to);
        gview.setAdapter(sim_adapter);

        List friendList = db.findAll(Friend.class);
        if (friendList.size() > 0) {
            initContactList(friendList);
            ListView listView = (ListView) view.findViewById(R.id.contact_list_view);
            listView.setAdapter(new SampleAdapter(getActivity(), R.layout.contact_list_item_1, mSampleList));
        }

        return view;
    }

    public List<Map<String, Object>> getData() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("contact_image", icon[i]);
            map.put("contact_text", iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }

    class SampleAdapter extends ArrayAdapter<Map<String, Object>> {

        public static final String KEY_ICON = "icon";
        public static final String KEY_CONTENT = "content";
        public static final String KEY_ID = "id";

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
                convertView = mInflater.inflate(R.layout.contact_list_item_1, parent, false);
                viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.contact_friend_image);
                viewHolder.contactTextView = (TextView) convertView.findViewById(R.id.contact_friend_name);
                viewHolder.contactId = (TextView) convertView.findViewById(R.id.contact_friend_id);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.imageViewIcon.setImageResource((int) mData.get(position).get(KEY_ICON));
            viewHolder.contactTextView.setText((String) mData.get(position).get(KEY_CONTENT));
            viewHolder.contactId.setText(mData.get(position).get(KEY_ID) + "");

            return convertView;
        }

        class ViewHolder {
            ImageView imageViewIcon;
            TextView contactTextView;
            TextView contactId;
        }

    }

    private void initContactList(List friendList) {
        Map<String, Object> map;
        mSampleList = new ArrayList<>();

        for (int i = 0; i < friendList.size(); i++) {
            Friend friend = (Friend) friendList.get(i);
            map = new HashMap<>();
            map.put(KEY_ICON, R.drawable.icon_1);
            map.put(KEY_ID, friend.getFriendid());
            map.put(KEY_CONTENT, friend.getFriendname());
            mSampleList.add(map);
        }
    }

}
