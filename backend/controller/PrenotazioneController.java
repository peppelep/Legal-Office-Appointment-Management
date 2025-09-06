package piattaforme.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import piattaforme.demo.models.Appuntamento;
import piattaforme.demo.models.Prenotazione;
import piattaforme.demo.models.StatoPrenotazione;
import piattaforme.demo.services.PrenotazioneService;
import piattaforme.demo.exceptions.NoAppuntamentoExistInRepository;
import piattaforme.demo.exceptions.NoPrenotazioneInRepository;
import piattaforme.demo.services.AppuntamentoService;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")

public class PrenotazioneController {
    @Autowired
    private PrenotazioneService prenotazioneService;
    @Autowired
    private AppuntamentoService appuntamentoService;
    
    //Restituisce tutte le prenotazioni
    @GetMapping("/all")
    public ResponseEntity<List<Prenotazione>> getAllPrenotazioni() {
        try {
            return new ResponseEntity<List<Prenotazione>>(prenotazioneService.getAll(), HttpStatus.OK);

        } catch (NoPrenotazioneInRepository e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Restituisce la prenotazione con quell'id
    @GetMapping("/getid/{id}")
    public ResponseEntity<Prenotazione> getPrenotazioneById(@PathVariable Long id) {
        try {
            Prenotazione prenotazione = prenotazioneService.getById(id);
            return ResponseEntity.ok(prenotazione);
        } catch (NoPrenotazioneInRepository e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Restituisce tutte le prenotazioni effettuate da un utente , quelle vecchie le filtro dal frontend
    @GetMapping("/get/{username}")
    public ResponseEntity<List<Prenotazione>> getPrenotazioneByUsername(@PathVariable String username) {
        try {
            return new ResponseEntity<List<Prenotazione>>(prenotazioneService.getByUsername(username), HttpStatus.OK);
        } catch (NoPrenotazioneInRepository e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    //Aggiunge una prenotazione ad un utente e notifica tramite mail
    @PostMapping("/add")
    public ResponseEntity<Prenotazione> addPrenotazione(@RequestBody Prenotazione prenotazione) throws NoAppuntamentoExistInRepository {
    try {             
        Appuntamento app = appuntamentoService.getById(prenotazione.getAppuntamento());

        // Controllare se ci sono posti disponibili
        if (app.getPostiDisponibili() > 0) {
            // Diminuire il numero di posti disponibili
            app.setPostiDisponibili(app.getPostiDisponibili() - 1);

            // Salvare l'appuntamento aggiornato nel database
            appuntamentoService.save(app);

            // Aggiungere la prenotazione
            Prenotazione newPrenotazione = prenotazioneService.add1(prenotazione);  

            prenotazioneService.notifyPrenotazione(prenotazione); //notifico l'utente che la sua prenotazione è andata a buon fine

            return new ResponseEntity<>(newPrenotazione, HttpStatus.OK);
        } 
        else { //posti tutti occupati , allora lo accodo
            prenotazione.setStato(StatoPrenotazione.IN_ATTESA);
            prenotazione.setPostoInCoda(app.getUtentiInAttesa() + 1 );  //setto la sua posizione in coda
            Prenotazione newPrenotazione = prenotazioneService.add1(prenotazione);
            prenotazioneService.notifyAccodamento(prenotazione);  //lo informo che è stato messo in attesa

            int numeroInCoda = app.getUtentiInAttesa() + 1 ; //aumento gli utenti in attesa per quell'appuntamento
            app.setUtentiInAttesa(numeroInCoda);

            appuntamentoService.save(app);

            return new ResponseEntity<>(newPrenotazione, HttpStatus.OK);
        }
    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
    }
    
    //Rimuove una prenotazione effettuata da un utente e notifica tramite mail
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removePrenotazione(@PathVariable Long id) throws NoAppuntamentoExistInRepository {
        try {
            Prenotazione prenotazione = prenotazioneService.getById(id);
            Appuntamento app = appuntamentoService.getById(prenotazione.getAppuntamento());

            //prendo tutti gli utenti che erano in attesa per quell'appuntamento
            List<Prenotazione> prenotazioni = prenotazioneService.getByAppuntamentoAndStato(prenotazione.getAppuntamento(),StatoPrenotazione.IN_ATTESA);

            // Aumento il numero di posti disponibili solo se la prenotazione era gia confermata
            if(prenotazione.getStato() == StatoPrenotazione.CONFERMATA){
                app.setPostiDisponibili(app.getPostiDisponibili() +1);
            }

            appuntamentoService.save(app);
            prenotazioneService.remove(prenotazione);

            prenotazioneService.notifyRimozionePrenotazione(prenotazione); //notifica l utente che la sua prenotazione è stata rimossa correttamente

            if(prenotazione.getStato() == StatoPrenotazione.CONFERMATA){
                for(Prenotazione p : prenotazioni){  //itera su quelli che stavano aspettando
                    String utente = p.getUsernameCliente();
                    prenotazioneService.notifyLiberazionePosto(p);  //lo aggiorna che la sua prenotazione è avanzata di una posizione

                    int postoNuovo = p.getPostoInCoda() - 1 ;
                    p.setPostoInCoda(postoNuovo);  //aggiorna il suo posto in coda

                    prenotazioneService.save(p) ;

                }
                prenotazioneService.notifyWaitList(prenotazione.getAppuntamento());  //conferma la prenotazione del primo utente in coda
            }

            else{  //essendo che la prenotazione rimossa non era stata confermata , non si creano nuovi posti Disponibili , ma la posizione di quelli in coda successivi scala
                int postoDelRimosso = prenotazione.getPostoInCoda();
                for(Prenotazione p : prenotazioni){
                    if(p.getId() != id){
                        if(p.getPostoInCoda() >= postoDelRimosso){ //scala di una posizione tutti quelli in coda dopo di lui
                            int postoNuovo = p.getPostoInCoda() - 1 ;
                            p.setPostoInCoda(postoNuovo);
                            prenotazioneService.save(p) ;
                        }
                    }
                }
            }

            if(app.getUtentiInAttesa() > 0 ){
                int numeroInCoda = app.getUtentiInAttesa() - 1 ;
                app.setUtentiInAttesa(numeroInCoda);

                // Salvare l'appuntamento aggiornato nel database
                appuntamentoService.save(app);
            }

            // Notifica agli utenti in attesa tramite SnS
            //if(prenotazione.getStato() == StatoPrenotazione.CONFERMATA){
            //    prenotazioneService.notifyWaitList(prenotazione.getAppuntamento());
            //}
            return ResponseEntity.noContent().build();
        } catch (NoPrenotazioneInRepository e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Restituisce le prenotazioni associate ad un Appuntamento 
    @GetMapping("/byAppuntamento/{id}")
    public ResponseEntity<List<Prenotazione>> getPrenotazioniByAppuntamento(@PathVariable Long id) {
        //QUESTO METODO è STATO AGGIUNTO PER POTER ELIMINARE TUTTE LE PRENOTAZIONI RELATIVE ALL' APPUNTAMENTO RIMOSSO
        try {
            List<Prenotazione> prenotazioni = prenotazioneService.getByAppuntamento(id);
            return ResponseEntity.ok(prenotazioni);
        } catch (NoPrenotazioneInRepository e) {
            return ResponseEntity.notFound().build();
        }
    }
}
