package com.example.entregaprototipo.Shop;

import static com.example.entregaprototipo.Shop.ActivityMainShop.MDATABASE;
import static com.example.entregaprototipo.Shop.ActivityMainShop.USER_UID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.entregaprototipo.Adapters.AdpProducts;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.example.entregaprototipo.UserModel.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class ActivityUserInfo extends AppCompatActivity {

    private Button btBack, btChat;

    private ImageView sellerProfile;
    private ArrayList<ProductData> sellers_product;
    private TextView tvSellersName;
    private String seller_uid;
    private UserData seller_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        sellers_product = new ArrayList<>();

        btBack = findViewById(R.id.btBack);
        btChat = findViewById(R.id.seller_chat_button);
        sellerProfile = findViewById(R.id.seller_profile);
        tvSellersName = findViewById(R.id.seller_name);

        Bundle extras = getIntent().getExtras();
        seller_uid = extras.getString("seller_uid");

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

        getSellersProduct();
        getSellerData();
    }

    public void getSellerData(){
        MDATABASE.child("FireBaseUsers").child(seller_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData seller_data = new UserData("NO_UID","NO_NAME","NO_MAIL",false,0.00,0,"NO_ADDRESS",0,false,"NO_URL");
                for(DataSnapshot data: snapshot.getChildren()){
                    if(data.getKey().equals("address")){
                        seller_data.setAddress(data.getValue().toString());
                    }
                    else if(data.getKey().equals("name")){
                        seller_data.setName(data.getValue().toString());
                        tvSellersName.setText(data.getValue().toString());
                    }
                    else if(data.getKey().equals("email")){
                        seller_data.setMail(data.getValue().toString());
                        seller_data.setGoogleAccount(false);
                    }
                    else if(data.getKey().equals("emailGoogle")){
                        seller_data.setMail(data.getValue().toString());
                        seller_data.setGoogleAccount(true);
                    }
                    else if(data.getKey().equals("money")){
                        seller_data.setCash(Double.parseDouble(data.getValue().toString()));
                    }
                    else if(data.getKey().equals("phone")){
                        seller_data.setPhoneNumber(Integer.parseInt(data.getValue().toString()));
                    }
                    else if(data.getKey().equals("countProduct")){
                        seller_data.setCountProduct(Integer.parseInt(data.getValue().toString()));
                    }
                    else if(data.getKey().equals("isRememberMe")){
                        if(data.getValue().toString().equals("true")){
                            seller_data.setRememberMe(true);
                        }
                        else if(data.getValue().toString().equals("false")){
                            seller_data.setRememberMe(false);
                        }
                    }
                    else if(data.getKey().equals("pic")){
                        seller_data.setProfileURL(data.getValue().toString());
                        Uri product_image = Uri.parse(data.getValue().toString());
                        Glide.with(getApplicationContext()).load(String.valueOf(product_image)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(sellerProfile);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        MDATABASE.child("GoogleUsers").child(seller_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData seller_data = new UserData("NO_UID","NO_NAME","NO_MAIL",false,0.00,0,"NO_ADDRESS",0,false,"NO_URL");
                for(DataSnapshot data: snapshot.getChildren()){
                    if(data.getKey().equals("address")){
                        seller_data.setAddress(data.getValue().toString());
                    }
                    else if(data.getKey().equals("name")){
                        seller_data.setName(data.getValue().toString());
                        tvSellersName.setText(data.getValue().toString());
                    }
                    else if(data.getKey().equals("email")){
                        seller_data.setMail(data.getValue().toString());
                        seller_data.setGoogleAccount(false);
                    }
                    else if(data.getKey().equals("emailGoogle")){
                        seller_data.setMail(data.getValue().toString());
                        seller_data.setGoogleAccount(true);
                    }
                    else if(data.getKey().equals("money")){
                        seller_data.setCash(Double.parseDouble(data.getValue().toString()));
                    }
                    else if(data.getKey().equals("phone")){
                        seller_data.setPhoneNumber(Integer.parseInt(data.getValue().toString()));
                    }
                    else if(data.getKey().equals("countProduct")){
                        seller_data.setCountProduct(Integer.parseInt(data.getValue().toString()));
                    }
                    else if(data.getKey().equals("isRememberMe")){
                        if(data.getValue().toString().equals("true")){
                            seller_data.setRememberMe(true);
                        }
                        else if(data.getValue().toString().equals("false")){
                            seller_data.setRememberMe(false);
                        }
                    }
                    else if(data.getKey().equals("pic")){
                        seller_data.setProfileURL(data.getValue().toString());
                        Uri product_image = Uri.parse(data.getValue().toString());
                        Glide.with(ActivityUserInfo.this).load(String.valueOf(product_image)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(sellerProfile);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getSellersProduct(){

        MDATABASE.child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot product : snapshot.getChildren()){
                    if(product.getKey().equals("countProduct")){
                        //Agregar codigo para cantidad de producto disponible
                    }
                    else{
                        String product_id = "";
                        String user_name = "";
                        String uid_user = "";
                        String product_name = "";
                        String product_description = "";
                        String product_category = "";
                        double product_price = 0.00;
                        ArrayList<String> url_main_image_data = new ArrayList<>();
                        ArrayList<String> users_liked = new ArrayList<>();
                        boolean product_available = false;
                        String seller_profile = "";
                        int ammount_aviable = 0;

                        for(DataSnapshot data : product.getChildren()){
                            if (data.getKey().equals("Category")) {
                                product_category = data.getValue().toString();
                            }
                            else if (data.getKey().equals("Description")) {
                                product_description = data.getValue().toString();
                            }
                            else if (data.getKey().equals("Price")) {
                                product_price = Double.parseDouble(data.getValue().toString());
                            }
                            else if (data.getKey().equals("Product")) {
                                product_name = data.getValue().toString();
                            }
                            else if (data.getKey().equals("Available")) {
                                if (data.getValue().toString().equals("true")) {
                                    product_available = true;
                                } else {
                                    product_available = false;
                                }
                            }
                            else if (data.getKey().equals("User_Name")) {
                                user_name = data.getValue().toString();
                            }
                            else if (data.getKey().equals("User_UID")) {
                                uid_user = data.getValue().toString();
                            }
                            else if (data.getKey().equals("Image2") || data.getKey().equals("Image3") || data.getKey().equals("Image4") ||
                                    data.getKey().equals("Image5") || data.getKey().equals("Image6") || data.getKey().equals("Image7") ||
                                    data.getKey().equals("Image8") || data.getKey().equals("Image1")){
                                url_main_image_data.add(data.getValue().toString());
                            }
                            else if (data.getKey().equals("liked_users")){
                                for(DataSnapshot user_liked: data.getChildren()){
                                    String checked_is_liked = user_liked.getValue().toString();
                                    if(checked_is_liked.equals("true")){
                                        users_liked.add(user_liked.getKey());
                                    }
                                }
                            }
                            else if (data.getKey().equals("SellerProfile")) {
                                seller_profile = data.getValue().toString();
                            }
                            else if (data.getKey().equals("Ammount")) {
                                String ammount = data.getValue().toString();
                                ammount_aviable = Integer.parseInt(ammount);
                            }
                        }

                        product_id = product.getKey();
                        ProductData new_product = new ProductData(product_id, user_name, uid_user, product_name,
                                product_description, product_category, product_price, url_main_image_data,
                                product_available, users_liked, seller_profile, ammount_aviable);

                        if(uid_user.equals(seller_uid)){
                            sellers_product.add(new_product);
                        }
                    }
                }
                createRecycleProductsA(sellers_product);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createRecycleProductsA(ArrayList<ProductData> sellers_products){
        AdpProducts adpProducts_adaptor = new AdpProducts(ActivityUserInfo.this, sellers_products,false,false, false ,false, false, true,  new ArrayList<>());
        RecyclerView recyclerViewMain = findViewById(R.id.userProductsRecycleView);
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(ActivityUserInfo.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewMain.setAdapter(adpProducts_adaptor);
    }

}