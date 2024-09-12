package Main;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class ScriptS3 {

    public static void main(String[] args) {
        String bucketName = "jarlambdatest";
        String localDir = "C:\\Users\\andrea.italiano\\Downloads\\dumps";
        String s3Prefix = "dumps/";

        String accessKey = "";
        String secretKey = "";
        Region region = Region.US_EAST_1;

        // Configurazione delle credenziali
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        // Trovare il file dump più recente
        Optional<File> latestFile = findLatestFile(localDir);

        if (latestFile.isPresent()) {
            File file = latestFile.get();
            String s3Key = s3Prefix + file.getName();
            // Caricare il nuovo file su S3

            boolean successo = uploadFileToS3(s3Client, bucketName, s3Key, file.toPath());
            if(successo) {
            System.out.println("File uploaded successfully: " + file.getName());
            // Eliminare i file più vecchi di 16 giorni su S3
                deleteOldestFileFromS3(s3Client, bucketName, s3Prefix);
            System.out.println("File uploaded successfully: " + file.getName());
            }
        } else {
            System.out.println("No files found in directory: " + localDir);
        }
    }

    private static Optional<File> findLatestFile(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".sql"));

        if (files == null || files.length == 0) {
            return Optional.empty();
        }

        File latestFile = null;
        for (File file : files) {
            if (latestFile == null || file.lastModified() > latestFile.lastModified()) {
                latestFile = file;
            }
        }
        return Optional.ofNullable(latestFile);
    }

    private static void deleteOldestFileFromS3(S3Client s3Client, String bucketName, String prefix) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        // Se non ci sono file nel bucket, uscire
        if (listResponse.contents().isEmpty()) {
            System.out.println("No files found in the S3 bucket.");
            return;
        }

        // Trova il file più vecchio
        S3Object oldestObject = listResponse.contents().get(0);
        for (S3Object s3Object : listResponse.contents()) {
            if (s3Object.lastModified().isBefore(oldestObject.lastModified())) {
                oldestObject = s3Object;
            }
        }

        // Elimina il file più vecchio
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(oldestObject.key())
                .build();
        s3Client.deleteObject(deleteRequest);

        System.out.println("Deleted oldest file from S3: " + oldestObject.key());
    }


    private static boolean uploadFileToS3(S3Client s3Client, String bucketName, String s3Key, Path filePath) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        s3Client.putObject(putObjectRequest, filePath);
        return true;
    }
}
