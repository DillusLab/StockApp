package com.example.dillus.stockapp.UI.Fragment_Main;

import static android.app.Activity.RESULT_OK;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_USER_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_USER;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_FIRST_TIME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_INTENT;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_NUEVO;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.UI.Activities.TiendaActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class TiendasFragment extends Fragment {

    private Button btnFragmentTiendas;

    private FirebaseAuth mfirebaseAuth;
    private FirebaseFirestore mfireStore;
    private FirebaseUser _USER;

    private ActivityResultLauncher<Intent> tiendaLauncher;

    private Context context;

    public TiendasFragment() {

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
        return inflater.inflate(R.layout.fragment_tiendas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarCampos(view);
        onClickEvent();
        activityResult();

        // Initialize Firebase Auth
        mfirebaseAuth = FirebaseAuth.getInstance();
        _USER = mfirebaseAuth.getCurrentUser();
        mfireStore = FirebaseFirestore.getInstance();
    }

    /** ╠════════════════════ Events ════════════════════╣ **/
    private void onClickEvent(){
        btnFragmentTiendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTienda(MODE_NUEVO);
            }
        });
    }

    /** ╠════════════════════ Methods ════════════════════╣ **/
    private void inicializarCampos(View view) {
        btnFragmentTiendas = view.findViewById(R.id.btnFragmentTiendas);
    }

    // Obtener tienda (s)
    private void obtenerTiendas() {
        mfireStore.collection(COLLECTION_USER_TIENDA)
                .whereEqualTo(FIREBASE_ID_USER, _USER.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot lista = task.getResult();
                            if(lista.size() == 0){ // Nuevo Usuario
                                goTienda(MODE_FIRST_TIME);
                            } else {
                                // Cargar Tiendas
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // Go tienda
    private void goTienda(String _mode){
        Intent intent = new Intent(context, TiendaActivity.class);
        intent.putExtra(MODE_INTENT, _mode);
        tiendaLauncher.launch(intent);
    }

    /** ╠════════════════════ ACTIVITY RESULT ════════════════════╣ **/
    private void activityResult(){
        tiendaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){

                        }
                    }
                });
    }
}