package com.example.project183.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.project183.Adapter.CategoryAdapter;
import com.example.project183.Adapter.SliderAdapter;
import com.example.project183.Domain.Category;
import com.example.project183.Domain.SliderItems;
import com.example.project183.Helper.TinyDB;
import com.example.project183.R;
import com.example.project183.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    private TinyDB tinyDB;
    private Runnable onDestroyListener;
    private BroadcastReceiver notificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tinyDB = new TinyDB(this);
        updateNotificationSign();
        BroadcastReceiver notificationReceiver = new BroadcastReceiver() { //
            @Override
            public void onReceive(Context context, Intent intent) {
                updateNotificationSign(); // 接收到广播时更新通知标记
            }
        };

        IntentFilter filter = new IntentFilter("UPDATE_NOTIFICATION_SIGN");



        //待修改 因为与Cart的listener发生了冲突
//        ChipNavigationBar bottomMenu = findViewById(R.id.bottomMenu);
//        bottomMenu.setOnItemSelectedListener(i -> {
//            if (i == R.id.favorites) {
//                Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
//                startActivity(intent);
//            }
//        });




        //If method 可以正常跳转
        binding.bottomMenu.setOnItemSelectedListener(i -> {
            if (i == R.id.cart) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            } else if (i == R.id.favorites) {
                startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
            }
        });



        initCategory();
        initBanner();
//        setVariable();

        ImageView notificationBell = findViewById(R.id.imageView6);

        notificationBell.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });


// 铃铛图标点击事件
        binding.imageView6.setOnClickListener(v -> {
            // 设置通知为已读
            tinyDB.putBoolean("hasUnreadNotifications", false);

            // 更新通知标记状态
            updateNotificationSign();

            // 跳转到通知页面
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        onDestroyListener = () -> unregisterReceiver(notificationReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (onDestroyListener != null) {
            onDestroyListener.run(); // 确保广播接收器被注销
        }
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banners");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewpager2.setAdapter(new SliderAdapter(items, binding.viewpager2));
        binding.viewpager2.setClipChildren(false);
        binding.viewpager2.setClipToPadding(false);
        binding.viewpager2.setOffscreenPageLimit(3);
        binding.viewpager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewpager2.setPageTransformer(compositePageTransformer);
    }


    //待修改

//    private void setVariable() {
//        binding.bottomMenu.setItemSelected(R.id.home, true);
//        binding.bottomMenu.setOnItemSelectedListener(i -> {
//            if(i==R.id.cart){
//                startActivity(new Intent(MainActivity.this, CartActivity.class));
//            }
//        });
//    }



    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Category.class));
                    }
                    if (list.size() > 0) {
                        binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                        binding.categoryView.setAdapter(new CategoryAdapter(list));
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //SignUpdating Method
    private void updateNotificationSign() {
        boolean hasUnread = tinyDB.getBoolean("hasUnreadNotifications");
        if (hasUnread) {
            binding.NotificationSignTxt.setVisibility(View.VISIBLE); // 显示 !!
        } else {
            binding.NotificationSignTxt.setVisibility(View.GONE); // 隐藏 !!
        }
    }




}