package org.example.vtadatabase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlainJavaIntegrationTest {

    @Test
    public void testIntegration() {

        String result = httpCall("GET", "http://localhost:8080/clients");
        Assertions.assertTrue(result.contains("Jan Jansen"));
    }

    private String httpCall(String method, String url) {

        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod(method);
            con.connect();

            int status = con.getResponseCode();

            if (status == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                return content.toString();
            }

            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // no result
        return null;
    }

}
