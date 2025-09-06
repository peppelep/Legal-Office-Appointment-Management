package piattaforme.demo.services;

import piattaforme.demo.exceptions.NoPrenotazioneInRepository;
import piattaforme.demo.models.Appuntamento;
import piattaforme.demo.models.Prenotazione;
import piattaforme.demo.models.StatoPrenotazione;
import java.util.List;

public interface PrenotazioneService {
    List<Prenotazione> getAll() throws NoPrenotazioneInRepository ;

    Prenotazione getById(Long id) throws NoPrenotazioneInRepository;

    Prenotazione add1(Prenotazione prenotazione);

    void remove(Prenotazione prenotazione);

    List<Prenotazione> getByUsername(String username) throws NoPrenotazioneInRepository ;

    List<Prenotazione> getByAppuntamento(Long id) throws NoPrenotazioneInRepository ;

    List<Prenotazione> getByAppuntamentoAndStato(Long appuntamento, StatoPrenotazione stato) ;

    void registrazioneEmail(String email) ;

    void notifyWaitList(Long appuntamento) ;

    void notifyPrenotazione(Prenotazione prenotazione) ;

    void notifyLiberazionePosto(Prenotazione prenotazione) ;

    void notifyAccodamento(Prenotazione prenotazione);

    void notifyRimozionePrenotazione(Prenotazione prenotazione) ;

    void notifyEliminazioneAppuntamento(Prenotazione prenotazione);

    Prenotazione save(Prenotazione prenotazione);
}
