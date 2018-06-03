package helpfire.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Roberta on 01/06/2018.
 */
public class Login extends AppCompatActivity {

    private TextView btRegistrati;
    private Button btAccedi;
    private AutoCompleteTextView userLogin, pswLogin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        new Controller();
        btRegistrati = (TextView) findViewById(R.id.registrazioneUtente);
        btAccedi = (Button) findViewById(R.id.btAccedi);
        userLogin = (AutoCompleteTextView) findViewById(R.id.userLogin);
        pswLogin = (AutoCompleteTextView) findViewById(R.id.pswLogin);



        btRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this, "reg", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this,Registrazione.class));

            }
        });

        btAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user,psw;
                user = userLogin.getText().toString();
                psw = pswLogin.getText().toString();
                boolean contr = false;
                Log.d("ARRAY","size - "+Controller.listaUtenti.size());
                for(int i = 0;i < Controller.listaUtenti.size(); i++){
                    if(Controller.listaUtenti.get(i).getUsername().equalsIgnoreCase(user) && Controller.listaUtenti.get(i).getPassword().equalsIgnoreCase(psw)){
                        contr = true;
                        startActivity(new Intent(Login.this,Segnalazione.class));
                    }
                }
                if(!contr) {
                    Toast.makeText(Login.this, "Credenziali sbagliate!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
