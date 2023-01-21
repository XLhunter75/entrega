package com.example.entregaprototipo.Profile;

import static com.example.entregaprototipo.Shop.ActivityMainShop.USER_UID;
import static com.example.entregaprototipo.Shop.FragmentHome.REGISTERED_PRODUCTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.entregaprototipo.Adapters.AdpProducts;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ActivityMyProducts extends AppCompatActivity {

    private Button btBack;
    private ArrayList<ProductData> myProduct_list, sold_products, not_sold_products;
    private TabLayout tabLayoutProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_produtcs);

        //Conseguir el view del activity
        View v = findViewById(android.R.id.content).getRootView();

        myProduct_list = new ArrayList<>();
        sold_products = new ArrayList<>();
        not_sold_products = new ArrayList<>();

        tabLayoutProducts = findViewById(R.id.tablayoutMyProducts);
        btBack = findViewById(R.id.btGoBack);

        fillYourProducts();
        showMyProduct(v);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayoutProducts.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showMyProduct(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public void onBackPressed(){
        finish();
    }

    public void createRecycleProducts(@NonNull View v, ArrayList<ProductData> list_product){
        AdpProducts adpProducts_adaptor_2 = new AdpProducts(v.getContext(), list_product,false,false);
        RecyclerView recyclerViewPopular = v.findViewById(R.id.recycleViewMyProducts);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewPopular.setAdapter(adpProducts_adaptor_2);
    }

    public void fillYourProducts(){
        for(ProductData product: REGISTERED_PRODUCTS){
            if(product.getUid_user().equals(USER_UID)){
                myProduct_list.add(product);
                if(product.isProduct_available()){
                    not_sold_products.add(product);
                }
                else{
                    sold_products.add(product);
                }
            }
        }
    }


    public void showMyProduct(@NonNull View v){
        switch (tabLayoutProducts.getSelectedTabPosition()){
            case 0: createRecycleProducts(v,myProduct_list);break;
            case 1: createRecycleProducts(v,not_sold_products);break;
            case 2: createRecycleProducts(v,sold_products);break;
        }
    }

}