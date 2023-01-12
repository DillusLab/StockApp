package com.example.dillus.stockapp.UI;

import static androidx.core.view.GravityCompat.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dillus.stockapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    // Firebase
    private FirebaseAuth mfirebaseAuth;
    private FirebaseFirestore mfireStore;
    private FirebaseUser user;

    // Navigation View
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView imgMainMenu;

    // Navigation Controller
    private NavController navController;
    private TextView tvMainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarCampos();

        imgMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(START);
            }
        });

        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        NavigationUI.setupWithNavController(navigationView,navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                tvMainTitle.setText(navDestination.getLabel());
            }
        });

    }
    /** ╠════════════════════ Methods ════════════════════╣ **/
    private void inicializarCampos() {
        // Navigation Drower an components
        drawerLayout = findViewById(R.id.drawerLayout);
        imgMainMenu = findViewById(R.id.imgMainMenu);

        // Navigation View
        navigationView = findViewById(R.id.navigationView);

        // Navigation Controller
        navController = Navigation.findNavController(this, R.id.navMainFragment);
        tvMainTitle = findViewById(R.id.tvMainTitle);

        //Navigation Header Items
        View header = navigationView.getHeaderView(0);
        /*tvHeaderName = header.findViewById(R.id.tvHeaderName);
        tvHeaderEmail = header.findViewById(R.id.tvHeaderEmail);
        tvHeaderTienda = header.findViewById(R.id.tvHeaderTienda);
        imgHeaderProfile = header.findViewById(R.id.imgHeaderProfile);
        imgHeaderProfile = header.findViewById(R.id.imgHeaderProfile);*/

        //Navigation Menu Items
        /*Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(2).getSubMenu().findItem(R.id.menuSesion_Logout);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                AuthUI.getInstance().signOut(MainActivity.this);
                goSplash();
                return true;
            }
        });*/
    }
}

//https://www.youtube.com/watch?v=OuvhZTSU5fQ&list=RDCMUCkFSUytHQoe9jV31Vf6fjag&start_radio=1&t=38s
