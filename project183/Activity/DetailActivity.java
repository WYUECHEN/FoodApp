package com.example.project183.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.project183.Domain.Foods;
import com.example.project183.Helper.ManagmentCart;
import com.example.project183.Helper.TinyDB;
import com.example.project183.R;
import com.example.project183.databinding.ActivityDetailBinding;

import java.util.ArrayList;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;
    private ManagmentCart managmentCart;
    private boolean isFavorite = false;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 初始化 TinyDB
        tinyDB = new TinyDB(this);


        getIntentExtra();
        setVariable();

        // 初始化收藏状态
        isFavorite = tinyDB.getBoolean("favourite_" + object.getTitle());
        updateFavouriteIcon();
        Log.d("DetailActivity", "Initial favourite state: " + isFavorite);
//new code
        binding.faveBtn.setOnClickListener(v -> {
            Log.d("DetailActivity", "Favourite button clicked!");
            if (object != null) {
                if (isFavorite) {
                    removeFromFavorites(object);
                } else {
                    addToFavorites(object);
                }
                isFavorite = !isFavorite; // 切换状态
                updateFavouriteIcon();    // 更新 UI 图标
                Log.d("DetailActivity", "Updated favourite state: " + isFavorite);
            } else {
                Log.e("DetailActivity", "Food object is null!");
            }
        });


    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);

        binding.backBtn.setOnClickListener(v -> finish());

        Glide.with(this)
                .load(object.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(60))
                .into(binding.pic);

        binding.priceTxt.setText("$" + object.getPrice());
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.ratingTxt.setText(object.getStar() + " Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText((num * object.getPrice() + "$"));

        binding.plusBtn.setOnClickListener(v -> {
            num = num + 1;
            binding.numTxt.setText(num + "");
            binding.totalTxt.setText("$" + (num * object.getPrice()));
        });

        binding.minusBtn.setOnClickListener(v -> {
            if (num > 1) {
                num = num - 1;
                binding.numTxt.setText(num + "");
                binding.totalTxt.setText("$" + (num * object.getPrice()));
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            object.setNumberInCart(num);
            managmentCart.insertFood(object);
        });






    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
        if (object == null) {
            Toast.makeText(this, "Error: Food data is missing!", Toast.LENGTH_SHORT).show();
            finish(); // 防止崩溃，直接关闭当前 Activity
        }
    }




    private void updateFavouriteIcon() {
        if (isFavorite) {
            binding.faveBtn.setImageResource(R.mipmap.favourite_filled); // 红色
        } else {
            binding.faveBtn.setImageResource(R.drawable.favorite); // 灰色
        }
    }

    private void addToFavorites(Foods food) {
        //new code
        Log.d("DetailActivity", "Adding to favourites: " + food.getTitle());
        ArrayList<Foods> favourites = tinyDB.getListObject("favouriteList", Foods.class);
        if (favourites == null) {
            favourites = new ArrayList<>();
        }
        favourites.add(food);
        tinyDB.putListObject("favouriteList", favourites);
        tinyDB.putBoolean("favourite_" + food.getTitle(), true);
        //New code
        Log.d("DetailActivity", "Favourite list size: " + favourites.size());
        Toast.makeText(this, "Added to Favourites", Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorites(Foods food) {
        Log.d("DetailActivity", "Removing from favourites: " + food.getTitle());
        ArrayList<Foods> favourites = tinyDB.getListObject("favouriteList", Foods.class);
        if (favourites != null) {
            favourites.removeIf(f -> f.getTitle().equals(food.getTitle()));
            tinyDB.putListObject("favouriteList", favourites);
        }
        tinyDB.putBoolean("favourite_" + food.getTitle(), false);
        Toast.makeText(this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
    }


}