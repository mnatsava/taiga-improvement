package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.util.Date;

/**
 * @author Sargis Sargsyan on 6/7/21
 * @project internal-training-exam
 */
public class ApiHelper {

    public static JsonObject createProject(JsonObject project) {
        Response response;
        response = ApiClient.post("/projects", project);
        String jsonString = null;
        try {
            jsonString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }

    public static JsonObject createIssue(JsonObject issue) {
        Response response;
        response = ApiClient.post("/issues", issue);
        String jsonString = null;
        try {
            jsonString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }

    public static void deleteProject(JsonObject project) {
        ApiClient.delete("/projects", project);
    }

    public static void deleteIssue(JsonObject issue) {
        ApiClient.delete("/issues", issue);
    }

    public static JsonObject getCurrentUser() {
        Response response;
        response = ApiClient.get("api/v1/users/me");
        String jsonString = null;
        try {
            jsonString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }

    public static JsonArray getAllProjects() {
        Response response = ApiClient.get("api/v1/projects?member=" + getCurrentUser().get("id").getAsString());
        String jsonString = null;
        try {
            jsonString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonParser.parseString(jsonString).getAsJsonArray();
    }


    public JsonObject getProject(String projectId) throws IOException {
        Response response = ApiClient.get("api/v1/projects/" + projectId);
        String jsonString = response.body().string();
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }

    public static JsonObject createProject() {
        JsonObject project = new JsonObject();
        project.addProperty("name", "Test Project " + new Date().toString());
        project.addProperty("description", "Test Project via Api Description");
        project.addProperty("creation_template", 1);
        project.addProperty("is_private", false);
        return createProject(project);
    }

    public static JsonObject createIssue(int projectID) {
        JsonObject issue = new JsonObject();
        issue.addProperty("project", projectID);
        issue.addProperty("subject", "Test Issue " + new Date().toString());
        issue.addProperty("description", "Test Issue via Api Description");
        return createIssue(issue);
    }
}