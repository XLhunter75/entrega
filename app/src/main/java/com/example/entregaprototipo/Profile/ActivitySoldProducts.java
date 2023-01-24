package com.example.entregaprototipo.Profile;

import static com.example.entregaprototipo.Shop.ActivityMainShop.LOGGED_USER;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MDATABASE;
import static com.example.entregaprototipo.Shop.FragmentHome.REGISTERED_PRODUCTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.entregaprototipo.Adapters.AdpProducts;
import com.example.entregaprototipo.ProductModel.BuyerSimpleData;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.ProductModel.PurchasedData;
import com.example.entregaprototipo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivitySoldProducts extends AppCompatActivity {

    private Button btBack;
    private ArrayList<ProductData> sold_products;
    private ArrayList<PurchasedData> list_sold;
    private ArrayList<BuyerSimpleData> buyer_simple_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold_products);

        btBack = findViewById(R.id.btGoBack);

        sold_products = new ArrayList<>();
        list_sold = new ArrayList<>();
        buyer_simple_data = new ArrayList<>();

        fillSoldProducts();

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void fillSoldProducts(){

        MDATABASE.child("Purchased").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot product: snapshot.getChildren()){

                    String buyer_uid = "";
                    String buyer_profile_url = "";
                    String buyer_name = "";

                    String seller_uid = "";
                    String seller_profile_url = "";
                    String seller_name = "";

                    String product_id = "";
                    Double product_price = 0.0;
                    String product_name = "";
                    String product_image_url = "";

                    String purchase_id = product.getKey();

                    for (DataSnapshot product_data: product.getChildren()){

                        if(product_data.getKey().equals("Buyer_profile")){
                            buyer_profile_url = product_data.getValue().toString();
                        }
                        else if(product_data.getKey().equals("Buyer_uid")){
                            buyer_uid = product_data.getValue().toString();
                        }
                        else if(product_data.getKey().equals("Buyer_name")){
                            buyer_name = product_data.getValue().toString();
                        }
                        else if(product_data.getKey().equals("Seller_name")){
                            seller_name = product_data.getValue().toString();
                        }
                        else if(product_data.getKey().equals("Price")){
                            String price_string = product_data.getValue().toString();
                            product_price = Double.parseDouble(price_string);
                        }
                        else if(product_data.getKey().equals("Product")){
                            product_name = product_data.getValue().toString();
                        }
                        else if(product_data.getKey().equals("Product_id")){
                            product_id = product_data.getValue().toString();
                        }
                        else if(product_data.getKey().equals("Product_image_url")){
                            product_image_url = product_data.getValue().toString();
                        }
                        else if(product_data.getKey().equals("Seller_profile")){
                            seller_profile_url = product_data.getValue().toString();
                        }
                        else if(product_data.getKey().equals("Seller_uid")){
                            seller_uid = product_data.getValue().toString();
                        }
                    }

                    list_sold.add(new PurchasedData(purchase_id,buyer_uid, buyer_profile_url, seller_uid, seller_profile_url, product_id, product_price, product_name, product_image_url, buyer_name, seller_name));

                }

                for(PurchasedData product: list_sold){
                    if(product.getSeller_uid().equals(LOGGED_USER.getUid())){
                        String id_product = product.getProduct_id();
                        for(ProductData search: REGISTERED_PRODUCTS){
                            if(search.getProduct_id().equals(id_product)){
                                sold_products.add(search);

                                String buyer = product.getBuyer_name();
                                String buyer_uid = product.getBuyer_uid();
                                String id_p = search.getProduct_id();

                                buyer_simple_data.add(new BuyerSimpleData(buyer, buyer_uid, id_p));

                            }
                        }
                    }
                }
                createRecycleProducts(sold_products, buyer_simple_data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void createRecycleProducts(ArrayList<ProductData> list_product, ArrayList<BuyerSimpleData> buyer_datas){
        AdpProducts adpProducts_adaptor_2 = new AdpProducts(ActivitySoldProducts.this, list_product,false,false, false ,true, false, false,  buyer_datas);
        RecyclerView recyclerViewPopular = findViewById(R.id.recycleViewSoldProducts);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(ActivitySoldProducts.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewPopular.setAdapter(adpProducts_adaptor_2);
    }

}