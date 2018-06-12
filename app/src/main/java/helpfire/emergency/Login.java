package helpfire.emergency;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Roberta on 01/06/2018.
 */
public class Login extends AppCompatActivity {

    private static final int ID_RICHIESTA_PERMISSION= 0;
    private TextView btRegistrati,txtErrore;
    private Button btAccedi;
    private AutoCompleteTextView userLogin, pswLogin;
    private ArrayList<Utente> lista;

    // CRED: nome del file sul quale verranno salvate le credenziali
    private final static String CREDENZIALI = "credenziali";
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences credenziali = getSharedPreferences(CREDENZIALI, MODE_PRIVATE);
        if(!credenziali.getString("username","").equals("") && !credenziali.getString("password","").equals("")){
            startActivity(new Intent(getApplicationContext(),Segnalazione.class));
            finish();
        }

        setContentView(R.layout.login);

        try {
            Controller.creaFile(getApplicationContext());
        } catch (IOException e) {
            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        lista = new ArrayList<>();

        btRegistrati = (TextView) findViewById(R.id.registrazioneUtente);
        txtErrore = (TextView) findViewById(R.id.txtErrore);
        btAccedi = (Button) findViewById(R.id.btAccedi);
        userLogin = (AutoCompleteTextView) findViewById(R.id.userLogin);
        pswLogin = (AutoCompleteTextView) findViewById(R.id.pswLogin);

        richiediPermessi();

        btRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Registrazione.class));

            }
        });

        btAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user, psw;
                user = userLogin.getText().toString().trim();
                psw = pswLogin.getText().toString().trim();

                //controllo che siano stati inseriti user e psw
                if (user.equalsIgnoreCase("") || psw.equalsIgnoreCase("")) {
                    if (user.equalsIgnoreCase("")) {
                        userLogin.setError("Inserisci nome utente.");
                    } else if (psw.equalsIgnoreCase("")) {
                        pswLogin.setError("Inserisci password.");
                    }
                }else {
                    boolean contr = false;
                    try {
                        lista = Controller.letturaFile();

                        if(lista != null){

                            Log.d("FILE", "lista in login " + lista);
                            for (int i = 0; i < lista.size(); i++) {
                                if (lista.get(i).getUsername().equalsIgnoreCase(user) && lista.get(i).getPassword().equalsIgnoreCase(psw)) {
                                    contr = true;

                                    //salvo le credenziali per il successivo accesso all'applicazione
                                    SharedPreferences credenziali = getSharedPreferences(CREDENZIALI, MODE_PRIVATE);
                                    edit = credenziali.edit();
                                    edit.putString("username", user);
                                    edit.putString("password",psw);
                                    edit.commit();

                                    startActivity(new Intent(Login.this, Segnalazione.class));
                                }
                            }

                            if (!contr) {
                                txtErrore.setVisibility(View.VISIBLE);
                                userLogin.setTextColor(Color.RED);
                                userLogin.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        userLogin.setTextColor(Color.BLACK);
                                        userLogin.setError(null);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });
                                pswLogin.setTextColor(Color.RED);
                                pswLogin.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        pswLogin.setTextColor(Color.BLACK);
                                        pswLogin.setError(null);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });
                                Toast.makeText(Login.this, "Credenziali sbagliate!", Toast.LENGTH_LONG).show();
                            }
                        }else {                       //fine if lista != null
                            Log.d("FILE","lista == null");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }// fine else
            }
        });
    }

    /**
     * Richiesta dei permessi
     */
    private void richiediPermessi(){
        int statoPermissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int statoPermissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int statoPermissionPos = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int statoPermissionLoc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (statoPermissionWrite == PackageManager.PERMISSION_DENIED || statoPermissionCamera == PackageManager.PERMISSION_DENIED ||
                statoPermissionPos == PackageManager.PERMISSION_DENIED || statoPermissionLoc == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ID_RICHIESTA_PERMISSION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ID_RICHIESTA_PERMISSION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ID_RICHIESTA_PERMISSION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ID_RICHIESTA_PERMISSION);
        }
    }

    private void clearEdit(){
        pswLogin.setText("");
        userLogin.setText("");
    }
}
