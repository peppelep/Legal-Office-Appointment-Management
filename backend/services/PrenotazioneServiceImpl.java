package piattaforme.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import piattaforme.demo.exceptions.NoPrenotazioneInRepository;
import piattaforme.demo.models.Appuntamento;
import piattaforme.demo.models.Prenotazione;
import piattaforme.demo.models.StatoPrenotazione;
import piattaforme.demo.repository.AppuntamentoRepository;
import piattaforme.demo.repository.PrenotazioneRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneServiceImpl implements PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private AppuntamentoRepository appuntamentoRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private EmailService notificationService;

    @Override
    public Prenotazione save(Prenotazione prenotazione) {
        return prenotazioneRepository.save(prenotazione);
    }

    //ritorna tutte le prenotazioni in ordine di id
    @Transactional(readOnly = true)
    @Override
    public List<Prenotazione> getAll() throws NoPrenotazioneInRepository {
        List<Prenotazione> allPrenotazioni = prenotazioneRepository.findAllByOrderByIdAsc();

        if (allPrenotazioni.isEmpty()) {
            throw new NoPrenotazioneInRepository();
        } else {
            return allPrenotazioni;
        }
    }

    //ritorna la prenotazione con quell'id
    @Transactional(readOnly = true)
    @Override
    public Prenotazione getById(Long id) throws NoPrenotazioneInRepository {
        Optional<Prenotazione> prenotazione = prenotazioneRepository.findById(id);
        if (prenotazione.isEmpty()) {
            throw new NoPrenotazioneInRepository();
        } else {
            return prenotazione.get();
        }
    }

    //aggiunge una prenotazione
    public Prenotazione add1(Prenotazione prenotazione) {
        return prenotazioneRepository.save(prenotazione);
    }

    // Metodo per ottenere il contenuto del file basato sul nome del file
    //private byte[] getFileContent(String fileName) throws IOException {
    //    return Files.readAllBytes(Paths.get(fileName));
    //}


    //rimuove una prenotazione
    @Transactional
    @Override
    public void remove(Prenotazione prenotazione) {
        prenotazioneRepository.delete(prenotazione);
    }

    //restituisce tutte le prenotazioni di un utente
    @Transactional(readOnly = true)
    @Override
    public List<Prenotazione> getByUsername(String username) {
        return prenotazioneRepository.findByUsernameCliente(username);
    }

    //restituisce tutte le prenotazioni per un determinato appuntamento
    @Transactional(readOnly = true)
    @Override
    public List<Prenotazione> getByAppuntamento(Long id) {
        return prenotazioneRepository.findByAppuntamento(id);
    }

    //restituisce tutte le prenotazioni per un determinato appuntamento , ma con l aggiunta dello stato
    public List<Prenotazione> getByAppuntamentoAndStato(Long appuntamento, StatoPrenotazione stato) {
        //questo metodo viene usato per la gestione della coda
        return prenotazioneRepository.findByAppuntamentoAndStato(appuntamento, stato);
    }

    //implementato inizialmente ma non usato
    public void registrazioneEmail(String email){
        notificationService.subscribeAndConfirm(email);
    }


    //Gestrione della coda :
    //  - prende la prima prenotazione tra quelle in attesa per quell'appuntamento
    //  - modifica lo stato da "IN_ATTESA" --> "CONFERMATA" e informa l'utente via mail
    //  - riduce quindi i posti disponibili di 1 , in quanto dopo la rimozione erano stati incrementati di 1
    public void notifyWaitList(Long appuntamentoId) {
        List<Prenotazione> waitList = prenotazioneRepository.findByAppuntamentoAndStato(appuntamentoId, StatoPrenotazione.IN_ATTESA);
        if (!waitList.isEmpty()) {
            Prenotazione nextInLine = waitList.get(0);
            nextInLine.setStato(StatoPrenotazione.CONFERMATA);
            notifyFineAttesa(nextInLine);
            Optional<Appuntamento> app = appuntamentoRepository.findById(appuntamentoId);
            Appuntamento appuntamento = app.get();
            appuntamento.setPostiDisponibili(appuntamento.getPostiDisponibili()-1);
            prenotazioneRepository.save(nextInLine);
            appuntamentoRepository.save(appuntamento);

            // Notifica l'utente ---> l'ho spostato nel controller
            //notificationService.sendNotification(
            //    "Un posto si è liberato per il tuo appuntamento.",
            //    "Posto Disponibile",
            //    nextInLine.getUsernameCliente() // Questo dovrebbe essere il numero di telefono o l'email dell'utente
            //);
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------
    //TUTTI I METODI SEGUENTI SONO UGUALI MA VARIANO PER IL MESSAGGIO SCRITTO SULLA MAIL CHE VERRA POI INVIATA TRAMITE SeS  :
    //-------------------------------------------------------------------------------------------------------------------------

    public void notifyPrenotazione(Prenotazione prenotazione) { //CONFERMA L'AGGIUNTA DELLA PRENOTAZIONE
        String email = prenotazione.getUsernameCliente();

        // Notifica l'utente
        notificationService.sendNotification2(
                "Prenotazione effettuata con successo",
                "Prenotazione",
                prenotazione.getUsernameCliente()
        );
    }

    public void notifyLiberazionePosto(Prenotazione prenotazione) {  //INFORMA AGLI UTENTI IN ATTESA CHE UN UTENTE PRENOTAZIONE HA RIMOSSO LA PRENOTAZIONE
        String email = prenotazione.getUsernameCliente();

        // Notifica l'utente
        notificationService.sendNotification2(
                "Un posto si è liberato per il tuo appuntamento.",
                "Prenotazione In Attesa",
                prenotazione.getUsernameCliente()
        );
    }

    public void notifyRimozionePrenotazione(Prenotazione prenotazione) {  //CONFERMA LA RIMOZIONE DELLA PRENOTAZIONE
        String email = prenotazione.getUsernameCliente();

        // Notifica l'utente
        notificationService.sendNotification2(
                "Prenotazione rimossa con successo",
                "Prenotazione Rimossa",
                prenotazione.getUsernameCliente()
        );
    }

    public void notifyFineAttesa(Prenotazione prenotazione) { //PRENOTAZIONE CHE PASSA DALLO STATO DI "ATTESA" AD ESSERE "CONFERMATA"
        String email = prenotazione.getUsernameCliente();

        // Notifica l'utente
        notificationService.sendNotification2(
                "Attesa finita , la tua prenotazione è andata a buon fine",
                "Prenotazione",
                prenotazione.getUsernameCliente()
        );
    }

    public void notifyAccodamento(Prenotazione prenotazione) {  //PRENOTAZIONE MESSA IN ATTESA
        String email = prenotazione.getUsernameCliente();

        // Notifica l'utente
        notificationService.sendNotification2(
                " Abbiamo inserito la Sua richiesta in lista d'attesa e La contatteremo immediatamente non appena si libererà un posto .Ci scusiamo per l'inconveniente e La ringraziamo per la Sua pazienza e comprensione",
                "Prenotazione in Attesa",
                email
        );
    }

    public void notifyEliminazioneAppuntamento(Prenotazione prenotazione) {  //APPUNTAMENTO RIMOSSO DALL'ADMIN
        String email = prenotazione.getUsernameCliente();

        // Notifica l'utente
        notificationService.sendNotification2(
                " La informiamo cortesemente che l'appuntamento precedentemente fissato è stato annullato. Ci scusiamo per l'inconveniente causato e desideriamo rassicurarla che provvederemo a riprogrammare l'appuntamento nei prossimi giorni",
                "Appuntamento Rimosso",
                email
        );
    }


}
    
