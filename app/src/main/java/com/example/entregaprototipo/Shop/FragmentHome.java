package com.example.entregaprototipo.Shop;

import static com.example.entregaprototipo.Shop.ActivityMainShop.ALL_USERS;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MDATABASE;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.entregaprototipo.Adapters.AdpShop;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.example.entregaprototipo.UserModel.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class FragmentHome extends Fragment {

    public static ArrayList<ProductData> REGISTERED_PRODUCTS;
    public static ArrayList<ProductData> FOUND_PRODUCTS;
    public static ArrayList<UserData> FOUND_USERS;

    private ArrayList<ProductData> all_products;
    private ArrayList<ProductData> random_popular;

    private Button btSearched;
    private EditText etWord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        btSearched = v.findViewById(R.id.btSearch);
        etWord = v.findViewById(R.id.etSearch);

        all_products = new ArrayList<>();
        random_popular = new ArrayList<>();

        //Preparar la pantalla de carga
        AlertDialog loading_dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setCancelable(false);

        //Preparar para agregar el layout
        LayoutInflater inflater2 = getLayoutInflater();
        View loading_v = inflater2.inflate(R.layout.resource_alertdialog_loading, null);
        TextView word_loading = loading_v.findViewById(R.id.tv_loading);
        word_loading.setText(R.string.loading_data);

        //Configurando el layout en el view
        builder.setView(loading_v);
        loading_dialog = builder.create();

        //Recoger todos los productos
        MDATABASE.child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Mostrar el layout
                loading_dialog.show();

                for(DataSnapshot product : snapshot.getChildren()){
                    if(product.getKey().equals("countProduct")){
                        //Agregar codigo para cantidad de producto disponible
                    }
                    else{
                        String user_name = "";
                        String uid_user = "";
                        String product_name = "";
                        String product_description = "";
                        String product_category = "";
                        double product_price = 0.00;
                        ArrayList<String> url_main_image_data = new ArrayList<>();
                        boolean product_available = false;

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
                            else if (data.getKey().equals("name")) {
                                user_name = data.getValue().toString();
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
                            else{
                                //En caso de haber errores un product, por ejemplo que falte un dato
                                break;
                            }
                        }
                        ProductData new_product = new ProductData(user_name, uid_user, product_name, product_description, product_category, product_price, url_main_image_data, product_available);
                        all_products.add(new_product);
                    }
                }

                REGISTERED_PRODUCTS = all_products;

                //Elegir de manera aleatoria productos destacados
                Random r = new Random();
                ArrayList<Integer> used_product = new ArrayList<>();
                for(int i = 0; i < 3; i++){
                    int selected_product = r.nextInt(all_products.size());
                    while (used_product.contains(selected_product)) {
                        selected_product = r.nextInt(all_products.size());
                    }
                    used_product.add(selected_product);
                    random_popular.add(all_products.get(selected_product));
                }

                createRecycleProductsA(v);
                loading_dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btSearched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word_search = etWord.getText().toString();

                if(word_search.isEmpty()){
                    return;
                }
                else{
                    FOUND_PRODUCTS = new ArrayList<>();
                    FOUND_USERS = new ArrayList<>();
                    for(ProductData product: all_products){
                        if(product.getProduct_name().contains(word_search)){
                            FOUND_PRODUCTS.add(product);
                        }
                    }
                    for(UserData user: ALL_USERS){
                        if(user.getName().contains(word_search)){
                            FOUND_USERS.add(user);
                        }
                    }

                }

            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    public void createRecycleProductsA(@NonNull View v){
        AdpShop adpShop_adaptor = new AdpShop(v.getContext(), all_products,false,false);
        RecyclerView recyclerViewMain = v.findViewById(R.id.mainRecycleViewProducts);
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewMain.setAdapter(adpShop_adaptor);

        AdpShop adpShop_adaptor_2 = new AdpShop(v.getContext(), random_popular,false,true);
        RecyclerView recyclerViewPopular = v.findViewById(R.id.popularRecycleViewProducts);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPopular.setAdapter(adpShop_adaptor_2);
    }
}