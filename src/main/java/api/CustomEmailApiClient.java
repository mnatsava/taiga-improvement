package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomEmailApiClient {

    private static Logger LOGGER = Logger.getLogger(CustomEmailApiClient.class);
    private static String API_KEY = "8fefec01202eb2b17717777db3a90fc898d7b194968e7a88af1b35b7ab71ec2e";
    private static final String BASE_EMAIL_URL = "https://api.mailslurp.com";
    private static String INBOX_ID = null;
    private static String EMAIL_ID = null;


    public static JsonObject post(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(BASE_EMAIL_URL + url)
                .method("POST", body)
                .addHeader("x-api-key", API_KEY)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert response != null;
        if (!response.isSuccessful()) {
            throw new Error("HTTP error code: " + String.valueOf(response.code()));
        }

        String jsonString = response.body().string();
        LOGGER.info("Post response " + jsonString);

        JsonObject object = JsonParser.parseString(jsonString).getAsJsonObject();
        INBOX_ID = object.get("id").getAsString();
        return object;
    }

    public static Response get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_EMAIL_URL + url)
                .get()
                .addHeader("x-api-key", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert response != null;
        if (!response.isSuccessful()) {
            throw new Error("HTTP error code: " + String.valueOf(response.code()));
        }

//        String jsonString = response.body().string();
//        LOGGER.info("GET Response " + JsonParser.parseString(jsonString).getAsJsonArray());
//        return JsonParser.parseString(jsonString).getAsJsonArray();
        return response;
    }

    public static void getActivationLinkAndConfirm() throws IOException {
        String verificationLink = null;

        Response response = CustomEmailApiClient.get("/emails/"+EMAIL_ID+"/raw/json");
        String rawData = response.body().string();
        String string = JsonParser.parseString(rawData).toString();
        Pattern pattern = Pattern.compile("verify-email.*");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            verificationLink = matcher.group(0).substring(13,49);
            LOGGER.info("Key : " + verificationLink);
        }


        ApiClient.get1("verify-email/" +verificationLink);
        LOGGER.info("Verification link " + verificationLink);
        ApiClient.post1("/users/change_email",verificationLink);

        deleteAllInboxes();

    }



    public static void deleteAllInboxes() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.mailslurp.com/inboxes")
                .method("DELETE", body)
                .addHeader("x-api-key", API_KEY)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert response != null;
        if (!response.isSuccessful()) {
            throw new Error("HTTP error code: " + String.valueOf(response.code()));
        }

    }

    public static String getReceivingEmailsID() {

        try {
            String jsonString = get("/inboxes/" + INBOX_ID + "/emails").body().string();
            JsonArray jsonElements = JsonParser.parseString(jsonString).getAsJsonArray();
            EMAIL_ID = jsonElements.get(0).getAsJsonObject().get("id").getAsString();
            LOGGER.info("Received Email id " + EMAIL_ID);
            return EMAIL_ID;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
