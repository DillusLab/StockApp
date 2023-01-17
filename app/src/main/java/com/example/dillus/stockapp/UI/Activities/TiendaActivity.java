package com.example.dillus.stockapp.UI.Activities;

import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_DESCRIPTION;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_NAME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_URI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dillus.stockapp.AppData.ClsTienda;
import com.example.dillus.stockapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

public class TiendaActivity extends AppCompatActivity {

    private FirebaseFirestore mfireStore;
    private ClsTienda clsTienda;

    private TextView tvTiendaIDCadena;
    private RoundedImageView imgTiendaMarca, imgTiendaPicturePicker;
    private EditText etTiendaNombreTienda, etFragmentRegisterEmail;
    private Button btnTiendaAdd;

    private FirebaseAuth mfirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        inicializarCampos();
        onClickEvent();

        // Initialize Firebase Auth
        mfirebaseAuth = FirebaseAuth.getInstance();
        
        btnTiendaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clsTienda = new ClsTienda();

                clsTienda.setId("");
                clsTienda.setName("Tienda 1");
                clsTienda.setDescription("Primera Tienda");
                clsTienda.setUri(null);

                addTienda(clsTienda);
            }
        });
    }

    /** ╠════════════════════ Events ════════════════════╣ **/
    private void onClickEvent(){
        imgTiendaPicturePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    /** ╠════════════════════ Methods ════════════════════╣ **/
    private void inicializarCampos() {
        tvTiendaIDCadena  = findViewById(R.id.tvTiendaIDCadena);
        imgTiendaMarca  = findViewById(R.id.imgTiendaMarca);
        imgTiendaPicturePicker  = findViewById(R.id.imgTiendaPicturePicker);
        etTiendaNombreTienda  = findViewById(R.id.etTiendaNombreTienda);
        etFragmentRegisterEmail  = findViewById(R.id.etFragmentRegisterEmail);
        btnTiendaAdd  = findViewById(R.id.btnTiendaAdd);
    }

    public void addTienda (ClsTienda clsTienda){

        mfireStore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put(FIREBASE_ID, clsTienda.getId());
        map.put(FIREBASE_NAME, clsTienda.getName());
        map.put(FIREBASE_DESCRIPTION, clsTienda.getDescription());
        map.put(FIREBASE_URI, clsTienda.getUri());

        mfireStore.collection(COLLECTION_TIENDA)
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String _id = documentReference.getId();
                        mfireStore.collection(COLLECTION_TIENDA).document(_id)
                                .update(FIREBASE_ID, _id)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent data = new Intent();
                                        String text = "Result to be returned....";
                                        data.setData(Uri.parse(text));
                                        setResult(RESULT_OK, data);
                                        finish();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TiendaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent data = new Intent();
                        String text = e.getMessage();
                        data.setData(Uri.parse(text));
                        setResult(RESULT_CANCELED, data);
                        finish();
                    }
                });
    }

    private void takePhoto(){
        camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
    }

    /** ╠════════════════════ Override ════════════════════╣ **/
    ActivityResultLauncher<Intent> camaraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Bundle extras = result.getData().getExtras();
                        Bitmap imgBitmap = (Bitmap) extras.get("data");
                        imgTiendaMarca.setImageBitmap(imgBitmap);
                    }
                }
            });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}