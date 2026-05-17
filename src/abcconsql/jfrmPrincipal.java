/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package abcconsql;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;

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
        
        
        try
        {
            //muestra los datos con un cachedRowSet para el modo desconectado(unidad 7)
            CachedRowSet crs = Conexion.getRowSetConLlave();
            
            while(crs.next())
            {
                Object [] tupla = new Object [4];
                tupla[0] = crs.getInt("Id");
                tupla[1] = crs.getString("Nombre");
                tupla[2] = crs.getString("Correo");
                tupla[3] = crs.getString("Secreto");
                modelo.addRow(tupla);
            }
            
            jtbtDatos.getColumnModel().getColumn(0).setMinWidth(0);
            jtbtDatos.getColumnModel().getColumn(0).setWidth(0);
            jtbtDatos.getColumnModel().getColumn(0).setMaxWidth(0);
            
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
    
    public void LimpiarLog()
    {
        jtxaLog.setText("");
    }
    
    public void AutoActualizar()
    {
        new Thread(() -> {

            while(true)
            {
                try {

                    Thread.sleep(2000);

                    SwingUtilities.invokeLater(() -> {
                        int tupla = jtbtDatos.getSelectedRow();
                        
                        Cargar();
                        
                        //mantiene la fila seleccionada
                        if(tupla != -1 && tupla < jtbtDatos.getRowCount())
                        {
                            jtbtDatos.setRowSelectionInterval(tupla, tupla);
                        }
                    });

                } catch (Exception e) {
                    break;
                }
            }

        }).start();
    }
    //funcion para mostrar un log de transacciones
    public void log(String mensaje)
    {
        SwingUtilities.invokeLater(() -> {
            jtxaLog.append(mensaje + "\n");
        });
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

            log("=========================");
            log("prueba de bloqueo circular");
            con = Conexion.ConectarBD();

            con.setAutoCommit(false);

            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            st = con.createStatement();

            // timeout 5 segundos
            st.execute("SET LOCK_TIMEOUT 5000");

            st.execute(abrirLlave);

            log("Bloqueando alumno " + primerId);

            ps1 = con.prepareStatement(update);
            ps1.setString(1, "Usuario " + primerId);
            ps1.setInt(2, primerId);
            ps1.executeUpdate();

            log("Esperando 5 segundos...");

            Thread.sleep(5000);

            log("Intentando bloquear alumno " + segundoId);

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

        jInternalFrame1 = new javax.swing.JInternalFrame();
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
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtxaLog = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmitHistorial = new javax.swing.JMenuItem();
        jmitSalir = new javax.swing.JMenuItem();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

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
                "Nombre", "Correo", "Secreto"
            }
        ));
        jtbtDatos.setShowGrid(true);
        jtbtDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbtDatosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtbtDatos);

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

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        jtxaLog.setColumns(20);
        jtxaLog.setRows(5);
        jScrollPane1.setViewportView(jtxaLog);

        jMenu1.setText("Archivo");

        jmitHistorial.setText("Historial Cambios");
        jmitHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmitHistorialActionPerformed(evt);
            }
        });
        jMenu1.add(jmitHistorial);

        jmitSalir.setText("Salir");
        jmitSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmitSalirActionPerformed(evt);
            }
        });
        jMenu1.add(jmitSalir);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

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
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                            .addGap(39, 39, 39)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jbtnAgregar)
                                .addComponent(jbtnBorrar))
                            .addGap(39, 39, 39)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jbtnLimpiar)
                                .addComponent(jbtnCambiar))
                            .addGap(18, 18, 18)
                            .addComponent(jLabel1))
                        .addComponent(jSeparator1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
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
        LimpiarLog();
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
        //dejo el borrar original nomas para que vean lo simple que era
        //String borrar = "DELETE FROM alumnos WHERE id=?";
        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        
        try
        {
            alumnoDAO dao = new alumnoDAO();
            log("============================");
            log("ejecutando una eliminación de registro");
            log("conexion con la base de datos exitosa");
            
            //borrar
            log("borrando al alumno: " + jtbtDatos.getValueAt(tupla, 1).toString());
            dao.eliminarAlumno(id);
            
            log("commit ejecutado - alumno eliminado");
            
            JOptionPane.showMessageDialog(this, "Alumno eliminado");
            Cargar();
            LimpiarCampo();
            
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
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
        alumnoDAO dao = new alumnoDAO();
        
        try
        {
            log("============================");
            log("Iniciando inserción");
            log("conexion con la base de datos establecida");
            
            //insertar en la BD
            log("insertando en la tabla alumno...");
            dao.insertarAlumno(nombre, correo, secreto);
            
            log("llave de encriptación cerrada");
            log("commit ejecutado - alumno agregado");
            
            JOptionPane.showMessageDialog(this, "Alumno agregado");
            Cargar();
            LimpiarCampo();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
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
        
        //update sin encriptar
        //String update = "UPDATE alumnos SET Nombre=?, Correo=?, Secreto=? WHERE Id=?";
        
        //update con encriptado
        String update = "{CALL actualizar(?,?,?,?)}";//procedimiento almacenado: actualizar
        
        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        
        try
        {
            int id = Integer.parseInt(jtbtDatos.getValueAt(tupla, 0).toString());
            alumnoDAO dao = new alumnoDAO();
            log("============================");
            log("iniciando modificación de registros");
            log("conexion con la base de datos establecida");
            // timeout de bloqueo
            st.execute("SET LOCK_TIMEOUT 5000");
            //st.execute(abrirLlave);
            log("llave de encriptación abierta");
            
            //modificar
            log("modificando el registro de la tabla alumnos...");
            dao.actualizarAlumno(id, jtxtNombre.getText(), jtxtCorreo.getText(), jtxtSecreto.getText());
            
            
            log("commit ejecutado - alumno modificado");
            
            JOptionPane.showMessageDialog(this, "Alumno actualizado");
            Cargar();
            LimpiarCampo();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
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

    private void jmitSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmitSalirActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jmitSalirActionPerformed

    private void jmitHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmitHistorialActionPerformed
        // TODO add your handling code here:
        jfrmHistorial historial = new jfrmHistorial();
        historial.setVisible(true);
        historial.setLocationRelativeTo(null);
    }//GEN-LAST:event_jmitHistorialActionPerformed

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
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton jbtnAgregar;
    public javax.swing.JButton jbtnBorrar;
    private javax.swing.JButton jbtnCambiar;
    private javax.swing.JButton jbtnDeadlock;
    private javax.swing.JButton jbtnDeadlock1;
    private javax.swing.JButton jbtnLimpiar;
    private javax.swing.JMenuItem jmitHistorial;
    private javax.swing.JMenuItem jmitSalir;
    private javax.swing.JTable jtbtDatos;
    private javax.swing.JTextArea jtxaLog;
    private javax.swing.JTextField jtxtCorreo;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JTextField jtxtSecreto;
    // End of variables declaration//GEN-END:variables
}
