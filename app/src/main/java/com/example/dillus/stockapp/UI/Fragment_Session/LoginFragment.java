package com.example.dillus.stockapp.UI.Fragment_Session;

import static android.app.Activity.RESULT_OK;
import static com.example.dillus.stockapp.AppLib.ClsMessage.msgRequiredEmailFormat;
import static com.example.dillus.stockapp.AppLib.ClsMessage.msgRequiredFields;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_USERS;
import static com.example.dillus.stockapp.AppLib.Clslibrary.COLLECTION_USER_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_EMAIL;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_TIENDA;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_ID_USER;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_NAME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.FIREBASE_PROVIDER;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_FIRST_TIME;
import static com.example.dillus.stockapp.AppLib.Clslibrary.MODE_INTENT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

import com.example.dillus.stockapp.AppData.ClsCategoria;
import com.example.dillus.stockapp.AppData.ClsTienda;
import com.example.dillus.stockapp.AppData.ClsUserTienda;
import com.example.dillus.stockapp.AppLib.ClsShowMessage;
import com.example.dillus.stockapp.AppPreferences.PreferTienda;
import com.example.dillus.stockapp.R;
import com.example.dillus.stockapp.UI.Activities.TiendaActivity;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    private ClsShowMessage clsShowMessage = new ClsShowMessage();

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
                onLoading(true);
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

        if (!Patterns.EMAIL_ADDRESS.matcher(etFragmentLoginEmail.getText().toString().trim()).matches())
            etFragmentLoginEmail.setError(msgRequiredEmailFormat);

        if (etFragmentLoginPassword.getText().toString().trim().isEmpty())
            etFragmentLoginPassword.setError(msgRequiredFields);

        return !etFragmentLoginEmail.getText().toString().trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(etFragmentLoginEmail.getText().toString().trim()).matches() &&
                !etFragmentLoginPassword.getText().toString().trim().isEmpty();
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

    /* --------------------------------------------------- */

    // Iniciar sesion con google account
    private void onSignInGoogle() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        googleSignIn_ActivityResult.launch(intent);
        //startActivityForResult(intent, REQUEST_CODE_GOOGLE_SIGN_IN);
    }

    // Autenticar google account credencial
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mfirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        if (user != null) {
                            existeUsuario(user);
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

    // Iniciar Sesion con usuario existente
    private void onSignInExistingUser() {
        mfirebaseAuth.signInWithEmailAndPassword(etFragmentLoginEmail.getText().toString(), etFragmentLoginPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        if (user != null) {
                            obtenerTiendas(user);
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

    // Verificar si existe el usuario en la base de datos
    private void existeUsuario(FirebaseUser _user) {
        mfireStore.collection(COLLECTION_USERS)
                .whereEqualTo(FIREBASE_ID_USER, _user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot lista = task.getResult();
                            if (lista.size() == 0) { // Nuevo Usuario
                                addUserDatabase(_user);
                            } else {
                                obtenerTiendas(_user);
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

    // Agregar usuario a firebase database
    private void addUserDatabase(FirebaseUser _user) {
        Map<String, Object> map = new HashMap<>();

        map.put(FIREBASE_ID_USER, _user.getUid());
        map.put(FIREBASE_NAME, _user.getDisplayName());
        map.put(FIREBASE_EMAIL, _user.getEmail());
        map.put(FIREBASE_PROVIDER, _user.getProviderId());

        mfireStore.collection(COLLECTION_USERS).document(_user.getUid()).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
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

    // Obtener tienda (s)
    private void obtenerTiendas(FirebaseUser _user) {
        mfireStore.collection(COLLECTION_USER_TIENDA)
                .whereEqualTo(FIREBASE_ID_USER, _user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> lista = queryDocumentSnapshots.getDocuments();
                        if (lista.size() > 0) {
                            ClsUserTienda cls = lista.get(0).toObject(ClsUserTienda.class);
                            assert cls != null;
                            new PreferTienda().setID_TIENDA(context, cls.getId_tienda());

                            mfireStore.collection(COLLECTION_TIENDA)
                                    .whereEqualTo(FIREBASE_ID_TIENDA, cls.getId_tienda())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> listatienda = queryDocumentSnapshots.getDocuments();
                                            if(listatienda.size() > 0){
                                                ClsTienda tienda = listatienda.get(0).toObject(ClsTienda.class);
                                                assert tienda != null;
                                                new PreferTienda().setNOMBRE_TIENDA(context, tienda.getName());
                                                new PreferTienda().setURI_TIENDA(context, tienda.getUri());

                                                goMain();
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
                        } else {
                            goCreateTienda();
                        }
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        /*if (task.isSuccessful()) {
                            QuerySnapshot lista = task.getResult();
                            if (lista.size() == 0) { // Nuevo Usuario
                                goCreateTienda();
                            } else if(lista.size() > 0){
                                lista.get
                                new PreferTienda().setID_TIENDA(context, lista.getDocuments().get(0).get(FIREBASE_ID_TIENDA).toString());
                                goMain();
                            }
                        }*/
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
    private void goCreateTienda() {
        Intent intent = new Intent(context, TiendaActivity.class);
        intent.putExtra(MODE_INTENT, MODE_FIRST_TIME);
        startActivity(intent);
        requireActivity().finish();
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
                                    clsShowMessage.showMessageError(context, Objects.requireNonNull(e.getMessage()));
                                    onLoading(false);
                                }
                            }
                        } else {
                            onLoading(false);
                        }
                    }
                });
    }
}














