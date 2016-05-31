package com.melon.mobileoffice.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.melon.mobileoffice.BaseFragment;
import com.melon.mobileoffice.R;
import com.melon.mobileoffice.work.customer.CustomerActivity;
import com.melon.mobileoffice.work.employee.EmployeeActivity;
import com.melon.mobileoffice.work.project.ProjectActivity;

public class WorkFragment extends BaseFragment {

    private MyGridView gridview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work, container, false);

        gridview = (MyGridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new MyGridAdapter(this.getContext()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        Toast.makeText(getActivity(), "签到成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "暂无公告", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        intent = new Intent();
                        intent.setClass(getActivity(), CustomerActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent();
                        intent.setClass(getActivity(), EmployeeActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent();
                        intent.setClass(getActivity(), ProjectActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        break;
                    default:
                        break;
                }
            }
        });

        return view;
    }

}
