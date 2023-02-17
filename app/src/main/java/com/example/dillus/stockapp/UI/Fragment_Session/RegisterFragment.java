package com.example.dillus.stockapp.UI.Fragment_Session;

import static com.example.dillus.stockapp.AppLib.ClsMessage.msgRequiredFields;
import static com.example.dillus.stockapp.AppLib.ClsMessage.msgSuccess;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_USERS;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_EMAIL;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_USER;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_NAME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_PROVIDER;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_FIRST_TIME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_INTENT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dillus.stockapp.AppLib.ClsShowMessage;
import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.UI.Activities.TiendaActivity;
import com.example.dillus.stockapp.UI.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterFragment extends Fragment {

    private ImageButton imgButtonFragmentRegisterBack;
    private ProgressBar progressbarFragmentRegister;
    private ImageView imgFragmentRegisterName, imgFragmentRegisterPassword,
            imgFragmentRegisterEmail, imgFragmentRegisterPasswordVisibility;
    private EditText etFragmentRegisterName, etFragmentRegisterEmail, etFragmentRegisterPassword;
    private Button btnFragmentRegisterRegister;

    private FirebaseAuth mfirebaseAuth;
    private FirebaseFirestore mfireStore;

    private Context context;
    private ClsShowMessage clsShowMessage = new ClsShowMessage();

    private NavController navController;

    private boolean passHidden = true;

    public RegisterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert container != null;
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarCampos(view);
        onClickEvent();
        onLoading(false);

        // Initialize Firebase Auth
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfireStore = FirebaseFirestore.getInstance();
    }

    /** ╠════════════════════ Events ════════════════════╣ **/
    private void onClickEvent(){
        imgButtonFragmentRegisterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLogin();
            }
        });

        imgFragmentRegisterPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passHidden) {
                    imgFragmentRegisterPasswordVisibility.setImageResource(R.drawable.ic_visibility_off);
                    imgFragmentRegisterPasswordVisibility.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
                    etFragmentRegisterPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etFragmentRegisterPassword.setSelection(etFragmentRegisterPassword.length());
                    passHidden = false;
                } else {
                    imgFragmentRegisterPasswordVisibility.setImageResource(R.drawable.ic_visibility);
                    imgFragmentRegisterPasswordVisibility.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorDisable));
                    etFragmentRegisterPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etFragmentRegisterPassword.setSelection(etFragmentRegisterPassword.length());
                    passHidden = true;
                }
            }
        });

        btnFragmentRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onValidarCampos()){
                    onLoading(true);
                    onSignUpNewUsers(etFragmentRegisterEmail.getText().toString().trim(),
                            etFragmentRegisterPassword.getText().toString().trim(),
                            etFragmentRegisterName.getText().toString().trim());
                }
            }
        });

    }

    /** ╠════════════════════ Methods ════════════════════╣ **/
    private void inicializarCampos(View view) {
        navController = Navigation.findNavController(view);

        imgButtonFragmentRegisterBack = view.findViewById(R.id.imgButtonFragmentRegisterBack);

        progressbarFragmentRegister = view.findViewById(R.id.progressbarFragmentRegister);

        imgFragmentRegisterName = view.findViewById(R.id.imgFragmentRegisterName);
        imgFragmentRegisterPassword = view.findViewById(R.id.imgFragmentRegisterPassword);
        imgFragmentRegisterEmail = view.findViewById(R.id.imgFragmentRegisterEmail);
        imgFragmentRegisterPasswordVisibility = view.findViewById(R.id.imgFragmentRegisterPasswordVisibility);

        etFragmentRegisterName = view.findViewById(R.id.etFragmentRegisterName);
        etFragmentRegisterEmail = view.findViewById(R.id.etFragmentRegisterEmail);
        etFragmentRegisterPassword = view.findViewById(R.id.etFragmentRegisterPassword);

        btnFragmentRegisterRegister = view.findViewById(R.id.btnFragmentRegisterRegister);
    }

    private void goLogin(){
        navController.navigate(R.id.loginFragment);
        clearData();
    }

    private void goMain() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void onLoading(boolean flag) {
        // TRUE -> Loading
        if (flag) {
            imgButtonFragmentRegisterBack.setEnabled(false);

            imgFragmentRegisterName.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorDisable));
            etFragmentRegisterName.setEnabled(false);

            imgFragmentRegisterEmail.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorDisable));
            etFragmentRegisterEmail.setEnabled(false);

            imgFragmentRegisterPassword.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorDisable));
            etFragmentRegisterPassword.setEnabled(false);

            progressbarFragmentRegister.setVisibility(View.VISIBLE);

            btnFragmentRegisterRegister.setEnabled(false);
        } else {
            imgButtonFragmentRegisterBack.setEnabled(false);

            imgFragmentRegisterName.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
            etFragmentRegisterName.setEnabled(true);

            imgFragmentRegisterEmail.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
            etFragmentRegisterEmail.setEnabled(true);

            imgFragmentRegisterPassword.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
            etFragmentRegisterPassword.setEnabled(true);

            progressbarFragmentRegister.setVisibility(View.INVISIBLE);

            btnFragmentRegisterRegister.setEnabled(true);
        }
    }

    private void clearData(){
        etFragmentRegisterName.setText("");
        etFragmentRegisterEmail.setText("");
        etFragmentRegisterPassword.setText("");
    }

    private boolean onValidarCampos() {
        if(etFragmentRegisterName.getText().toString().trim().isEmpty())
            etFragmentRegisterName.setError(msgRequiredFields);

        if(etFragmentRegisterEmail.getText().toString().trim().isEmpty())
            etFragmentRegisterEmail.setError(msgRequiredFields);

        if(etFragmentRegisterPassword.getText().toString().trim().isEmpty())
            etFragmentRegisterPassword.setError(msgRequiredFields);

        return !etFragmentRegisterName.getText().toString().trim().isEmpty() &&
               !etFragmentRegisterEmail.getText().toString().trim().isEmpty() &&
               !etFragmentRegisterPassword.getText().toString().trim().isEmpty();
    }

    /* --------------------------------------------------- */

    // Registrar nuevo usuario
    private void onSignUpNewUsers(String _email, String _password, String _name){
        mfirebaseAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        if(user != null){
                            actualizarDatos(user, _name);
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

    // Actualizar Datos del Usuario
    private void actualizarDatos(FirebaseUser _user, String _name){

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                .Builder().setDisplayName(_name).build();

        _user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Guardar en firebase
                            addUserDatabase(_user);
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

    // Agregar usuario a firebase database
    private void addUserDatabase(FirebaseUser _user){
        Map<String, Object> map = new HashMap<>();

        map.put(FIREBASE_ID_USER, _user.getUid());
        map.put(FIREBASE_NAME, _user.getDisplayName());
        map.put(FIREBASE_EMAIL, _user.getEmail());
        map.put(FIREBASE_PROVIDER, _user.getProviderId());

        mfireStore.collection(COLLECTION_USERS).document(_user.getUid()).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            goCreateTienda();
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

    // Go tienda
    private void goCreateTienda(){
        Intent intent = new Intent(context, TiendaActivity.class);
        intent.putExtra(MODE_INTENT, MODE_FIRST_TIME);
        startActivity(intent);
        requireActivity().finish();
    }
}