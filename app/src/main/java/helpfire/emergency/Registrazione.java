package helpfire.emergency;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class Registrazione extends AppCompatActivity {
    private AutoCompleteTextView nomeUtente,cognomeUtente,cfUtente, dataNascitaUtente, comuneNascitaUtente, indirizzoUtente,capUtente,
        numTelUetente,emailUtente,userUtente, pswUtente, confPswUtente;
    private Button btConf;
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

        btConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //controllo che i campi obbligatori siano stati compilati
                if(!nomeUtente.getText().toString().equals("") && !cognomeUtente.getText().toString().equals("") && !cfUtente.getText().toString().equals("") && !numTelUetente.getText().toString().equals("")
                        && !userUtente.getText().toString().equals("") && !pswUtente.getText().toString().equals("") && !confPswUtente.getText().toString().equals("")){

                    //controllo che le password coincidano
                    if(pswUtente.getText().toString().equalsIgnoreCase(confPswUtente.getText().toString())){

                        //se l'utente non è già esistente salvo il nuovo utente altrimenti mostro il messaggio di errore
                        try {
                            Utente ut = new Utente(nomeUtente.getText().toString(),cognomeUtente.getText().toString(),cfUtente.getText().toString(),
                                    dataNascitaUtente.getText().toString(),comuneNascitaUtente.getText().toString(),indirizzoUtente.getText().toString(),
                                    capUtente.getText().toString(),numTelUetente.getText().toString(),emailUtente.getText().toString(), userUtente.getText().toString(),
                                    pswUtente.getText().toString());
                            Log.d("MIO","utente -- "+ut);

                            Controller.inserisciUtente(ut);
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
                    /*nomeUtente.setHintTextColor(Color.RED);
                    cognomeUtente.setHintTextColor(Color.RED);
                    cfUtente.setHintTextColor(Color.RED);
                    numTelUetente.setHintTextColor(Color.RED);
                    userUtente.setHintTextColor(Color.RED);
                    pswUtente.setHintTextColor(Color.RED);
                    confPswUtente.setTextColor(Color.RED);*/
                    Toast.makeText(Registrazione.this, "Compila tutti i campi obigatori!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

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
}
