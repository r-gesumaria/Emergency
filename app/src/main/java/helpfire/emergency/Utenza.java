package helpfire.emergency;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Roberta on 07/06/2018.
 */
public class Utenza implements Serializable{
    private ArrayList<Utente> listaUtenti;

    public Utenza() {
        this.listaUtenti = new ArrayList<>();
    }

    //per la simulazione
    public void riempi(){
        listaUtenti.add(new Utente("Luigi", "Biaggi", "LGBGG12H3ER44735", "12/03/1990", "Salerno", "via Roma,46,SA", "84121", "33456547890", "lBiaggi@gmail.com", "luigiviaggi", "123"));
        listaUtenti.add(new Utente("Lucai", "Orsii", "LCORS12H3ER44735", "17/05/1989", "Salerno", "via Napoli,96,SA", "84121", "33456549790", "lOrsi@gmail.com", "lucaorsi", "124"));
        listaUtenti.add(new Utente("Lia", "Rossi", "LARSS12H3ER44735", "02/04/1994", "Salerno", "via Buonarroti,40,SA", "84121", "33456547660", "lRossi@gmail.com", "liarossi", "125"));
        listaUtenti.add(new Utente("li", "lo", "1234567890987654", "", "", "", "", "3341231230", "", "lilo", "1"));
    }

    public boolean addUtente(Utente u){
        if(u != null){
            if(!listaUtenti.contains(u)){
                listaUtenti.add(u);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Utente> getListaUtenti() {
        return listaUtenti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utenza)) return false;

        Utenza utenza = (Utenza) o;

        return listaUtenti != null ? listaUtenti.equals(utenza.listaUtenti) : utenza.listaUtenti == null;

    }

    @Override
    public int hashCode() {
        return listaUtenti != null ? listaUtenti.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Utenza{" +
                "listaUtenti=" + listaUtenti +
                '}';
    }
}
