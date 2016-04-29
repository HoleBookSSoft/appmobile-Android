package com.servissoft.holebook;

/**
 * Created by raulandrez on 21/09/15.
 */

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.servissoft.holebook.DB.CustomListAdapterPunto;
import com.servissoft.holebook.DB.DbManagerPunto;
import com.servissoft.holebook.entidades.Punto;
import com.servissoft.holebook.libs.Httppostaux;
import com.servissoft.holebook.utils.GpsHoleBook;
import com.servissoft.holebook.utils.DatosHoleBook;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity {


    private CustomListAdapterPunto adapter;
    private Button boton,boton2,boton3,boton4;
    private ListView lista;
    TextView textX, etiqueta;
    SensorManager sensorManager;
    Sensor sensor;
    Punto p;
    Random randomno;
    GpsHoleBook gps;
    DatosHoleBook estadisticas;
    double x, y, z, lastx, lasty, lastz,g,promedio;
    EditText edtiddesv,edtidfact;
    boolean cambio;
    Httppostaux post;
    double limitdes,factor;
    JSONArray puntosJSON = null;
    ToneGenerator audio;
    boolean estado = false;
    int hueco,huecoes, i;
    List<Punto> puntosList;
    String URL_connect = "";
    private DbManagerPunto managerPunto;
    private ProgressDialog pDialog;


    SensorEventListener accelListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            cambio = false;
            cambio = true;
            if ((x >= 0 && lastx <= 0) || (x <= 0 && lastx >= 0)) {
                //cambio y
                cambio = true;
                lastx = x;
            } else {
                if ((x < 0 && x < lastx) || (x > 0 && x > lastx)) {
                    lastx = x;
                }
            }
            if ((y >= 0 && lasty <= 0) || (y <= 0 && lasty >= 0)) {
                //cambio y
                cambio = true;
                lasty = y;
            } else {
                if ((y < 0 && y < lasty) || (y > 0 && y > lasty)) {
                    lasty = y;
                }
            }
            if ((z >= 0 && lastz <= 0) || (z <= 0 && lastz >= 0)) {
                //cambio z
                cambio = true;
                lastz = z;
            } else {
                if ((z < 0 && z < lastz) || (z > 0 && z > lastz)) {
                    lastz = z;
                }
            }
            if (cambio ) {

                lastx = x;
                lasty = y;
                lastz = z;
                g=Math.sqrt((double) (lastx*lastx) + (lasty*lasty) + (lastz*lastz));
                estadisticas.setDato(g, i);
                promedio=estadisticas.getPromedio();

                p = new Punto();
                // p.setAx(lastx);
                p.setAx(g);

               // p.setAy(lasty);
                p.setAy(promedio);

                //p.setAz(lastz);
                p.setAz(gps.velocidad*3.6);

                gps.setTextView();
                p.setX(gps.longitud);
                p.setY(gps.latitud);
                p.setT(new Date());
                p.setHueco(hueco+huecoes);
                g=Math.sqrt((double) (lastx*lastx) + (lasty*lasty) + (lastz*lastz));
                estadisticas.setDato(g, i);
                promedio=estadisticas.getPromedio();


                i++;
                if(promedio>=(limitdes+( factor*(gps.velocidad*0.36))) && ((double)(gps.velocidad*3.6)>10)){
                    audio.startTone(5);
                    huecoes=20;
                }
                else{
                    audio.stopTone();
                    huecoes=10;
                }


                textX.setText("\n Vel Km/h"+((double)(gps.velocidad*3.6))+ "\n pa("+i+"): " + (promedio) + "\n gps.x : " + gps.latitud + "\n gps.y : " + gps.longitud);

               // textX.setText("X : " + lastx + "\n Y : " + lasty + "\n Z1 : " + lastz + "\n G: " + g + "\n gps.x : " + gps.latitud + "\n gps.y : " + gps.longitud);
                //ingresarPunto(lastx,lasty,lastz,gps.latitud,gps.longitud,new Date());
                //ingresarPuntoEnLocal();
                if (estado) {
                    managerPunto.insertar(p);

                }
              // new tareaTraerPuntosDeBDLocal().execute();
            }

            etiqueta.setText("db count "+managerPunto.contarTodos()+"("+managerPunto.contarHuecos()+")");

        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boton = (Button) findViewById(R.id.button);
        boton2 = (Button) findViewById(R.id.button2);
        boton4 = (Button) findViewById(R.id.btdes);

        boton3 = (Button) findViewById(R.id.button3);
        edtiddesv = (EditText)findViewById(R.id.editTextdesv);
        edtidfact = (EditText)findViewById(R.id.editTextfact);
        hueco=0;
        i=0;
        huecoes=10;
        limitdes= Double.parseDouble(edtiddesv.getText().toString());
        factor=Double.parseDouble(edtidfact.getText().toString());
        post = new Httppostaux();
        managerPunto = new DbManagerPunto(this);
        estadisticas=new DatosHoleBook(0,5);
        randomno = new Random();
        managerPunto.vaciarTabla();
        audio = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        sensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textX = (TextView) findViewById(R.id.tv_acel);

        etiqueta = (TextView) findViewById(R.id.tv_gps);

        gps = new GpsHoleBook(getApplicationContext(), etiqueta);
        gps.getLocation();
        gps.setTextView();
        x = y = z = lastx = lasty = lastz = 0;

        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (estado){
                    estado=false;
                    boton.setText("Iniciar");
                }
                else{
                    estado=true;
                    boton.setText("Parar");
                }
              // ingresarPuntoEnLocal();
              //  new tareaTraerPuntosDeBDLocal().execute();
              // managerPunto.vaciarTabla();
            }
        });
        boton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                etiqueta.setText("db count "+managerPunto.contarTodos());
                // ingresarPuntoEnLocal();
                new tareaTraerPuntosDeBDLocal().execute();
                // managerPunto.vaciarTabla();
            }
        });
        boton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                limitdes= Double.parseDouble(edtiddesv.getText().toString());
                factor=Double.parseDouble(edtidfact.getText().toString());
            }
        });
        etiqueta.setText("db count "+managerPunto.contarTodos());
        new tareaTraerPuntosDeBDLocal().execute();

        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        lock.disableKeyguard();
        boton3.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hueco = 1;
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    hueco = 0;
                }
                return false;

            }
        });

    }

    public void ingresarPuntoEnLocal() {
        p = new Punto();
        p.setAx(lastx);
        p.setAy(lasty);
        p.setAz(lastz);
        gps.setTextView();
        p.setX(gps.latitud);
        p.setY(gps.longitud);
        p.setHueco(hueco);
        p.setT(new Date());
        ///textX.setText("X : " + x + "\n Y : " + y + "\n Z1 : " + z + "\n gps.x : " + gps.latitud + "\n gps.y : "+gps.longitud);

        managerPunto.insertar(p);
       // managerPunto.vaciarTabla();


    }


    public void onResume() {
        super.onResume();
        sensorManager.registerListener(accelListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(accelListener);
    }

    public boolean ingresarPunto(Double ax, Double ay, Double az, Double x, Double y, int hueco, Date t) {
        int resultado = -1;

        ArrayList<NameValuePair> parametrosaenviar = new ArrayList<NameValuePair>();
        parametrosaenviar.add(new BasicNameValuePair("ax", "" + ax));
        parametrosaenviar.add(new BasicNameValuePair("ay", "" + ay));
        parametrosaenviar.add(new BasicNameValuePair("az", "" + az));
        parametrosaenviar.add(new BasicNameValuePair("x", "" + x));
        parametrosaenviar.add(new BasicNameValuePair("y", "" + y));
        parametrosaenviar.add(new BasicNameValuePair("hueco", "" + hueco));
        parametrosaenviar.add(new BasicNameValuePair("t", getDateTime(t)));

        URL_connect ="http://181.48.179.100:8150/ws/addpoint.php";
        JSONArray jdata = post.getserverdata(parametrosaenviar, URL_connect);
        SystemClock.sleep(150);

        if (jdata != null && jdata.length() > 0) {
            JSONObject json_data;
            try {
                json_data = jdata.getJSONObject(0);
                resultado = json_data.getInt("resultado");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (resultado == 0) {//
                Log.d("Mensaje: ", "Error en el Exitoso");
                return false;
            } else {//
                Log.d("Mensaje: ", "Ingreso Exitoso "+hueco);
                new tareaTraerPuntosDeBDLocal().execute();
                return true;
            }

        } else {
            Log.d("JSON  ", "ERROR....");
            return false;
        }

    }



    /**
     * Async task class to get json by making HTTP call
     */
    private class tareaTraerPuntosDeBDLocal extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Espere por favor...");
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                puntosList = new ArrayList<Punto>();
                puntosList = managerPunto.cargarLista();
                adapter = new CustomListAdapterPunto(MainActivity.this, puntosList);


                for (Punto x : puntosList) {
                    try{
                        if (ingresarPunto(x.getAx(), x.getAy(), x.getAz(), x.getX(), x.getY(),x.getHueco(), x.getT())){
                            Log.d("managerPunto  ", "ID "+ x.getPuntoId());
                            managerPunto.eliminar(x.getPuntoId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                // managerPunto.vaciarTabla();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
/*            if (pDialog.isShowing())
                pDialog.dismiss();
            managerPunto.vaciarTabla();
            lista.setAdapter(adapter);*/


        }

    }

    private class tareaEnviarPuntoAlWS extends AsyncTask<String, String, String> {

        Double ax;
        Double ay;
        Double az;
        Double x;
        Double y;
        int hueco;
        Date t;

        protected void onPreExecute() {
            pDialog = new ProgressDialog(getApplication());
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            ax = Double.parseDouble(params[0]);
            ay = Double.parseDouble(params[1]);
            az = Double.parseDouble(params[2]);
            x = Double.parseDouble(params[3]);
            y = Double.parseDouble(params[4]);
            hueco = Integer.parseInt(params[5]);
            t = pasarADate(params[6]);

            if (ingresarPunto(ax, ay, az, x, y, hueco ,t)) {
                return "ok";
            } else {
                return "err";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            Log.e("onPostExecute=", "" + result);
            if (result.equals("ok")) {
            }
        }

    }

    public Date pasarADate(String myTimestamp) {

        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = form.parse(myTimestamp);
        } catch (Exception e) {
            e.printStackTrace();
            //date = new Date();
        }
        return date;
    }

    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}