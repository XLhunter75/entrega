package com.example.entregaprototipo.Shop;


import static android.app.Activity.RESULT_OK;
import static com.example.entregaprototipo.Shop.ActivityMainShop.LOGGED_USER;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MAUTH;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MDATABASE;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MTSTORAGE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.entregaprototipo.LoginRegister.ActivityLogin;
import com.example.entregaprototipo.Profile.ActivityBoughtProducts;
import com.example.entregaprototipo.Profile.ActivityMyProducts;
import com.example.entregaprototipo.Profile.ActivitySoldProducts;
import com.example.entregaprototipo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class FragmentProfile extends Fragment implements View.OnClickListener{

    private TextView tvName;
    private ImageView ivProfile;
    private Button btBought, btSold, btWallet, btExit, btChangeName, btChangeProfile, btYourProducts;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        tvName = v.findViewById(R.id.profile_name_user);
        ivProfile = v.findViewById(R.id.profile_image_user);

        btBought = v.findViewById(R.id.btBought);
        btSold = v.findViewById(R.id.btSold);
        btWallet = v.findViewById(R.id.btWallet);
        btYourProducts = v.findViewById(R.id.btYourProducts);
        btChangeName = v.findViewById(R.id.btChangeName);
        btChangeProfile = v.findViewById(R.id.btChangePhoto);
        btExit = v.findViewById(R.id.btExit);

        btBought.setOnClickListener(this);
        btExit.setOnClickListener(this);
        btChangeName.setOnClickListener(this);
        btChangeProfile.setOnClickListener(this);
        btYourProducts.setOnClickListener(this);
        btWallet.setOnClickListener(this);
        btSold.setOnClickListener(this);

        //Rellenar el perfil
        tvName.setText(LOGGED_USER.getName());
        Uri product_image = Uri.parse(LOGGED_USER.getProfileURL());
        Glide.with(FragmentProfile.this.getContext()).load(String.valueOf(product_image)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivProfile);


        btWallet.setText("Tienes: "+ String.format( "%,.2f" ,LOGGED_USER.getCash()) + "€");
        tvName.setText(LOGGED_USER.getName());

        //Necesario activarlo otra vez en caso de querer hacer un SignOut, para que cuando salga lo registre en googleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(FragmentProfile.this.getContext(), gso);

        //Necesario para busqueda de imagenes
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK && result.getData() != null){
                //Tras haber elegido una imagen
                Intent data_imagen = result.getData();
                Uri imageUri = data_imagen.getData();
                ContentResolver contentResolver =getActivity().getContentResolver();
                try{
                    if(Build.VERSION.SDK_INT < 28){
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
                        saveImage(imageUri, LOGGED_USER.getUid(), v);
                        ivProfile.setImageBitmap(bitmap);
                    }
                    else{
                        ImageDecoder.Source source = ImageDecoder.createSource(contentResolver,imageUri);
                        saveImage(imageUri, LOGGED_USER.getUid(), v);
                        Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                        ivProfile.setImageBitmap(bitmap);
                    }
                }
                catch (IOException e){
                    System.out.println(e.getMessage());
                }

            }
            else{
                Toast.makeText(FragmentProfile.this.getContext(),"Cancelado...",Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btBought:
                Intent b = new Intent(FragmentProfile.this.getContext(), ActivityBoughtProducts.class);
                startActivity(b);
                break;
            case R.id.btSold:
                Intent d = new Intent(FragmentProfile.this.getContext(), ActivitySoldProducts.class);
                startActivity(d);
                break;
            case R.id.btWallet:
                break;
            case R.id.btYourProducts:
                Intent i = new Intent(FragmentProfile.this.getContext(), ActivityMyProducts.class);
                startActivity(i);
                break;
            case R.id.btChangeName:
                changeName(v);
                break;
            case R.id.btChangePhoto:
                Intent c = new Intent(Intent.ACTION_PICK);
                c.setType("image/*");
                activityResultLauncher.launch(c);
                break;
            case R.id.btExit:
                Intent a = new Intent(FragmentProfile.this.getContext(), ActivityLogin.class);

                if (!LOGGED_USER.isGoogleAccount()) {
                    MAUTH.signOut();
                    Toast.makeText(FragmentProfile.this.getContext(), R.string.exit, Toast.LENGTH_SHORT).show();
                    startActivity(a);
                }

                //Para Google se convierte el fragment en activity y finaliza el signout usando finish()
                else if (LOGGED_USER.isGoogleAccount()) {
                    googleSignInClient.signOut().addOnCompleteListener((Activity) FragmentProfile.this.getContext(), task -> {
                        Toast.makeText(FragmentProfile.this.getContext(), R.string.exit, Toast.LENGTH_SHORT).show();
                        startActivity(a);
                    });
                }
                break;
        }

    }

    public void changeName(View v){
        //Preparar la pantalla de carga
        AlertDialog change_name_dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(FragmentProfile.this.getContext());
        builder.setCancelable(false);

        //Preparar para agregar el layout
        LayoutInflater inflater = getLayoutInflater();
        v = inflater.inflate(R.layout.alert_change_name, null);

        //Configurando el layout en el view
        builder.setView(v);
        change_name_dialog = builder.create();

        change_name_dialog.show();

        TextView tvChangeTitle;
        EditText etChange;
        Button btChange, btCancel;

        tvChangeTitle = v.findViewById(R.id.titleChange);
        tvChangeTitle.setText("Cambiar Nombre");
        etChange = v.findViewById(R.id.etNameToChange);
        etChange.setHint("Nombre");
        btChange = v.findViewById(R.id.btChangeNameDatabase);
        btCancel = v.findViewById(R.id.buttonCancel);

        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name = etChange.getText().toString();
                if(new_name.isEmpty()){
                    Toast.makeText(FragmentProfile.this.getContext(), R.string.emptyName, Toast.LENGTH_SHORT).show();
                    etChange.requestFocus();
                    return;
                }else if(new_name.length() > 25){
                    Toast.makeText(FragmentProfile.this.getContext(), R.string.too_long_name, Toast.LENGTH_SHORT).show();
                    etChange.requestFocus();
                    return;
                }
                if(LOGGED_USER.isGoogleAccount()){
                    MDATABASE.child("GoogleUsers").child(LOGGED_USER.getUid()).child("name").setValue(new_name);
                }
                else if(!LOGGED_USER.isGoogleAccount()){
                    MDATABASE.child("FireBaseUsers").child(LOGGED_USER.getUid()).child("name").setValue(new_name);
                }

                tvName.setText(new_name);

                //Cambio de nombre de todos los post del mismo usuario
                MDATABASE.child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot post: snapshot.getChildren()){
                            for(DataSnapshot data_post: post.getChildren()){
                                if(data_post.getKey().equals("User_UID") && data_post.getValue().toString().equals(LOGGED_USER.getUid())){
                                    MDATABASE.child("Productos").child(post.getKey()).child("User_Name").setValue(new_name);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Cambio para las compra ventas
                MDATABASE.child("Purchased").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot post: snapshot.getChildren()){
                            for(DataSnapshot data_post: post.getChildren()){
                                if(data_post.getKey().equals("Buyer_uid") && data_post.getValue().toString().equals(LOGGED_USER.getUid())){
                                    MDATABASE.child("Purchased").child(post.getKey()).child("Buyer_name").setValue(new_name);
                                }
                                else if(data_post.getKey().equals("Seller_uid") && data_post.getValue().toString().equals(LOGGED_USER.getUid())){
                                    MDATABASE.child("Purchased").child(post.getKey()).child("Seller_name").setValue(new_name);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Toast.makeText(FragmentProfile.this.getContext(), "Nombre cambiado", Toast.LENGTH_SHORT).show();
                change_name_dialog.dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_name_dialog.dismiss();
            }
        });

    }

    public void saveImage(Uri imageUri, String uid, View v){

        AlertDialog loading_alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(FragmentProfile.this.getContext());
        builder.setCancelable(false);

        //Preparar para agregar el layout
        LayoutInflater inflater = getLayoutInflater();
        v = inflater.inflate(R.layout.resource_alertdialog_loading, null);

        //Configurando el layout en el view
        builder.setView(v);
        loading_alert = builder.create();

        loading_alert.show();

        TextView text_loading = v.findViewById(R.id.tv_loading);
        text_loading.setText("Cambiando de perfil...");

        if(imageUri != null){

            if(LOGGED_USER.isGoogleAccount()){
                StorageReference ubicacionImagen = MTSTORAGE.child("GoogleUsers").child(LOGGED_USER.getUid()).child("profile.png");
                ubicacionImagen.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ubicacionImagen.getDownloadUrl().addOnCompleteListener(task -> {
                            Uri imageURL = task.getResult();
                            MDATABASE.child("GoogleUsers").child(LOGGED_USER.getUid()).child("pic").setValue(imageURL.toString());
                        });
                        Toast.makeText(FragmentProfile.this.getContext(), R.string.success_change_name, Toast.LENGTH_SHORT).show();
                        loading_alert.dismiss();
                    }
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(FragmentProfile.this.getContext(), "Algo a salido mal...", Toast.LENGTH_SHORT).show();
                        loading_alert.dismiss();
                    }
                });
            }
            else if(!LOGGED_USER.isGoogleAccount()){
                StorageReference ubicacionImagen = MTSTORAGE.child("FireBaseUsers").child(LOGGED_USER.getUid()).child("profile.png");
                ubicacionImagen.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ubicacionImagen.getDownloadUrl().addOnCompleteListener(task -> {
                            Uri imageURL = task.getResult();
                            MDATABASE.child("FireBaseUsers").child(LOGGED_USER.getUid()).child("pic").setValue(imageURL.toString());
                        });
                        Toast.makeText(FragmentProfile.this.getContext(), "Imagen subido exitosamente", Toast.LENGTH_SHORT).show();
                        loading_alert.dismiss();
                    }
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(FragmentProfile.this.getContext(), "Algo a salido mal...", Toast.LENGTH_SHORT).show();
                        loading_alert.dismiss();
                    }
                });
            }
        }
    }

}