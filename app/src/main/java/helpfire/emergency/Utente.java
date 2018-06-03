package helpfire.emergency;

/**
 * Created by Roberta on 02/06/2018.
 */
public class Utente {
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
}
