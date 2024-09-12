///**
// * @Author: SoftwareDoctor andrea_italiano87@yahoo.com
// * @Date: 2024-09-10 15:31:36
// * @LastEditors: SoftwareDoctor andrea_italiano87@yahoo.com
// * @LastEditTime: 2024-09-11 08:55:51
// * @FilePath: src/main/java/Main/FunctionLambda.java
// * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
// */
//package Main;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//
//import java.io.File;
//


/*
Il modo più comune e consigliato per definire un gestore è implementare l' interfaccia RequestHandler e sovrascrivere il suo metodo handleRequest() :
 */


//public class FunctionLambda implements RequestHandler<Object, String> {
//// AWS SDK per Java per le interazioni S3
////    private final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
//    private final String bucketName = "your-s3-bucket-name";
//    private final String filePath = "/path/to/your/dump/file.dump";
//    private final String s3Key = "path/in/bucket/file.dump"; 
////la chiave viene definita quando carico un file in un bucket S3
//    // la chiave è il percorso del file all interno del bucket. è il percorso completo dell'oggetto dentro il bucket. 
//    private final String accessKey = "your-access-key";
//    private final String secretAccessKey = "your-secret-access-key";
//    private final String region = "your-region";  //es. 'us-west-2'
//    
//    //la funzione Lambda dovrà essere creata nella stessa regione in cui creo il bucket 
//    // enable amazon function URL --> In "Impostazioni avanzate", seleziona l'opzione "Abilita URL funzione", che ci consentirà di richiamare questa funzione pubblicamente senza dover configurare un gateway API.
//
//    public AmazonS3 createS3Client() {
//        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);
//
//        return AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .withRegion(region)
//                .build();
//    }
//    
//    @Override
//    public String handleRequest(Object o, Context context) {
//        try {
//            File file = new File(filePath);
//            if (!file.exists()) {
//                throw new RuntimeException("File not found: " + filePath);
//            }
//            //Creazione una richiesta di caricamento dell'oggetto
//            PutObjectRequest request = new PutObjectRequest(s3Key, bucketName, file);
//            //Richiesta di caricamento 
//            AmazonS3 awsCredentials = createS3Client();
//            awsCredentials.putObject(request);
//            
//            return "File uploaded successfully to S3 bucket: " + bucketName;
//        } catch (Exception e) {
//            context.getLogger().log("Error: " + e.getMessage());
//            return "Error uploading file to S3";
//        }
//    }
//}
//
///*
//
//Sì, ci sono diverse modalità e configurazioni che puoi utilizzare per interagire con Amazon S3 a seconda delle tue esigenze e delle best practice di sicurezza. Ecco alcune alternative e considerazioni:
//
//1. Uso di Profili AWS e Ruoli IAM
//Se stai lavorando in un ambiente che supporta profili AWS (ad esempio, quando utilizzi AWS SDK su una macchina locale o EC2), puoi configurare il profilo AWS per gestire le credenziali senza doverle codificare direttamente nel tuo codice.
//
//
//AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//    .withCredentials(new ProfileCredentialsProvider("your-profile-name"))
//    .withRegion(Regions.fromName(REGION))
//    .build();
//2. Uso di IAM Roles in Ambienti AWS
//Quando esegui la tua applicazione su servizi AWS come EC2, Lambda o ECS, è consigliato utilizzare i ruoli IAM associati all'istanza o al servizio. In questo modo, non è necessario gestire manualmente le credenziali nel codice. AWS SDK utilizzerà automaticamente le credenziali fornite dal ruolo associato.
//
//
//AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//    .withRegion(Regions.fromName(REGION))
//    .build();
// */



/*
Se la Lambda viene invocata tramite un'API Gateway, allora sì, è necessario restituire una risposta con un APIGatewayProxyResponseEvent, perché è il formato atteso dall'API Gateway.
Se la Lambda viene invocata automaticamente da un'altra fonte, come un CloudWatch Event (cron job) o un S3 trigger, allora non è necessario un APIGatewayProxyResponseEvent. Puoi configurare un cron job con CloudWatch Events per eseguire la tua Lambda ogni 15 giorni senza alcun coinvolgimento di API Gateway.
 */


/*
AmazonS3 viene configurato automaticamente con le IAM Roles dell'istanza Lambda. Quindi, non hai bisogno di fornire le credenziali manualmente se hai già assegnato i permessi S3 alla funzione Lambda.
 */


/*
Il Maven Shade Plugin è essenziale quando si creano funzioni AWS Lambda con Java. Ci consente di impacchettare la nostra applicazione e le sue dipendenze in un singolo file JAR autonomo, noto anche come JAR "uber" o "fat" .

Il plugin estrae il contenuto di tutte le nostre dipendenze e le inserisce nelle classi del nostro progetto, che è il modo in cui AWS Lambda si aspetta che distribuiamo il nostro codice.
 */