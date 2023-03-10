package com.example.entregaprototipo.Shop;

import static com.example.entregaprototipo.Shop.ActivityMainShop.ALL_USERS;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MDATABASE;
import static com.example.entregaprototipo.Shop.ActivityMainShop.USER_UID;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.entregaprototipo.Adapters.AdpProducts;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.example.entregaprototipo.UserModel.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentHome extends Fragment {

    public static ArrayList<ProductData> REGISTERED_PRODUCTS;
    private ArrayList<ProductData> all_products;
    private ArrayList<ProductData> not_my_products;
    private ArrayList<ProductData> random_popular;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Button btSearched;
    private EditText etWord;

    private ImageSlider imageSlider;
    private List<SlideModel> slideModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        btSearched = v.findViewById(R.id.btSearch);
        etWord = v.findViewById(R.id.etSearch);

        all_products = new ArrayList<>();
        random_popular = new ArrayList<>();
        not_my_products = new ArrayList<>();

        imageSlider = v.findViewById(R.id.slider);
        slideModels = new ArrayList<>();

        swipeRefreshLayout = v.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                all_products = new ArrayList<>();
                random_popular = new ArrayList<>();
                not_my_products = new ArrayList<>();
                slideModels = new ArrayList<>();

                createRecycleProductsA(v);
                fillProducts(v);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        fillProducts(v);

        btSearched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word_search = etWord.getText().toString();

                if(word_search.isEmpty()){
                    return;
                }
                else{
                    Intent i = new Intent(FragmentHome.this.getContext(), ActivityResultSearch.class);
                    i.putExtra("wordSearch", word_search);
                    startActivity(i);
                    etWord.setText("");
                }

            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    public void fillProducts(@NonNull View v){
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
                                product_description, product_category, product_price, url_main_image_data, product_available,
                                users_liked, seller_profile,ammount_aviable);

                        if(!uid_user.equals(USER_UID) && product_available == true){
                            not_my_products.add(new_product);
                        }
                        all_products.add(new_product);
                    }
                }

                REGISTERED_PRODUCTS = all_products;

                //Elegir de manera aleatoria productos destacados
                Random r = new Random();
                ArrayList<Integer> used_product = new ArrayList<>();
                for(int i = 0; i < 3; i++){
                    int selected_product = r.nextInt(not_my_products.size());
                    while (used_product.contains(selected_product)) {
                        selected_product = r.nextInt(not_my_products.size());
                    }
                    used_product.add(selected_product);
                    random_popular.add(not_my_products.get(selected_product));

                    //A??ADE IMAGENES AL SLIDER
                    slideModels.add(new SlideModel(random_popular.get(i).getUrl_set_image_data().get(0), random_popular.get(i).getProduct_name(), ScaleTypes.CENTER_INSIDE));
                }

                createRecycleProductsA(v);
                loading_dialog.dismiss();
                //LISTA DE IMAGENES DEL SLIDER
                imageSlider.setImageList(slideModels);


                //IMAGESLIDER CLICK LISTENER
                imageSlider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
                        Intent a = new Intent(v.getContext(), ActivityProductInfo.class);
                        a.putExtra("id_product", random_popular.get(i).getProduct_id());
                        v.getContext().startActivity(a);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void createRecycleProductsA(@NonNull View v){
        AdpProducts adpProducts_adaptor = new AdpProducts(v.getContext(), not_my_products,false,false, false ,false,false, true,  new ArrayList<>());
        RecyclerView recyclerViewMain = v.findViewById(R.id.mainRecycleViewProducts);
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewMain.setAdapter(adpProducts_adaptor);

        AdpProducts adpProducts_adaptor_2 = new AdpProducts(v.getContext(), random_popular,false,true, false ,false, false, true,  new ArrayList<>());
        RecyclerView recyclerViewPopular = v.findViewById(R.id.popularRecycleViewProducts);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPopular.setAdapter(adpProducts_adaptor_2);
        System.out.println(random_popular.size());
    }
}