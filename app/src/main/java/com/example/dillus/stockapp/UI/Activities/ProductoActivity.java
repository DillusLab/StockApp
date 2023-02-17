package com.example.dillus.stockapp.UI.Activities;

import static com.example.dillus.stockapp.AppLib.ClsMessage.msgRequiredFields;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_CATEGORIA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_MARCA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_PRODUCTO;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_UNIDAD;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLOR_BLACK;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLOR_BLUE;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLOR_GRAY;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLOR_GREEN;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLOR_ORANGE;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLOR_PURPLE;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLOR_RED;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLOR_WHITE;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLOR_YELLOW;
import static com.example.dillus.stockapp.AppLib.Clslibrary.DIALOG_CATEGORIA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.DIALOG_MARCA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.DIALOG_UNIDAD_VENTA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.EXTENSION_JPG;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_TIPO;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_URI;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_EDITAR;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_FIRST_TIME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_INTENT;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_NUEVO;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_VER;
import static com.example.dillus.stockapp.AppLib.Clslibrary.REQUEST_CODE_CAMERA_PERMISSIONS;
import static com.example.dillus.stockapp.AppLib.Clslibrary.REQUEST_CODE_GALERY_PERMISSIONS;
import static com.example.dillus.stockapp.AppLib.Clslibrary.RESOLUCION_IMAGE;
import static com.example.dillus.stockapp.AppLib.Clslibrary.STORAGE_PATH_PRODUCTO;
import static com.example.dillus.stockapp.AppLib.Clslibrary.STORAGE_PATH_TIENDA_LOGO;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import com.example.dillus.stockapp.AppData.ClsCategoria;
import com.example.dillus.stockapp.AppData.ClsProducto;
import com.example.dillus.stockapp.AppData.ClsUnidad;
import com.example.dillus.stockapp.AppLib.ClsShowMessage;
import com.example.dillus.stockapp.AppPreferences.PreferTienda;
import com.example.dillus.stockapp.BuildConfig;
import com.example.dillus.stockapp.ExtrasClass.CaptureCode;
import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.UI.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ProductoActivity extends AppCompatActivity {

    private ImageButton imgButtonProductoBack;
    private TextView tvProductoTitle;
    private ProgressBar progressbarActivityProducto;

    private RoundedImageView imgProductoImagen, imgProductoImagenPicker;
    private RoundedImageView imgProductoAddCategoria, imgProductoAddUnidad, imgProductoAddMarca;

    private EditText etProductoCodigo, etProductoNombre, etProductoDescripcion;
    private ImageView imgProductoCodigoPicker;
    private Spinner spinnerProductoCategoria, spinnerProductoUnidad, spinnerProductoMarca;

    private LinearLayout linearLayoutProductoColores;
    private RoundedImageView imgProductoColorBlack, imgProductoColorWhite, imgProductoColorRed, imgProductoColorBlue,
            imgProductoColorGreen, imgProductoColorYellow, imgProductoColorOrange, imgProductoColorGray, imgProductoColorPurple;

    private Button btnProductoAdd;

    private String imageFilePath;
    private Uri imageUriUpload;

    private Context context;

    private String _MODE;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galeryLauncher;
    private ActivityResultLauncher<ScanOptions> scanCodeLauncher;

    private FirebaseAuth mfirebaseAuth;
    private FirebaseFirestore mfireStore;

    private ClsShowMessage clsShowMessage;
    private ClsProducto clsProducto;

    private boolean _ALL_SPINNER;
    private boolean _ADD_TIPO;

    private String _ID_TIENDA;

    private String _TIPO_TEMP; // Unidad - Categoría

    private final ArrayList<String> listaSpinnerCategoria = new ArrayList<>();
    private final ArrayList<String> listaSpinnerUnidad = new ArrayList<>();
    private final ArrayList<String> listaSpinnerMarca = new ArrayList<>();
    private List<String> listaColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        inicializarCampos();
        setMode();
        onClickEvent();
        activityResult();

        context = ProductoActivity.this;

        _ID_TIENDA = new PreferTienda().getID_TIENDA(context);

        // Initialize Firebase Auth
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfireStore = FirebaseFirestore.getInstance();

        onLoading(true);

        _ALL_SPINNER = true;
        _ADD_TIPO = false;

        cargarSpinnerCategoria();
    }

    /**
     * ╠════════════════════ Events ════════════════════╣
     **/
    private void onClickEvent() {
        imgButtonProductoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imgProductoImagenPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPhotoOption();
                //takePhoto();
            }
        });

        imgProductoCodigoPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        imgProductoAddCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd(DIALOG_CATEGORIA);
            }
        });

        imgProductoAddUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd(DIALOG_UNIDAD_VENTA);
            }
        });

        imgProductoAddMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd(DIALOG_MARCA);
            }
        });

        linearLayoutProductoColores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogColors();
            }
        });

        btnProductoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    onLoading(true);

                    switch (_MODE) {
                        case MODE_NUEVO:
                            clsProducto = new ClsProducto();
                            clsProducto.setCodigo(etProductoCodigo.getText().toString().trim());
                            clsProducto.setId_tienda(_ID_TIENDA);
                            clsProducto.setNombre(etProductoNombre.getText().toString().trim());
                            clsProducto.setDescripcion(etProductoDescripcion.getText().toString().trim());
                            clsProducto.setCategoria(spinnerProductoCategoria.getSelectedItem().toString());
                            clsProducto.setUnidad(spinnerProductoUnidad.getSelectedItem().toString());
                            clsProducto.setMarca(spinnerProductoMarca.getSelectedItem().toString());

                            if (imageUriUpload != null)
                                clsProducto.setUri(imageUriUpload.toString());
                            else
                                clsProducto.setUri(null);

                            clsProducto.setColores(listaColors);

                            addProducto();
                            break;

                        case MODE_EDITAR:
                            break;
                    }

                }
            }
        });
    }

    /**
     * ╠════════════════════ Methods ════════════════════╣
     **/
    private void inicializarCampos() {
        clsShowMessage = new ClsShowMessage();

        imgButtonProductoBack = findViewById(R.id.imgButtonProductoBack);
        tvProductoTitle = findViewById(R.id.tvProductoTitle);
        progressbarActivityProducto = findViewById(R.id.progressbarActivityProducto);

        imgProductoImagen = findViewById(R.id.imgProductoImagen);
        imgProductoImagenPicker = findViewById(R.id.imgProductoImagenPicker);

        imgProductoAddCategoria = findViewById(R.id.imgProductoAddCategoria);
        imgProductoAddUnidad = findViewById(R.id.imgProductoAddUnidad);
        imgProductoAddMarca = findViewById(R.id.imgProductoAddMarca);

        etProductoCodigo = findViewById(R.id.etProductoCodigo);
        etProductoNombre = findViewById(R.id.etProductoNombre);
        etProductoDescripcion = findViewById(R.id.etProductoDescripcion);

        imgProductoCodigoPicker = findViewById(R.id.imgProductoCodigoPicker);
        spinnerProductoCategoria = findViewById(R.id.spinnerProductoCategoria);
        spinnerProductoUnidad = findViewById(R.id.spinnerProductoUnidad);
        spinnerProductoMarca = findViewById(R.id.spinnerProductoMarca);

        linearLayoutProductoColores = findViewById(R.id.linearLayoutProductoColores);
        imgProductoColorBlack = findViewById(R.id.imgProductoColorBlack);
        imgProductoColorWhite = findViewById(R.id.imgProductoColorWhite);
        imgProductoColorRed = findViewById(R.id.imgProductoColorRed);
        imgProductoColorBlue = findViewById(R.id.imgProductoColorBlue);
        imgProductoColorGreen = findViewById(R.id.imgProductoColorGreen);
        imgProductoColorYellow = findViewById(R.id.imgProductoColorYellow);
        imgProductoColorOrange = findViewById(R.id.imgProductoColorOrange);
        imgProductoColorGray = findViewById(R.id.imgProductoColorGray);
        imgProductoColorPurple = findViewById(R.id.imgProductoColorPurple);

        btnProductoAdd = findViewById(R.id.btnProductoAdd);

        // Obtener Extras
        Intent intent = getIntent();
        _MODE = intent.getStringExtra(MODE_INTENT);

        imageFilePath = null;
        imageUriUpload = null;
    }

    private void setMode() {
        switch (_MODE) {
            case MODE_NUEVO:
                break;

            case MODE_EDITAR:
                // Obtener tienda edit
                break;

            case MODE_VER:
                break;
        }
    }

    private boolean validarCampos() {
        if (etProductoCodigo.getText().toString().trim().isEmpty())
            etProductoCodigo.setError(msgRequiredFields);

        if (etProductoNombre.getText().toString().trim().isEmpty())
            etProductoNombre.setError(msgRequiredFields);

        if (etProductoDescripcion.getText().toString().trim().isEmpty())
            etProductoDescripcion.setError(msgRequiredFields);

        if (spinnerProductoCategoria.getSelectedItemPosition() == 0)
            spinnerProductoCategoria.performClick();
        else if (spinnerProductoUnidad.getSelectedItemPosition() == 0)
            spinnerProductoUnidad.performClick();
        else if (spinnerProductoMarca.getSelectedItemPosition() == 0)
            spinnerProductoMarca.performClick();

        return !etProductoCodigo.getText().toString().trim().isEmpty() &&
                !etProductoNombre.getText().toString().trim().isEmpty() &&
                !etProductoDescripcion.getText().toString().trim().isEmpty() &&
                spinnerProductoCategoria.getSelectedItemPosition() != 0 &&
                spinnerProductoUnidad.getSelectedItemPosition() != 0 &&
                spinnerProductoMarca.getSelectedItemPosition() != 0;
    }

    private void onLoading(boolean flag) {
        // TRUE -> Loading
        if (flag) {
            imgButtonProductoBack.setEnabled(false);

            imgProductoImagenPicker.setEnabled(false);
            imgProductoCodigoPicker.setEnabled(false);
            imgProductoCodigoPicker.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorDisable));

            imgProductoAddCategoria.setEnabled(false);
            imgProductoAddCategoria.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDisable));
            imgProductoAddUnidad.setEnabled(false);
            imgProductoAddUnidad.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDisable));
            imgProductoAddMarca.setEnabled(false);
            imgProductoAddMarca.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDisable));

            etProductoCodigo.setEnabled(false);
            etProductoNombre.setEnabled(false);
            etProductoDescripcion.setEnabled(false);

            spinnerProductoCategoria.setEnabled(false);
            spinnerProductoUnidad.setEnabled(false);
            spinnerProductoMarca.setEnabled(false);

            btnProductoAdd.setEnabled(false);

            progressbarActivityProducto.setVisibility(View.VISIBLE);

        } else {
            imgButtonProductoBack.setEnabled(true);

            imgProductoImagenPicker.setEnabled(true);
            imgProductoCodigoPicker.setEnabled(true);
            imgProductoCodigoPicker.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));

            imgProductoAddCategoria.setEnabled(true);
            imgProductoAddCategoria.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            imgProductoAddUnidad.setEnabled(true);
            imgProductoAddUnidad.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            imgProductoAddMarca.setEnabled(true);
            imgProductoAddMarca.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            etProductoCodigo.setEnabled(true);
            etProductoNombre.setEnabled(true);
            etProductoDescripcion.setEnabled(true);

            spinnerProductoCategoria.setEnabled(true);
            spinnerProductoUnidad.setEnabled(true);
            spinnerProductoMarca.setEnabled(true);

            btnProductoAdd.setEnabled(true);

            progressbarActivityProducto.setVisibility(View.INVISIBLE);
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

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureCode.class);
        scanCodeLauncher.launch(options);
    }

    private void showDialogAdd(String _tipo) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ProductoActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_edittext, null);

        final TextView tvDialog = mView.findViewById(R.id.tvDialog);
        tvDialog.setText(_tipo);
        EditText etDialog = mView.findViewById(R.id.etDialog);
        etDialog.setHint(_tipo);
        Button btnDialogCancel = mView.findViewById(R.id.btnDialogCancel);
        Button btnDialogAccept = mView.findViewById(R.id.btnDialogAccept);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnDialogAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _TIPO_TEMP = etDialog.getText().toString().trim();

                if (!_TIPO_TEMP.isEmpty()) {
                    alertDialog.dismiss();
                    onLoading(true);

                    _ALL_SPINNER = false;
                    _ADD_TIPO = true;

                    switch (_tipo) {
                        case DIALOG_CATEGORIA:
                            agregarCategoria(_TIPO_TEMP);
                            break;

                        case DIALOG_UNIDAD_VENTA:
                            agregarUnidad(_TIPO_TEMP);
                            break;

                        case DIALOG_MARCA:
                            agregarMarca(_TIPO_TEMP);
                            break;
                    }

                } else {
                    etDialog.setError(msgRequiredFields);
                }
            }
        });

        alertDialog.show();

        /*final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_edittext);


        dialog.show();*/
    }

    private void showDialogColors() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ProductoActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_colors, null);

        Button btnDialogCancel = mView.findViewById(R.id.btnDialogColorCancel);
        Button btnDialogAccept = mView.findViewById(R.id.btnDialogColorAccept);

        CheckBox checkboxDialogColorBlack = mView.findViewById(R.id.checkboxDialogColorBlack);
        CheckBox checkboxDialogColorWhite = mView.findViewById(R.id.checkboxDialogColorWhite);
        CheckBox checkboxDialogColorRed = mView.findViewById(R.id.checkboxDialogColorRed);
        CheckBox checkboxDialogColorBlue = mView.findViewById(R.id.checkboxDialogColorBlue);
        CheckBox checkboxDialogColorGreen = mView.findViewById(R.id.checkboxDialogColorGreen);
        CheckBox checkboxDialogColorYellow = mView.findViewById(R.id.checkboxDialogColorYellow);
        CheckBox checkboxDialogColorOrange = mView.findViewById(R.id.checkboxDialogColorOrange);
        CheckBox checkboxDialogColorGray = mView.findViewById(R.id.checkboxDialogColorGray);
        CheckBox checkboxDialogColorPurple = mView.findViewById(R.id.checkboxDialogColorPurple);

        Map<String, String> mapColor = new HashMap<>();

        checkboxDialogColorBlack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mapColor.put(COLOR_BLACK, COLOR_BLACK);
                } else {
                    mapColor.remove(COLOR_BLACK);
                }
            }
        });

        checkboxDialogColorWhite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mapColor.put(COLOR_WHITE, COLOR_WHITE);
                } else {
                    mapColor.remove(COLOR_WHITE);
                }
            }
        });

        checkboxDialogColorRed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mapColor.put(COLOR_RED, COLOR_RED);
                } else {
                    mapColor.remove(COLOR_RED);
                }
            }
        });

        checkboxDialogColorBlue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mapColor.put(COLOR_BLUE, COLOR_BLUE);
                } else {
                    mapColor.remove(COLOR_BLUE);
                }
            }
        });

        checkboxDialogColorGreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mapColor.put(COLOR_GREEN, COLOR_GREEN);
                } else {
                    mapColor.remove(COLOR_GREEN);
                }
            }
        });

        checkboxDialogColorYellow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mapColor.put(COLOR_YELLOW, COLOR_YELLOW);
                } else {
                    mapColor.remove(COLOR_YELLOW);
                }
            }
        });

        checkboxDialogColorOrange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mapColor.put(COLOR_ORANGE, COLOR_ORANGE);
                } else {
                    mapColor.remove(COLOR_ORANGE);
                }
            }
        });

        checkboxDialogColorGray.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mapColor.put(COLOR_GRAY, COLOR_GRAY);
                } else {
                    mapColor.remove(COLOR_GRAY);
                }
            }
        });

        checkboxDialogColorPurple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mapColor.put(COLOR_PURPLE, COLOR_PURPLE);
                } else {
                    mapColor.remove(COLOR_PURPLE);
                }
            }
        });


        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnDialogAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaColors = new ArrayList<String>(mapColor.keySet());
                imgColorsSetGone();
                for (String color : listaColors) {
                    switch (color) {
                        case COLOR_BLACK:
                            imgProductoColorBlack.setVisibility(View.VISIBLE);
                            break;

                        case COLOR_WHITE:
                            imgProductoColorWhite.setVisibility(View.VISIBLE);
                            break;

                        case COLOR_RED:
                            imgProductoColorRed.setVisibility(View.VISIBLE);
                            break;

                        case COLOR_BLUE:
                            imgProductoColorBlue.setVisibility(View.VISIBLE);
                            break;

                        case COLOR_GREEN:
                            imgProductoColorGreen.setVisibility(View.VISIBLE);
                            break;

                        case COLOR_YELLOW:
                            imgProductoColorYellow.setVisibility(View.VISIBLE);
                            break;

                        case COLOR_ORANGE:
                            imgProductoColorOrange.setVisibility(View.VISIBLE);
                            break;

                        case COLOR_GRAY:
                            imgProductoColorGray.setVisibility(View.VISIBLE);
                            break;

                        case COLOR_PURPLE:
                            imgProductoColorPurple.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

        /*final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_edittext);


        dialog.show();*/
    }

    private void imgColorsSetGone() {
        imgProductoColorBlack.setVisibility(View.GONE);
        imgProductoColorWhite.setVisibility(View.GONE);
        imgProductoColorRed.setVisibility(View.GONE);
        imgProductoColorBlue.setVisibility(View.GONE);
        imgProductoColorGreen.setVisibility(View.GONE);
        imgProductoColorYellow.setVisibility(View.GONE);
        imgProductoColorOrange.setVisibility(View.GONE);
        imgProductoColorGray.setVisibility(View.GONE);
        imgProductoColorPurple.setVisibility(View.GONE);
    }

    private int obterIndexAddCategoria() {
        for (int i = 0; i < listaSpinnerCategoria.size(); i++) {
            if (listaSpinnerCategoria.get(i).equals(_TIPO_TEMP)) {
                return i;
            }
        }
        return -1;
    }

    private int obterIndexAddUnidad() {
        for (int i = 0; i < listaSpinnerUnidad.size(); i++) {
            if (listaSpinnerUnidad.get(i).equals(_TIPO_TEMP)) {
                return i;
            }
        }

        return -1;
    }

    private int obterIndexAddMarca() {
        for (int i = 0; i < listaSpinnerMarca.size(); i++) {
            if (listaSpinnerMarca.get(i).equals(_TIPO_TEMP)) {
                return i;
            }
        }

        return -1;
    }

    private void goFinish() {
        Intent data = new Intent();
        switch (_MODE) {
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
    // Cargar spinner categoria
    private void cargarSpinnerCategoria() {
        mfireStore.collection(COLLECTION_CATEGORIA)
                .whereEqualTo(FIREBASE_ID_TIENDA, _ID_TIENDA)
                .orderBy(FIREBASE_TIPO)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> lista = queryDocumentSnapshots.getDocuments();

                        listaSpinnerCategoria.clear();
                        listaSpinnerCategoria.add("");

                        for (DocumentSnapshot doc : lista) {
                            ClsCategoria cat = doc.toObject(ClsCategoria.class);
                            assert cat != null;
                            listaSpinnerCategoria.add(cat.getTipo());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_spinner, listaSpinnerCategoria);
                        spinnerProductoCategoria.setAdapter(adapter);

                        if (_ALL_SPINNER) {
                            cargarSpinnerUnidad();
                        } else {
                            if (_ADD_TIPO) {
                                int sw = obterIndexAddCategoria();
                                if (sw >= 0) {
                                    onLoading(false);
                                    spinnerProductoCategoria.setSelection(sw);
                                    _ADD_TIPO = false;
                                }
                            } else {
                                onLoading(false);
                            }
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

    // Cargar spinner unidad
    private void cargarSpinnerUnidad() {
        mfireStore.collection(COLLECTION_UNIDAD)
                .whereEqualTo(FIREBASE_ID_TIENDA, _ID_TIENDA)
                .orderBy(FIREBASE_TIPO)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> lista = queryDocumentSnapshots.getDocuments();

                        listaSpinnerUnidad.clear();
                        listaSpinnerUnidad.add("");

                        for (DocumentSnapshot doc : lista) {
                            ClsUnidad uni = doc.toObject(ClsUnidad.class);
                            assert uni != null;
                            listaSpinnerUnidad.add(uni.getTipo());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_spinner, listaSpinnerUnidad);
                        spinnerProductoUnidad.setAdapter(adapter);

                        if (_ALL_SPINNER) {
                            cargarSpinnerMarca();
                        } else {
                            if (_ADD_TIPO) {
                                int sw = obterIndexAddUnidad();
                                if (sw >= 0) {
                                    onLoading(false);
                                    spinnerProductoUnidad.setSelection(sw);
                                    _ADD_TIPO = false;
                                }
                            } else {
                                onLoading(false);
                            }
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

    // Cargar spinner unidad
    private void cargarSpinnerMarca() {
        mfireStore.collection(COLLECTION_MARCA)
                .whereEqualTo(FIREBASE_ID_TIENDA, _ID_TIENDA)
                .orderBy(FIREBASE_TIPO)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> lista = queryDocumentSnapshots.getDocuments();

                        listaSpinnerMarca.clear();
                        listaSpinnerMarca.add("");

                        for (DocumentSnapshot doc : lista) {
                            ClsUnidad uni = doc.toObject(ClsUnidad.class);
                            assert uni != null;
                            listaSpinnerMarca.add(uni.getTipo());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_spinner, listaSpinnerMarca);
                        spinnerProductoMarca.setAdapter(adapter);

                        if (_ADD_TIPO) {
                            int sw = obterIndexAddMarca();
                            if (sw >= 0) {
                                onLoading(false);
                                spinnerProductoMarca.setSelection(sw);
                                _ADD_TIPO = false;
                            }
                        } else {
                            onLoading(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        clsShowMessage.showMessageError(context, Objects.requireNonNull(e.getMessage()));
                        onLoading(false);
                        Log.e("Error Marca", e.getMessage());
                    }
                });
    }

    /* --------------------------------------------------- */
    // Agregar objeto categoria a Firebase
    private void agregarCategoria(String _tipo) {
        Map<String, String> map = new HashMap<>();

        map.put(FIREBASE_ID_TIENDA, _ID_TIENDA);
        map.put(FIREBASE_TIPO, _tipo);

        mfireStore.collection(COLLECTION_CATEGORIA).add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        cargarSpinnerCategoria();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

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

    // Agregar objeto unidad a Firebase
    private void agregarUnidad(String _tipo) {
        Map<String, String> map = new HashMap<>();

        map.put(FIREBASE_ID_TIENDA, _ID_TIENDA);
        map.put(FIREBASE_TIPO, _tipo);

        mfireStore.collection(COLLECTION_UNIDAD).add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        cargarSpinnerUnidad();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

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

    // Agregar objeto unidad a Firebase
    private void agregarMarca(String _tipo) {
        Map<String, String> map = new HashMap<>();

        map.put(FIREBASE_ID_TIENDA, _ID_TIENDA);
        map.put(FIREBASE_TIPO, _tipo);

        mfireStore.collection(COLLECTION_MARCA).add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        cargarSpinnerMarca();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

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
    // Agregar producto
    private void addProducto() {
        mfireStore.collection(COLLECTION_PRODUCTO).add(clsProducto)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        uploadImage(documentReference.getId());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

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
    private void uploadImage(String docReferences) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference reference = storage.getReference()
                .child(STORAGE_PATH_PRODUCTO)
                .child(clsProducto.getId_tienda() + "-" + clsProducto.getCodigo());
        reference.putFile(Uri.parse(clsProducto.getUri()))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        clsProducto.setUri(uri.toString());
                                        updateTiendaURI(docReferences);
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
    private void updateTiendaURI(String docReferences) {
        mfireStore.collection(COLLECTION_PRODUCTO).document(docReferences)
                .update(FIREBASE_URI, clsProducto.getUri())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
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
                                    .start(ProductoActivity.this);
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
                                    .start(ProductoActivity.this);
                        }
                    }
                });

        scanCodeLauncher = registerForActivityResult(
                new ScanContract(),
                new ActivityResultCallback<ScanIntentResult>() {
                    @Override
                    public void onActivityResult(ScanIntentResult result) {
                        if (result.getContents() != null) {
                            etProductoCodigo.setText(result.getContents());
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
            imgProductoImagen.setImageURI(imageUriUpload);
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

    /* --------------------------------------------------- *//*

    //**
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