package com.example.admin.hibernateisaac;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.admin.hibernateisaac.gestion.GestorHibernate;
import com.example.admin.hibernateisaac.pojo.Keep;
import com.example.admin.hibernateisaac.pojo.Usuario;

import java.net.MalformedURLException;
import java.util.List;

public class Principal extends AppCompatActivity {

    private Usuario user;
    private List<Keep> listaKeeps;
    private Adaptador ad;
    private GestorHibernate gestorHibernate = new GestorHibernate();


    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        lv = (ListView) findViewById(R.id.listView);
        user = getIntent().getParcelableExtra("usuario");
    }

    @Override
    protected void onResume() {
        new Threadd().execute();

        super.onResume();
    }


    public void addKeep(View v) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_keep, null);
        final EditText et = (EditText) view.findViewById(R.id.etAdd);
        adb.setView(view)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Keep k= new Keep(gestorHibernate.getId(listaKeeps), et.getText().toString(), true);
                        listaKeeps.add(k);
                        ad.notifyDataSetChanged();
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    gestorHibernate.create(k, user);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        Thread t = new Thread(r);
                        t.start();

                        ad.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancelar", null).show();
    }

    private class Threadd extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                listaKeeps = gestorHibernate.getKeeps(user);
            } catch (MalformedURLException e) {
                Log.v("estado4",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Void) {
            super.onPostExecute(Void);
            ad =new Adaptador(getBaseContext(),R.layout.item,listaKeeps);
            lv.setAdapter(ad);
        }
    }
}
