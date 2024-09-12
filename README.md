# Script to upload the dump in S3 AWS service

## Descrizione
Questo progetto Java ha lo scopo di caricare automaticamente l'ultimo file dump `.sql` presente in una directory locale su un bucket Amazon S3 e di eliminare il file più vecchio dal bucket S3. Lo script utilizza l'SDK AWS versione 2.x per interagire con il servizio S3.

## Tecnologie Utilizzate
- **Java 17**
- **Maven** 
- **AWS SDK 2.x** per interagire con S3
- **SLF4J e Log4j** per logging
- **Lombok**

## Funzionalità
- **Caricamento automatico su S3**: Carica il file dump `.sql` più recente presente in una directory locale su un bucket S3 specificato.
- **Eliminazione automatica del file più veccho su S3**: L'eliminazione avviene solo se l'upload è avvenuto correttamente
- **Supporto per la configurazione delle credenziali AWS**: Utilizza le credenziali di accesso AWS (chiave di accesso e chiave segreta) per connettersi a S3.

## Configurazione
1. **Modifica dei parametri**:
    - `bucketName`: Nome del bucket S3 dove caricare i file.
    - `localDir`: Percorso locale dove sono salvati i file dump `.sql`.
    - `s3Prefix`: Prefisso nel bucket S3 dove verranno salvati i file.
    - `accessKey` e `secretKey`: Chiavi di accesso AWS.

2. **Esecuzione del progetto**:
   Il progetto può essere eseguito come una normale applicazione Java:
   ```bash
   mvn clean install
   java -jar target/LambdaS3DumpMySql-1.0-SNAPSHOT.jar

## Autore
Il progetto è stato creato da Andrea Italiano.


