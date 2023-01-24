package com.example.entregaprototipo.Shop;

import static com.example.entregaprototipo.Shop.ActivityMainShop.ALL_USERS;
import static com.example.entregaprototipo.Shop.FragmentHome.REGISTERED_PRODUCTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.entregaprototipo.Adapters.AdpProducts;
import com.example.entregaprototipo.Adapters.AdpUsers;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.example.entregaprototipo.UserModel.UserData;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ActivityResultSearch extends AppCompatActivity {

    private ArrayList<UserData> found_users;
    private ArrayList<ProductData> found_products;
    private String wordSearch;
    private TabLayout tabLayoutProducts;
    private Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        found_products = new ArrayList<>();
        found_users = new ArrayList<>();

        tabLayoutProducts = findViewById(R.id.tabLayoutFounds);
        btBack = findViewById(R.id.btGoBack);

        Bundle extras = getIntent().getExtras();
        wordSearch = extras.getString("wordSearch");

        fillFoundUser();
        fillFoundProducts();

        //Rellenarlo por 1ra vez
        createRecycleProducts(found_products);
            tabLayoutProducts.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getPosition() == 0){
                        createRecycleProducts(found_products);
                    } else if (tab.getPosition() == 1) {
                        createRecycleUsers(found_users);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void fillFoundUser(){
        for(UserData user: ALL_USERS){
            if(user.getName().contains(wordSearch)){
                found_users.add(user);
            }
        }
    }
    public void fillFoundProducts(){
        for(ProductData product: REGISTERED_PRODUCTS){
            if(product.getProduct_name().contains(wordSearch)){
                found_products.add(product);
            }
        }
    }

    public void createRecycleProducts(ArrayList<ProductData> list_product){
        AdpProducts adpProducts_adaptor_2 = new AdpProducts(ActivityResultSearch.this, list_product,false,false, false, false, false,true,  new ArrayList<>());
        RecyclerView recyclerViewPopular = findViewById(R.id.resultsRecycleView);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewPopular.setAdapter(adpProducts_adaptor_2);
    }

    public void createRecycleUsers(ArrayList<UserData> list_users){
        AdpUsers adpProducts_adaptor_2 = new AdpUsers(ActivityResultSearch.this, list_users);
        RecyclerView recyclerViewPopular = findViewById(R.id.resultsRecycleView);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewPopular.setAdapter(adpProducts_adaptor_2);
    }

}