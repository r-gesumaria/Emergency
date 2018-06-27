package helpfire.emergency;

import android.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Roberta on 11/06/2018.
 */
public class EditAccount extends AppCompatActivity {

    private AutoCompleteTextView nomeUtente,cognomeUtente,cfUtente, dataNascitaUtente, comuneNascitaUtente, indirizzoUtente,capUtente,
            numTelUetente,emailUtente,userUtente;
    private TextInputLayout confermaPsw;
    private Button btConf;

    private ArrayList<Utente> lista;
    private String nome,cognome, cf, numTel, user, psw;

    private String userCr, pswCr;
    private Utente utente;

    private AlertDialog.Builder alert;

    private TextView btModificaPsw;

    // CRED: nome del file sul quale verranno salvate le credenziali
    private final static String CREDENZIALI = "credenziali";
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        nomeUtente = (AutoCompleteTextView) findViewById(R.id.nomeUetente);
        cognomeUtente = (AutoCompleteTextView) findViewById(R.id.cognomeUetente);
        cfUtente = (AutoCompleteTextView) findViewById(R.id.cfUetente);
        dataNascitaUtente = (AutoCompleteTextView) findViewById(R.id.dataUetente);
        comuneNascitaUtente = (AutoCompleteTextView) findViewById(R.id.comuneUetente);
        indirizzoUtente = (AutoCompleteTextView) findViewById(R.id.indirizzoUetente);
        numTelUetente = (AutoCompleteTextView) findViewById(R.id.telefonoUetente);
        capUtente = (AutoCompleteTextView) findViewById(R.id.capUetente);
        emailUtente = (AutoCompleteTextView) findViewById(R.id.emailUetente);
        userUtente = (AutoCompleteTextView) findViewById(R.id.usernameUetente);
        userUtente.setEnabled(false);
        btModificaPsw = (TextView) findViewById(R.id.linkModificaPsw);
        btModificaPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creaAlert();
            }
        });

        btConf = (Button) findViewById(R.id.btConeferma);
        btConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlloCampi();
            }
        });

        //acquisisco le credenziali dell'utente
        SharedPreferences credenziali = getSharedPreferences(CREDENZIALI, MODE_PRIVATE);
        if(!credenziali.getString("username","").equals("") && !credenziali.getString("password","").equals("")){
            userCr = credenziali.getString("username","");
            pswCr = credenziali.getString("password","");
        }else{
            Log.d("EDIT","Mancano le credenziali");
        }

        //acquisisco i dati dell'utente
        try {
            lista = Controller.letturaFile();
            Log.d("FILE","lista "+lista);
            for(int i = 0; i<lista.size();i++) {
                Log.d("FILE","user cr"+ userCr);
                Log.d("FILE","psw cr "+pswCr);

                if (lista.get(i).getUsername().equalsIgnoreCase(userCr) && lista.get(i).getPassword().equalsIgnoreCase(pswCr)) {
                    utente = lista.get(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        nomeUtente.setText(utente.getNome());
        cognomeUtente.setText(utente.getCognome());
        cfUtente.setText(utente.getCf());
        dataNascitaUtente.setText(utente.getDataNascita());
        comuneNascitaUtente.setText(utente.getComuneNascita());
        indirizzoUtente.setText(utente.getIndirizzo());
        numTelUetente.setText(utente.getNunTel());
        capUtente.setText(utente.getCap());
        emailUtente.setText(utente.getEmail());
        userUtente.setText(userCr);
    }

    private void controlloCampi(){
        nome = nomeUtente.getText().toString().trim();
        cognome = cognomeUtente.getText().toString();
        cf = cfUtente.getText().toString().trim();
        numTel = numTelUetente.getText().toString().trim();
        user= userUtente.getText().toString().trim();

        if(!nome.equals("") && !cognome.equals("") && !cf.equals("") && !numTel.equals("") && !user.equals("")){

            if(cf.length()<16){
                Toast.makeText(EditAccount.this, "Il codice fiscale deve essere di 16 caratteri.", Toast.LENGTH_LONG).show();
                cfUtente.setError("Il codice fiscale deve essere di 16 caratteri.");
                cfUtente.setTextColor(Color.RED);
                cfUtente.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        cfUtente.setTextColor(Color.BLACK);
                        cfUtente.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }else if(numTel.length()<10){
                Toast.makeText(EditAccount.this, "Cifre non sufficienti.", Toast.LENGTH_LONG).show();
                numTelUetente.setError("Cifre non sufficienti.");
                numTelUetente.setTextColor(Color.RED);
                numTelUetente.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        numTelUetente.setTextColor(Color.BLACK);
                        numTelUetente.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }else {
                //confronto il nuovo utente con il nuovo, se sono diversi elimino il precedente ed inserisco il nuovo
                try {

                    Utente ut = new Utente(nomeUtente.getText().toString().trim(), cognomeUtente.getText().toString().trim(), cfUtente.getText().toString().trim(),
                            dataNascitaUtente.getText().toString().trim(), comuneNascitaUtente.getText().toString().trim(), indirizzoUtente.getText().toString().trim(),
                            capUtente.getText().toString().trim(), numTelUetente.getText().toString().trim(), emailUtente.getText().toString().trim(),
                            userCr, pswCr);
                    if (!utente.equals(ut)) {

                        //rimuovo il vecchio utenete e aggiungo il nuovo
                        Controller.rimuoviUtente(utente);
                        Controller.inserisciUtente(ut);

                        //salvo le nuove credenzili per un successivo accesso automatico
                        SharedPreferences credenziali = getSharedPreferences(CREDENZIALI, MODE_PRIVATE);
                        edit = credenziali.edit();
                        edit.putString("username", user);
                        edit.putString("password",pswCr);
                        edit.commit();

                    }
                    Toast.makeText(EditAccount.this, "Modifica avventuta con successo.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Segnalazione.class));
                }catch(Exception e) {
                    userUtente.setError(e.getMessage());
                    Toast.makeText(EditAccount.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(EditAccount.this, "Compila tutti i campi obigatori!", Toast.LENGTH_LONG).show();
        }
    }

    private void creaAlert(){
        alert = new AlertDialog.Builder(EditAccount.this);
        LayoutInflater inflater = EditAccount.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_modifica_password,null);

        final AutoCompleteTextView vecchiaPsw = dialogView.findViewById(R.id.vecchiaPsw);
        final AutoCompleteTextView nuovaPsw = dialogView.findViewById(R.id.nuovaPsw);
        final AutoCompleteTextView confNuovaPsw = dialogView.findViewById(R.id.confPsw);
        Button btAnnulla = dialogView.findViewById(R.id.btAnnullaModifica);
        Button btSalva = dialogView.findViewById(R.id.btConfermaPsw);

        alert.setView(dialogView);
        alert.setTitle("Modifica password");
        alert.setMessage(R.string.aiutoModificaPsw);
        alert.setCancelable(false);
        final AlertDialog alertMod = alert.show();

        nuovaPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pswVecchia = vecchiaPsw.getText().toString().trim();
                if(!pswVecchia.equals(pswCr)){
                    vecchiaPsw.setError("La password non è corretta");
                    vecchiaPsw.setTextColor(Color.RED);
                    vecchiaPsw.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            nuovaPsw.setText("");
                            vecchiaPsw.setError(null);
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            vecchiaPsw.setTextColor(Color.BLACK);

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {


                        }
                    });
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertMod.dismiss();
            }
        });

        btSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pswNuova = nuovaPsw.getText().toString().trim();
                String pswConfNuova = confNuovaPsw.getText().toString().trim();
                if(vecchiaPsw.getText().toString().length() != 0 && nuovaPsw.getText().toString().length() != 0 && confNuovaPsw.getText().toString().length() != 0) {
                    if (pswNuova.equals(pswConfNuova)) {

                        try {
                            Utente ut = new Utente(nomeUtente.getText().toString().trim(), cognomeUtente.getText().toString().trim(), cfUtente.getText().toString().trim(),
                                    dataNascitaUtente.getText().toString().trim(), comuneNascitaUtente.getText().toString().trim(), indirizzoUtente.getText().toString().trim(),
                                    capUtente.getText().toString().trim(), numTelUetente.getText().toString().trim(), emailUtente.getText().toString().trim(),
                                    userCr, pswNuova);

                            if (!utente.equals(ut)) {

                                //rimuovo il vecchio utenete e aggiungo il nuovo
                                Controller.rimuoviUtente(utente);
                                Controller.inserisciUtente(ut);

                                //salvo le nuove credenzili per un successivo accesso automatico
                                SharedPreferences credenziali = getSharedPreferences(CREDENZIALI, MODE_PRIVATE);
                                edit = credenziali.edit();

                                edit.putString("username", userCr);
                                edit.putString("password", pswNuova);
                                edit.commit();
                            }
                            Toast.makeText(EditAccount.this, "Password modificata con successo.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), EditAccount.class));
                            finish();
                        } catch (Exception e) {
                            userUtente.setError(e.getMessage());
                            Toast.makeText(EditAccount.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        confNuovaPsw.setError("Le password non coincidono.");
                        confNuovaPsw.setTextColor(Color.RED);
                    }
                }else{
                    Toast.makeText(EditAccount.this, "Compilare tutti i campi.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(EditAccount.this,Segnalazione.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}