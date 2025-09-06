package piattaforme.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import piattaforme.demo.models.Prenotazione;
import piattaforme.demo.services.S3Service;
import piattaforme.demo.exceptions.NoPrenotazioneInRepository;
import java.io.IOException;

@RestController
@RequestMapping("/documenti")
@CrossOrigin(origins = "*")
public class DocumentoController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/carica")
    public String carica(@RequestParam("file") MultipartFile file , @RequestParam("nome") String nome , @RequestParam("appuntamento") String appuntamento) throws IOException, NoPrenotazioneInRepository {
        return s3Service.uploadFileCartella(file,nome,appuntamento);
    }

    @PostMapping("/eliminaDocumenti")
    public void eliminaDocumenti(@RequestParam("id") Long id) throws IOException, NoPrenotazioneInRepository {
        s3Service.deleteFilesByUserName(id);
    }


}