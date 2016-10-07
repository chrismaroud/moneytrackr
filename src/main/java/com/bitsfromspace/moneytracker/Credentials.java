package com.bitsfromspace.moneytracker;

import com.bitsfromspace.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import static com.bitsfromspace.utils.Conditions.notNull;

/**
 * @author chris
 * @since 06/10/2016.
 */
@Singleton
@Named
public class Credentials {

    private final String googleClientId;
    private final String googleClientSecret;

    @Inject
    public Credentials(@Value("${moneytrackr.credentials.secretFile}") String secretFilePath) {
        final AtomicReference<String> googleClientIdRef = new AtomicReference<>();
        final AtomicReference<String> googleClientSecretRef = new AtomicReference<>();

        ExceptionUtils.tryCatch(() -> {
            final Properties secretFileProperties = new Properties();
            try (FileReader reader = new FileReader(secretFilePath)) {
                secretFileProperties.load(reader);
            }
            googleClientIdRef.set(secretFileProperties.getProperty("google.client.clientId"));
            googleClientSecretRef.set(secretFileProperties.getProperty("google.client.clientSecret"));

            notNull(googleClientIdRef.get(), "google.client.clientId");
            notNull(googleClientSecretRef.get(), "google.client.clientSecret");
        }, (Throwable throwable) -> {throw new IllegalArgumentException("Unable to load credentials from secret file.", throwable);});

        this.googleClientId = googleClientIdRef.get();
        this.googleClientSecret = googleClientSecretRef.get();
    }

    public String getGoogleClientId() {
        return googleClientId;
    }

    public String getGoogleClientSecret() {
        return googleClientSecret;
    }
}
