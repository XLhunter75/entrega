package com.example.entregaprototipo.Shop;

import static com.example.entregaprototipo.Shop.ActivityMainShop.LOGGED_USER;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MDATABASE;
import static com.example.entregaprototipo.Shop.ActivityMainShop.USER_UID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.entregaprototipo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityProductInfo extends AppCompatActivity {

    private Button btBack, btChat, btBuy;
    private TextView etProduct, etPrice, etSeller, etDescription;
    private ImageView sellerProfile;
    private CheckBox cbLike;
    private ImageSlider imagesPack;

    private String product_id;
    private String seller_uid;
    private ArrayList<String> url_main_image_data;
    private List<SlideModel> slideModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        btBack = findViewById(R.id.btBack);
        btChat = findViewById(R.id.seller_chat_button);
        btBuy = findViewById(R.id.bt_buy);
        etProduct = findViewById(R.id.product_name);
        etPrice = findViewById(R.id.product_price);
        etSeller = findViewById(R.id.seller_name);
        etDescription = findViewById(R.id.product_description);
        sellerProfile = findViewById(R.id.seller_profile);
        cbLike = findViewById(R.id.likeBtn);
        imagesPack = findViewById(R.id.productImages);

        Bundle extras = getIntent().getExtras();
        product_id = extras.getString("id_product");

        slideModels = new ArrayList<>();
        url_main_image_data = new ArrayList<>();

        MDATABASE.child("Productos").child(product_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot product_data: snapshot.getChildren()){
                    if (product_data.getKey().equals("Description")) {
                        etDescription.setText(product_data.getValue().toString());
                    }
                    else if (product_data.getKey().equals("Price")) {

                        Double price = Double.parseDouble(product_data.getValue().toString());

                        etPrice.setText(String.format( "%,.2f" ,price) + "€");
                    }
                    else if (product_data.getKey().equals("Product")) {
                        etProduct.setText(product_data.getValue().toString());
                    }
                    else if (product_data.getKey().equals("User_Name")) {
                        etSeller.setText(product_data.getValue().toString());
                    }
                    else if (product_data.getKey().equals("Available")) {
                        if (product_data.getValue().toString().equals("true")) {
                            btBuy.setEnabled(true);
                        } else {
                            btBuy.setEnabled(false);
                            btBuy.setText("Vendido");
                        }
                    }
                    else if (product_data.getKey().equals("SellerProfile")) {
                        Uri product_image = Uri.parse(product_data.getValue().toString());
                        Glide.with(ActivityProductInfo.this).load(String.valueOf(product_image)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(sellerProfile);
                    }
                    else if (product_data.getKey().equals("User_UID")) {
                        seller_uid = product_data.getValue().toString();
                    }
                    else if (product_data.getKey().equals("Image2") || product_data.getKey().equals("Image3") || product_data.getKey().equals("Image4") ||
                            product_data.getKey().equals("Image5") || product_data.getKey().equals("Image6") || product_data.getKey().equals("Image7") ||
                            product_data.getKey().equals("Image8") || product_data.getKey().equals("Image1")){
                        url_main_image_data.add(product_data.getValue().toString());
                    }
                    else if (product_data.getKey().equals("liked_users")){
                        for(DataSnapshot user_liked: product_data.getChildren()){
                            if(user_liked.getValue().toString().equals("true") && user_liked.getKey().equals(USER_UID)){
                                cbLike.setChecked(true);
                            }
                        }
                    }
                }
                for(int i = 0; i < url_main_image_data.size(); i++){
                    slideModels.add(new SlideModel(url_main_image_data.get(i), "", ScaleTypes.CENTER_INSIDE));
                }
                imagesPack.setImageList(slideModels);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sellerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityProductInfo.this, ActivityUserInfo.class);
                i.putExtra("seller_uid", seller_uid);
                v.getContext().startActivity(i);
            }
        });

    }
}