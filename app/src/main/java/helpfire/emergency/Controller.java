package helpfire.emergency;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Roberta on 03/06/2018.
 */
public class  Controller {
    public static ArrayList<Utente> listaUtenti;
    public ArrayList<Emergenza> listaEmergenze;

    public Controller() {
        listaUtenti = new ArrayList<>();
        listaUtenti.add(new Utente("Luigi","Biaggi","LGBGG12H3ER44735","12/03/1990","Salerno","via Roma,46,SA","84121","33456547890","lBiaggi@gmail.com","luigiviaggi","123"));
        listaUtenti.add(new Utente("Lucai","Orsii","LCORS12H3ER44735","17/05/1989","Salerno","via Napoli,96,SA","84121","33456549790","lOrsi@gmail.com","lucaorsi","124"));
        listaUtenti.add(new Utente("Lia","Rossi","LARSS12H3ER44735","02/04/1994","Salerno","via Buonarroti,40,SA","84121","33456547660","lRossi@gmail.com","liarossi","125"));
        //listaEmergenze.add(new Emergenza("Incendio","","Luigi","Biaggi","33456547890"));
        //listaEmergenze.add(new Emergenza("Incendio","","Luigi","Biaggi","33456547890"));
    }


    public static void addUtente(Utente u) throws Exception{
        if(listaUtenti.contains(u)){
            throw new Exception("Utente già esistente, cambiare username.");
        }else{
            Log.d("ARRAY", "aggiungo");
            listaUtenti.add(u);
        }

    }

    public void addEmergenza(Emergenza e){
        listaEmergenze.add(e);
    }


}
