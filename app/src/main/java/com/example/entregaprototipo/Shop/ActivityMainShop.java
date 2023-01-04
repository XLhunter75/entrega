package com.example.entregaprototipo.Shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.example.entregaprototipo.UserModel.UserData;
import com.example.entregaprototipo.databinding.ActivityMainShopBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ActivityMainShop extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    public static String USER_UID;
    public static boolean isGoogleAccount, isNormalAccount;

    public static UserData LOGGED_USER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop);
        //Se usara este fragmento al empezar
        replaceFragment(new FragmentHome());

        isGoogleAccount = false;
        isNormalAccount = false;

        Bundle extras = getIntent().getExtras();
        USER_UID = extras.getString("UIDusuario");
        isGoogleAccount = extras.getBoolean("googleAccount");
        isNormalAccount = extras.getBoolean("normalAccount");

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(isNormalAccount){
            fill_logged_user("FireBaseUsers");
        }
        else if(isGoogleAccount){
            fill_logged_user("GoogleUsers");
        }

        //Creacion de campos para cada fragmento
        //Haber incluido en Graddle(app) viewBiding
        ActivityMainShopBinding binding = ActivityMainShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.shop_home:
                    replaceFragment(new FragmentHome());
                    break;
                case R.id.shop_category:
                    replaceFragment(new FragmentCategory());
                    break;
                case R.id.shop_cart:
                    replaceFragment(new FragmentCart());
                    break;
                case R.id.shop_perfil:
                    replaceFragment(new FragmentPerfil());
                    break;
                case R.id.shop_debug:
                    replaceFragment(new FragmentDebugShop());
                    break;
            }
            return true;
        });
    }

    public void fill_logged_user(String type_user){
        LOGGED_USER = new UserData("NO_NAME","NO_MAIL",false,0.00,0,"NO_ADDRESS",0,false,"NO_URL");
        mDatabase.child(type_user).child(USER_UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    if(data.getKey().equals("address")){
                        LOGGED_USER.setAddress(data.getValue().toString());
                    }
                    else if(data.getKey().equals("name")){
                        LOGGED_USER.setName(data.getValue().toString());
                    }
                    else if(data.getKey().equals("email")){
                        LOGGED_USER.setMail(data.getValue().toString());
                        LOGGED_USER.setGoogleAccount(false);
                    }
                    else if(data.getKey().equals("emailGoogle")){
                        LOGGED_USER.setMail(data.getValue().toString());
                        LOGGED_USER.setGoogleAccount(true);
                    }
                    else if(data.getKey().equals("money")){
                        LOGGED_USER.setCash(Double.parseDouble(data.getValue().toString()));
                    }
                    else if(data.getKey().equals("phone")){
                        LOGGED_USER.setPhoneNumber(Integer.parseInt(data.getValue().toString()));
                    }
                    else if(data.getKey().equals("countProduct")){
                        LOGGED_USER.setCountProduct(Integer.parseInt(data.getValue().toString()));
                    }
                    else if(data.getKey().equals("isRememberMe")){
                        if(data.getValue().toString().equals("true")){
                            LOGGED_USER.setRememberMe(true);
                        }
                        else if(data.getValue().toString().equals("false")){
                            LOGGED_USER.setRememberMe(false);
                        }
                    }
                    else if(data.getKey().equals("pic")){
                        LOGGED_USER.setProfileURL(data.getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView2, fragment);
        fragmentTransaction.commit();
    }
}