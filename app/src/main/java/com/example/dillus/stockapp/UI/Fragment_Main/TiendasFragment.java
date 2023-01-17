package com.example.dillus.stockapp.UI.Fragment_Main;

import static android.app.Activity.RESULT_OK;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_NUEVO;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_STRING_NUEVO;
import static com.example.dillus.stockapp.AppLib.Clslibrary.REQUEST_CODE_ADD_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.REQUEST_CODE_GOOGLE_SIGN_IN;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dillus.stockapp.AppData.ClsTienda;
import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.UI.Activities.TiendaActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class TiendasFragment extends Fragment {

    private Button btnFragmentTiendas;



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
    }
    /** ╠════════════════════ Events ════════════════════╣ **/
    private void onClickEvent(){
        btnFragmentTiendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTienda();
            }
        });
    }
    /** ╠════════════════════ Methods ════════════════════╣ **/
    private void inicializarCampos(View view) {
        btnFragmentTiendas = view.findViewById(R.id.btnFragmentTiendas);
    }

    private void goTienda(){
        Intent intent = new Intent(context, TiendaActivity.class);
        intent.putExtra(MODE_STRING_NUEVO, MODE_NUEVO);
        startActivityForResult(intent, REQUEST_CODE_ADD_TIENDA);
    }
    /** ╠════════════════════ Override ════════════════════╣ **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_TIENDA && resultCode == RESULT_OK) {
            btnFragmentTiendas.setText(data.getDataString());
        } else {
            //LoginActivity.this.finish();
        }
    }
}