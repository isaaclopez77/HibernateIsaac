package com.example.admin.hibernateisaac.gestion;

import android.util.Log;

import com.example.admin.hibernateisaac.pojo.Keep;
import com.example.admin.hibernateisaac.pojo.Usuario;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 02/03/2016.
 */
public class GestorHibernate {


    private String url = "http://192.168.1.35:8080/HibernateIsaac/go";

    public GestorHibernate() {}

    public List<Keep> getKeeps(Usuario u) throws MalformedURLException {
        List<Keep> keeps = new ArrayList<>();
        URL url = new URL("http://192.168.1.35:8080/HibernateIsaac/go");
        BufferedReader in = null;
        String res = "";
        String login;
        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            String destino = url + "?tabla=keep&op=read&login=" + login + "&origen=android";

            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;

            while ((linea = in.readLine()) != null) {
                res += linea;
            }
            in.close();

            JSONObject obj = new JSONObject(res);
            JSONArray array = (JSONArray) obj.get("r");

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = (JSONObject) array.get(i);

                Keep keep = new Keep(o.getInt("idan"), o.getString("cont"), true);
                keeps.add(keep);
            }
            return keeps;
        } catch (Exception e) {
            Log.v("estado3",e.toString());
        }
        return null;
    }

    public long getId(List<Keep> l) {
        long next = -1;
        for (Keep k : l) {
            if (k.getId() > next) {
                next = k.getId();
            }
        }
        return next+1;
    }

    public void create (Keep k,Usuario u) throws MalformedURLException {
        URL url = new URL("http://192.168.1.35:8080/HibernateIsaac/go");
        BufferedReader in = null;
        String res = "";
        String login;
        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            String destino = url+"?tabla=keep&op=create&origen=android&login="+login+"&idAndroid="+k.getId()+"&contenido="+k.getContenido();
            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));

        } catch (MalformedURLException e) {
            Log.v("estad2o",e.toString());
        } catch (IOException e) {
            Log.v("estado",e.toString());
        }
    }



    public boolean validar(Usuario u) throws MalformedURLException {
        URL url = new URL("http://192.168.1.35:8080/HibernateIsaac/go");
        BufferedReader in = null;
        String res = "";
        String login;
        String pass;
        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            pass = URLEncoder.encode(u.getPass(), "UTF-8");

            String destino = url +"?tabla=usuario&op=login&origen=android&login="+login+"&pass="+pass+"&accion=";
            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;

            while ((linea = in.readLine()) != null) {
                res += linea;
            }

            in.close();
            JSONObject obj = new JSONObject(res);

            return obj.getBoolean("r");
        } catch (Exception e) {
            Log.v("estado1",e.toString());
        }
        return false;
    }
}
