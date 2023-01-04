package com.example.entregaprototipo.LoginRegister;

import static android.widget.Toast.LENGTH_LONG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entregaprototipo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.*;

import java.util.ArrayList;

public class ActivityRegister extends AppCompatActivity {

    private EditText etName, etMail, etPassword, etRePassword, etAddress, etPhone;
    private Button btRegister;
    private TextView tvLogin;
    //SqliteHelper db;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ArrayList<String> list_mail = new ArrayList<>();
    private ArrayList<String> list_users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        etName = findViewById(R.id.editName);
        etMail = findViewById(R.id.edtiMail);
        etPassword = findViewById(R.id.editPassword);
        etRePassword = findViewById(R.id.edtiRePassword);
        etAddress = findViewById(R.id.editAddress);
        etPhone = findViewById(R.id.editPhone);

        btRegister = findViewById(R.id.buttonRegister);
        tvLogin = findViewById(R.id.textButtonLogin);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("FireBaseUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    for(DataSnapshot data: snapshot.getChildren()){
                        String databaseName = data.child("name").getValue().toString();
                        String databaseMail = data.child("email").getValue().toString();
                        list_users.add(databaseName);
                        list_mail.add(databaseMail);
                    }
                }
                catch (NullPointerException e){
                    Toast.makeText(ActivityRegister.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String mail = etMail.getText().toString();
                String password = etPassword.getText().toString();
                String re_password = etRePassword.getText().toString();
                String phone = etPhone.getText().toString();
                String address = etAddress.getText().toString();

                if(!password.equals(re_password)){
                    Toast.makeText(ActivityRegister.this, R.string.dontMatchPassword, Toast.LENGTH_SHORT).show();
                    etRePassword.requestFocus();
                    return;
                }
                else if(password.length() < 6){
                    Toast.makeText(ActivityRegister.this, R.string.tooShortPassword, Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                    return;
                }
                if(name.isEmpty()){
                    Toast.makeText(ActivityRegister.this, R.string.emptyName, Toast.LENGTH_SHORT).show();
                    etName.requestFocus();
                    return;
                }
                if(phone.isEmpty()){
                    Toast.makeText(ActivityRegister.this, R.string.emptyPhone, Toast.LENGTH_SHORT).show();
                    etName.requestFocus();
                    return;
                }
                if(address.isEmpty()){
                    Toast.makeText(ActivityRegister.this, R.string.emptyAdress, Toast.LENGTH_SHORT).show();
                    etName.requestFocus();
                    return;
                }
                if(mail.isEmpty()){
                    Toast.makeText(ActivityRegister.this, R.string.emptyMail, Toast.LENGTH_SHORT).show();
                    etMail.requestFocus();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    Toast.makeText(ActivityRegister.this, R.string.notMatchMail, Toast.LENGTH_SHORT).show();
                    etMail.requestFocus();
                    return;
                }

                for(String check_name: list_users){
                    if(name.equals(check_name)){
                        Toast.makeText(ActivityRegister.this, R.string.nameUsed, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                for(String check_mail: list_mail){
                    if(name.equals(mail)){
                        Toast.makeText(ActivityRegister.this, R.string.nameUsed, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //Boolean isSuccessRegistered = db.insertNewUsers(name,mail,password,0);
                /*if(isSuccessRegistered){
                    Toast.makeText(ActivityRegister.this, R.string.successRegister, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(ActivityRegister.this, R.string.failedRegister, Toast.LENGTH_SHORT).show();
                }*/

                mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(ActivityRegister.this, R.string.check_mail, LENGTH_LONG).show();

                                String uid = mAuth.getCurrentUser().getUid();
                                mDatabase.child("FireBaseUsers").child(uid).child("name").setValue(name);
                                mDatabase.child("FireBaseUsers").child(uid).child("email").setValue(mail);
                                mDatabase.child("FireBaseUsers").child(uid).child("phone").setValue(phone);
                                mDatabase.child("FireBaseUsers").child(uid).child("address").setValue(address);
                                mDatabase.child("FireBaseUsers").child(uid).child("isRememberMe").setValue(false);
                                mDatabase.child("FireBaseUsers").child(uid).child("countProduct").setValue(0);
                                mDatabase.child("FireBaseUsers").child(uid).child("money").setValue(40000.00);

                                StorageReference ubicacionImagen = mStorage.child("FireBaseUsers").child(uid).child("profile.png");
                                Uri imageUri = Uri.parse("android.resource://" + ActivityRegister.this.getPackageName() + "/" + R.drawable.ic_default_profile);
                                ubicacionImagen.putFile(imageUri).addOnSuccessListener(taskSnapshot -> ubicacionImagen.getDownloadUrl().addOnCompleteListener(task2 -> {
                                    Uri imageURL = task2.getResult();
                                    mDatabase.child("FireBaseUsers").child(uid).child("pic").setValue(imageURL.toString());
                                }));
                                finish();

                            } else {
                                Toast.makeText(ActivityRegister.this, R.string.failure_register, LENGTH_LONG).show();
                            }
                        });

                    } else {
                        Toast.makeText(ActivityRegister.this, getString(R.string.failure_register), LENGTH_LONG).show();
                    }
                });

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(i);
            }
        });
    }
}