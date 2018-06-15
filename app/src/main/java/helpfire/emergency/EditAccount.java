package helpfire.emergency;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Roberta on 11/06/2018.
 */
public class EditAccount extends AppCompatActivity {

    private AutoCompleteTextView nomeUtente,cognomeUtente,cfUtente, dataNascitaUtente, comuneNascitaUtente, indirizzoUtente,capUtente,
            numTelUetente,emailUtente,userUtente, pswUtente, confPswUtente;
    private TextInputLayout confermaPsw;

    private ArrayList<Utente> lista;
    private String nome,cognome, cf, numTel, user, psw;

    private String userCr, pswCr;
    private Utente utente;

    // CRED: nome del file sul quale verranno salvate le credenziali
    private final static String CREDENZIALI = "credenziali";
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

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
        pswUtente = (AutoCompleteTextView) findViewById(R.id.pswUetente);
        confPswUtente = (AutoCompleteTextView) findViewById(R.id.confPswUetente);
        confermaPsw = (TextInputLayout) findViewById(R.id.confermaPsw);
        confermaPsw.setVisibility(View.INVISIBLE);

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
            for(int i = 0; i<lista.size();i++) {
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
        pswUtente.setText(pswCr);
        confPswUtente.setText(pswCr);

        pswUtente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confPswUtente.setText("");
                confermaPsw.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void controlloCampi(){
        nome = nomeUtente.getText().toString().trim();
        cognome = cognomeUtente.getText().toString();
        cf = cfUtente.getText().toString().trim();
        numTel = numTelUetente.getText().toString().trim();
        user= userUtente.getText().toString().trim();
        psw = pswUtente.getText().toString().trim();
        if(!nome.equals("") && !cognome.equals("") && !cf.equals("") && !numTel.equals("") && !user.equals("") && !psw.equals("") && !confPswUtente.getText().toString().trim().equals("")){

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
            }else if(psw.equals(confPswUtente.getText().toString().trim())){
               //confronto il nuovo utente con il nuovo, se sono diversi elimino il precedente ed inserisco il nuovo
                try {
                    Utente ut = new Utente(nomeUtente.getText().toString().trim(), cognomeUtente.getText().toString().trim(), cfUtente.getText().toString().trim(),
                            dataNascitaUtente.getText().toString().trim(), comuneNascitaUtente.getText().toString().trim(), indirizzoUtente.getText().toString().trim(),
                            capUtente.getText().toString().trim(), numTelUetente.getText().toString().trim(), emailUtente.getText().toString().trim(),
                            user, psw);
                    if (!utente.equals(ut)) {
                        //rimuovo il vecchio utenete e aggiungo il nuovo
                        Controller.rimuoviUtente(utente);
                        Controller.inserisciUtente(ut);

                        //salvo le nuove credenzili per un successivo accesso automatico
                        SharedPreferences credenziali = getSharedPreferences(CREDENZIALI, MODE_PRIVATE);
                        edit = credenziali.edit();
                        edit.putString("username", user);
                        edit.putString("password",psw);
                        edit.commit();
                    }
                    Toast.makeText(EditAccount.this, "Modifica avventuta con successo.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Segnalazione.class));
                }catch(Exception e) {
                    userUtente.setError(e.getMessage());
                    Toast.makeText(EditAccount.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else{
                confPswUtente.setError("Le password non coincidono!");
                Toast.makeText(EditAccount.this, "Le password non coincidono!", Toast.LENGTH_LONG).show();
                confPswUtente.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        confPswUtente.setTextColor(Color.BLACK);
                        confPswUtente.setError(null);
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        }else{
            Toast.makeText(EditAccount.this, "Compila tutti i campi obigatori!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.salva_registrazione, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_saveFile) {
            controlloCampi();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
