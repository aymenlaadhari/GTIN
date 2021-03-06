/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dasLieferDialog;

import dasLieferdao.JlieferDao;
import dasLieferdao.JlieferDaoInterface;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.LieferKund;
import model.LieferKundPrufer;

/**
 *
 * @author aladhari
 */
public class JDialogSuchen extends javax.swing.JDialog {
    private final DefaultTableModel tableModel;
    private JlieferDaoInterface jlieferDaoInterface;
    private final Object[] rowDataTable = new Object[8];
    private List<LieferKundPrufer> list;
    private final String kdNummerIn;
    private final LieferKund lieferKund;
    /**
     * Creates new form JDialogSuchen
     * @param parent
     * @param lieferKundIn
     
    
     * @param kdNummer
     * @param dbUrl
   
     */
    public JDialogSuchen(java.awt.Frame parent, LieferKund lieferKundIn,String kdNummer, String dbUrl) {
        super(parent);
        initComponents();
        kdNummerIn = kdNummer;
        lieferKund = lieferKundIn;
        setTitle("Artikel Suche für Kunden Num: "+kdNummer);
        tableModel = (DefaultTableModel) jTable.getModel();
        initialise(lieferKund, dbUrl);
        populateTable(list);
        
    }
    private void initialise(LieferKund lieferKund,String dbUrl) {
    jlieferDaoInterface = new JlieferDao(dbUrl);
    jTextFieldKdGross.setText(lieferKund.getGroesse());
    jTextFieldKdVariant.setText(lieferKund.getVariante());
    jTextFieldKdfarb.setText(lieferKund.getFarbe());
    jTextFieldkdArtNum.setText(lieferKund.getArtikel_Nr());
    list = jlieferDaoInterface.getLieferKundSuche(kdNummerIn, lieferKund.getArtikel_Nr(), lieferKund.getFarbe(), lieferKund.getGroesse(), lieferKund.getVariante());
    }
    
    private void populateTable(List<LieferKundPrufer> listGtinAnderung) {
        tableModel.setRowCount(0);
        listGtinAnderung.stream().forEach(cnsmr -> {

            addToTable(cnsmr);
            tableModel.addRow(rowDataTable);

        });
    }
    
    private void addToTable(LieferKundPrufer kund){
        rowDataTable[0] = kund.getGtin();
        rowDataTable[1] = kund.getKundNummer();
        rowDataTable[2] = kund.getKundenArtikelNummer();
        rowDataTable[3] = kund.getFarbeNummer();
        rowDataTable[4] = kund.getGroesse();
        rowDataTable[5] = kund.getVariante();
        rowDataTable[6] = kund.getPosGrPreis();
        rowDataTable[7] = kund.getGtinPreis();           
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextFieldkdArtNum = new javax.swing.JTextField();
        jTextFieldKdfarb = new javax.swing.JTextField();
        jTextFieldKdGross = new javax.swing.JTextField();
        jTextFieldKdVariant = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTextFieldkdArtNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldkdArtNumKeyReleased(evt);
            }
        });

        jTextFieldKdfarb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKdfarbKeyReleased(evt);
            }
        });

        jTextFieldKdGross.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKdGrossKeyReleased(evt);
            }
        });

        jTextFieldKdVariant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKdVariantKeyReleased(evt);
            }
        });

        jLabel1.setText("Kd-Artikel-Num");

        jLabel2.setText("Kd-Farbe");

        jLabel3.setText("kd-Größe");

        jLabel4.setText("Kd-Variante");

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "GTIN", "Kd_Nr", "Kd_Art_Nr", "Kd_Farbe", "Kd_Groesse", "Kd_Variante", "Gr_Preis1", "var_Preis"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldkdArtNum, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel1)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(jLabel2)
                                .addGap(104, 104, 104)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jTextFieldKdfarb, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldKdGross, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(56, 56, 56))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jTextFieldKdVariant, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldkdArtNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKdfarb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKdGross, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKdVariant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldKdfarbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdfarbKeyReleased
        // TODO add your handling code here:
         list = jlieferDaoInterface.getLieferKundSuche(kdNummerIn, jTextFieldkdArtNum.getText(), jTextFieldKdfarb.getText(), jTextFieldKdGross.getText(), jTextFieldKdVariant.getText());
         populateTable(list);
    }//GEN-LAST:event_jTextFieldKdfarbKeyReleased

    private void jTextFieldKdGrossKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdGrossKeyReleased
        // TODO add your handling code here:
        list = jlieferDaoInterface.getLieferKundSuche(kdNummerIn, jTextFieldkdArtNum.getText(), jTextFieldKdfarb.getText(), jTextFieldKdGross.getText(), jTextFieldKdVariant.getText());
         populateTable(list);
    }//GEN-LAST:event_jTextFieldKdGrossKeyReleased

    private void jTextFieldKdVariantKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdVariantKeyReleased
        // TODO add your handling code here:
        list = jlieferDaoInterface.getLieferKundSuche(kdNummerIn, jTextFieldkdArtNum.getText(), jTextFieldKdfarb.getText(), jTextFieldKdGross.getText(), jTextFieldKdVariant.getText());
         populateTable(list);
    }//GEN-LAST:event_jTextFieldKdVariantKeyReleased

    private void jTextFieldkdArtNumKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldkdArtNumKeyReleased
        // TODO add your handling code here:
        list = jlieferDaoInterface.getLieferKundSuche(kdNummerIn, jTextFieldkdArtNum.getText(), jTextFieldKdfarb.getText(), jTextFieldKdGross.getText(), jTextFieldKdVariant.getText());
         populateTable(list);
    }//GEN-LAST:event_jTextFieldkdArtNumKeyReleased

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
            java.util.logging.Logger.getLogger(JDialogSuchen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogSuchen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogSuchen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogSuchen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogSuchen dialog = new JDialogSuchen(new javax.swing.JFrame(), new LieferKund(),"","");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextFieldKdGross;
    private javax.swing.JTextField jTextFieldKdVariant;
    private javax.swing.JTextField jTextFieldKdfarb;
    private javax.swing.JTextField jTextFieldkdArtNum;
    // End of variables declaration//GEN-END:variables
}
