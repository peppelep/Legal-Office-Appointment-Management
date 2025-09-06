package piattaforme.demo.models;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
@Table(name = "prenotazione")
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@OneToOne(targetEntity = Appuntamento.class)
    private Long appuntamento;

    private String usernameCliente;

    private String documento; //non serve perche non carico solo un documento alla volta , e ho optato per una gestione a cartelle dei file nel bucket
    private String tipoAppuntamento;

    private LocalTime ora;

    private LocalDate  data ;

    private String nomeAvvocato;

    private int postoInCoda ; //serve per vedere quanti utenti ho davanti in coda


    @Enumerated(EnumType.STRING)
    private StatoPrenotazione stato;

    // Costruttore vuoto (richiesto da JPA)
    public Prenotazione() {
    }

    // Getter e Setter per id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter e Setter per appuntamento
    public Long getAppuntamento() {
        return appuntamento;
    }

    public void setAppuntamento(Long appuntamento) {
        this.appuntamento = appuntamento;
    }

    // Getter e Setter per usernameCliente
    public String getUsernameCliente() {
        return usernameCliente;
    }

    public void setUsernameCliente(String usernameCliente) {
        this.usernameCliente = usernameCliente;
    }

    public String getDocumento(){
        return documento;
    }

    public void setDocumento(String documento){
        this.documento = documento ;
    }

    public StatoPrenotazione getStato() {
        return stato;
    }

    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    public String getTipoAppuntamento() {
        return tipoAppuntamento;
    }

    public void setTipoAppuntamento(String tipoAppuntamento) {
        this.tipoAppuntamento = tipoAppuntamento;
    }

    // Getter e Setter per ora
    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    // Getter e Setter per data
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    // Getter e Setter per nomeAvvocato
    public String getNomeAvvocato() {
        return nomeAvvocato;
    }

    public void setNomeAvvocato(String nomeAvvocato) {
        this.nomeAvvocato = nomeAvvocato;
    }

    public int getPostoInCoda(){
        return this.postoInCoda;
    }

    public void setPostoInCoda(int posto){
        this.postoInCoda = posto ;
    }
}
