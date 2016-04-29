package com.servissoft.holebook.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.servissoft.holebook.entidades.Punto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;




/**
 * Created by iproject on 26/02/15.
 */
public class DbManagerPunto {

    public static final String TABLE_NAME = "punto";


    public static final String CN_PUNTOID = "_id";
    public static final String CN_RUTAID = "ruta_id";
    public static final String CN_X = "x";
    public static final String CN_Y = "y";
    public static final String CN_T = "t";
    public static final String CN_AX = "ax";
    public static final String CN_AY = "ay";
    public static final String CN_AZ = "az";
    public static final String hueco = "hueco";
    public static final String CN_FECHAREGISTRO = "fecha_registro";


    public static final String CREATE_TABLE = " create table " + TABLE_NAME + " ("
            + CN_PUNTOID + " integer primary key autoincrement, "
            + CN_RUTAID + " text, "
            + CN_X + " text, "
            + CN_Y + " text, "
            + CN_T + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + CN_AX + " text, "
            + CN_AY + " text, "
            + CN_AZ + " text, "
            + hueco + " text, "
            + CN_FECHAREGISTRO + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    public static final String DROP_QUERY = "drop table " + TABLE_NAME;
    private DbHelper helper;
    private SQLiteDatabase db;

    public DbManagerPunto(Context context) {

        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
       // db.execSQL(DROP_QUERY);
       // db.execSQL(CREATE_TABLE);

    }

    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private ContentValues generarContentValues(Punto punto) {
        ContentValues valores = new ContentValues();
        valores.put(CN_RUTAID, punto.getPuntoId());
        valores.put(CN_X, punto.getX());
        valores.put(CN_Y, punto.getY());
        if(punto.getT()!=null) {
            valores.put(CN_T, getDateTime(punto.getT()));
        }
        valores.put(CN_AX, punto.getAx());
        valores.put(CN_AY, punto.getAy());
        valores.put(CN_AZ, punto.getAz());
        valores.put(hueco, punto.getHueco());
        if(punto.getFechaRegistro()!=null) {
            valores.put(CN_FECHAREGISTRO, getDateTime(punto.getFechaRegistro()));
        }
        return valores;
    }

    public void insertar(Punto punto) {
        db.insert(TABLE_NAME, null, generarContentValues(punto));
    }

    public void insertarFormaNativa(Punto punto) {
        db.execSQL("insert into " + TABLE_NAME + " values(null, '" + punto.getX() + "', '" + punto.getY() + "'," + punto.getT() + ") ");
    }

    public void eliminar(Integer idAgCultivo) {
        db.delete(TABLE_NAME, CN_PUNTOID + "=?", new String[]{idAgCultivo.toString()});
    }

    public void eliminarMultiples(String[] idsAgCultivo) {
        db.delete(TABLE_NAME, CN_PUNTOID + " IN(?,?)", idsAgCultivo); //new String[]{idAgCultivo.toString(), idAgCultivo2.toString()}
    }

    public void modificar(Punto punto) {
        db.update(TABLE_NAME, generarContentValues(punto), CN_PUNTOID + "=?", new String[]{punto.getPuntoId().toString()});
    }

    public Cursor cargarTodos() {
        String[] columnas = new String[]{CN_PUNTOID + " as _id", CN_RUTAID,CN_X,CN_Y,CN_T, CN_AX,CN_AY,CN_AZ,hueco,CN_FECHAREGISTRO};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);
    }

    public Cursor buscarPorNombre(String nombre) {
        String[] columnas = new String[]{CN_PUNTOID + " as _id", CN_RUTAID,CN_X,CN_Y,CN_T, CN_AX,CN_AY,CN_AZ,hueco,CN_FECHAREGISTRO};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);
    }
    public int contarTodos() {
        Cursor mCount = db.rawQuery("select count(*) as total from "+TABLE_NAME, null);
        mCount.moveToFirst();
        return mCount.getInt(0);
    }
    public int contarHuecos() {
        Cursor mCount = db.rawQuery("select count(*) as total from "+TABLE_NAME+" WHERE hueco>=20", null);
        mCount.moveToFirst();
        return mCount.getInt(0);
    }


    public List<Punto> cargarLista() {
        List<Punto> aux = new ArrayList<Punto>();
        String[] columnas = new String[]{CN_PUNTOID + "", CN_RUTAID,CN_X,CN_Y,CN_T, CN_AX,CN_AY,CN_AZ,hueco,CN_FECHAREGISTRO};
        Cursor cur = db.query(TABLE_NAME, columnas,null, null, null, null, null);
        if (cur.moveToFirst() == false) {
            //el cursor está vacío
        } else {
            int puntoIdColumn = cur.getColumnIndex(CN_PUNTOID + "");
            int rutaIdColumn = cur.getColumnIndex(CN_RUTAID);
            int xColumn = cur.getColumnIndex(CN_X);
            int yColumn = cur.getColumnIndex(CN_Y);
            int tColumn = cur.getColumnIndex(CN_T);
            int axColumn = cur.getColumnIndex(CN_AX);
            int ayColumn = cur.getColumnIndex(CN_AY);
            int azColumn = cur.getColumnIndex(CN_AZ);
            int huecoColumn = cur.getColumnIndex(hueco);
            int fechaRegistroColumn = cur.getColumnIndex(CN_FECHAREGISTRO);
            while (cur.moveToNext()) {
                Punto punto = new Punto();
                punto.setPuntoId(cur.getInt(puntoIdColumn));
                punto.setRutaId(cur.getInt(rutaIdColumn));
                punto.setX(cur.getDouble(xColumn));
                punto.setY(cur.getDouble(yColumn));
                punto.setT(pasarADate(cur.getString(tColumn)));
                punto.setAx(cur.getDouble(axColumn));
                punto.setAy(cur.getDouble(ayColumn));
                punto.setAz(cur.getDouble(azColumn));
                punto.setHueco(cur.getInt(huecoColumn));
                punto.setFechaRegistro(pasarADate(cur.getString(fechaRegistroColumn)));
                aux.add(punto);
            }
        }
        return aux;
    }

    public void vaciarTabla() {
        db.delete(TABLE_NAME, null, null);
       /* String selectQuery = "DELETE FROM " + TABLE_NAME + " ";
        Cursor cursor = db.rawQuery(selectQuery, null);*/
    }

    public Date pasarADate(String myTimestamp) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = form.parse(myTimestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
