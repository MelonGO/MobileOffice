package com.melon.mobileoffice.message;

import android.os.Bundle;

import com.melon.mobileoffice.BaseFragment;
import com.melon.mobileoffice.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseRefreshFragment extends BaseFragment {

    public static final int REFRESH_DELAY = 2000;

    public static final String KEY_ICON = "icon";
    public static final String KEY_CONTENT = "content";

    protected List<Map<String, Integer>> mSampleList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, Integer> map;
        mSampleList = new ArrayList<>();

        int[] icons = {
                R.drawable.icon_1,
                R.drawable.icon_2,
                R.drawable.icon_3
        };

        int[] contents = {
                R.string.msg_content_1,
                R.string.msg_content_2,
                R.string.msg_content_3
        };

        for (int i = 0; i < icons.length; i++) {
            map = new HashMap<>();
            map.put(KEY_ICON, icons[i]);
            map.put(KEY_CONTENT, contents[i]);
            mSampleList.add(map);
        }
    }

}
