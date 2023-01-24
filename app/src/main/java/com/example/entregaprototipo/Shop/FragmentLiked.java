package com.example.entregaprototipo.Shop;

import static com.example.entregaprototipo.Shop.ActivityMainShop.LOGGED_USER;
import static com.example.entregaprototipo.Shop.FragmentHome.REGISTERED_PRODUCTS;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.entregaprototipo.Adapters.AdpProducts;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;

import java.util.ArrayList;

public class FragmentLiked extends Fragment {


    private ArrayList<ProductData> list_product_fav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_liked, container, false);


        list_product_fav = new ArrayList<>();

        fillLikeProducts();
        createRecycleProducts(v, list_product_fav);


        return v;
    }

    public void fillLikeProducts(){
        for(ProductData product: REGISTERED_PRODUCTS){
            for(String uid: product.getUser_liked()){
                if(uid.equals(LOGGED_USER.getUid())){
                    list_product_fav.add(product);
                }
            }
        }
    }

    public void createRecycleProducts(@NonNull View v, ArrayList<ProductData> list_product){
        AdpProducts adpProducts_adaptor_2 = new AdpProducts(v.getContext(), list_product,false,false, false ,false, false, true,  new ArrayList<>());
        RecyclerView recyclerViewPopular = v.findViewById(R.id.recycleViewFavProducts);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewPopular.setAdapter(adpProducts_adaptor_2);
    }
}