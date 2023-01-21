package com.example.entregaprototipo.Shop;

import static com.example.entregaprototipo.Shop.FragmentHome.REGISTERED_PRODUCTS;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.entregaprototipo.Adapters.AdpProducts;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FragmentCategory extends Fragment {

    private TabLayout tbItems;
    private ArrayList<ProductData> list_product;
    private ArrayList<ProductData> list_electronic, list_dress, list_book_music, list_collection, list_service, list_other;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        tbItems = v.findViewById(R.id.tabLayoutCategory);
        //Rellenara y mostrara los productos al iniciar por 1ra vez
        fillProduct();
        showProduct(v);

        tbItems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showProduct(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return v;
    }

    public void createRecycleProductsA(@NonNull View v, ArrayList<ProductData> list_show){
        AdpProducts adpProducts_adaptor = new AdpProducts(v.getContext(), list_show,true,false);
        RecyclerView recyclerViewMain = v.findViewById(R.id.recicleViewProducts2);
        recyclerViewMain.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        recyclerViewMain.setAdapter(adpProducts_adaptor);
    }

    public void fillProduct(){
        if(REGISTERED_PRODUCTS != null){
            list_product = new ArrayList<>();
            list_electronic = new ArrayList<>();
            list_dress = new ArrayList<>();
            list_book_music = new ArrayList<>();
            list_collection = new ArrayList<>();
            list_service = new ArrayList<>();
            list_other = new ArrayList<>();
            for(ProductData product: REGISTERED_PRODUCTS){
                switch (product.getProduct_category()){
                    case "Electronica": list_electronic.add(product);break;
                    case "Ropa": list_dress.add(product);break;
                    case "Libro/Cine/Musica": list_book_music.add(product);break;
                    case "Coleccion": list_collection.add(product);break;
                    case "Servicios": list_service.add(product);break;
                    case "Otros": list_other.add(product);break;
                }
            }
        }
    }

    public void showProduct(@NonNull View v){
        switch (tbItems.getSelectedTabPosition()){
            case 0: createRecycleProductsA(v,list_electronic);break;
            case 1: createRecycleProductsA(v,list_book_music);break;
            case 2: createRecycleProductsA(v,list_dress);break;
            case 3: createRecycleProductsA(v,list_collection);break;
            case 4: createRecycleProductsA(v,list_service);break;
            case 5: createRecycleProductsA(v,list_other);break;
        }
    }
}