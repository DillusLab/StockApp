package com.example.dillus.stockapp.UI.Fragment_Session;

import static com.example.dillus.stockapp.AppLib.ClsMessage.msgRequiredFields;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.UI.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterFragment extends Fragment {

    private ImageButton imgButtonFragmentRegisterBack;
    private ImageView imgFragmentRegisterName, imgFragmentRegisterPassword,
            imgFragmentRegisterEmail, imgFragmentRegisterPasswordVisibility;
    private EditText etFragmentRegisterName, etFragmentRegisterEmail, etFragmentRegisterPassword;
    private Button btnFragmentRegisterRegister;

    private FirebaseAuth mfirebaseAuth;

    private Context context;

    private NavController navController;

    private boolean passHidden = true;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance(String param1, String param2) {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarCampos(view);
        onClickEvent();

        // Initialize Firebase Auth
        mfirebaseAuth = FirebaseAuth.getInstance();
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
        getActivity().finish();
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

    private void onSignUpNewUsers(String _email, String _password, String _name){
        mfirebaseAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Get current user
                        FirebaseUser user = mfirebaseAuth.getCurrentUser();
                        // Update user name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                                .Builder().setDisplayName(_name).build();

                        assert user != null;
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            goMain();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}