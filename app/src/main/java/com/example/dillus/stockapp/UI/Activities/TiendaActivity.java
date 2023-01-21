package com.example.dillus.stockapp.UI.Activities;

import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.EXTENSION_JPG;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_DESCRIPTION;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_NAME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_URI;
import static com.example.dillus.stockapp.AppLib.Clslibrary.REQUEST_CODE_CAMERA_PERMISSIONS;
import static com.example.dillus.stockapp.AppLib.Clslibrary.STORAGE_PATH_TIENDA_LOGO;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.dillus.stockapp.AppData.ClsTienda;
import com.example.dillus.stockapp.BuildConfig;
import com.example.dillus.stockapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TiendaActivity extends AppCompatActivity {

    private FirebaseFirestore mfireStore;
    private ClsTienda clsTienda;

    private TextView tvTiendaIDCadena;
    private RoundedImageView imgTiendaMarca, imgTiendaPicturePicker;
    private EditText etTiendaNombreTienda, etFragmentRegisterEmail;
    private Button btnTiendaAdd;

    private FirebaseAuth mfirebaseAuth;

    private  ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galeryLauncher;

    private String ID_TIENDA;
    private String imageFilePath;
    private Uri imageUriUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        inicializarCampos();
        onClickEvent();
        activityResult();

        // Initialize Firebase Auth
        mfirebaseAuth = FirebaseAuth.getInstance();
    }

    /** ╠════════════════════ Events ════════════════════╣ **/
private void onClickEvent(){
    imgTiendaPicturePicker.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialogPhotoOption();
            //takePhoto();
        }
    });

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


    /** ╠════════════════════ Methods ════════════════════╣ **/
    private void inicializarCampos() {
        tvTiendaIDCadena  = findViewById(R.id.tvTiendaIDCadena);
        imgTiendaMarca  = findViewById(R.id.imgTiendaMarca);
        imgTiendaPicturePicker  = findViewById(R.id.imgTiendaPicturePicker);
        etTiendaNombreTienda  = findViewById(R.id.etTiendaNombreTienda);
        etFragmentRegisterEmail  = findViewById(R.id.etFragmentRegisterEmail);
        btnTiendaAdd  = findViewById(R.id.btnTiendaAdd);

        imageFilePath = null;
        imageUriUpload = null;
    }

    private boolean validarCampos(){

        return false;
    }

    private void onLoad(){

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
                        // UPDATE ID
                        ID_TIENDA = documentReference.getId();
                        mfireStore.collection(COLLECTION_TIENDA).document(ID_TIENDA)
                                .update(FIREBASE_ID, ID_TIENDA)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        uploadImage();
                                        /*Intent data = new Intent();
                                        String text = "Result to be returned....";
                                        data.setData(Uri.parse(text));
                                        setResult(RESULT_OK, data);
                                        finish();*/
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

    public void showDialogPhotoOption(){
        String[] arrayOptions = {"Cámara", "Galería"};

        AlertDialog.Builder builder = new AlertDialog.Builder(TiendaActivity.this);
        //builder.setTitle("Tienda Logo");
        builder.setItems(arrayOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        permisoCamera();
                        break;

                    case 1:
                        galeryPhoto();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cameraPhoto(){
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
            Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            cameraLauncher.launch(pictureIntent);
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
    }

    @SuppressLint("IntentReset")
    private void galeryPhoto(){
        //mgaleryLauncher.launch("image/*");
        galeryLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }

    private void uploadImage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference reference = storage.getReference().child(STORAGE_PATH_TIENDA_LOGO).child(ID_TIENDA);
        reference.putFile(imageUriUpload)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.e("URI", uri.toString());
                            }
                        });
                    }
                });
    }

    /** ╠════════════════════ PERMISSIONS ════════════════════╣ **/
    private void permisoCamera(){
        if(ContextCompat.checkSelfPermission(TiendaActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            cameraPhoto();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSIONS);
        }
    }

    /** ╠════════════════════ ACTIVITY RESULT ════════════════════╣ **/
    private void activityResult(){
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            Uri photoUri = Uri.fromFile(new File(imageFilePath));
                            String destinationUri = UUID.randomUUID().toString() + EXTENSION_JPG;
                            UCrop.of(photoUri, Uri.fromFile(new File(getCacheDir(), destinationUri)))
                                    .withAspectRatio(1, 1)
                                    .withMaxResultSize(1000, 1000)
                                    .start(TiendaActivity.this);
                        }
                    }
                });

        galeryLauncher = registerForActivityResult( ///NO TOCAR
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK && result.getData() != null){

                            Uri photoUri = result.getData().getData();
                            String destinationUri = UUID.randomUUID().toString() + EXTENSION_JPG;

                            UCrop.of(photoUri, Uri.fromFile(new File(getCacheDir(), destinationUri)))
                                    .withAspectRatio(1, 1)
                                    .withMaxResultSize(200, 200)
                                    .start(TiendaActivity.this);
                        }
                    }
                });
    }

    /** ╠════════════════════ Override ════════════════════╣ **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            imageUriUpload = UCrop.getOutput(data);
            imgTiendaMarca.setImageURI(imageUriUpload);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /** ╠════════════════════ Methods Camera Take Picture ════════════════════╣ **/
    private File createImageFile() throws IOException {
        String imageFileName = UUID.randomUUID().toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, EXTENSION_JPG, storageDir);

        imageFilePath = image.getAbsolutePath();
        return image;
    }
}
/*Bundle extras = result.getData().getExtras();
Bitmap imgBitmap = (Bitmap) extras.get("data");
imgTiendaMarca.setImageBitmap(imgBitmap);*/