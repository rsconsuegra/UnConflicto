package org.underpressureapps.unconflicto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

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
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:42.0) Gecko/20100101 Firefox/42.0";

    class ScheduleSubject
    {
        public String Name;
        public String Professors;
        public String NRC;
        public ArrayList<Block> Blocks;
    }

    class Block
    {
        public String Day;
        public String StartHour;
        public String Duration;
        public String StartDate;
        public String EndDate;
        public String Location;
    }

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
                    conn.setRequestProperty("User-Agent", USER_AGENT);
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

                OutputStream os;
                BufferedWriter writer;
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
                            //System.out.println("Cookies are:"+cookieManager.getCookieStore().getCookies().toString());

                        }
                    });
                    try {
                        sendGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    StringBuilder schedule=null;

                    try {
                        schedule= sendPost();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                    String var= schedule.toString();
                    i.putExtra("Horario ",var);
                    startActivity(i);
                }

                //Log.d("Tag",body);

            }
        }).start();
    }

    private void sendGet() throws Exception {

        String url = "https://pomelo.uninorte.edu.co/pls/prod/bwskfshd.P_CrseSchdDetl";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    private StringBuilder sendPost() throws Exception {

        String url = "https://pomelo.uninorte.edu.co/pls/prod/bwskfshd.P_CrseSchdDetl";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Accept-Encoding", "gzip, deflate");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestMethod("POST");


        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("term_in", "201630");
        String query = builder.build().getEncodedQuery();


        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(query);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;

        StringBuilder response = new StringBuilder();
        boolean sw=false;

        String word;

        String[] options={"Hora: ","Días: ","Dónde: ","Rango de Fecha: "};
        int i=0;
        while ((inputLine = in.readLine()) != null) {

            if (inputLine.toString().contains("Clase regular")){
                sw = true;
                continue;
            }

            if(inputLine.contains("detalle de horario de curso")){
                word=inputLine.substring(inputLine.lastIndexOf("\">")+2,inputLine.lastIndexOf("<"));
                response.append("CURSO: "+word);
                System.out.println("CURSO: "+word);
            }

            if(sw){
                if(inputLine.contains("Teoría") || inputLine.contains("Conferencia") || inputLine.contains("Práctica Dirigida")|| inputLine.contains("Laboratorio") /*inputLine.contains("</TR>")*/){
                    sw=false;
                    i=0;
                    continue;
                }
                word=inputLine.substring(inputLine.indexOf(">")+1,inputLine.lastIndexOf("<")).replace("&nbsp;","D");
                response.append(options[i]+word);
                System.out.println(options[i]+word);
                i++;
            }


            /*response.append(inputLine);
            System.out.println(inputLine);*/
        }
        in.close();

        //print result
        //System.out.println(response.toString());
        return response;
    }
}
