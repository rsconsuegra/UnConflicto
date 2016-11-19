package org.underpressureapps.unconflicto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    EditText usuario;
    EditText pass;
    public static String TAG = "AppFirebase";
    public static final String LOGIN_URL = "https://pomelo.uninorte.edu.co/pls/prod/twbkwbis.P_ValLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    CookieManager cookieManager = new CookieManager();

    public void onClickIniciar(View view) {
        usuario   = (EditText)findViewById(R.id.edusuario);
        pass      = (EditText)findViewById(R.id.edcontraseña);
        final MainActivity context =  this;

        Uri base_addres =Uri.parse("https://pomelo.uninorte.edu.co");
        new Thread(new Runnable() {
            public void run() {


                URL url = null;
                CookieHandler.setDefault(cookieManager);


                String user     =usuario.getText().toString();
                String password =pass.getText().toString();
                //Save cokies

                try {
                    url = new URL(LOGIN_URL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpsURLConnection conn = null;

                try {
                    conn = (HttpsURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:42.0) Gecko/20100101 Firefox/42.0");
                    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                    conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
                    conn.setRequestProperty("Connection", "keep-alive");
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Cookie", "TESTID=set");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("sid", user)
                        .appendQueryParameter("PIN", password);
                String query = builder.build().getEncodedQuery();
                //System.out.println(query);

                OutputStream os = null;
                try {
                    os = conn.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedWriter writer = null;

                    writer = new BufferedWriter(
                            new OutputStreamWriter(os));

                try {
                    writer.write(query);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    conn.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InputStream in = null;
                try {
                    in = conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String encoding = conn.getContentEncoding();
                encoding = encoding == null ? "UTF-8" : encoding;
                String body = null;
                try {
                    body = IOUtils.toString(in, encoding);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if(!body.contains("Bienvenido")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Acceso: Usuario/Contraseña Incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                            System.out.println(cookieManager.getCookieStore().getCookies().get(0).getValue());
                        }
                    });
                }

                //Log.d("Tag",body);
            }
        }).start();
    }
}
