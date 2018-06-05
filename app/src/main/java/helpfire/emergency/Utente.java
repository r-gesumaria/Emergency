package helpfire.emergency;

import java.io.Serializable;

/**
 * Created by Roberta on 02/06/2018.
 */
public class Utente implements Serializable{
    private String nome, cognome, cf, dataNascita, comuneNascita,indirizzo, cap, nunTel, email, username, password;

    public Utente(String nome, String cognome, String cf, String dataNascita, String comuneNascita, String indirizzo, String cap, String nunTel, String email, String username, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.dataNascita = dataNascita;
        this.comuneNascita = comuneNascita;
        this.indirizzo = indirizzo;
        this.cap = cap;
        this.nunTel = nunTel;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCf() {
        return cf;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public String getComuneNascita() {
        return comuneNascita;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getCap() {
        return cap;
    }

    public String getNunTel() {
        return nunTel;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", cf='" + cf + '\'' +
                ", dataNascita='" + dataNascita + '\'' +
                ", comuneNascita='" + comuneNascita + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", cap='" + cap + '\'' +
                ", nunTel='" + nunTel + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utente)) return false;

        Utente utente = (Utente) o;

        if (nome != null ? !nome.equals(utente.nome) : utente.nome != null) return false;
        if (cognome != null ? !cognome.equals(utente.cognome) : utente.cognome != null)
            return false;
        if (cf != null ? !cf.equals(utente.cf) : utente.cf != null) return false;
        if (dataNascita != null ? !dataNascita.equals(utente.dataNascita) : utente.dataNascita != null)
            return false;
        if (comuneNascita != null ? !comuneNascita.equals(utente.comuneNascita) : utente.comuneNascita != null)
            return false;
        if (indirizzo != null ? !indirizzo.equals(utente.indirizzo) : utente.indirizzo != null)
            return false;
        if (cap != null ? !cap.equals(utente.cap) : utente.cap != null) return false;
        if (nunTel != null ? !nunTel.equals(utente.nunTel) : utente.nunTel != null) return false;
        if (email != null ? !email.equals(utente.email) : utente.email != null) return false;
        if (username != null ? !username.equals(utente.username) : utente.username != null)
            return false;
        return password != null ? password.equals(utente.password) : utente.password == null;

    }

    @Override
    public int hashCode() {
        int result = nome != null ? nome.hashCode() : 0;
        result = 31 * result + (cognome != null ? cognome.hashCode() : 0);
        result = 31 * result + (cf != null ? cf.hashCode() : 0);
        result = 31 * result + (dataNascita != null ? dataNascita.hashCode() : 0);
        result = 31 * result + (comuneNascita != null ? comuneNascita.hashCode() : 0);
        result = 31 * result + (indirizzo != null ? indirizzo.hashCode() : 0);
        result = 31 * result + (cap != null ? cap.hashCode() : 0);
        result = 31 * result + (nunTel != null ? nunTel.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
