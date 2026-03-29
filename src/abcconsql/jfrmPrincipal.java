/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package abcconsql;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kevig
 */
public class jfrmPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form jfrmPrincipal
     */
    
    
    public jfrmPrincipal() {
        initComponents();
        //llamo a cargar desde un inicio para ver si si se conecta con la base de datos
        //y no descubrir si se conecto o no, cuando mandes a llamar algun metodo del ABC
        AutoActualizar();
    }
    
    public void Cargar()
    {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Correo");
        modelo.addColumn("Secretamente Odia");

        jtbtDatos.setModel(modelo);
        
        //select sin desencriptar
        //String sql = "SELECT * FROM alumnos";
        
        //select que si desencripta
        String sql = 
                "BEGIN " +
                "OPEN SYMMETRIC KEY llave1 " +
                "DECRYPTION BY CERTIFICATE certificado1 " +
                
                "SELECT id, Nombre, Correo, " +
                "Convert(NVARCHAR, decryptByKey(SecretoEncriptado)) as Secreto " +
                "FROM alumnos " +
                
                "CLOSE SYMMETRIC KEY llave1 " +
                "END";
        
        try (Connection con = Conexion.ConectarBD();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql))
        {
            
            while(rs.next())
            {
                Object [] tupla = new Object [4];
                tupla[0] = rs.getInt("id");
                tupla[1] = rs.getString("Nombre");
                tupla[2] = rs.getString("Correo");
                tupla[3] = rs.getString("Secreto");
                modelo.addRow(tupla);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    public void LimpiarCampo()
    {
        jtxtNombre.setText("");
        jtxtCorreo.setText("");
        jtxtSecreto.setText("");
    }
    
    
    public void AutoActualizar()
    {
        new Thread(() -> {

            while(true)
            {
                try {

                    Thread.sleep(3000);

                    SwingUtilities.invokeLater(() -> {
                        Cargar();
                    });

                } catch (Exception e) {
                    break;
                }
            }

        }).start();
    }
    
    public void probarDeadlock(int primerId, int segundoId)
    {
        String abrirLlave = "open symmetric key llave1 decryption by certificate certificado1";
        String cerrarLlave = "close symmetric key llave1";

        String update =
                "update alumnos set nombre=? where id=?";

        Connection con = null;
        Statement st = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        try {

            con = Conexion.ConectarBD();

            con.setAutoCommit(false);

            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            st = con.createStatement();

            // timeout 5 segundos
            st.execute("SET LOCK_TIMEOUT 5000");

            st.execute(abrirLlave);

            System.out.println("Bloqueando alumno " + primerId);

            ps1 = con.prepareStatement(update);
            ps1.setString(1, "Usuario " + primerId);
            ps1.setInt(2, primerId);
            ps1.executeUpdate();

            System.out.println("Esperando 5 segundos...");

            Thread.sleep(5000);

            System.out.println("Intentando bloquear alumno " + segundoId);

            ps2 = con.prepareStatement(update);
            ps2.setString(1, "Usuario " + segundoId);
            ps2.setInt(2, segundoId);
            ps2.executeUpdate();

            st.execute(cerrarLlave);

            con.commit();

            JOptionPane.showMessageDialog(this, "Transacción completada");

        }
        catch (Exception ex) {

            try {

                if(st != null)
                    st.execute(cerrarLlave);

                if(con != null)
                    con.rollback();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }

            JOptionPane.showMessageDialog(this, "Deadlock detectado:\n" + ex.getMessage());
        }
        finally {

            try {

                if(ps1 != null) ps1.close();
                if(ps2 != null) ps2.close();
                if(st != null) st.close();

                if(con != null){
                    con.setAutoCommit(true);
                    con.close();
                }

            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, e2.toString());
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbtnAgregar = new javax.swing.JButton();
        jbtnBorrar = new javax.swing.JButton();
        jbtnLimpiar = new javax.swing.JButton();
        jbtnCambiar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtxtNombre = new javax.swing.JTextField();
        jtxtCorreo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbtDatos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtxtSecreto = new javax.swing.JTextField();
        jbtnDeadlock = new javax.swing.JButton();
        jbtnDeadlock1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 255, 255));
        setResizable(false);

        jbtnAgregar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jbtnAgregar.setText("Agregar");
        jbtnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAgregarActionPerformed(evt);
            }
        });

        jbtnBorrar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jbtnBorrar.setText("Borrar");
        jbtnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBorrarActionPerformed(evt);
            }
        });

        jbtnLimpiar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jbtnLimpiar.setText("Limpiar");
        jbtnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnLimpiarActionPerformed(evt);
            }
        });

        jbtnCambiar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jbtnCambiar.setText("Cambiar");
        jbtnCambiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCambiarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Nombre");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Correo");

        jtxtNombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jtxtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNombreKeyTyped(evt);
            }
        });

        jtxtCorreo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("ABC CON SQL");

        jtbtDatos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jtbtDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Correo", "Secreto"
            }
        ));
        jtbtDatos.setShowGrid(true);
        jtbtDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbtDatosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtbtDatos);
        if (jtbtDatos.getColumnModel().getColumnCount() > 0) {
            jtbtDatos.getColumnModel().getColumn(0).setPreferredWidth(10);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Kevin Israel Serrano Lugo #24130793");

        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("Para Borrar/Cambiar, primero selecciona una tupla");

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel6.setText("Ahora con encriptado");

        jLabel7.setText("Secretamente odia:");

        jtxtSecreto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jbtnDeadlock.setBackground(new java.awt.Color(255, 255, 0));
        jbtnDeadlock.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jbtnDeadlock.setText("X");
        jbtnDeadlock.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtnDeadlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeadlockActionPerformed(evt);
            }
        });

        jbtnDeadlock1.setBackground(new java.awt.Color(255, 255, 0));
        jbtnDeadlock1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jbtnDeadlock1.setText("X");
        jbtnDeadlock1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtnDeadlock1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeadlock1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(216, 216, 216)
                        .addComponent(jLabel5)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(41, 41, 41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbtnDeadlock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnDeadlock1)
                        .addGap(52, 52, 52))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(109, 109, 109))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbtnAgregar)
                            .addComponent(jbtnLimpiar)
                            .addComponent(jbtnBorrar, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jbtnCambiar, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(18, 18, 18)
                                    .addComponent(jtxtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jtxtSecreto, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(31, 31, 31))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jbtnDeadlock1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6))
                            .addComponent(jbtnDeadlock))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jtxtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jtxtSecreto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jbtnAgregar)
                            .addComponent(jbtnBorrar))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbtnLimpiar)
                            .addComponent(jbtnCambiar))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNombreKeyTyped
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jtxtNombreKeyTyped

    private void jtbtDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbtDatosMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jtbtDatosMouseClicked

    private void jbtnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnLimpiarActionPerformed
        // TODO add your handling code here:
        //limpiar los datos
        LimpiarCampo();
    }//GEN-LAST:event_jbtnLimpiarActionPerformed

    private void jbtnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBorrarActionPerformed
        // TODO add your handling code here:
        int tupla = jtbtDatos.getSelectedRow();
        
        if (tupla == -1) //si no selecciono nada
        {
            JOptionPane.showMessageDialog(this, "Selecciona un registro");
            return;
        }
        
        int id = Integer.parseInt(jtbtDatos.getValueAt(tupla, 0).toString());
        
        //borrar en sql
        //borrar no se modifico pq no es necesario desencriptar algo que vas a borrar
        String borrar = "DELETE FROM alumnos WHERE id=?";
        
        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        
        try
        {
            con = Conexion.ConectarBD();
            
            //control transaccional
            con.setAutoCommit(false);
            
            //timeout para lo circular
            st = con.createStatement();
            
            st.execute("SET LOCK_TIMEOUT 5000");
            
            //aislamiento
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            
            //borrar
            ps = con.prepareStatement(borrar);
            
            ps.setInt(1, id);
            ps.executeUpdate();
            
            //commit
            con.commit();
            
            JOptionPane.showMessageDialog(this, "Alumno eliminado");
            Cargar();
            LimpiarCampo();
            
        } catch(Exception ex) {

            try {
                if(con != null)//llama un rollback
                    con.rollback();
            } catch(Exception e){
                JOptionPane.showMessageDialog(null, e.toString());
            }

            JOptionPane.showMessageDialog(null, ex.toString());
        }
        finally {//cerrar todo

            try {

                if(ps != null)
                    ps.close();

                if(con != null){
                    con.setAutoCommit(true);
                    con.close();
                }

            } catch(Exception e2){
                JOptionPane.showMessageDialog(null, e2.toString());
            }
        }
    }//GEN-LAST:event_jbtnBorrarActionPerformed

    private void jbtnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAgregarActionPerformed
        // TODO add your handling code here:
        String nombre = jtxtNombre.getText();
        String correo = jtxtCorreo.getText();
        String secreto = jtxtSecreto.getText();
        
        //validar datos
        if(nombre.isEmpty() || correo.isEmpty() || secreto.isEmpty()){
        JOptionPane.showMessageDialog(null, "Campos vacíos");
        return;
        }
        //comandos SQL
        String abrirLlave = "open symmetric key llave1 decryption by certificate certificado1";
        String agregar = 
                "insert into alumnos(Nombre, Correo, SecretoEncriptado) " +
                "values(?,?, EncryptByKey(Key_GUID('llave1'),?)) ";
        String cerrarLlave = "close symmetric key llave1";
        
        
        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        
        try
        {
            con = Conexion.ConectarBD();
            
            //control de transacciones
            con.setAutoCommit(false);
            
            st = con.createStatement();
            
            // timeout de bloqueo
            st.execute("SET LOCK_TIMEOUT 5000");
            
            st.execute(abrirLlave);
            
            //nivel de aislamiento
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            
            //insertar en la BD
            ps = con.prepareStatement(agregar);
            
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, secreto);
            ps.executeUpdate();
            
            st.execute(cerrarLlave);
            
            Thread.sleep(1000);
            //confirmar transaccion
            con.commit();
            
            JOptionPane.showMessageDialog(this, "Alumno agregado");
            Cargar();
            LimpiarCampo();
            
        } catch (Exception ex) {

            try {

                if(st != null)//cierra la llave
                    st.execute(cerrarLlave);

                if(con != null) //hace un rollback (si es necesario)
                    con.rollback();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }

            JOptionPane.showMessageDialog(null, ex.toString());
        }
        finally { //cerrar todo al final

            try {

                if(ps != null)
                    ps.close();

                if(st != null)
                    st.close();

                if(con != null){
                    con.setAutoCommit(true);
                    con.close();
                }

            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, e2.toString());
            }
        }
    }//GEN-LAST:event_jbtnAgregarActionPerformed

    private void jbtnCambiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCambiarActionPerformed
        // TODO add your handling code here:
        int tupla = jtbtDatos.getSelectedRow();
        
        if (tupla == -1) //si no selecciono nada
        {
            JOptionPane.showMessageDialog(this, "Selecciona un registro");
            return;
        }
        
        //validaciones
        if(jtxtNombre.getText().isEmpty() || 
           jtxtCorreo.getText().isEmpty() || 
           jtxtSecreto.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Campos vacíos");
            return;
        }
        
        int id = Integer.parseInt(jtbtDatos.getValueAt(tupla, 0).toString());
        //update sin encriptar
        //String update = "UPDATE alumnos SET Nombre=?, Correo=?, Secreto=? WHERE Id=?";
        
        //update con encriptado
        String abrirLlave = "open symmetric key llave1 decryption by certificate certificado1";
        String update = 
                "update alumnos set Nombre=?, Correo=?, " +
                "SecretoEncriptado=  EncryptByKey(Key_GUID('llave1'),?) " +
                
                "where id = ? ";
        String cerrarLlave = "close symmetric key llave1";
        
        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        
        try
        {
            con = Conexion.ConectarBD();
            
            //autocommit falso
            con.setAutoCommit(false);
            
            //abrir llave
            st = con.createStatement();
            
            // timeout de bloqueo
            st.execute("SET LOCK_TIMEOUT 5000");
            
            st.execute(abrirLlave);
            
            //nivel de aislamiento
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            
            //modificar
            ps = con.prepareStatement(update);
            
            ps.setString(1, jtxtNombre.getText());
            ps.setString(2, jtxtCorreo.getText());
            ps.setString(3, jtxtSecreto.getText());
            ps.setInt(4, id);
            ps.executeUpdate();
            
            //cerrar llave
            st.execute(cerrarLlave);
            
            Thread.sleep(5000);
            //confirmar transaccion
            con.commit();
            
            JOptionPane.showMessageDialog(this, "Alumno actualizado");
            Cargar();
            LimpiarCampo();
            
        } catch (Exception ex) {

            try {

                if(st != null)
                    st.execute(cerrarLlave);

                if(con != null)
                    con.rollback();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }

            JOptionPane.showMessageDialog(null, ex.toString());
        }
        finally {

            try {

                if(ps != null)
                    ps.close();

                if(st != null)
                    st.close();

                if(con != null){
                    con.setAutoCommit(true);
                    con.close();
                }

            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, e2.toString());
            }
        }
    }//GEN-LAST:event_jbtnCambiarActionPerformed

    private void jbtnDeadlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeadlockActionPerformed
        // TODO add your handling code here:
        new Thread(() -> {
            probarDeadlock(1, 2);
        }).start();
    }//GEN-LAST:event_jbtnDeadlockActionPerformed

    private void jbtnDeadlock1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeadlock1ActionPerformed
        // TODO add your handling code here:
        new Thread(() -> {
            probarDeadlock(2, 1);
        }).start();
    }//GEN-LAST:event_jbtnDeadlock1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(jfrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jfrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jfrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jfrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jfrmPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnAgregar;
    public javax.swing.JButton jbtnBorrar;
    private javax.swing.JButton jbtnCambiar;
    private javax.swing.JButton jbtnDeadlock;
    private javax.swing.JButton jbtnDeadlock1;
    private javax.swing.JButton jbtnLimpiar;
    private javax.swing.JTable jtbtDatos;
    private javax.swing.JTextField jtxtCorreo;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JTextField jtxtSecreto;
    // End of variables declaration//GEN-END:variables
}
