package com.example.dillus.stockapp.UI.Activities;

import static com.example.dillus.stockapp.AppLib.ClsMessage.msgRequiredFields;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_USER_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.EXTENSION_JPG;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_DESCRIPTION;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_USER;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_NAME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_URI;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_EDITAR;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_FIRST_TIME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_INTENT;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_NUEVO;
import static com.example.dillus.stockapp.AppLib.Clslibrary.REQUEST_CODE_CAMERA_PERMISSIONS;
import static com.example.dillus.stockapp.AppLib.Clslibrary.REQUEST_CODE_GALERY_PERMISSIONS;
import static com.example.dillus.stockapp.AppLib.Clslibrary.RESOLUCION_IMAGE;
import static com.example.dillus.stockapp.AppLib.Clslibrary.STORAGE_PATH_TIENDA_LOGO;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.example.dillus.stockapp.AppLib.ClsShowMessage;
import com.example.dillus.stockapp.AppPreferences.PreferTienda;
import com.example.dillus.stockapp.BuildConfig;
import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.UI.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TiendaActivity extends AppCompatActivity {

    private ClsTienda clsTienda;

    private TextView tvTiendaIDCadena;
    private ImageButton imgButtonTiendaBack;

    private ProgressBar progressbarActivityTienda;

    private RoundedImageView imgTiendaMarca, imgTiendaPicturePicker;
    private EditText etTiendaNombreTienda, etTiendaRegisterDescripcion;

    private Button btnTiendaAdd;

    private FirebaseAuth mfirebaseAuth;
    private FirebaseFirestore mfireStore;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galeryLauncher;

    private String imageFilePath;
    private Uri imageUriUpload;

    private Context context;

    private String _MODE;

    private ClsShowMessage clsShowMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        inicializarCampos();
        setMode();
        onClickEvent();
        activityResult();

        onLoading(false);

        context = TiendaActivity.this;

        // Initialize Firebase Auth
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfireStore = FirebaseFirestore.getInstance();
    }

    /**
     * ╠════════════════════ Events ════════════════════╣
     **/
    private void onClickEvent() {
        imgButtonTiendaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
                if (validarCampos()) {
                    onLoading(true);
                    clsTienda = new ClsTienda();

                    clsTienda.setId("");
                    clsTienda.setName(etTiendaNombreTienda.getText().toString().trim());
                    clsTienda.setDescription(etTiendaRegisterDescripcion.getText().toString().trim());

                    if (imageUriUpload != null)
                        clsTienda.setUri(imageUriUpload.toString());
                    else
                        clsTienda.setUri(null);

                    addTienda();
                }
            }
        });
    }

    /**
     * ╠════════════════════ Methods ════════════════════╣
     **/
    private void inicializarCampos() {
        clsShowMessage = new ClsShowMessage();

        // Obtener Extras
        Intent intent = getIntent();
        _MODE = intent.getStringExtra(MODE_INTENT);

        tvTiendaIDCadena = findViewById(R.id.tvTiendaIDCadena);
        imgButtonTiendaBack = findViewById(R.id.imgButtonTiendaBack);

        progressbarActivityTienda = findViewById(R.id.progressbarActivityTienda);

        imgTiendaMarca = findViewById(R.id.imgTiendaMarca);
        imgTiendaPicturePicker = findViewById(R.id.imgTiendaPicturePicker);
        etTiendaNombreTienda = findViewById(R.id.etTiendaNombreTienda);
        etTiendaRegisterDescripcion = findViewById(R.id.etTiendaRegisterDescripcion);
        btnTiendaAdd = findViewById(R.id.btnTiendaAdd);

        imageFilePath = null;
        imageUriUpload = null;
    }

    private void setMode(){
        switch (_MODE){
            case MODE_NUEVO:
                break;

            case MODE_EDITAR:
                // Obtener tienda edit
                break;

            case MODE_FIRST_TIME:
                imgButtonTiendaBack.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private boolean validarCampos() {
        if (etTiendaNombreTienda.getText().toString().trim().isEmpty())
            etTiendaNombreTienda.setError(msgRequiredFields);

        if (etTiendaRegisterDescripcion.getText().toString().trim().isEmpty())
            etTiendaRegisterDescripcion.setError(msgRequiredFields);

        return !etTiendaNombreTienda.getText().toString().trim().isEmpty() &&
                !etTiendaRegisterDescripcion.getText().toString().trim().isEmpty();
    }

    private void onLoading(boolean flag) {
        // TRUE -> Loading
        if (flag) {
            imgButtonTiendaBack.setEnabled(false);

            imgTiendaPicturePicker.setEnabled(false);
            etTiendaNombreTienda.setEnabled(false);
            etTiendaRegisterDescripcion.setEnabled(false);

            btnTiendaAdd.setEnabled(false);

            progressbarActivityTienda.setVisibility(View.VISIBLE);

        } else {
            imgButtonTiendaBack.setEnabled(true);

            imgTiendaPicturePicker.setEnabled(true);
            etTiendaNombreTienda.setEnabled(true);
            etTiendaRegisterDescripcion.setEnabled(true);

            btnTiendaAdd.setEnabled(true);

            progressbarActivityTienda.setVisibility(View.INVISIBLE);
        }
    }

    public void showDialogPhotoOption() {
        String[] arrayOptions = {"Cámara", "Galería"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Tienda Logo");
        builder.setItems(arrayOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
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

    private void cameraPhoto() {
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

    private void galeryPhoto() {
        galeryLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }

    private void goFinish(){
        Intent data = new Intent();
        switch (_MODE){
            case MODE_NUEVO:
            case MODE_EDITAR:
                data.setData(Uri.parse(_MODE));
                setResult(RESULT_OK, data);
                finish();
                break;

            case MODE_FIRST_TIME:
                goMain();
                break;
        }
    }

    private void goMain() {
        Intent intent = new Intent(context, MainActivity.class);
        this.finish();
        startActivity(intent);
    }

    /* --------------------------------------------------- */
    // Agregar Tienda
    public void addTienda() {

        Map<String, Object> map = new HashMap<>();
        map.put(FIREBASE_ID_TIENDA, clsTienda.getId());
        map.put(FIREBASE_NAME, clsTienda.getName());
        map.put(FIREBASE_DESCRIPTION, clsTienda.getDescription());
        map.put(FIREBASE_URI, clsTienda.getUri());

        mfireStore.collection(COLLECTION_TIENDA)
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // UPDATE ID
                        clsTienda.setId(documentReference.getId());
                        tvTiendaIDCadena.setText(clsTienda.getId());

                        updateTiendaID();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        clsShowMessage.showMessageError(context, Objects.requireNonNull(e.getMessage()));
                        onLoading(false);
                    }
                });
    }

    // Update IdTienda
    private void updateTiendaID() {
        mfireStore.collection(COLLECTION_TIENDA).document(clsTienda.getId())
                .update(FIREBASE_ID_TIENDA, clsTienda.getId())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (clsTienda.getUri() != null)
                                uploadImage();
                            else
                                updateTiendaID();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        clsShowMessage.showMessageError(context, Objects.requireNonNull(e.getMessage()));
                        onLoading(false);
                    }
                });
    }

    // Subir imagen a firebase
    private void uploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference reference = storage.getReference().child(STORAGE_PATH_TIENDA_LOGO).child(clsTienda.getId());
        reference.putFile(Uri.parse(clsTienda.getUri()))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        updateTiendaURI(uri);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        clsShowMessage.showMessageError(context, Objects.requireNonNull(e.getMessage()));
                                        onLoading(false);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        clsShowMessage.showMessageError(context, Objects.requireNonNull(e.getMessage()));
                        onLoading(false);
                    }
                });
    }

    // Update IdTienda
    private void updateTiendaURI(Uri _uriTienda) {
        mfireStore.collection(COLLECTION_TIENDA).document(clsTienda.getId())
                .update(FIREBASE_URI, _uriTienda)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addUserTienda();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        clsShowMessage.showMessageError(context, Objects.requireNonNull(e.getMessage()));
                        onLoading(false);
                    }
                });
    }

    // Agregar usuario-tienda
    private void addUserTienda() {
        Map<String, Object> map = new HashMap<>();
        map.put(FIREBASE_ID_USER, mfirebaseAuth.getUid());
        map.put(FIREBASE_ID_TIENDA, clsTienda.getId());

        mfireStore.collection(COLLECTION_USER_TIENDA).add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            new PreferTienda().setID_TIENDA(context, clsTienda.getId());
                            goFinish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        clsShowMessage.showMessageError(context, Objects.requireNonNull(e.getMessage()));
                        onLoading(false);
                    }
                });
    }

    /* --------------------------------------------------- */

    /**
     * ╠════════════════════ PERMISSIONS ════════════════════╣
     **/
    private void permisoCamera() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            cameraPhoto();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSIONS);
        }
    }

    private void permisoGaleria() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            galeryPhoto();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_GALERY_PERMISSIONS);
        }
    }

    /**
     * ╠════════════════════ ACTIVITY RESULT ════════════════════╣
     **/
    private void activityResult() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Uri photoUri = Uri.fromFile(new File(imageFilePath));
                            String destinationUri = UUID.randomUUID().toString() + EXTENSION_JPG;
                            UCrop.of(photoUri, Uri.fromFile(new File(getCacheDir(), destinationUri)))
                                    .withAspectRatio(1, 1)
                                    .withMaxResultSize(RESOLUCION_IMAGE, RESOLUCION_IMAGE)
                                    .start(TiendaActivity.this);
                        }
                    }
                });

        galeryLauncher = registerForActivityResult( ///NO TOCAR
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                            Uri photoUri = result.getData().getData();
                            String destinationUri = UUID.randomUUID().toString() + EXTENSION_JPG;

                            UCrop.of(photoUri, Uri.fromFile(new File(getCacheDir(), destinationUri)))
                                    .withAspectRatio(1, 1)
                                    .withMaxResultSize(RESOLUCION_IMAGE, RESOLUCION_IMAGE)
                                    .start(TiendaActivity.this);
                        }
                    }
                });
    }

    /**
     * ╠════════════════════ Override ════════════════════╣
     **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            imageUriUpload = UCrop.getOutput(data);
            imgTiendaMarca.setImageURI(imageUriUpload);
        }

        if (requestCode == REQUEST_CODE_CAMERA_PERMISSIONS && resultCode == RESULT_OK) {
            cameraPhoto();
        }

        if (requestCode == REQUEST_CODE_GALERY_PERMISSIONS && resultCode == RESULT_OK) {
            galeryPhoto();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * ╠════════════════════ Methods Camera Take Picture ════════════════════╣
     **/
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

/*Intent data = new Intent();
String text = "Result to be returned....";
data.setData(Uri.parse(text));
setResult(RESULT_OK, data);
finish();*/