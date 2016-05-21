package com.melon.mobileoffice.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melon.mobileoffice.BaseFragment;
import com.melon.mobileoffice.R;

public class InfoFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        LinearLayout user_info = (LinearLayout) view.findViewById(R.id.user_info);
        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), InfoDetailActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
