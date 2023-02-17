package com.example.dillus.stockapp.AppLib;

public class Clslibrary {

    // REQUEST CODE
    public static final int REQUEST_CODE_GOOGLE_SIGN_IN = 10001;
    public static final int REQUEST_CODE_ADD_TIENDA = 10002;

    public static final int REQUEST_CODE_CAMERA_PERMISSIONS = 10101;
    public static final int REQUEST_CODE_GALERY_PERMISSIONS = 10102;

    // ACTIVITY MODE STRING
    public static final String MODE_INTENT = "MODE";

    // ACTIVITY MODE
    public static final String MODE_NUEVO = "MODE_NUEVO";
    public static final String MODE_EDITAR = "MODE_EDITAR";
    public static final String MODE_VER = "MODE_VER";
    public static final String MODE_FIRST_TIME = "FIRST_TIME";

    // DATABASE FIREBASE
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_CATEGORIA = "categoria";
    public static final String COLLECTION_UNIDAD = "unidad";
    public static final String COLLECTION_MARCA = "marca";
    public static final String COLLECTION_TIENDA = "tienda";
    public static final String COLLECTION_USER_TIENDA = "user_tienda";
    public static final String COLLECTION_PRODUCTO = "producto";

    // FIREBASE STORAGE
    public static final String STORAGE_PATH_TIENDA_LOGO = "tienda_logo";
    public static final String STORAGE_PATH_PRODUCTO = "producto";

    // CAMPO ATRIBUTOS
    public static final String FIREBASE_ID_USER = "id_user";
    public static final String FIREBASE_ID_TIENDA = "id_tienda";
    public static final String FIREBASE_CODIGO= "codigo";

    public static final String FIREBASE_ID_PROVEEDOR = "id_proveedor";

    public static final String FIREBASE_NAME = "name";
    public static final String FIREBASE_EMAIL = "email";
    public static final String FIREBASE_PASSWORD = "password";
    public static final String FIREBASE_DESCRIPTION = "description";
    public static final String FIREBASE_PROVIDER = "provider";
    public static final String FIREBASE_URI = "uri";
    public static final String FIREBASE_TIENDAS = "tiendas";

    public static final String FIREBASE_TIPO = "tipo";

    // PROVIDER
    public static final String PROVIDER_GOOGLE_ACCOUNT = "GOOGLE_ACCOUNT";

    // COUNT DOWN
    public static final String STRING_COUNTDOWN = "STRING_COUNTDOWN";
    public static final long COUNTDOWN_0 = 0;
    public static final long COUNTDOWN = 2000;

    // TRANSITION
    public static final long DURATION_TRANSITION = 2000;


    // EXTENSION
    public static final String EXTENSION_JPG = ".jpg";

    // RESOLUCION JPG
    public static final int RESOLUCION_IMAGE = 1000;

    // COLOR STRING
    public static final String COLOR_BLACK = "black";
    public static final String COLOR_WHITE = "white";
    public static final String COLOR_RED = "red";
    public static final String COLOR_BLUE = "blue";
    public static final String COLOR_GREEN = "green";
    public static final String COLOR_YELLOW = "yellow";
    public static final String COLOR_ORANGE = "orange";
    public static final String COLOR_GRAY = "gray";
    public static final String COLOR_PURPLE = "purple";

    // Dialog options
    public static final String DIALOG_CATEGORIA = "Categoría";
    public static final String DIALOG_UNIDAD_VENTA = "Unidad Venta";
    public static final String DIALOG_MARCA = "Marca";

}
