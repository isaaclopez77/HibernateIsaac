/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion;

import hibernate.Keep;
import hibernate.Usuario;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author USER
 */
public class GestorKeep {
    
    public static JSONObject addKeep(Keep k, String usuario){
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        
        Usuario u = (Usuario)sesion.get(Usuario.class, usuario);
        k.setUsuario(u);
        sesion.save(k);
        
        Long id = ((BigInteger) sesion.createSQLQuery
            ("select last_insert_id()").uniqueResult())
            .longValue();       
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        
        JSONObject obj = new JSONObject();
        obj.put("r", id);
        return obj;
    }
    
    public static JSONObject getKeeps(String usuario){
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        
        String hql = "from Keep where login = :login";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        List<Keep> keeps = q.list();
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        //{"r": true}
        //{"r": false}
        
        JSONArray array= new JSONArray();
        for(Keep k: keeps){
            JSONObject obj = new JSONObject();
            obj.put("idan", k.getIdAndroid());
            obj.put("cont", k.getContenido());
            obj.put("est", k.getEstado());
            array.put(obj);
        }
        
        JSONObject obj2 = new JSONObject();
        obj2.put("r", array);
        return obj2;
    }
    
    public static void addKeep(Keep k){
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        sesion.save(k);
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
    }
    
    
    public static List<Keep> listKeeps(String usuario){
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        
        SessionFactory factory = configuration.buildSessionFactory(builder.build());
        Session sesion = factory.openSession();
        sesion.beginTransaction();
        
        String hql = "from Keep where login = :login";
        Query q = sesion.createQuery(hql);
        q.setString("login", usuario);
        List<Keep> keeps = q.list();
        
        sesion.getTransaction().commit();
        sesion.flush();
        sesion.close();
        
        return keeps;
    }
}
