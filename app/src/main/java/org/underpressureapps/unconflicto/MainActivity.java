package org.underpressureapps.unconflicto;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
UnConflicto is an app to create schedule matrix for students of UniNorte. (And teachers I hope).
Create by: Randy Consuegra (randyc@uninorte.edu.co);Carlos Diaz(caberrio@uninorte.edu.co)
Fall 2016 (Colombian's Second Semester)
*/
public class MainActivity extends AppCompatActivity {
    //We're using ButterKnife library to avoid findviewbyid

    @BindView(R.id.edusuario) EditText usuario;
    @BindView(R.id.edcontraseña) EditText pass;
    public static String TAG = "AppFirebase";
    public static final String LOGIN_URL = "https://pomelo.uninorte.edu.co/pls/prod/twbkwbis.P_ValLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    CookieManager cookieManager = new CookieManager();

    public void onClickIniciar(View view) {
        /*usuario   = (EditText)findViewById(R.id.edusuario);
        pass      = (EditText)findViewById(R.id.edcontraseña);*/
        final MainActivity context =  this;

        //Uri base_addres =Uri.parse("https://pomelo.uninorte.edu.co");

        //Http login
        new Thread(new Runnable() {
            public void run() {


                URL url = null;
                CookieHandler.setDefault(cookieManager);


                String user     =usuario.getText().toString();
                String password =pass.getText().toString();
                user="randyc";
                password="270295randy";
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

                //Protocols to get an answer from server.
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

                //User Login
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("sid", user)
                        .appendQueryParameter("PIN", password);
                String query = builder.build().getEncodedQuery();
                //System.out.println(query);

                OutputStream os = null;
                BufferedWriter writer = null;
                InputStream in = null;


                try {
                    os = conn.getOutputStream();
                    writer = new BufferedWriter(
                            new OutputStreamWriter(os));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();
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


                if(body == null  || body.isEmpty()|| !body.contains("Bienvenido")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Acceso: Usuario/Contraseña Incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //Get Student's name
                    final String name =body.substring(body.indexOf("Bienvenido"),body.indexOf("Bienvenido")+40);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Bienvenido"+name.substring(name.indexOf(",")+1,name.lastIndexOf(",")).replace("+"," "), Toast.LENGTH_SHORT).show();
                            System.out.println(cookieManager.getCookieStore().getCookies().get(0).getValue());
                        }
                    });
                }
                //Log.d("Tag",body);
            }
        }).start();
    }
}
