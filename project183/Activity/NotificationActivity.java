package com.example.project183.Activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project183.Helper.TinyDB;
import com.example.project183.R;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private ListView notificationListView;
    private Button backBtn, clearBtn;
    private TinyDB tinyDB;
    private ArrayList<String> notifications;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification); // 绑定 notification.xml 布局文件

        // 初始化组件
        notificationListView = findViewById(R.id.notificationListView);
        backBtn = findViewById(R.id.button);
        clearBtn = findViewById(R.id.ClearBtn);
        tinyDB = new TinyDB(this);

        // 加载通知
        loadNotifications();

        // 返回按钮
        backBtn.setOnClickListener(v -> finish());

        // 清空通知按钮
        clearBtn.setOnClickListener(v -> {
            notifications.clear();
            notifications.add("No notifications available.");
            tinyDB.putListString("NotificationList", notifications);
            adapter.notifyDataSetChanged();
        });
    }

    private void loadNotifications() {
        // 从 TinyDB 加载通知
        notifications = tinyDB.getListString("NotificationList");
        if (notifications.isEmpty()) {
            notifications.add("No notifications available.");
        }

        // 设置适配器
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notifications);
        notificationListView.setAdapter(adapter);
    }
}

