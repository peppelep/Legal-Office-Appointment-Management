package piattaforme.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import piattaforme.demo.services.EmailService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    //Registrazione a SnS per gestione mail Pub/Sub.
    @PostMapping("/registrazione")
    public void registrazione(@RequestBody String email) {
        emailService.subscribeAndConfirm(email);
    }

    //Verifica se è o meno registrato a SnS
    @GetMapping("/isSubscribed")
    public boolean isSubscribed(@RequestParam String email) {
        return emailService.isSubscribed(email);
    }

    //Registrazione a SeS per gestione mail.
    @PostMapping("/registrazioneSeS")
    public void registrazioneSeS(@RequestBody String email) {
        emailService.sendVerificationEmail(email);
    }

    //Verifica se è registrato su SeS
    @GetMapping("/isSubscribedSeS")
    public boolean isSubscribedSeS(@RequestParam String email) {
        return emailService.isEmailVerified(email);
    }
    
    
}
