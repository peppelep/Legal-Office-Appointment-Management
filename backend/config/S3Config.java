package piattaforme.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


@Configuration
public class S3Config {
    @Bean  //Un bean in Spring è un oggetto istanziato, assemblato e gestito dal contenitore IoC (Inversion of Control) di Spring
    public AmazonS3 s3Client() {  // creo un'istanza del client S3 di AWS utilizzato per interagire con il servizio S3

        String endpointUrl = "https://s3.eu-north-1.amazonaws.com";  

        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        String region = "eu-north-1";

        if (accessKey == null || secretKey == null) {
            throw new IllegalArgumentException("AWS credenziali errate");
        }
        
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);  //La classe BasicAWSCredentials memorizza le credenziali di accesso AWS

        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();     //Il metodo build() crea e restituisce un'istanza completamente configurata di AmazonS3 basata sulle configurazioni fornite.
    }

}
