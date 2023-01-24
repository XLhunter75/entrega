package com.example.entregaprototipo.Shop;

import static com.example.entregaprototipo.Shop.ActivityMainShop.LOGGED_USER;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MDATABASE;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MTSTORAGE;
import static com.example.entregaprototipo.Shop.ActivityMainShop.USER_UID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ActivityProductInfo extends AppCompatActivity {

    private Button btChat, btBuy;

    private ImageButton btBack;
    private TextView etProduct, etPrice, etSeller, etDescription;
    private ImageView sellerProfile;
    private CheckBox cbLike;
    private ImageSlider imagesPack;

    private String product_id;
    private String seller_uid;
    private ArrayList<String> url_main_image_data;
    private List<SlideModel> slideModels = new ArrayList<>();
    private ProductData product;
    private int count_purchased;
    private Double seller_money;
    private Boolean found_seller;

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

        MDATABASE.child("Purchased").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    if(data.getKey().equals("purchase_ammount")){
                        String count_purchased_string = data.getValue().toString();
                        count_purchased = Integer.parseInt(count_purchased_string);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        MDATABASE.child("Productos").child(product_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {                            String product_id = "";
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
                for(DataSnapshot product_data: snapshot.getChildren()){
                    if (product_data.getKey().equals("Category")) {
                        product_category = product_data.getValue().toString();
                    }
                    else if (product_data.getKey().equals("Description")) {
                        etDescription.setText(product_data.getValue().toString());
                        product_description = product_data.getValue().toString();
                    }
                    else if (product_data.getKey().equals("Price")) {
                        product_price = Double.parseDouble(product_data.getValue().toString());

                        Double price = Double.parseDouble(product_data.getValue().toString());

                        etPrice.setText(String.format( "%,.2f" ,price) + "€");
                    }
                    else if (product_data.getKey().equals("Product")) {
                        product_name = product_data.getValue().toString();

                        etProduct.setText(product_data.getValue().toString());
                    }
                    else if (product_data.getKey().equals("User_Name")) {
                        user_name = product_data.getValue().toString();
                        etSeller.setText(product_data.getValue().toString());
                    }
                    else if (product_data.getKey().equals("Available")) {
                        if (product_data.getValue().toString().equals("true")) {
                            btBuy.setEnabled(true);
                            product_available = true;
                        } else {
                            btBuy.setEnabled(false);
                            product_available = false;
                            btBuy.setText("Sin stock");
                        }
                    }
                    else if (product_data.getKey().equals("SellerProfile")) {
                        seller_profile = product_data.getValue().toString();
                        Uri product_image = Uri.parse(product_data.getValue().toString());
                        Glide.with(ActivityProductInfo.this).load(String.valueOf(product_image)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(sellerProfile);
                    }
                    else if (product_data.getKey().equals("User_Name")) {
                        user_name = product_data.getValue().toString();
                    }
                    else if (product_data.getKey().equals("User_UID")) {
                        uid_user = product_data.getValue().toString();
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
                                users_liked.add(user_liked.getKey());
                            }
                        }
                    }
                    else if (product_data.getKey().equals("Ammount")) {
                        String ammount = product_data.getValue().toString();
                        ammount_aviable = Integer.parseInt(ammount);
                    }
                }
                product = new ProductData(product_id, user_name, uid_user, product_name,
                        product_description, product_category, product_price, url_main_image_data, product_available,
                        users_liked, seller_profile,ammount_aviable);

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

                if(product.getProduct_price() > LOGGED_USER.getCash()){

                    Toast.makeText(ActivityProductInfo.this, "No tienes suficiente dinero.", Toast.LENGTH_SHORT).show();
                    return;

                }

                //Preparar la pantalla de carga
                AlertDialog confirm_dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProductInfo.this);
                builder.setCancelable(false);

                //Preparar para agregar el layout
                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.dialog_confirm_purchase, null);

                //Configurando el layout en el view
                builder.setView(v);
                confirm_dialog = builder.create();

                Button confirmBuy = v.findViewById(R.id.btYes);
                Button notConfirmBut = v.findViewById(R.id.btNo);
                //Mostrar el layout
                confirm_dialog.show();

                confirmBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processBuy();
                    }
                });

                notConfirmBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirm_dialog.dismiss();
                    }
                });


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

    public void processBuy(){
        MDATABASE.child("Productos").child(product_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(product.isProduct_available()){
                    for(DataSnapshot info_product: snapshot.getChildren()){
                        if(info_product.getKey().equals("Ammount")){
                            String ammount_aviable = info_product.getValue().toString();
                            int check_ammount = Integer.parseInt(ammount_aviable);
                            if(check_ammount != 0 && check_ammount > 0){

                                if(LOGGED_USER.isGoogleAccount()){

                                    Double cash_user_logged = LOGGED_USER.getCash();
                                    Double real_price_product = product.getProduct_price();
                                    cash_user_logged -= real_price_product;
                                    MDATABASE.child("GoogleUsers").child(LOGGED_USER.getUid()).child("money").setValue(cash_user_logged);
                                    int actual_ammount = product.getAmmount_aviable();
                                    --actual_ammount;

                                    if(actual_ammount == 0){
                                        MDATABASE.child("Productos").child(product_id).child("Available").setValue(false);
                                    }
                                    MDATABASE.child("Productos").child(product_id).child("Ammount").setValue(actual_ammount);
                                    Toast.makeText(ActivityProductInfo.this, "Comprado", Toast.LENGTH_SHORT).show();

                                    updateCashSeller();

                                    updatePurchaseProduct();


                                    finish();
                                }
                                else if (!LOGGED_USER.isGoogleAccount()) {

                                    Double cash_user_logged = LOGGED_USER.getCash();
                                    Double real_price_product = product.getProduct_price();
                                    cash_user_logged -= real_price_product;
                                    MDATABASE.child("FireBaseUsers").child(LOGGED_USER.getUid()).child("money").setValue(cash_user_logged);
                                    int actual_ammount = product.getAmmount_aviable();
                                    --actual_ammount;
                                    MDATABASE.child("Productos").child(product_id).child("Ammount").setValue(actual_ammount);
                                    Toast.makeText(ActivityProductInfo.this, "Comprado", Toast.LENGTH_SHORT).show();

                                    updateCashSeller();

                                    updatePurchaseProduct();

                                    finish();

                                }
                                else{
                                    //Error
                                }

                            }
                            else{
                                Toast.makeText(ActivityProductInfo.this, "Vaya, no queda stock...", Toast.LENGTH_SHORT).show();
                                btBuy.setEnabled(false);
                                btBuy.setText("Sin stock");
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void updatePurchaseProduct(){
        //Agregacion de producto
        count_purchased++;
        MDATABASE.child("Purchased").child("ProductPurchased"+Integer.toString(count_purchased)).child("Product").setValue(product.getProduct_name());
        MDATABASE.child("Purchased").child("ProductPurchased"+Integer.toString(count_purchased)).child("Seller_uid").setValue(product.getUid_user());
        MDATABASE.child("Purchased").child("ProductPurchased"+Integer.toString(count_purchased)).child("Buyer_uid").setValue(LOGGED_USER.getUid());
        MDATABASE.child("Purchased").child("ProductPurchased"+Integer.toString(count_purchased)).child("Price").setValue(product.getProduct_price());
        MDATABASE.child("Purchased").child("ProductPurchased"+Integer.toString(count_purchased)).child("Product_image_url").setValue(product.getUrl_set_image_data().get(0));
        MDATABASE.child("Purchased").child("ProductPurchased"+Integer.toString(count_purchased)).child("Seller_profile").setValue(product.getUser_profile_url());
        MDATABASE.child("Purchased").child("ProductPurchased"+Integer.toString(count_purchased)).child("Buyer_profile").setValue(LOGGED_USER.getProfileURL());
        MDATABASE.child("Purchased").child("ProductPurchased"+Integer.toString(count_purchased)).child("Buyer_name").setValue(LOGGED_USER.getName());
        MDATABASE.child("Purchased").child("ProductPurchased"+Integer.toString(count_purchased)).child("Seller_name").setValue(product.getUser_name_product());
        MDATABASE.child("Purchased").child("ProductPurchased"+Integer.toString(count_purchased)).child("Product_id").setValue(product_id);
        MDATABASE.child("Purchased").child("purchase_ammount").setValue(Integer.toString(count_purchased));
    }

    public void updateCashSeller(){

        MDATABASE.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                //Busqueda de ususarios de firebase
                if(snapshot.getKey().equals("FireBaseUsers")){
                    for(DataSnapshot user: snapshot.getChildren()){
                        if(user.getKey().equals(product.getUid_user())){
                            for(DataSnapshot user_data: user.getChildren()){
                                if(user_data.getKey().equals("money")){
                                    String seller_cash_string = user_data.getValue().toString();
                                    Double seller_cash = Double.parseDouble(seller_cash_string);
                                    seller_cash += product.getProduct_price();
                                    MDATABASE.child("FireBaseUsers").child(product.getUid_user()).child("money").setValue(seller_cash);
                                    break;
                                }
                            }
                        }
                    }
                }

                //Busqueda de usuaarios de firebase
                else if(snapshot.getKey().equals("GoogleUsers")){
                    for(DataSnapshot user: snapshot.getChildren()){
                        if(user.getKey().equals(product.getUid_user())){
                            for(DataSnapshot user_data: user.getChildren()){
                                if(user_data.getKey().equals("money")){
                                    String seller_cash_string = user_data.getValue().toString();
                                    Double seller_cash = Double.parseDouble(seller_cash_string);
                                    seller_cash += product.getProduct_price();
                                    MDATABASE.child("GoogleUsers").child(product.getUid_user()).child("money").setValue(seller_cash);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}