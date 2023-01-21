package com.example.entregaprototipo.Shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.entregaprototipo.R;
import com.example.entregaprototipo.UserModel.UserData;
import com.example.entregaprototipo.databinding.ActivityMainShopBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ActivityMainShop extends AppCompatActivity {

    public static FirebaseAuth MAUTH;
    public static StorageReference MTSTORAGE;
    public static DatabaseReference MDATABASE;

    public static String USER_UID;
    public static boolean isGoogleAccount, isNormalAccount;

    public static UserData LOGGED_USER;
    public static ArrayList<UserData> ALL_USERS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop);
        //Se usara este fragmento al empezar
        replaceFragment(new FragmentHome());

        isGoogleAccount = false;
        isNormalAccount = false;
        ALL_USERS = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        USER_UID = extras.getString("UIDusuario");
        isGoogleAccount = extras.getBoolean("googleAccount");
        isNormalAccount = extras.getBoolean("normalAccount");

        MAUTH = FirebaseAuth.getInstance();
        MTSTORAGE = FirebaseStorage.getInstance().getReference();
        MDATABASE = FirebaseDatabase.getInstance().getReference();

        if(isNormalAccount){
            fill_logged_user("FireBaseUsers");
        }
        else if(isGoogleAccount){
            fill_logged_user("GoogleUsers");
        }

        fill_list_user();

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
                    replaceFragment(new FragmentChat());
                    break;
                case R.id.shop_perfil:
                    replaceFragment(new FragmentProfile());
                    break;
                case R.id.shop_debug:
                    replaceFragment(new FragmentAddProduct());
                    break;
            }
            return true;
        });
    }

    public void fill_logged_user(String type_user){
        LOGGED_USER = new UserData("NO_NAME","NO_MAIL",false,0.00,0,"NO_ADDRESS",0,false,"NO_URL");
        MDATABASE.child(type_user).child(USER_UID).addValueEventListener(new ValueEventListener() {
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

    public void fill_list_user(){MDATABASE.child("FireBaseUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot user: snapshot.getChildren()){
                    UserData new_user = new UserData("NO_NAME","NO_MAIL",false,0.00,0,"NO_ADDRESS",0,false,"NO_URL");
                    for(DataSnapshot data: user.getChildren()){
                        if(data.getKey().equals("address")){
                            new_user.setAddress(data.getValue().toString());
                        }
                        else if(data.getKey().equals("name")){
                            new_user.setName(data.getValue().toString());
                        }
                        else if(data.getKey().equals("email")){
                            new_user.setMail(data.getValue().toString());
                            new_user.setGoogleAccount(false);
                        }
                        else if(data.getKey().equals("emailGoogle")){
                            new_user.setMail(data.getValue().toString());
                            new_user.setGoogleAccount(true);
                        }
                        else if(data.getKey().equals("money")){
                            new_user.setCash(Double.parseDouble(data.getValue().toString()));
                        }
                        else if(data.getKey().equals("phone")){
                            new_user.setPhoneNumber(Integer.parseInt(data.getValue().toString()));
                        }
                        else if(data.getKey().equals("countProduct")){
                            new_user.setCountProduct(Integer.parseInt(data.getValue().toString()));
                        }
                        else if(data.getKey().equals("isRememberMe")){
                            if(data.getValue().toString().equals("true")){
                                new_user.setRememberMe(true);
                            }
                            else if(data.getValue().toString().equals("false")){
                                new_user.setRememberMe(false);
                            }
                        }
                        else if(data.getKey().equals("pic")){
                            new_user.setProfileURL(data.getValue().toString());
                        }
                    }
                    ALL_USERS.add(new_user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        MDATABASE.child("GoogleUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot user: snapshot.getChildren()){
                    UserData new_user = new UserData("NO_NAME","NO_MAIL",false,0.00,0,"NO_ADDRESS",0,false,"NO_URL");
                    for(DataSnapshot data: user.getChildren()){
                        if(data.getKey().equals("address")){
                            new_user.setAddress(data.getValue().toString());
                        }
                        else if(data.getKey().equals("name")){
                            new_user.setName(data.getValue().toString());
                        }
                        else if(data.getKey().equals("email")){
                            new_user.setMail(data.getValue().toString());
                            new_user.setGoogleAccount(false);
                        }
                        else if(data.getKey().equals("emailGoogle")){
                            new_user.setMail(data.getValue().toString());
                            new_user.setGoogleAccount(true);
                        }
                        else if(data.getKey().equals("money")){
                            new_user.setCash(Double.parseDouble(data.getValue().toString()));
                        }
                        else if(data.getKey().equals("phone")){
                            new_user.setPhoneNumber(Integer.parseInt(data.getValue().toString()));
                        }
                        else if(data.getKey().equals("countProduct")){
                            new_user.setCountProduct(Integer.parseInt(data.getValue().toString()));
                        }
                        else if(data.getKey().equals("isRememberMe")){
                            if(data.getValue().toString().equals("true")){
                                new_user.setRememberMe(true);
                            }
                            else if(data.getValue().toString().equals("false")){
                                new_user.setRememberMe(false);
                            }
                        }
                        else if(data.getKey().equals("pic")){
                            new_user.setProfileURL(data.getValue().toString());
                        }
                    }
                    ALL_USERS.add(new_user);
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
    @Override
    public void onBackPressed(){
        //Salir del programa
    }
}