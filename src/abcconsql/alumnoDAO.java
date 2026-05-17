/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abcconsql;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Statement;
/**
 *
 * @author kevig
 */
public class alumnoDAO {
    //metodo para insertar
    public void insertarAlumno(String nombre, String correo, String secreto) throws Exception {

        String sql = "{CALL insertar(?,?,?)}";
        String cerrarLlave = "close symmetric key llave1";

        Connection con = null;
        CallableStatement cs = null;
        Statement st = null;

        try {
            con = Conexion.ConectarBD();

            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            st = con.createStatement();
            st.execute("SET LOCK_TIMEOUT 5000");

            cs = con.prepareCall(sql);

            cs.setString(1, nombre);
            cs.setString(2, correo);
            cs.setString(3, secreto);

            cs.execute();

            con.commit();

        } catch (Exception ex) {

            if (con != null) {
                con.rollback();
            }

            throw ex;

        } finally {

            if (cs != null) cs.close();
            if (st != null) st.close();

            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }
    //metodo para actualizar
    public void actualizarAlumno(int id, String nombre, String correo, String secreto) throws Exception 
    {

        Connection con = null;
        CallableStatement cs = null;
        Statement st = null;

        try {
            con = Conexion.ConectarBD();

            // control de transacción
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            // configuración adicional (opcional pero recomendable)
            st = con.createStatement();
            st.execute("SET LOCK_TIMEOUT 5000");

            cs = con.prepareCall("{call actualizar(?, ?, ?, ?)}");

            cs.setInt(1, id);
            cs.setString(2, nombre);
            cs.setString(3, correo);
            cs.setString(4, secreto);

            cs.execute();

            con.commit();

        } catch (Exception ex) {

            if (con != null) {
                con.rollback();
            }

            throw ex;

        } finally {

            if (cs != null) cs.close();
            if (st != null) st.close();

            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }
    //metodo para eliminar de la BD
    public void eliminarAlumno(int id) throws Exception 
    {

        Connection con = null;
        CallableStatement cs = null;
        Statement st = null;

        try {
            con = Conexion.ConectarBD();

            // manejo de transacción
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            st = con.createStatement();
            st.execute("SET LOCK_TIMEOUT 5000");

            cs = con.prepareCall("{call eliminar(?)}");

            cs.setInt(1, id);

            cs.execute();

            con.commit();

        } catch (Exception ex) {

            if (con != null) {
                con.rollback();
            }

            throw ex;

        } finally {

            if (cs != null) cs.close();
            if (st != null) st.close();

            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }
}
