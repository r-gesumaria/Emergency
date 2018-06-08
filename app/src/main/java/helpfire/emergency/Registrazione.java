package helpfire.emergency;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import java.io.IOException;
import java.util.Calendar;

public class Registrazione extends AppCompatActivity {

    private AutoCompleteTextView nomeUtente,cognomeUtente,cfUtente, dataNascitaUtente, comuneNascitaUtente, indirizzoUtente,capUtente,
        numTelUetente,emailUtente,userUtente, pswUtente, confPswUtente;
    private Button btConf;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    // CRED: nome del file sul quale verranno salvate le credenziali
    private final static String CREDENZIALI = "credenziali";
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        pswUtente = (AutoCompleteTextView) findViewById(R.id.pswUetente);
        confPswUtente = (AutoCompleteTextView) findViewById(R.id.confPswUetente);
        btConf = (Button) findViewById(R.id.btConfReg);

        insertData();

        SharedPreferences credenziali = getSharedPreferences(CREDENZIALI, MODE_PRIVATE);
        if(!credenziali.getString("username","").equals("") && !credenziali.getString("password","").equals("")){
           startActivity(new Intent(getApplicationContext(),Segnalazione.class));
        }

        btConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //controllo che i campi obbligatori siano stati compilati
                if(!nomeUtente.getText().toString().equals("") && !cognomeUtente.getText().toString().equals("") && !cfUtente.getText().toString().equals("") && !numTelUetente.getText().toString().equals("")
                        && !userUtente.getText().toString().equals("") && !pswUtente.getText().toString().equals("") && !confPswUtente.getText().toString().equals("")){

                    if(cfUtente.getText().toString().length()<16){
                        Toast.makeText(Registrazione.this, "Il codice fiscale deve essere di 16 caratteri.", Toast.LENGTH_LONG).show();
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
                    }else if(numTelUetente.getText().toString().length()<10){
                        Toast.makeText(Registrazione.this, "Cifre non sufficienti.", Toast.LENGTH_LONG).show();
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
                    }else if(pswUtente.getText().toString().equalsIgnoreCase(confPswUtente.getText().toString())){
                        //se l'utente non è già esistente salvo il nuovo utente altrimenti mostro il messaggio di errore
                        try {
                            Utente ut = new Utente(nomeUtente.getText().toString(),cognomeUtente.getText().toString(),cfUtente.getText().toString(),
                                    dataNascitaUtente.getText().toString(),comuneNascitaUtente.getText().toString(),indirizzoUtente.getText().toString(),
                                    capUtente.getText().toString(),numTelUetente.getText().toString(),emailUtente.getText().toString(), userUtente.getText().toString(),
                                    pswUtente.getText().toString());

                            Controller.inserisciUtente(ut);

                            //salvo le credenziali per il successivo accesso all'applicazione
                            SharedPreferences credenziali = getSharedPreferences(CREDENZIALI, MODE_PRIVATE);
                            edit = credenziali.edit();
                            edit.putString("username", userUtente.getText().toString());
                            edit.putString("password",pswUtente.getText().toString());
                            edit.commit();

                            clear();
                            startActivity(new Intent(Registrazione.this,Login.class));

                            Toast.makeText(Registrazione.this, "Registazione avvenuta con successo.", Toast.LENGTH_SHORT).show();
                        } catch (IOException ioe){
                            ioe.printStackTrace();
                            Toast.makeText(Registrazione.this, ioe.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (ClassNotFoundException cne){
                            cne.printStackTrace();
                            Toast.makeText(Registrazione.this, cne.getMessage(), Toast.LENGTH_LONG).show();
                        } catch(Exception e) {
                            userUtente.setError(e.getMessage());
                            Toast.makeText(Registrazione.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        confPswUtente.setError("Le password non coincidono!");
                        Toast.makeText(Registrazione.this, "Le password non coincidono!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(Registrazione.this, "Compila tutti i campi obigatori!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    /**
     * Pulizia editText
     */
    private void clear(){
        nomeUtente.setText("");
        cognomeUtente.setText("");
        cfUtente.setText("");
        dataNascitaUtente.setText("");
        comuneNascitaUtente.setText("");
        indirizzoUtente.setText("");
        numTelUetente.setText("");
        capUtente.setText("");
        emailUtente.setText("");
        userUtente.setText("");
        pswUtente.setText("");
        confPswUtente.setText("");
        btConf.setText("");
    }


    private void insertData(){
        dataNascitaUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int anno = cal.get(Calendar.YEAR);
                int mese = cal.get(Calendar.MONTH);
                int giorno = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Registrazione.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,giorno,mese,anno);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anno, int mese, int giorno) {
                dataNascitaUtente.setText(giorno+"/"+ (mese+1) + "/"+anno);
            }
        };
    }
}
