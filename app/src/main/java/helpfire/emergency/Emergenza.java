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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Emergenza)) return false;

        Emergenza emergenza = (Emergenza) o;

        if (tipo != null ? !tipo.equals(emergenza.tipo) : emergenza.tipo != null) return false;
        if (descrizione != null ? !descrizione.equals(emergenza.descrizione) : emergenza.descrizione != null)
            return false;
        if (nomeUtente != null ? !nomeUtente.equals(emergenza.nomeUtente) : emergenza.nomeUtente != null)
            return false;
        if (cognomeUtente != null ? !cognomeUtente.equals(emergenza.cognomeUtente) : emergenza.cognomeUtente != null)
            return false;
        if (numTelUtente != null ? !numTelUtente.equals(emergenza.numTelUtente) : emergenza.numTelUtente != null)
            return false;
        if (posizione != null ? !posizione.equals(emergenza.posizione) : emergenza.posizione != null)
            return false;
        return media != null ? media.equals(emergenza.media) : emergenza.media == null;

    }

    @Override
    public int hashCode() {
        int result = tipo != null ? tipo.hashCode() : 0;
        result = 31 * result + (descrizione != null ? descrizione.hashCode() : 0);
        result = 31 * result + (nomeUtente != null ? nomeUtente.hashCode() : 0);
        result = 31 * result + (cognomeUtente != null ? cognomeUtente.hashCode() : 0);
        result = 31 * result + (numTelUtente != null ? numTelUtente.hashCode() : 0);
        result = 31 * result + (posizione != null ? posizione.hashCode() : 0);
        result = 31 * result + (media != null ? media.hashCode() : 0);
        return result;
    }
}
