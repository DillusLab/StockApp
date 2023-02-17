package com.example.dillus.stockapp.AppData;

import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_DESCRIPTION;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_NAME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_URI;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ClsTienda {

    private FirebaseFirestore mfireStore;

    private String id;
    private String name;
    private String description;
    private String uri;

    public ClsTienda() {

    }

    public ClsTienda(String id, String name, String description, String uri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void addTienda (ClsTienda clsTienda, Context context){

        mfireStore = FirebaseFirestore.getInstance();
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
                        String _id = documentReference.getId();
                        mfireStore.collection(COLLECTION_TIENDA).document(_id)
                                .update(FIREBASE_ID_TIENDA, _id)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Done!!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ID TIENDA ERROR: ", e.getMessage());
                    }
                });
    }

    public boolean addIdTienda (ClsTienda clsTienda){

        mfireStore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        //map.put(FIREBASE_ID, _id);

        return false;
    }
    class hiloAdd extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }
}
