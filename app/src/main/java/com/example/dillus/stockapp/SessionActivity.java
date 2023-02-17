package com.example.dillus.stockapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

public class SessionActivity extends AppCompatActivity {

    private boolean exit = true;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session);

        navController = Navigation.findNavController(SessionActivity.this, R.id.navSesionFragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                exit = navDestination.getId() == R.id.loginFragment;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(exit)
            this.finish();
        else
            super.onBackPressed();

    }
}