package ke.paystep.mpesaservicefull.service;

import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class MpesaAuthentication
{
    @Value("${mpesa.consumer_key}")
    private String consumer_key;

    @Value("${mpesa.consumer_secret}")
    private String consumer_secret;

    private static final Logger LOGGER = LoggerFactory.getLogger(MpesaAuthentication.class);

//    @EventListener(ApplicationReadyEvent.class)
    private String authenticate() throws IOException
    {
        String access_token = consumer_key + ":" + consumer_secret;
        byte[] bytes = access_token.getBytes(StandardCharsets.ISO_8859_1);
        String encoded = Base64.getEncoder().encodeToString(bytes);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .get()
                .addHeader("authorization", "Basic " + encoded)
                .addHeader("cache-control", "no-cache")

                .build();

        Response response = client.newCall(request).execute();

        JSONObject jsonObject = new JSONObject(response.body().string());

        LOGGER.info("The token: "+jsonObject.getString("access_token"));

        return jsonObject.getString("access_token");
    }

    public JSONObject connect(String requestJson, String url) throws
            IOException {
        OkHttpClient client = new OkHttpClient();

        String accessToken = authenticate();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + accessToken)
                .addHeader("cache-control", "no-cache")

                .build();

        Response response = client.newCall(request).execute();
        LOGGER.info(response.toString());

        return new JSONObject(response.body().string());
    }
}
