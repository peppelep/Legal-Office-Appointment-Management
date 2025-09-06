package piattaforme.demo.services;

import java.util.Map;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sns.model.Subscription;
import software.amazon.awssdk.services.sns.model.SubscriptionLimitExceededException;
import software.amazon.awssdk.services.sns.model.ConfirmSubscriptionRequest;
import software.amazon.awssdk.services.sns.model.ListSubscriptionsByTopicRequest;
import software.amazon.awssdk.services.sns.model.ListSubscriptionsByTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesRequest;
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesResponse;
import software.amazon.awssdk.services.ses.model.IdentityVerificationAttributes;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SesException;
import software.amazon.awssdk.services.ses.model.VerificationStatus;
import software.amazon.awssdk.services.ses.model.VerifyEmailAddressRequest;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityRequest;

@Service
public class EmailService {

    private final SnsClient snsClient;
    private final SesClient sesClient;


    public EmailService() {  //CREA LE ISTANZE DEL CLIENT CHE PERMETTONO DI INTERAGIRE CON SnS e SeS

        String accessKey = System.getenv("AWS_ACCESS_KEY_ID"); //prendo questo valore dalla variabili di ambiente
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY"); //prendo questo valore dalla variabili di ambiente
        String region = "eu-north-1";

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.sesClient = SesClient.builder()
                                  //.region(Region.US_EAST_1) // regione vecchia
                                    .region(Region.EU_NORTH_1) // Imposta la regione SES
                                    .build();
                                
        
        this.snsClient = SnsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(region)) // Imposta la regione SES
                .build();
    }

    //------------------------------------------------------------------------------------------------
    // SNS
    //------------------------------------------------------------------------------------------------


    //MANDA MAIL TRAMITE SnS E DUNQUE SECONDO IL MODELLO PUB/SUB , PER INFORMARE CHE è STATO AGGIUNTO UN NUOVO APPUNTAMENTO
    public void sendNotificationAll(String message, String subject) { //, String emailAddress
        PublishRequest request = PublishRequest.builder()
                .message(message)
                .subject(subject)
                .messageAttributes(
                        Map.of("email", software.amazon.awssdk.services.sns.model.MessageAttributeValue.builder()
                                .dataType("String")
                                .stringValue("mymail@gmail.com")
                                .build())
                )
                //.topicArn("arn:aws:sns:us-east-1:654654609903:PrenotazioniAppuntamentiLegal") VECCHIO ARN
                .topicArn("arn:aws:sns:eu-north-1:905418194193:AppuntamentiLegale")
                .build();

        PublishResponse result = snsClient.publish(request);
    }

    //VERIFICA SE L'UTENTE è REGISTRATO O MENO SU SnS
    public boolean isSubscribed(String emailAddress) {
        try {
            ListSubscriptionsByTopicRequest request = ListSubscriptionsByTopicRequest.builder()
                    //.topicArn("arn:aws:sns:us-east-1:654654609903:PrenotazioniAppuntamentiLegal") //VECHIO ARN
                    .topicArn("arn:aws:sns:eu-north-1:905418194193:AppuntamentiLegale")
                    .build();
    
            ListSubscriptionsByTopicResponse response = snsClient.listSubscriptionsByTopic(request);
            
            for (Subscription subscription : response.subscriptions()) {
                if (subscription.endpoint().equals(emailAddress)) {
                    if (!subscription.subscriptionArn().equals("PendingConfirmation")) {
                        System.out.println("Utente " + emailAddress + " è iscritto alle notifiche e ha confermato.");
                        return true;
                    } else {
                        System.out.println("Utente " + emailAddress + " è iscritto alle notifiche ma deve confermare.");
                    }
                }
            }
        } catch (SnsException e) {
            System.out.println("Si è verificato un errore durante il controllo dell'iscrizione alle notifiche: " + e.getMessage());
        }
        return false;
    }

    //INVIA UNA MAIL ALL'UTENTE PER POTERSI REGISTRARE SU SnS TRAMITE UN CLICK SU UN LINK
    public void subscribeAndConfirm(String emailAddress) {
        try {
            // Iscrivi l'utente all'argomento SNS
            SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                    .protocol("email")
                    .endpoint(emailAddress)
                    //.topicArn("arn:aws:sns:us-east-1:654654609903:PrenotazioniAppuntamentiLegal") VECCHIO ARN
                    .topicArn("arn:aws:sns:eu-north-1:905418194193:AppuntamentiLegale")
                    .build();
            SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);
    
            // Richiedo la conferma dell'abbonamento
            String subscriptionArn = subscribeResponse.subscriptionArn();
            ConfirmSubscriptionRequest confirmSubscriptionRequest = ConfirmSubscriptionRequest.builder()
                    //.topicArn("arn:aws:sns:us-east-1:654654609903:PrenotazioniAppuntamentiLegal") VECCHIO ARN
                    .topicArn("arn:aws:sns:eu-north-1:905418194193:AppuntamentiLegale")
                    .token(subscriptionArn)
                    .build();
            snsClient.confirmSubscription(confirmSubscriptionRequest);
    
            System.out.println("Utente " + emailAddress + " iscritto e abbonamento confermato.");
        } catch (SubscriptionLimitExceededException e) {
            // Gestisco il caso in cui l'utente è già iscritto
            System.out.println("Utente " + emailAddress + " è già iscritto alle notifiche.");
        } catch (SnsException e) {
            // Gestisco altre eccezioni SNS, ad esempio problemi di connessione, autorizzazione, ecc.
            System.out.println("Si è verificato un errore durante l'iscrizione alle notifiche: " + e.getMessage());
        }
    }


    //------------------------------------------------------------------------------------------------
    // SeS
    //------------------------------------------------------------------------------------------------

    //METODO PER VERIFICARE(REGISTRARE) L'INDIRIZZO EMAIL SU SES 
    public void verifyEmail(String emailAddress) {    
        // Verifica su SES
        try {
            // Costruisco la richiesta per confermare l'indirizzo email
            VerifyEmailAddressRequest verifyEmailAddressRequest = VerifyEmailAddressRequest.builder()
                    .emailAddress(emailAddress)
                    .build();
            sesClient.verifyEmailAddress(verifyEmailAddressRequest);
            System.out.println("Indirizzo email " + emailAddress + " confermato con successo su SES.");
        } catch (SesException e) {
            System.err.println("Errore durante la conferma dell'indirizzo email su SES: " + e.getMessage());
        }
    }

    //METODO PER VERIFICARE SE è STATO VERIFICATO L'INDIRIZZO EMAIL SU SES 
    public boolean isEmailVerified(String emailAddress) {
        try {
            GetIdentityVerificationAttributesResponse response = sesClient.getIdentityVerificationAttributes(
                    GetIdentityVerificationAttributesRequest.builder().identities(emailAddress).build());
            Map<String, IdentityVerificationAttributes> verificationAttributes = response.verificationAttributes();
            if (verificationAttributes.containsKey(emailAddress)) {
                IdentityVerificationAttributes attrs = verificationAttributes.get(emailAddress);
                return attrs != null && attrs.verificationStatus() == VerificationStatus.SUCCESS;
            }
        }catch (SesException e) {
            System.err.println("Errore durante il controllo della verifica dell'indirizzo email su SES: " + e.getMessage());
        }
        return false;
    }

    //INVIA MAIL TRAMITE SeS
    public void sendNotification2(String message, String subject, String emailAddress) {
        try {
            // Verifica se l'indirizzo email è verificato su SES
            boolean isVerified = isEmailVerified(emailAddress);
    
            // Se l'indirizzo email non è verificato, procedi con la verifica
            if (!isVerified) {
                verifyEmail(emailAddress);
            }
            // Costruisco il corpo del messaggio
            Content subjectContent = Content.builder().data(subject).build();
            Content bodyContent = Content.builder().data(message).build();
            Body body = Body.builder().text(bodyContent).build();
            Message msg = Message.builder().subject(subjectContent).body(body).build();
    
            // Costruisco la richiesta di invio email
            SendEmailRequest request = SendEmailRequest.builder()
                    //.source("YOUR_VERIFIED_EMAIL_ADDRESS") // Indirizzo email verificato su SES
                    .source("mymail@gmail.com")      
                    .destination(Destination.builder().toAddresses(emailAddress).build())
                    .message(msg)
                    .build();
      
            // Invia l'email
            SendEmailResponse response = sesClient.sendEmail(request);
            System.out.println("Email inviata a " + emailAddress + ": " + subject + " - " + message);
        } catch (SesException e) {
            System.err.println(emailAddress);
            System.err.println("Errore durante l'invio dell'email: " + e.getMessage());
        }
    }


    //METODO RICHIAMATO DAL CONTROLLER PER VERIFICARE(REGISTRARE) L'INDIRIZZO EMAIL SU SES 
    public void sendVerificationEmail(String emailAddress) { //NON LO USO MAI , DOPO HO IMPLEMENTATO DIFFERENTEMENTE
        try {
            // Verifica se l'indirizzo email è già verificato su SES
            boolean isVerified = isEmailVerified(emailAddress);

            // Se l'indirizzo email è già verificato, esce dal metodo
            if (isVerified) {
                System.out.println("L'indirizzo email " + emailAddress + " è già verificato su SES.");
                return;
            }

            // Costruisco la richiesta per l'invio dell'email di verifica
            VerifyEmailIdentityRequest request = VerifyEmailIdentityRequest.builder()
                .emailAddress(emailAddress)
                .build();

            // Invia l'email di verifica
            sesClient.verifyEmailIdentity(request);
            System.out.println("Email di verifica inviata a " + emailAddress + ".");

        } catch (SesException e) {
            System.err.println("Errore durante l'invio dell'email di verifica: " + e.getMessage());
        }
    }


}