package com.academo.util.config.storage;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleDriveConfigService {

    private final Drive driveService;

    public GoogleDriveConfigService() throws Exception, GeneralSecurityException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new FileInputStream("academostorage-6d230d1bc095.json"))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        driveService = new Drive.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
                .setApplicationName("AcademoStorage")
                .build();

    }

    public Drive getDriveService() {
        return driveService;
    }
}
