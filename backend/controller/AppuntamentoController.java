package piattaforme.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import piattaforme.demo.exceptions.NoPrenotazioneInRepository;
import piattaforme.demo.models.Appuntamento;
import piattaforme.demo.models.Prenotazione;
import piattaforme.demo.services.AppuntamentoService;
import piattaforme.demo.exceptions.NoAppuntamentoExistInRepository;
import piattaforme.demo.services.EmailService;
import piattaforme.demo.services.PrenotazioneService;
import piattaforme.demo.services.S3Service;

import java.util.List;

@RestController
@RequestMapping("/appuntamenti")
@CrossOrigin(origins = "*")
public class AppuntamentoController {

    @Autowired
    private AppuntamentoService appuntamentoService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private S3Service s3Service;

    //Restituisce tutti gli Appuntamenti 
    @GetMapping("/all")
    public ResponseEntity<List<Appuntamento>> getAllAppuntamenti() {
        try {
            return new ResponseEntity<List<Appuntamento>>(appuntamentoService.getAll(), HttpStatus.OK);
        } catch (NoAppuntamentoExistInRepository e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Restituisce l'appuntamento con quell'id 
    @GetMapping("/get/{id}")
    public ResponseEntity<Appuntamento> getAppuntamentoById(@PathVariable Long id) {
        try {
            Appuntamento appuntamento = appuntamentoService.getById(id);
            return ResponseEntity.ok(appuntamento);
        } catch (NoAppuntamentoExistInRepository e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Aggiunge un appuntamento 
    @PostMapping("/add")
    public ResponseEntity<Appuntamento> addAppuntamento(@RequestBody Appuntamento appuntamento) {
        try {
            Appuntamento newAppuntamento = appuntamentoService.add1(appuntamento);

            //creo il messaggio che invio tramite SnS mettendo le specifiche dell'appuntamento
            String message = "Nuovo appuntamento aggiunto:\n" +
                    "Tipo Appuntamento: " + newAppuntamento.getTipo() + "\n" +
                    "Posti Disponibili: " + newAppuntamento.getPostiDisponibili() + "\n" +
                    "Avvocato: " + newAppuntamento.getNomeAvvocato() + "\n" +
                    "Data: " + newAppuntamento.getData() + "\n" +
                    "Ora: " + newAppuntamento.getOra();

            String subject = "Nuovo Appuntamento";

            // Invia la notifica SNS
            emailService.sendNotificationAll(message, subject);

            return ResponseEntity.status(HttpStatus.CREATED).body(newAppuntamento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //rimuove un appuntamento 
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeAppuntamento(@PathVariable Long id) {
        try {
            Appuntamento appuntamento = appuntamentoService.getById(id);
            appuntamentoService.remove(appuntamento);

            List<Prenotazione> prenotazioni = this.prenotazioneService.getByAppuntamento(id);  //prendo tutte le prenotazioni effettuate su quell'appuntamento

            for(Prenotazione p : prenotazioni){
                this.s3Service.deleteFilesByUserName(p.getId());  //elimino i file associati a quella prenotazione
                this.prenotazioneService.remove(p);
                this.prenotazioneService.notifyEliminazioneAppuntamento(p);  //notifico i clienti prenotati che l'appuntamento è stato rimosso
            }

            return ResponseEntity.noContent().build();
        } catch (NoAppuntamentoExistInRepository e) {
            return ResponseEntity.notFound().build();
        } catch (NoPrenotazioneInRepository e) {
            throw new RuntimeException(e);
        }
    }
}