package org.underpressureapps.unconflicto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_URL = "https://pomelo.uninorte.edu.co/pls/prod/twbkwbis.P_ValLogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickIngreso(View view) {
        // Creating HTTP client
        HttpClient httpClient = new DefaultHttpClient();

        // Creating HTTP Post
        HttpPost httpPost = new HttpPost(LOGIN_URL);

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("sid", "randyc"));
        nameValuePair.add(new BasicNameValuePair("PIN", "270295randy"));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        }
        catch (UnsupportedEncodingException e) {
            // writing error to Log
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);

            // writing response to log
            Log.d("Http Response:", response.toString());

        } catch (ClientProtocolException e) {
            // writing exception to log
            e.printStackTrace();

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }

    }
}
