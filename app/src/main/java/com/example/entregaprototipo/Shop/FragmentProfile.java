package com.example.entregaprototipo.Shop;


import static com.example.entregaprototipo.Shop.ActivityMainShop.LOGGED_USER;
import static com.example.entregaprototipo.Shop.ActivityMainShop.MAUTH;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.entregaprototipo.LoginRegister.ActivityLogin;
import com.example.entregaprototipo.Profile.ActivityMyProducts;
import com.example.entregaprototipo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


public class FragmentProfile extends Fragment implements View.OnClickListener{

    private TextView tvName;
    private ImageView ivProfile;
    private Button btBought, btSold, btWallet, btExit, btConfiguration, btYourProducts;
    private GoogleSignInClient googleSignInClient;
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
        btConfiguration = v.findViewById(R.id.btConfiguration);
        btExit = v.findViewById(R.id.btExit);

        btBought.setOnClickListener(this);
        btExit.setOnClickListener(this);
        btConfiguration.setOnClickListener(this);
        btYourProducts.setOnClickListener(this);
        btWallet.setOnClickListener(this);
        btSold.setOnClickListener(this);

        //Rellenar el perfil
        tvName.setText(LOGGED_USER.getName());
        Uri product_image = Uri.parse(LOGGED_USER.getProfileURL());
        Glide.with(v).load(String.valueOf(product_image)).into(ivProfile);

        btWallet.setText("Tienes: "+ String.format( "%,.2f" ,LOGGED_USER.getCash()) + "€");
        tvName.setText(LOGGED_USER.getName());

        //Necesario activarlo otra vez en caso de querer hacer un SignOut, para que cuando salga lo registre en googleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(FragmentProfile.this.getContext(), gso);

        return v;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btBought:
                break;
            case R.id.btSold:
                break;
            case R.id.btWallet:
                break;
            case R.id.btYourProducts:
                Intent i = new Intent(FragmentProfile.this.getContext(), ActivityMyProducts.class);
                startActivity(i);
                break;
            case R.id.btConfiguration:
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
}