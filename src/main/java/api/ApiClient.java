package api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.util.UUID;


/**
 * @author Sargis Sargsyan on 6/7/21
 * @project internal-training-exam
 */
public class ApiClient {

    private static String generatedText = UUID.randomUUID().toString();
    private static String ACCESS_TOKEN = null;
    private static final String BASE_URL = "https://api.taiga.io/api/v1";

    public static JsonObject login(String email, String password) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n        \"accepted_terms\": \"true\",\n        \"email\": \"" + email +"\",\n        \"full_name\": \"mnats_avagyan1lkj\",\n        \"password\": \"253388pap\",\n        \"type\": \"public\",\n        \"username\": \"" + generatedText + "\"\n    }");

        Request request = new Request.Builder()
                .url("https://api.taiga.io/api/v1/auth/register")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        Response response;
        String jsonString;
        JsonObject object = null;
        try {
            response = client.newCall(request).execute();
            jsonString = response.body().string();
            object = JsonParser.parseString(jsonString).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ACCESS_TOKEN = object.get("auth_token").getAsString();
        return object;
    }

    public static Response post(String url, JsonObject jsonObject) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
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

        return response;
    }

    public static void post1(String url, String token) {            //TODO need to change
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"email_token\": \"" +token +"\"}\n");
        Request request = new Request.Builder()
                .url(BASE_URL+ url)
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            System.out.println("Second post " + jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Response get(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.taiga.io/" + url)
                .get()
                .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
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

        return response;
    }

    public static Response get1(String url) {    //TODO need to change
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://tree.taiga.io/" + url)
                .get()
                .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
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

        return response;
    }
    public static void delete(String url, JsonObject jsonObject) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + url + "/" + jsonObject.get("id").getAsString())
                .delete(null)
                .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
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
    }
}