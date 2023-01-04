package com.example.entregaprototipo.Shop;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.entregaprototipo.LoginRegister.ActivityLogin;
import com.example.entregaprototipo.R;
import com.example.entregaprototipo.UserModel.UserData;


public class FragmentPerfil extends Fragment {

    TextView tvNombre, tvMail;
    Button btQuit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        tvNombre = v.findViewById(R.id.tvNameShow);
        tvMail = v.findViewById(R.id.tvMailShow);
        btQuit = v.findViewById(R.id.btQuit);
        return v;
    }
}