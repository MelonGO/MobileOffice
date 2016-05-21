package com.melon.mobileoffice.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.melon.mobileoffice.R;

public class MsgDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.msg_detail_toolbar);
        TextView title = (TextView) findViewById(R.id.msg_toolbar_title);
        title.setText("消息");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgDetailActivity.this.finish();
            }
        });
    }

}
