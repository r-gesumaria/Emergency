package helpfire.emergency;

import android.content.Context;
import android.util.Log;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Roberta on 03/06/2018.
 */
public class  Controller {
    private static ObjectOutputStream oos;
    private static File lista;
    private static FileInputStream fis;
    private static int count =0;

    public static boolean creaFile(Context c) throws IOException {
        lista = new File(c.getFilesDir(),"listaUtenti.txt");
        if (!lista.exists()) {
            FileOutputStream fos = new FileOutputStream(lista);
            oos = new ObjectOutputStream(fos);
            //Controller c = new Controller();
            oos.writeObject(new Utente("Luigi", "Biaggi", "LGBGG12H3ER44735", "12/03/1990", "Salerno", "via Roma,46,SA", "84121", "33456547890", "lBiaggi@gmail.com", "luigiviaggi", "123"));
            oos.writeObject(new Utente("Lucai", "Orsii", "LCORS12H3ER44735", "17/05/1989", "Salerno", "via Napoli,96,SA", "84121", "33456549790", "lOrsi@gmail.com", "lucaorsi", "124"));
            oos.writeObject(new Utente("Lia", "Rossi", "LARSS12H3ER44735", "02/04/1994", "Salerno", "via Buonarroti,40,SA", "84121", "33456547660", "lRossi@gmail.com", "liarossi", "125"));
            oos.writeObject(new Utente("li", "lo", "1234567890987654", "", "", "", "", "3341231230", "", "lilo", "1"));
            oos.close();
            count = 4;
            return true;
        }else
            return false;
    }

    public static boolean inserisciUtente(Utente u) throws Exception {
        ArrayList<Utente> ut = new ArrayList<>();
        if (lista.exists()) {
            ut = letturaFile();
            if(!ut.contains(u)) {
                oos.writeObject(u);
                oos.close();
                count++;
                return true;
            }else
                throw new Exception("Utente già esistente.");

        }else
            return false;
    }

    public static ArrayList<Utente> letturaFile() throws IOException, ClassNotFoundException {
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        if (lista.exists()) {
            fis = new FileInputStream(lista);
            ObjectInputStream reader = new ObjectInputStream(fis);

            for(int i = 0; i<count;i++){
                Object o = reader.readObject();
                Log.d("MIO","ogg --- "+(Utente) o);
                listaUtenti.add((Utente) o);
            }
            Log.d("MIO","lista--- "+listaUtenti);
            return  listaUtenti;
        }else
            return null;
    }
}


