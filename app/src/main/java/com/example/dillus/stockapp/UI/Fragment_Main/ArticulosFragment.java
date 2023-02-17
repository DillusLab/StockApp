package com.example.dillus.stockapp.UI.Fragment_Main;

import static android.app.Activity.RESULT_OK;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_PRODUCTO;
import static com.example.dillus.stockapp.AppLib.Clslibrary.EXTENSION_JPG;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_EDITAR;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_INTENT;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_NUEVO;
import static com.example.dillus.stockapp.AppLib.Clslibrary.RESOLUCION_IMAGE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dillus.stockapp.AppAdapter.ListaProductoAdapter;
import com.example.dillus.stockapp.AppData.ClsCategoria;
import com.example.dillus.stockapp.AppData.ClsProducto;
import com.example.dillus.stockapp.AppLib.ClsShowMessage;
import com.example.dillus.stockapp.AppPreferences.PreferTienda;
import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.UI.Activities.ProductoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArticulosFragment extends Fragment {

    private RoundedImageView imgButtonFragmentProductosAdd;

    private ActivityResultLauncher<Intent> productoLauncher;

    private Context context;

    private FirebaseAuth mfirebaseAuth;
    private FirebaseFirestore mfireStore;

    private ClsShowMessage clsShowMessage;

    private String _ID_TIENDA;

    private RecyclerView recyclerViewFragmentProductos;
    private List<ClsProducto> listaProducto;

    public ArticulosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert container != null;
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_articulos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarCampos(view);
        onClickEvent();
        activityResult();

        // Initialize Firebase Auth
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfireStore = FirebaseFirestore.getInstance();

        cargarProductosTienda();

    }

    /**
     * ╠════════════════════ Events ════════════════════╣
     **/
    private void onClickEvent() {
        imgButtonFragmentProductosAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductoActivity.class);
                intent.putExtra(MODE_INTENT, MODE_NUEVO);
                productoLauncher.launch(intent);
            }
        });
    }

    /**
     * ╠════════════════════ Methods ════════════════════╣
     **/
    private void inicializarCampos(View view) {
        imgButtonFragmentProductosAdd = view.findViewById(R.id.imgButtonFragmentProductosAdd);

        _ID_TIENDA = new PreferTienda().getID_TIENDA(context);

        recyclerViewFragmentProductos = view.findViewById(R.id.recyclerViewFragmentProductos);
        recyclerViewFragmentProductos.setHasFixedSize(true);
        LinearLayoutManager llgestion = new LinearLayoutManager(context);
        llgestion.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewFragmentProductos.setLayoutManager(llgestion);
        recyclerViewFragmentProductos.setItemAnimator(new DefaultItemAnimator());

        listaProducto = new ArrayList<>();
    }

    /* --------------------------------------------------- */
    // Cargar productos
    private void cargarProductosTienda(){
        mfireStore.collection(COLLECTION_PRODUCTO)
                .whereEqualTo(FIREBASE_ID_TIENDA, _ID_TIENDA)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> lista = queryDocumentSnapshots.getDocuments();

                        listaProducto.clear();

                        for (DocumentSnapshot doc : lista) {
                            ClsProducto producto = doc.toObject(ClsProducto.class);
                            assert producto != null;
                            listaProducto.add(producto);
                        }
                        recyclerViewFragmentProductos.setAdapter(null);
                        ListaProductoAdapter listaProductoAdapter = new ListaProductoAdapter(listaProducto, context);
                        recyclerViewFragmentProductos.setAdapter(listaProductoAdapter);

                        Toast.makeText(context, listaProducto.size() + " - Productos", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /**
     * ╠════════════════════ ACTIVITY RESULT ════════════════════╣
     **/
    private void activityResult() {
        productoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            // Cargar Productos
                            Toast.makeText(context, result.getData().getDataString(), Toast.LENGTH_SHORT).show();
                            cargarProductosTienda();
                        }
                    }
                });
    }

}