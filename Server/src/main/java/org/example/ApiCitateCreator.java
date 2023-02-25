package org.example;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Scanner;

public class ApiCitateCreator implements gettingCitates{

    public String getCitate() {
        String citate = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.forismatic.com/api/1.0/?method=getQuote&lang=ru&format=text&jsonp=?");
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            Scanner sc = new Scanner(httpResponse.getEntity().getContent());


            citate = sc.nextLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return citate;
    }
}
