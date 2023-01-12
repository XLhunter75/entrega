package com.example.entregaprototipo.Shop;

import static com.example.entregaprototipo.Shop.FragmentHome.registered_products;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.entregaprototipo.Adapters.AdpShop;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FragmentCategory extends Fragment {

    private TabLayout tbItems;
    private RecyclerView recyclerView;
    private ArrayList<ProductData> list_product;
    private ArrayList<ProductData> list_electronic, list_dress, list_book_music, list_collection, list_service, list_other;

    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;

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
        AdpShop adpShop_adaptor = new AdpShop(v.getContext(), list_show,true,false);
        RecyclerView recyclerViewMain = v.findViewById(R.id.recicleViewProducts2);
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewMain.setAdapter(adpShop_adaptor);
    }

    public void fillProduct(){
        if(registered_products != null){
            list_product = new ArrayList<>();
            list_electronic = new ArrayList<>();
            list_dress = new ArrayList<>();
            list_book_music = new ArrayList<>();
            list_collection = new ArrayList<>();
            list_service = new ArrayList<>();
            list_other = new ArrayList<>();
            for(ProductData product: registered_products){
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