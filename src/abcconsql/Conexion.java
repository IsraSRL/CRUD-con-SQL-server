/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abcconsql;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author kevig
 */
public class Conexion {
    
    static Connection conectar = null;
    
    static String usuario = "sa";
    static String contraseña = "1234"; 
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=PRUEBA;encrypt=true;trustServerCertificate=true;";
    
    public static Connection ConectarBD(){
        
        try{
            conectar = DriverManager.getConnection(URL, usuario, contraseña);
            //si se conecta pero, como se llama esta función para poder hacer los elementos de ABC
            //el mensaje aparece cuando haces cualquier acción
            //si crees que no esta conectado/ hay algun error con la BD, quitale el comentario
            //System.out.println("Se conecto correctamente con la base de datos");
            
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se pudo conectar con la base de datos \n Error:" + e.toString());
        }
        
        return conectar;
    }
    
}
