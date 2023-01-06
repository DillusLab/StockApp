package com.example.dillus.stockapp;

import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_USERS;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COUNTDOWN;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_EMAIL;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_NAME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.REQUEST_CODE_SIGN_IN;
import static com.example.dillus.stockapp.AppLib.Clslibrary.STRING_COUNTDOWN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.dillus.stockapp.UI.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("CustomSplashScreen")
public class    SplashActivity extends AppCompatActivity {

    private long countDown;

    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseFirestore mfireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        inicializarCampos();

        if(getIntent().getExtras() != null)
            countDown = getIntent().getExtras().getLong(STRING_COUNTDOWN);
        else
            countDown = COUNTDOWN;

        //Initialize Firebase Auth y Firestore
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfireStore = FirebaseFirestore.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                new CountDownTimer(countDown, 1000) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long longMillis) {
                        //tvSplashText.setText("" + ((longMillis / 1000) + 1));
                    }

                    public void onFinish() {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if(user != null){
                            //Toast.makeText(SplashActivity.this, "Bienvenido " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            goMain();
                        } else {
                            AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.GoogleBuilder().build();

                            startActivityForResult(AuthUI.getInstance().
                                    createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.ic_stockapp)
                                    .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), googleIdp))

                                    .build(), REQUEST_CODE_SIGN_IN);
                        }
                    }
                }.start();
            }
        };
    }

    /** ╠════════════════════ Methods ════════════════════╣ **/
    private void inicializarCampos() {
        //tvSplashText = findViewById(R.id.tvSplashText);
    }

    private void goMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    /** ╠════════════════════ Methods Firebase ════════════════════╣ **/
    private void saveDataFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String _id =user.getUid();
            String _name =user.getDisplayName();
            String _email =user.getEmail();
            Uri _photoUrl = user.getPhotoUrl();

            Map<String, Object> map = new HashMap<>();
            map.put(FIREBASE_ID, _id);
            map.put(FIREBASE_NAME, _name);
            map.put(FIREBASE_EMAIL, _email);

            mfireStore.collection(COLLECTION_USERS).document(_id).set(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //Save data on DB Local

                            //Go main activity
                            goMain();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // No user is signed in
        }
    }

    /** ╠════════════════════ @Override ════════════════════╣ **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SIGN_IN && resultCode == RESULT_OK){
            //Guardar Datos en DB Firestore y DB Local
            //saveDataFirebase();
            goMain();
        } else {
            SplashActivity.this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mfirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null){
            mfirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onBackPressed() {

    }
}