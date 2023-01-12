package com.example.dillus.stockapp.UI.Fragment_Session;

import static com.example.dillus.stockapp.AppLib.Clslibrary.COUNTDOWN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.UI.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashFragment extends Fragment {

    private Context context;

    private long countDown;
    private Transition transition;

    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseFirestore mfireStore;

    private NavController navController;

    public SplashFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarCampos(view);

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
                        if (user != null) {
                            goMain();
                        } else {
                            goLogin();
                        }
                    }
                }.start();
            }
        };
    }
    /** ╠════════════════════ Methods ════════════════════╣ **/
    private void inicializarCampos(View view) {
        navController = Navigation.findNavController(view);
    }

    private void goLogin(){
        navController.navigate(R.id.loginFragment);
    }

    private void goMain() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mfirebaseAuth.addAuthStateListener(mAuthStateListener); // firebaseAuth is of class FirebaseAuth
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mfirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    /** ╠════════════════════ @Override ════════════════════╣ **/
}