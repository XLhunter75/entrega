package com.example.entregaprototipo.Shop;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import static com.example.entregaprototipo.Shop.ActivityMainShop.LOGGED_USER;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MDATABASE;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MTSTORAGE;
import static com.example.entregaprototipo.Shop.ActivityMainShop.USER_UID;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entregaprototipo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentAddProduct extends Fragment  implements View.OnClickListener {

    private EditText etName, etDescription, etPrice;
    private ImageButton minusBtn, plusBtn;
    private TextView tvItemCount;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8;
    private int selected_image = 0;
    private Button btSave;
    private Spinner spinnerCategory;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Uri imagenUri;
    private boolean[] position_used = new boolean[9];
    private ArrayList<Uri> used_uri = new ArrayList<>();

    private boolean product_save;
    private int product_count, itemCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_product, container, false);

        itemCount = 0;

        etName = v.findViewById(R.id.etNombreAgregar);
        etDescription = v.findViewById(R.id.etDescripcionAgregar);
        etPrice = v.findViewById(R.id.etPrecioAgregar);

        imageView1 = v.findViewById(R.id.imageAgregar);
        imageView2 = v.findViewById(R.id.imageAgregar2);
        imageView3 = v.findViewById(R.id.imageAgregar3);
        imageView4 = v.findViewById(R.id.imageAgregar4);
        imageView5 = v.findViewById(R.id.imageAgregar5);
        imageView6 = v.findViewById(R.id.imageAgregar6);
        imageView7 = v.findViewById(R.id.imageAgregar7);
        imageView8 = v.findViewById(R.id.imageAgregar8);
        tvItemCount = v.findViewById(R.id.tvItemCount);
        minusBtn = v.findViewById(R.id.decrementBtn);
        plusBtn = v.findViewById(R.id.incrementBtn);

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemCount++;
                tvItemCount.setText("" + itemCount);
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemCount <= 0) {
                    itemCount = 0;
                }
                else {
                    itemCount--;
                }
                tvItemCount.setText("" + itemCount);
            }
        });

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        imageView8.setOnClickListener(this);


        btSave = v.findViewById(R.id.btGuardarDB);
        product_save = false;

        spinnerCategory = v.findViewById(R.id.spinnerCategoria);
        String[] categorias = {"Electronica","Ropa","Libro/Cine/Musica","Coleccion","Servicios","Otros"};
        ArrayAdapter<String> adaptador_spinner = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, categorias);
        spinnerCategory.setAdapter(adaptador_spinner);


        MDATABASE.child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    if(data.getKey().equals("countProduct")){
                        product_count = Integer.parseInt(data.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Cargador de imagenes
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    //Obtiene datos de imagen
                    Intent data = result.getData();
                    imagenUri = data.getData();
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    try {
                        //Dependiendo de que version sea, ejecutara un codigo u otro
                        Bitmap bitmap;
                        if(Build.VERSION.SDK_INT < 28) {
                            checkPositionIsUsed(selected_image);
                            position_used[selected_image] = true;
                            used_uri.add(imagenUri);
                            switch (selected_image){
                                case 1:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView1.setImageBitmap(bitmap);
                                    break;
                                case 2:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView2.setImageBitmap(bitmap);
                                    break;
                                case 3:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView3.setImageBitmap(bitmap);
                                    break;
                                case 4:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView4.setImageBitmap(bitmap);
                                    break;
                                case 5:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView5.setImageBitmap(bitmap);
                                    break;
                                case 6:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView6.setImageBitmap(bitmap);
                                    break;
                                case 7:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView7.setImageBitmap(bitmap);
                                    break;
                                case 8:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView8.setImageBitmap(bitmap);
                                    break;
                            }
                        }
                        else{
                            ImageDecoder.Source source = ImageDecoder.createSource(contentResolver,imagenUri);
                            bitmap = ImageDecoder.decodeBitmap(source);
                            checkPositionIsUsed(selected_image);
                            position_used[selected_image] = true;
                            used_uri.add(imagenUri);
                            switch (selected_image){
                                case 1:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView1.setImageBitmap(bitmap);
                                    break;
                                case 2:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView2.setImageBitmap(bitmap);
                                    break;
                                case 3:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView3.setImageBitmap(bitmap);
                                    break;
                                case 4:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView4.setImageBitmap(bitmap);
                                    break;
                                case 5:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView5.setImageBitmap(bitmap);
                                    break;
                                case 6:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView6.setImageBitmap(bitmap);
                                    break;
                                case 7:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView7.setImageBitmap(bitmap);
                                    break;
                                case 8:
                                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri);
                                    imageView8.setImageBitmap(bitmap);
                                    break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(result.getResultCode() == RESULT_CANCELED){
                    Toast.makeText(FragmentAddProduct.this.getContext(),"Cancelado...",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_product = etName.getText().toString();
                String description = etDescription.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();
                String price_product = etPrice.getText().toString();

                if(name_product.isEmpty()){
                    Toast.makeText(FragmentAddProduct.this.getContext(),  R.string.product_name_empty, Toast.LENGTH_SHORT).show();
                    etName.requestFocus();
                    return;
                }
                else if(description.isEmpty()){
                    Toast.makeText(FragmentAddProduct.this.getContext(), R.string.product_description_empty, Toast.LENGTH_SHORT).show();
                    etDescription.requestFocus();
                    return;
                }
                else if(category.isEmpty()){
                    Toast.makeText(FragmentAddProduct.this.getContext(), R.string.product_category_empty, Toast.LENGTH_SHORT).show();
                    spinnerCategory.requestFocus();
                    return;
                }
                else if(price_product.isEmpty()){
                    Toast.makeText(FragmentAddProduct.this.getContext(), R.string.product_category_empty, Toast.LENGTH_SHORT).show();
                    etPrice.requestFocus();
                    return;
                }
                else if(used_uri.size() == 0){
                    Toast.makeText(FragmentAddProduct.this.getContext(), R.string.product_image_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    //Preparar la pantalla de carga
                    AlertDialog loading_dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(FragmentAddProduct.this.getContext());
                    builder.setCancelable(false);

                    //Preparar para agregar el layout
                    LayoutInflater inflater = getLayoutInflater();
                    v = inflater.inflate(R.layout.resource_alertdialog_loading, null);

                    //Configurando el layout en el view
                    builder.setView(v);
                    loading_dialog = builder.create();

                    //Mostrar el layout
                    loading_dialog.show();

                    //Agregacion de producto
                    product_count++;
                    MDATABASE.child("Productos").child("Product"+Integer.toString(product_count)).child("Product").setValue(name_product);
                    MDATABASE.child("Productos").child("Product"+Integer.toString(product_count)).child("Description").setValue(description);
                    MDATABASE.child("Productos").child("Product"+Integer.toString(product_count)).child("Category").setValue(category);
                    MDATABASE.child("Productos").child("Product"+Integer.toString(product_count)).child("Price").setValue(price_product);
                    MDATABASE.child("Productos").child("Product"+Integer.toString(product_count)).child("User_Name").setValue(LOGGED_USER.getName());
                    MDATABASE.child("Productos").child("Product"+Integer.toString(product_count)).child("User_UID").setValue(USER_UID);
                    MDATABASE.child("Productos").child("Product"+Integer.toString(product_count)).child("SellerProfile").setValue(LOGGED_USER.getProfileURL());
                    MDATABASE.child("Productos").child("Product"+Integer.toString(product_count)).child("Available").setValue(true);
                    MDATABASE.child("Productos").child("Product"+Integer.toString(product_count)).child("liked_users").setValue("");
                    MDATABASE.child("Productos").child("countProduct").setValue(Integer.toString(product_count));
                    int number_image = 0;
                    if(used_uri != null){
                        for(Uri data_image: used_uri){
                            number_image++;
                            String number_string_image =  Integer.toString(number_image);
                            StorageReference ubicationImagen = MTSTORAGE.child("Productos").child("Product"+Integer.toString(product_count)).child("Image"+number_string_image+".png");
                            ubicationImagen.putFile(data_image).addOnSuccessListener(taskSnapshot -> ubicationImagen.getDownloadUrl().addOnCompleteListener(task2 -> {
                                Uri imageURL = task2.getResult();
                                MDATABASE.child("Productos").child("Product"+Integer.toString(product_count)).child("Image"+number_string_image).setValue(imageURL.toString());
                                if(!product_save){
                                    product_save = true;
                                    loading_dialog.dismiss();
                                    Toast.makeText(FragmentAddProduct.this.getContext(), R.string.product_added, Toast.LENGTH_SHORT).show();
                                }
                            }));
                        }
                    }

                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
    //Cada imagen
    @Override
    public void onClick(View v){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        switch (v.getId()){
            case R.id.imageAgregar:
                selected_image = 1;
                activityResultLauncher.launch(i);
                break;
            case R.id.imageAgregar2:
                selected_image = 2;
                activityResultLauncher.launch(i);
                break;
            case R.id.imageAgregar3:
                selected_image = 3;
                activityResultLauncher.launch(i);
                break;
            case R.id.imageAgregar4:
                selected_image = 4;
                activityResultLauncher.launch(i);
                break;
            case R.id.imageAgregar5:
                selected_image = 5;
                activityResultLauncher.launch(i);
                break;
            case R.id.imageAgregar6:
                selected_image = 6;
                activityResultLauncher.launch(i);
                break;
            case R.id.imageAgregar7:
                selected_image = 7;
                activityResultLauncher.launch(i);
                break;
            case R.id.imageAgregar8:
                selected_image = 8;
                activityResultLauncher.launch(i);
                break;
        }
    }

    public void checkPositionIsUsed(int position){
        if(position_used[position] == true){
            used_uri.remove(position);
        }
    }
}