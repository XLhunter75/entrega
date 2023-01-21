package com.example.entregaprototipo.LoginRegister;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entregaprototipo.Cargadores.CargarAplicacion;
import com.example.entregaprototipo.R;
import com.example.entregaprototipo.Shop.ActivityMainShop;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
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

public class ActivityLogin extends AppCompatActivity {

    private EditText etName, etPass;
    private CheckBox cbRemember;
    private Button btLogin;
    private TextView tvRegister;
    private GoogleSignInClient googleSignInClient;

    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    private ArrayList<String> list_Mail = new ArrayList<>();
    private boolean mailUsed = false;
    private ArrayList<String> list_users = new ArrayList<>();

    private boolean googleAccount, normalAccount;

    //Inicio rapido
    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            //Para usuarios de firebase
            mDatabase.child("FireBaseUsers").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean isRememberMe = (Boolean) snapshot.child("isRememberMe").getValue();

                    if (snapshot.child("email").getValue() != null) {
                        if (snapshot.child("isRememberMe").getValue() != null) {
                            if (isRememberMe) {
                                String correo = snapshot.child("email").getValue().toString();
                                cbRemember.setChecked(true);
                                etName.setText(correo);
                            }
                        } else {
                            mDatabase.child(uid).child("isRememberMe").setValue(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        etName = findViewById(R.id.editLoginMail);
        etPass = findViewById(R.id.editLoginPassword);
        cbRemember = findViewById(R.id.checkboxRememberMe);
        btLogin = findViewById(R.id.buttonLogin);
        tvRegister = findViewById(R.id.textButtonRegister);
        SignInButton btGoogle = findViewById(R.id.btGoogle);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        normalAccount = false;
        googleAccount = false;

        mDatabase.child("GoogleUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    String databaseName = data.child("name").getValue().toString();
                    String databaseMailGoogle = data.child("emailGoogle").getValue().toString();
                    list_users.add(databaseName);
                    list_Mail.add(databaseMailGoogle);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Con gmail
        ActivityResultLauncher<Intent> resultLauncherGoogle = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent i = result.getData();

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(i);
                    try{
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseWithGoogle(account);
                    }
                    catch (ApiException e){
                        Toast.makeText(ActivityLogin.this, "ERROR GOOGLE", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(ActivityLogin.this, gso);
        btGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                resultLauncherGoogle.launch(signInIntent);
            }
        });


        //Con cuenta firebase
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etName.getText().toString();
                String pass = etPass.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(ActivityLogin.this, R.string.emptyMail, Toast.LENGTH_SHORT).show();
                    etName.requestFocus();
                    return;
                }
                if(pass.isEmpty()){
                    Toast.makeText(ActivityLogin.this, R.string.emptyPass, Toast.LENGTH_SHORT).show();
                    etPass.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (mAuth.getCurrentUser().isEmailVerified()) {
                            user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            String fullName = user.getDisplayName();
                            isRememberMe(uid);

                            Intent i = new Intent(ActivityLogin.this, CargarAplicacion.class);
                            i.putExtra("UIDusuario", uid);
                            normalAccount = true;
                            i.putExtra("googleAccount", googleAccount);
                            i.putExtra("normalAccount", normalAccount);
                            startActivity(i);

                            Toast.makeText(ActivityLogin.this, getString(R.string.welcome_login), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(ActivityLogin.this, R.string.check_mail, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActivityLogin.this, R.string.failedLogin, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(i);
            }
        });

    }

    private void firebaseWithGoogle(GoogleSignInAccount account){
        if (account !=  null) {
            String email = account.getEmail();
            String uid = account.getId();
            String user_name = account.getDisplayName();

            checkGoogleMailUsed(email);

            Intent i = new Intent(ActivityLogin.this, CargarAplicacion.class);
            if(mailUsed){
                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getKey().equals("GoogleUsers")){
                            String new_uid = "";
                            for (DataSnapshot data : snapshot.getChildren()) {
                                if (data.child("emailGoogle").getValue().toString().equals(email)) {
                                    new_uid = data.getKey();
                                    break;
                                }
                            }
                            googleAccount = true;
                            i.putExtra("normalAccount", normalAccount);
                            i.putExtra("googleAccount", googleAccount);
                            i.putExtra("UIDusuario", new_uid);
                            startActivity(i);
                            //clean();
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else{
                mDatabase.child("GoogleUsers").child(uid).child("name").setValue(user_name);
                mDatabase.child("GoogleUsers").child(uid).child("emailGoogle").setValue(email);
                mDatabase.child("GoogleUsers").child(uid).child("phone").setValue(0);
                mDatabase.child("GoogleUsers").child(uid).child("address").setValue("None");
                mDatabase.child("GoogleUsers").child(uid).child("isRememberMe").setValue(false);
                mDatabase.child("GoogleUsers").child(uid).child("countProduct").setValue(0);
                mDatabase.child("GoogleUsers").child(uid).child("money").setValue(40000.00);

                StorageReference ubicacionImagen = mStorage.child("GoogleUsers").child(uid).child("profile.png");
                Uri imageUri = Uri.parse("android.resource://" + ActivityLogin.this.getPackageName() + "/" + R.drawable.ic_default_profile);
                ubicacionImagen.putFile(imageUri).addOnSuccessListener(taskSnapshot -> ubicacionImagen.getDownloadUrl().addOnCompleteListener(taskImage -> {
                    Uri imageURL = taskImage.getResult();
                    mDatabase.child("GoogleUsers").child(uid).child("pic").setValue(imageURL.toString());
                    googleAccount = true;
                    i.putExtra("normalAccount", normalAccount);
                    i.putExtra("googleAccount", googleAccount);
                    i.putExtra("UIDusuario", uid);
                    startActivity(i);
                    clean();
                }));
            }
        }
        else{
            Toast.makeText(ActivityLogin.this, "Error login", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkGoogleMailUsed(String mail){
        for(String check_mail: list_Mail){
            if(check_mail.equals(mail)){
                mailUsed = true;
                break;
            }
        }
    }

    private void clean(){
        etName.setText("");
        etPass.setText("");
    }

    private void isRememberMe(String uid) {
        if (cbRemember.isChecked()) {
            mDatabase.child("FireBaseUsers").child(uid).child("isRememberMe").setValue(true);

        } else {
            mDatabase.child("FireBaseUsers").child(uid).child("isRememberMe").setValue(false);
        }
    }
}