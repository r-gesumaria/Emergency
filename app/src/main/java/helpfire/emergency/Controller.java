package helpfire.emergency;

import android.content.Context;
import android.util.Log;

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

    public static boolean creaFile(Context c) throws IOException {
        lista = new File(c.getFilesDir(),"listaUtenti.txt");
        Log.d("FILE","CREAZIONE -- "+lista.exists());
        if (!lista.exists()) {
            Log.d("FILE", "creazione - il file non esiste");

            lista.createNewFile();

            FileOutputStream fos = new FileOutputStream(lista);
            oos = new ObjectOutputStream(fos);

            Utenza utenza = new Utenza();
            utenza.riempi();
            oos.writeObject(utenza);
            oos.close();
            return true;
        }else
            return false;
    }

    public static boolean inserisciUtente(Utente u) throws Exception {
        ArrayList<Utente> ut = new ArrayList<>();
        if (lista.exists()) {
            Log.d("FILE","inserimento il file esiste");
            ut = letturaFile();
            if(!ut.contains(u)) {
                Log.d("FILE","inserimento - inserisco");
                ut.add(u);
                Utenza utenza = new Utenza(ut);
                FileOutputStream fos = new FileOutputStream(lista);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(utenza);
                oos.close();
                return true;
            }else
                throw new Exception("Utente già esistente.");

        }else{
            Log.d("FILE","inserimento il file non esiste");
            lista.createNewFile();
            ut.add(u);
            FileOutputStream fos = new FileOutputStream(lista);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ut);
            oos.close();
            return false;
        }
    }

    public static ArrayList<Utente> letturaFile() throws IOException, ClassNotFoundException {
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        if (lista.exists()) {

            fis = new FileInputStream(lista);
            ObjectInputStream reader = new ObjectInputStream(fis);
            Utenza ut = (Utenza) reader.readObject();
            listaUtenti = ut.getListaUtenti();
            //listaUtenti = (ArrayList) reader.readObject();
            return  listaUtenti;
        }else
            return null;
    }

    public static boolean rimuoviUtente(Utente u) throws IOException, ClassNotFoundException {
        ArrayList<Utente> ut = new ArrayList<>();
        if(lista.exists()) {
            ut = letturaFile();
            if(ut.contains(u)) {
                ut.remove(u);
                return true;
            }
        }
        return false;
    }
}


