package piattaforme.demo.models;

import  jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appuntamento")
public class Appuntamento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String tipo;
    private LocalTime ora;
    private LocalDate  data ;
    private String dettagli;
    private String nomeAvvocato;
    private int postiDisponibili;
    private int utentiInAttesa ; // serve a vedere quanti utenti ci sono in coda

    // Costruttore vuoto (richiesto da JPA)
    public Appuntamento() {
    }

    // Getter e Setter per id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter e Setter per tipo
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Getter e Setter per dataInizio
    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    // Getter e Setter per dataFine
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    // Getter e Setter per dettagli
    public String getDettagli() {
        return dettagli;
    }

    public void setDettagli(String dettagli) {
        this.dettagli = dettagli;
    }

    // Getter e Setter per luogoStudio
    public String getNomeAvvocato() {
        return nomeAvvocato;
    }

    public void setNomeAvvocato(String nomeAvvocato) {
        this.nomeAvvocato = nomeAvvocato;
    }

    // Getter e Setter per postiDisponibili
    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public int getUtentiInAttesa() {
        return this.utentiInAttesa;
    }

    public void setUtentiInAttesa(int utentiInAttesa) {
        this.utentiInAttesa = utentiInAttesa;
    }


}

    