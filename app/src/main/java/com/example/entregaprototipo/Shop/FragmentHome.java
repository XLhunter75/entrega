package com.example.entregaprototipo.Shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.entregaprototipo.Adapters.AdpShop;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    public static ArrayList<ProductData> ALL_PRODUCT = new ArrayList<>();
    private ArrayList<ProductData> random_popular = new ArrayList<>();
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Inflate the layout for this fragment
        return v;
    }

    public void createRecycleProductsA(@NonNull View v){
        AdpShop adpShop_adaptor = new AdpShop(FragmentHome.this.getContext(), ALL_PRODUCT,false,true);
        RecyclerView recyclerViewMain = v.findViewById(R.id.mainRecycleViewProducts);
        recyclerViewMain.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        recyclerViewMain.setAdapter(adpShop_adaptor);

        /*AdpShop adpShop_adaptor_2 = new AdpShop(FragmentHome.this.getContext(), random_popular_products,false,true);
        RecyclerView recyclerViewPopular = v.findViewById(R.id.popularRecycleViewProducts);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPopular.setAdapter(adpShop_adaptor_2);*/
    }
}