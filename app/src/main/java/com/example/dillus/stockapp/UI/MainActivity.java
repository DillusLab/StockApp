package com.example.dillus.stockapp.UI;

import static androidx.core.view.GravityCompat.START;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_USER_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_USER;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_FIRST_TIME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_INTENT;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.dillus.stockapp.AppPreferences.PreferTienda;
import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.SessionActivity;
import com.example.dillus.stockapp.UI.Activities.TiendaActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

public class MainActivity extends AppCompatActivity {

    // Firebase
    private FirebaseAuth mfirebaseAuth;
    private FirebaseFirestore mfireStore;
    private FirebaseUser _USER;

    private ActivityResultLauncher<Intent> tiendaLauncher;

    // Navigation View
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView imgMainMenu, imgMainMore;

    // Navigation View Header
    private RoundedImageView imgHeaderProfile;
    private TextView tvHeaderName, tvHeaderEmail, tvHeaderTienda;


    // Navigation Controller
    private NavController navController;
    private TextView tvMainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarCampos();
        onClickEvent();
        activityResult();

    }

    /**
     * ╠════════════════════ Methods ════════════════════╣
     **/
    private void onClickEvent() {
        imgMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(START);
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                tvMainTitle.setText(navDestination.getLabel());
            }
        });

        imgMainMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, imgMainMore);
                popupMenu.inflate(R.menu.main_menu_bar);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.itemMainBar_Actualizar:
                                Toast.makeText(MainActivity.this, "Actualizar", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    /**
     * ╠════════════════════ Methods ════════════════════╣
     **/
    private void inicializarCampos() {
        // Initialize Firebase Auth
        mfirebaseAuth = FirebaseAuth.getInstance();
        _USER = mfirebaseAuth.getCurrentUser();
        mfireStore = FirebaseFirestore.getInstance();

        // Navigation Drower an components
        drawerLayout = findViewById(R.id.drawerLayout);

        imgMainMenu = findViewById(R.id.imgMainMenu);
        tvMainTitle = findViewById(R.id.tvMainTitle);
        imgMainMore = findViewById(R.id.imgMainMore);

        // Navigation View
        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        // Navigation Controller
        navController = Navigation.findNavController(this, R.id.navMainFragment);



        NavigationUI.setupWithNavController(navigationView, navController);

        //Navigation Header Items
        View header = navigationView.getHeaderView(0);
        tvHeaderName = header.findViewById(R.id.tvHeaderName);
        tvHeaderEmail = header.findViewById(R.id.tvHeaderEmail);
        tvHeaderTienda = header.findViewById(R.id.tvHeaderTienda);
        imgHeaderProfile = header.findViewById(R.id.imgHeaderProfile);

        tvHeaderName.setText(_USER.getDisplayName());
        tvHeaderEmail.setText(_USER.getEmail());
        tvHeaderTienda.setText(new PreferTienda().getNOMBRE_TIENDA(this));
        Glide.with(this)
                .load(_USER.getPhotoUrl())
                .centerCrop()
                .into(imgHeaderProfile);

        //Navigation Menu Items
        Menu menu = navigationView.getMenu();
        MenuItem menuItem;
        menuItem = menu.getItem(3).getSubMenu().getItem(0);

        if (menuItem.getItemId() == R.id.navMenuProfile) {
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

        menuItem = menu.getItem(4).getSubMenu().getItem(0);

        if (menuItem.getItemId() == R.id.navMenuSesionLogout) {
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    AuthUI.getInstance().signOut(MainActivity.this);
                    goSplash();
                    return false;
                }
            });
        }
    }

    private void obtenerTiendas() {
        mfireStore.collection(COLLECTION_USER_TIENDA)
                .whereEqualTo(FIREBASE_ID_USER, _USER.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot lista = task.getResult();
                            if (lista.size() == 0) { // Nuevo Usuario
                                goTienda();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goTienda() {
        Intent intent = new Intent(MainActivity.this, TiendaActivity.class);
        intent.putExtra(MODE_INTENT, MODE_FIRST_TIME);
        tiendaLauncher.launch(intent);
    }

    private void goSplash() {
        Intent intent = new Intent(MainActivity.this, SessionActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * ╠════════════════════ ACTIVITY RESULT ════════════════════╣
     **/
    private void activityResult() {
        tiendaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {

                        } else {
                            MainActivity.this.finish();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        obtenerTiendas();
    }
}

//https://www.youtube.com/watch?v=OuvhZTSU5fQ&list=RDCMUCkFSUytHQoe9jV31Vf6fjag&start_radio=1&t=38s
