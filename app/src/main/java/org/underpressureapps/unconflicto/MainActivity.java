package org.underpressureapps.unconflicto;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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


    private DatabaseReference database =FirebaseDatabase.getInstance().getReference();

// ...
    FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
    public boolean userexistsw=false;

    @BindView(R.id.edusuario) EditText usuario;
    @BindView(R.id.edcontraseña) EditText pass;
    public static String TAG = "AppFirebase";
    public static final String LOGIN_URL = "https://pomelo.uninorte.edu.co/pls/prod/twbkwbis.P_ValLogin";
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:42.0) Gecko/20100101 Firefox/42.0";

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

                boolean onLine = hasActiveInternetConnection(MainActivity.this.getApplicationContext());

                if (onLine==false){
                    return;
                }

                URL url = null;
                CookieHandler.setDefault(cookieManager);

                String user = usuario.getText().toString();
                String password = pass.getText().toString();
                user="randyc";
                password="270295randy";
                //Save cookies

                // Here goes de first POST to do a login in POMELO (Aurora)
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
                //

                conn.setDoInput(true);
                conn.setDoOutput(true);

                //User Login
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("sid", user)
                        .appendQueryParameter("PIN", password);
                String query = builder.build().getEncodedQuery();

                //Connection
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
                //Conn ends.

                try {
                    System.out.println(conn.getResponseCode());
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
                //Here Ends POST

                if(body == null  || body.isEmpty()|| !body.contains("Bienvenido")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Acceso: Usuario/Contraseña Incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //Get Student's name
                    final String name =body.substring(body.indexOf("Bienvenido"),body.indexOf("Bienvenido")+140);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Bienvenido"+name.substring(name.indexOf(",")+1,name.lastIndexOf(",")).replace("+"," "), Toast.LENGTH_SHORT).show();
                        }
                    });
                    String codigo="";
                    try {
                        codigo=sendGet(name.substring(name.indexOf(",")+1,name.lastIndexOf(",")).replace("+"," "));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(codigo);

                    //DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                    final String finalCodigo = codigo;
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //System.out.println("-------------IT'S ALIVE-------------.");
                            if (dataSnapshot.child("users").child(finalCodigo).getValue() != null) {
                                System.out.println("--------El Codigo del estudiante ya se encuentra en la Base de Datos--------");
                                userexistsw=true;
                            }else{
                                System.out.println("---HELLO---");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(userexistsw==false)
                        parseSchedule(codigo, name);

                    Intent i = new Intent(MainActivity.this,RecyclerActivity.class);
                    i.putExtra("Codigo",codigo);
                    startActivity(i);
                }

            }
        }).start();
    }

    private String sendGet(String name) throws Exception {

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

        String codigo="";
        while ((inputLine = in.readLine()) != null) {

            if(inputLine.contains(name)){
                codigo=inputLine.substring(inputLine.indexOf("\">2")+1,inputLine.lastIndexOf(name));
                continue;
            }
            response.append(inputLine);
        }
        in.close();

        //print result
        //System.out.println(response.toString());

        return codigo;
    }

    private Schedule sendPost() throws Exception {

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
        boolean sw = false;

        String word;

        Block bloque = new Block();
        List<Block> bloques = new ArrayList<Block>();

        String[] options={"Hora: ","Días: ","Dónde: ","Rango de Fecha: "};
        int i=0;

        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("Clase regular")){
                sw = true;
                continue;
            }

            if(inputLine.contains("detalle de horario de curso")){
                word=inputLine.substring(inputLine.lastIndexOf("\">")+2,inputLine.lastIndexOf("<"));
                response.append("CURSO: "+word);
                bloque.setCourseName(word);
                //System.out.println("CURSO: "+word);
                continue;
            }

            if(sw){
               if(inputLine.contains("Teoría") || inputLine.contains("Conferencia") || inputLine.contains("Práctica Dirigida")|| inputLine.contains("Laboratorio") /*inputLine.contains("</TR>")*/){
                   sw=false;
                    i=0;
                    continue;
                }
                word=inputLine.substring(inputLine.indexOf(">")+1,inputLine.lastIndexOf("<")).replace("&nbsp;","D");
                response.append(options[i]+word);
                //System.out.println(options[i]+word);

                switch (i){
                    case 0:
                        String[] hora = word.split(" - ");
                        bloque.setStartHour(hora[0]);
                        bloque.setEndHour(hora[1]);
                        break;
                    case 1:
                        bloque.setDay(word);
                        if(bloque.getCourseName() == null)
                            bloque.setCourseName(bloques.get(bloques.size()-1).getCourseName());
                        bloques.add(bloque);
                        bloque= new Block();
                        break;
                }
                i++;
            }

            /*response.append(inputLine);
            System.out.println(inputLine);*/
        }
        in.close();
        Schedule schedulelist = new Schedule(bloques);
        //print result
        //System.out.println(response.toString());

        return schedulelist;
    }

    // isNetworkAvailabe and hasActiveInternetConnection checks internet if there's an internet connection available and stable.

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public  boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                /*http://stackoverflow.com/questions/10242351/display-an-alert-when-internet-connection-not-available-in-android-application*/
                runOnUiThread(new Runnable() {
                    public void run() {
                Toast.makeText(MainActivity.this, "Error checking internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {

            runOnUiThread(new Runnable() {
                public void run() {
            Toast.makeText(MainActivity.this, "No network available!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return false;
    }


    public synchronized void parseSchedule (String codigo, String name){
        System.out.println("-******** Holi ********-");
        //Here we add student's schedule to Firebase, in case he wasn't.
        DatabaseReference userRef = databaseInstance.getReference("users/" + codigo + "");
        DatabaseReference scheduleRef = databaseInstance.getReference("schedules/" + codigo + "");

        userRef.child("uni_code").setValue(codigo);
        scheduleRef.child("owner").setValue(codigo);
        userRef.child("name").setValue(name.substring(name.indexOf(",") + 1, name.lastIndexOf(",")).replace("+", " "));
        //DatabaseReference mesajeRef = database.child(codigo);

        Schedule schedule = null;

        //Here we convert Schedule object into JsonObejct
        try {
            schedule = sendPost();

            //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(schedule);
            System.out.println(json);

            JSONObject obj = null;
            try {
                obj = new JSONObject(json);
                System.out.println("My App" + obj.toString());
            } catch (Throwable t) {
                System.out.println("My App" + "Could not parse malformed JSON: \"" + json + "\"");
            }

            //jsonToMap translate JsonObject into a map that allow us to upload it to Firebase.
            Map<String, Object> map = jsonToMap(obj);
            userRef.child("schedule").setValue(map);
            scheduleRef.child("schedule").setValue(map);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Convert object to JSON and JSON to map

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}