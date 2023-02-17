package com.example.dillus.stockapp.AppPreferences;

import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_NAME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_URI;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferTienda {

    private final String PREFERENCES_NAME = "PREFERENCES_TIENDA";

    private String ID_TIENDA;
    private String NOMBRE_TIENDA;
    private String URI_TIENDA;

    public String getID_TIENDA(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.ID_TIENDA = sharedPreferences.getString(FIREBASE_ID_TIENDA, "null");
        return ID_TIENDA;
    }

    public String getNOMBRE_TIENDA(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.NOMBRE_TIENDA = sharedPreferences.getString(FIREBASE_NAME, "null");
        return NOMBRE_TIENDA;
    }

    public String getURI_TIENDA(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.URI_TIENDA = sharedPreferences.getString(FIREBASE_URI, "null");
        return URI_TIENDA;
    }

    public void setID_TIENDA(Context context, String id_tienda) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIREBASE_ID_TIENDA, id_tienda);
        editor.apply();
        this.ID_TIENDA = id_tienda;
    }

    public void setNOMBRE_TIENDA(Context context, String nombre_tienda) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIREBASE_NAME, nombre_tienda);
        editor.apply();
        this.NOMBRE_TIENDA = nombre_tienda;
    }

    public void setURI_TIENDA(Context context, String uri_tienda) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIREBASE_URI, uri_tienda);
        editor.apply();
        this.URI_TIENDA = uri_tienda;
    }
}
