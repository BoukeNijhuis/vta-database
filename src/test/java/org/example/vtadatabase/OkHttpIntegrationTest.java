package org.example.vtadatabase;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OkHttpIntegrationTest {

    @Test
    public void testIntegration() {

        String result = httpCall("GET", "http://localhost:8080/clients");
        Assertions.assertTrue(result.contains("Jan Jansen"));
    }

    private String httpCall(String method, String url) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                // no result
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
