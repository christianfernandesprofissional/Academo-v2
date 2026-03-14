package com.academo.service.storage.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

//@Component
public class DriveService {


    private final Drive drive;

    public DriveService() throws Exception {
        // Caminho para o JSON que você baixou do Console do Google
        FileInputStream in = new FileInputStream("src/main/resources/client_secret_685572874715-63fravm0v1rqfllot0bpq1oacvioi3j3.apps.googleusercontent.com.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                GsonFactory.getDefaultInstance(), new InputStreamReader(in));

        List<String> scopes = Collections.singletonList(DriveScopes.DRIVE_FILE);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                clientSecrets,
                scopes)
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        // Autoriza o usuário (abre navegador para login)
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");

        drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential)  // aqui usamos Credential diretamente
                .setApplicationName("Spring Drive Upload")
                .build();
    }

    public String uploadFile(MultipartFile multipartFile) throws Exception {
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(multipartFile.getOriginalFilename());

        com.google.api.services.drive.model.File uploadedFile = drive.files().create(
                fileMetadata,
                new InputStreamContent(
                        multipartFile.getContentType(),
                        multipartFile.getInputStream()
                )
        ).setFields("id").execute();

        // Este ID retornado é o ID do arquivo dentro do Google Drive
        // Aqui no Academo será armazenado no atributo "path"
        return uploadedFile.getId();
    }

    public DownloadedFile getFile(String fileId) throws Exception {
        // Recupera o metadado do arquivo
        com.google.api.services.drive.model.File fileMetadata = drive.files()
                .get(fileId)
                .setFields("name, mimeType")
                .execute();

        // Cria o fluxo de saída para armazenar os bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Faz o download do conteúdo
        drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);

        // Retorna um objeto com as informações do arquivo
        return new DownloadedFile(
                fileMetadata.getName(),
                fileMetadata.getMimeType(),
                outputStream.toByteArray()
        );
    }

    public void deleteFile(String fileId) throws Exception {
        drive.files().delete(fileId).execute();
    }

    public record DownloadedFile(String name, String mimeType, byte[] content) {}
}
