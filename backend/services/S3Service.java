package piattaforme.demo.services;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.AmazonS3;
import piattaforme.demo.exceptions.NoPrenotazioneInRepository;
import piattaforme.demo.models.Prenotazione;
import piattaforme.demo.repository.PrenotazioneRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class S3Service {
    private final AmazonS3 s3 ;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    public S3Service(AmazonS3 s3) {
        this.s3 = s3 ; 
    }

    public String uploadFile(MultipartFile file) throws IOException {  //Non lo uso  alla fine
        String bucketName = "springboot-s3-giuseppe-lepore";
        String originalName = file.getOriginalFilename();

        try{
            File file1 = convertMultiPartFile(file);
            PutObjectResult put = s3.putObject(bucketName,originalName,file1); //errore qua
            return put.getContentMd5();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    //springboot-s3-giuseppe-lepore --> giuseppe.lepore.new@gmail.com --> ConsultazioneFiscale --> file.txt
    public String uploadFileCartella(MultipartFile file, String nome , String appuntamento ) throws IOException { //MultipartFile file caricato tramite richiesta http
        String bucketName = "springboot-s3-giuseppe-lepore";
        String originalName = file.getOriginalFilename();
        String filePath = nome + "/" + appuntamento + "/" + originalName;  // mi permette la gestione dei documenti a cartelle

        try {
            File file1 = convertMultiPartFile(file);  //converto il multipartFile in un File
            PutObjectResult put = s3.putObject(bucketName, filePath, file1);  //carico file

            //md5 viene utilizzato per verificare l integrita dei dati durante il trasferimento dei file
            //l'MD5 puo essere calcolato sia per il file originale che per il file ricevuto per verificare che non ci siano
            //state modifiche durante il trasferimento
            return put.getContentMd5();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File convertMultiPartFile(MultipartFile file) throws IOException{
        File convFile = new File(file.getOriginalFilename());  //creo nuovo file
        FileOutputStream fos = new FileOutputStream(convFile); //apro un flusso di output verso il file creato
        fos.write(file.getBytes());  // scrivo il contenuto del MultipartFile
        fos.close();
        return convFile ; 
    }

    public void deleteFilesByUserName(Long id) throws NoPrenotazioneInRepository {
        Optional<Prenotazione> prenotazione = prenotazioneRepository.findById(id);  //prendo la prentoazione connessa a quell'id
        Prenotazione p ;

        if (prenotazione.isEmpty()) { //verifico che esiste una prenotazione con quell'id
            throw new NoPrenotazioneInRepository();
        } else {
            p = prenotazione.get();
        }

        String bucketName = "springboot-s3-giuseppe-lepore";
        String prefix = p.getUsernameCliente() + "/" + p.getTipoAppuntamento() + "/";  //mi serve per eliminare solo i file relativi a un utente per il tipo di appuntamento della prenotazione

        ObjectListing objectListing = s3.listObjects(bucketName, prefix);
        for (S3ObjectSummary summary : objectListing.getObjectSummaries()) { //ritorna una lista di ogetti s3ObjectSummary ognuno dei quali contiene : nomeFile, dimensione, dataUltimaNotifica, ..
            s3.deleteObject(new DeleteObjectRequest(bucketName, summary.getKey()));
        }

        // Se ha piu di 1000 elementi continuo a iterare gli elementi rimanenti
        while (objectListing.isTruncated()) { //veririfica se objectListing non è completo
            objectListing = s3.listNextBatchOfObjects(objectListing);
            for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
                s3.deleteObject(new DeleteObjectRequest(bucketName, summary.getKey()));
            }
        }
    }
    
}
