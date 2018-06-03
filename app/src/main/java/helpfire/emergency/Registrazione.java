package helpfire.emergency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

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
                if(!nomeUtente.getText().equals("") && !cognomeUtente.getText().equals("") && !cfUtente.getText().equals("") && !numTelUetente.getText().equals("")
                        && !userUtente.getText().equals("") && !pswUtente.getText().equals("") && !confPswUtente.getText().equals("")){
                    Log.d("ARRAY","entro2");
                    //controllo che le password coincidano
                    if(pswUtente.getText().toString().equalsIgnoreCase(confPswUtente.getText().toString())){
                        Log.d("ARRAY","entro3");
                        //se l'utente non è già esistente salvo il nuovo utente altrimenti mostro il messaggio di errore
                        try {
                            Log.d("ARRAY","entro4");
                            Utente ut = new Utente(nomeUtente.getText().toString(),cognomeUtente.getText().toString(),cfUtente.getText().toString(),
                                    dataNascitaUtente.getText().toString(),comuneNascitaUtente.getText().toString(),indirizzoUtente.getText().toString(),
                                    capUtente.getText().toString(),numTelUetente.getText().toString(),emailUtente.getText().toString(), userUtente.getText().toString(),
                                    pswUtente.getText().toString());
                            Log.d("ARRAY", "utente - "+ut.toString());
                            Controller.addUtente(ut);
                            Log.d("ARRAY","utenti--" + Controller.listaUtenti.toString());
                        } catch (Exception e) {
                            Toast.makeText(Registrazione.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        confPswUtente.setError("Le password non coincidono!");
                    }
                }else{
                    Toast.makeText(Registrazione.this, "Compila tutti i campi obigatori!", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(Registrazione.this,Login.class));
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salva_registrazione, menu);
        Log.d("ARRAY","entro - "+nomeUtente.getText().equals(""));
        Log.d("ARRAY","nome  - "+nomeUtente.getText());
        Log.d("ARRAY","nome  - "+nomeUtente.getText().toString());
        //controllo che i campi obbligatori siano stati compilati
        if(!nomeUtente.getText().equals("") && !cognomeUtente.getText().equals("") && !cfUtente.getText().equals("") && !numTelUetente.getText().equals("")
                && !userUtente.getText().equals("") && !pswUtente.getText().equals("") && !confPswUtente.getText().equals("")){
            Log.d("ARRAY","entro2");
            //controllo che le password coincidano
            if(pswUtente.getText().toString().equalsIgnoreCase(confPswUtente.getText().toString())){
                Log.d("ARRAY","entro3");
                //se l'utente non è già esistente salvo il nuovo utente altrimenti mostro il messaggio di errore
                try {
                    Log.d("ARRAY","entro4");
                    Utente ut = new Utente(nomeUtente.getText().toString(),cognomeUtente.getText().toString(),cfUtente.getText().toString(),
                            dataNascitaUtente.getText().toString(),comuneNascitaUtente.getText().toString(),indirizzoUtente.getText().toString(),
                            capUtente.getText().toString(),numTelUetente.getText().toString(),emailUtente.getText().toString(), userUtente.getText().toString(),
                            pswUtente.getText().toString());
                    Log.d("ARRAY", "utente - "+ut.toString());
                    Controller.addUtente(ut);
                    Log.d("ARRAY","utenti--" + Controller.listaUtenti.toString());
                } catch (Exception e) {
                    Toast.makeText(Registrazione.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else{
                confPswUtente.setError("Le password non coincidono!");
            }
        }else{
            Toast.makeText(Registrazione.this, "Compila tutti i campi obigatori!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }*/
}
