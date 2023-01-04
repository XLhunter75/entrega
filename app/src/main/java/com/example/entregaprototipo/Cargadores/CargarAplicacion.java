package com.example.entregaprototipo.Cargadores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.example.entregaprototipo.Shop.ActivityMainShop;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CargarAplicacion extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String uid;
    private boolean waitLoading = false;
    private boolean isGoogleAccount, isNormalAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        uid = extras.getString("UIDusuario");
        isGoogleAccount = extras.getBoolean("googleAccount");
        isNormalAccount = extras.getBoolean("normalAccount");

        Intent i = new Intent(CargarAplicacion.this, ActivityMainShop.class);
        i.putExtra("normalAccount", isNormalAccount);
        i.putExtra("googleAccount", isGoogleAccount);
        i.putExtra("UIDusuario", uid);
        try {
            if(!waitLoading){
                TimeUnit.SECONDS.sleep(1);
                waitLoading = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(i);

    }
}


