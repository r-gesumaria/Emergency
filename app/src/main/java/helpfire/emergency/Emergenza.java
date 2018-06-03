package helpfire.emergency;

import android.media.Image;

/**
 * Created by Roberta on 02/06/2018.
 */
public class Emergenza {
    private String tipo, descrizione, nomeUtente, cognomeUtente, numTelUtente, posizione;
    private Image media;

    public Emergenza(String tipo, String descrizione, String nomeUtente, String cognomeUtente, String numTelUtente/*, String posizione, Image media*/) {
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.nomeUtente = nomeUtente;
        this.cognomeUtente = cognomeUtente;
        this.numTelUtente = numTelUtente;
        //this.posizione = posizione;
        //this.media = media;
    }


}
