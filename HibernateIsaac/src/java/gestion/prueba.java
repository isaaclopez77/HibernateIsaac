/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion;

import hibernate.Keep;

/**
 *
 * @author USER
 */
public class prueba {
    public static void main(String[] args) {
        Keep k = new Keep(null, 654, "nota 3", null, "inestable");
        GestorKeep.addKeep(k, "pepe");
        
    }
    
    
}
