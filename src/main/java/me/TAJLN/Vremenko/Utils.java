package me.TAJLN.Vremenko;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static String oneaccount_id(String token) throws IOException, JSONException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.one-account.io/v1/oauth/introspect");

        httpPost.setHeader("Authorization", "Bearer " + token);

        CloseableHttpResponse response = client.execute(httpPost);

        JSONObject obj = new JSONObject(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
        System.out.println(obj.getString("sub"));
        client.close();

        return obj.getString("sub");
    }
}
