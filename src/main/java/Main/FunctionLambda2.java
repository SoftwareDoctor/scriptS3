//package Main;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
//import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
//import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.S3Object;
//
//import java.io.File;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//
//public class FunctionLambda2 implements RequestHandler<Object, String> {
//
//    private final S3Client s3Client = S3Client.builder()
//            .region(Region.US_EAST_1)
//            .build();
//
//    @Override
//    public String handleRequest(Object o, Context context) {
//        String bucketName = System.getenv("jarlambdatest");
//        String filePath = System.getenv("CC:\\Users\\andrea.italiano\\Downloads\\dumps\\dump.sql");
//        String s3Key = "dumps/" + Instant.now().toString() + "-jarlambdatest";
//        String folderPrefix = "dumps/";
//        try {
//            File file = new File(filePath);
//            if (!file.exists()) {
//                throw new RuntimeException("File not found: " + filePath);
//            }
//            uploadNewFile(bucketName, s3Key, filePath);
//
//            deleteOldFile(bucketName, folderPrefix);
//
//            context.getLogger().log("File uploaded successfully and old files deleted from S3 bucket: " + bucketName);
//            return "File uploaded successfully and old files deleted from S3 bucket: " + bucketName;
//        } catch (Exception e) {
//            context.getLogger().log("Error: " + e.getMessage());
//            return "Error uploading file to S3 or deleting old files.";
//        }
//    }
//
//    private void deleteOldFile(String bucketName, String folderPrefix) {
//        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
//                .bucket(bucketName)
//                .prefix(folderPrefix)
//                .build();
//
//        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);
//
//        Instant cutoff = Instant.now().minus(16, ChronoUnit.DAYS);
//
//        for (S3Object s3Object : listResponse.contents()) {
//            // Ottiengo l'ultima modifica dell'oggetto
//            Instant lastModified = s3Object.lastModified();
//
//            // Elimino solo i file più vecchi di 16 giorni
//            if (lastModified.isBefore(cutoff)) {
//                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(s3Object.key())
//                        .build();
//                s3Client.deleteObject(deleteRequest);
//            }
//        }
//    }
//
//    private void uploadNewFile(String bucketName, String s3Key, String filePath) {
//        File file = new File(filePath);
//        PutObjectRequest putRequest = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(s3Key)
//                .build();
//        s3Client.putObject(putRequest, file.toPath());
//    }
//}
