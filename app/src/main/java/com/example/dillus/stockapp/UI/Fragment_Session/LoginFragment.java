package com.example.dillus.stockapp.UI.Fragment_Session;

import static android.app.Activity.RESULT_OK;
import static com.example.dillus.stockapp.AppLib.ClsMessage.msgNetworkError;
import static com.example.dillus.stockapp.AppLib.ClsMessage.msgNoPasswordExits;
import static com.example.dillus.stockapp.AppLib.ClsMessage.msgNoUserExits;
import static com.example.dillus.stockapp.AppLib.ClsMessage.msgRequiredFields;
import static com.example.dillus.stockapp.AppLib.ClsMessageErrorFirebase.msgErrorFirebase_NetworkError;
import static com.example.dillus.stockapp.AppLib.ClsMessageErrorFirebase.msgErrorFirebase_NoPasswordExits;
import static com.example.dillus.stockapp.AppLib.ClsMessageErrorFirebase.msgErrorFirebase_NoUserExits;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_USERS;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_EMAIL;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_NAME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_PASSWORD;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_PROVIDER;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_TIENDAS;
import static com.example.dillus.stockapp.AppLib.Clslibrary.PROVIDER_GOOGLE_ACCOUNT;
import static com.example.dillus.stockapp.AppLib.Clslibrary.REQUEST_CODE_GOOGLE_SIGN_IN;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.UI.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginFragment extends Fragment {

    private EditText etFragmentLoginEmail, etFragmentLoginPassword;
    private Button btnFragmentLoginAcceder, btnFragmentLoginRegistrarse, btnFragmentLoginAccederGoogle;
    private ImageView imgFragmentLoginEmail, imgFragmentLoginPassword;
    private ProgressBar progressbarFragmentLogin;

    private FirebaseAuth mfirebaseAuth;
    private FirebaseFirestore mfireStore;

    private GoogleSignInClient mGoogleSignInClient;

    private Context context;

    private NavController navController;

    private ActivityResultLauncher<Intent> googleSignIn_ActivityResult;

    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarCampos(view);
        onClickEvent();
        onLoading(false);
        activityResult();

        // Initialize Firebase Auth
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfireStore = FirebaseFirestore.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        progressbarFragmentLogin.setVisibility(View.INVISIBLE);

    }

    /**
     * ╠════════════════════ Events ════════════════════╣
     **/
    private void onClickEvent() {
        btnFragmentLoginAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onValidarCampos()) {
                    onLoading(true);
                    onSignInExistingUser();
                }
            }
        });

        btnFragmentLoginRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister();
            }
        });

        btnFragmentLoginAccederGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInGoogle();
            }
        });

    }

    /**
     * ╠════════════════════ Methods ════════════════════╣
     **/
    private void inicializarCampos(View view) {
        navController = Navigation.findNavController(view);

        etFragmentLoginEmail = view.findViewById(R.id.etFragmentLoginEmail);
        etFragmentLoginPassword = view.findViewById(R.id.etFragmentLoginPassword);

        btnFragmentLoginAcceder = view.findViewById(R.id.btnFragmentLoginAcceder);
        btnFragmentLoginRegistrarse = view.findViewById(R.id.btnFragmentLoginRegistrarse);
        btnFragmentLoginAccederGoogle = view.findViewById(R.id.btnFragmentLoginAccederGoogle);

        imgFragmentLoginEmail = view.findViewById(R.id.imgFragmentLoginEmail);
        imgFragmentLoginPassword = view.findViewById(R.id.imgFragmentLoginPassword);
        progressbarFragmentLogin = view.findViewById(R.id.progressbarFragmentLogin);
    }

    private boolean onValidarCampos() {
        if (etFragmentLoginEmail.getText().toString().trim().isEmpty())
            etFragmentLoginEmail.setError(msgRequiredFields);

        if (etFragmentLoginPassword.getText().toString().trim().isEmpty())
            etFragmentLoginPassword.setError(msgRequiredFields);

        return !etFragmentLoginEmail.getText().toString().trim().isEmpty() && !etFragmentLoginPassword.getText().toString().trim().isEmpty();
    }

    private void goMain() {
        Intent intent = new Intent(context, MainActivity.class);
        getActivity().finish();
        startActivity(intent);
    }

    private void goRegister() {
        navController.navigate(R.id.registerFragment);
        clearData();
    }

    private void clearData() {
        etFragmentLoginEmail.setText("");
        etFragmentLoginPassword.setText("");
    }

    // Iniciar Sesion con usuario existente
    private void onSignInExistingUser() {
        mfirebaseAuth.signInWithEmailAndPassword(etFragmentLoginEmail.getText().toString(), etFragmentLoginPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get current user
                            FirebaseUser user = mfirebaseAuth.getCurrentUser();

                            // Save data on DB

                            // Go main activity
                            goMain();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onLoading(false);
                        showMessageError(Objects.requireNonNull(e.getMessage()));
                    }
                });
    }

    private void onSignInGoogle() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        googleSignIn_ActivityResult.launch(intent);
        //startActivityForResult(intent, REQUEST_CODE_GOOGLE_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mfirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult.getUser() != null) {
                            Map<String, Object> map = new HashMap<>();

                            Map<String, Object> map_tiendas = new HashMap<>();
                            List<Map<String, Object>> lista_tiendas = new ArrayList<>();

                            map.put(FIREBASE_ID, authResult.getUser().getUid());
                            map.put(FIREBASE_NAME, authResult.getUser().getDisplayName());
                            map.put(FIREBASE_EMAIL, authResult.getUser().getEmail());
                            map.put(FIREBASE_PROVIDER, PROVIDER_GOOGLE_ACCOUNT);
                            map.put(FIREBASE_PASSWORD, "");
                            map.put(FIREBASE_TIENDAS, lista_tiendas);
                            mfireStore.collection(COLLECTION_USERS).document(authResult.getUser().getUid()).set(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            obtenerTiendas(authResult.getUser().getUid());
                                            // VERIFICAR TIENDAS
                                            //goMain();
                                        }
                                    })
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                            Toast.makeText(context, authResult.getUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(context, "Complete!!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void obtenerTiendas(String uid) {
        Map<String, Object> map = new HashMap<>();
        map.put(FIREBASE_ID, "asdasdasd");
        mfireStore.collection(COLLECTION_USERS)
                .document(uid)
                .collection(FIREBASE_TIENDAS)asdasd
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(context, "DONE", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showMessageError(String msg) {
        if (msg.equals(msgErrorFirebase_NoPasswordExits))
            Toast.makeText(context, msgNoPasswordExits, Toast.LENGTH_SHORT).show();

        else if (msg.equals(msgErrorFirebase_NoUserExits))
            Toast.makeText(context, msgNoUserExits, Toast.LENGTH_SHORT).show();

        else if (msg.equals(msgErrorFirebase_NetworkError))
            Toast.makeText(context, msgNetworkError, Toast.LENGTH_SHORT).show();

        else
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void onLoading(boolean flag) {
        // TRUE -> Loading
        if (flag) {
            imgFragmentLoginEmail.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorDisable));
            imgFragmentLoginPassword.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorDisable));

            etFragmentLoginEmail.setEnabled(false);
            etFragmentLoginPassword.setEnabled(false);

            progressbarFragmentLogin.setVisibility(View.VISIBLE);

            btnFragmentLoginAcceder.setEnabled(false);
            btnFragmentLoginRegistrarse.setEnabled(false);
            btnFragmentLoginAccederGoogle.setEnabled(false);
        } else {
            imgFragmentLoginEmail.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
            imgFragmentLoginPassword.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));

            etFragmentLoginEmail.setEnabled(true);
            etFragmentLoginPassword.setEnabled(true);

            progressbarFragmentLogin.setVisibility(View.INVISIBLE);

            btnFragmentLoginAcceder.setEnabled(true);
            btnFragmentLoginRegistrarse.setEnabled(true);
            btnFragmentLoginAccederGoogle.setEnabled(true);
        }
    }

    /**
     * ╠════════════════════ ACTIVITY RESULT ════════════════════╣
     **/
    private void activityResult() {
        googleSignIn_ActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            if (task.isSuccessful()) {
                                try {
                                    GoogleSignInAccount account = task.getResult(ApiException.class);
                                    firebaseAuthWithGoogle(account.getIdToken());
                                } catch (ApiException e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
    }

    /**
     * ╠════════════════════ Override ════════════════════╣
     **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN && resultCode == RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            //LoginActivity.this.finish();
        }
    }
}