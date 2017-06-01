/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dasLieferDialog;

import dasLieferdao.JlieferDao;
import dasLieferdao.JlieferDaoInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.LieferKundPrufer;

/**
 *
 * @author aladhari
 */
public class JDialogKundenArtikelDatenAndern extends javax.swing.JDialog {

    private List<LieferKundPrufer> listGtinAnderung;
    private final DefaultTableModel tableModel;
    private final Object[] rowData = new Object[7];
    private final JlieferDaoInterface daoInterface;
    private final LieferKundPrufer kundPruferFamamk, kundPrufer;
    private String preisGrossBasis, preisVarianten, gtinParam, meldung;

    /**
     * Creates new form JDialogKundenArtikelDatenAndern
     *
     * @param parent
     * @param modal
     * @param listGtinAnderung
     * @param kundPruferFamamk
     * @param kundPrufer
     * @param dbUrl
     * @param gtinParam
     * @param preisGrossBasis
     * @param preisVarianten
     */
    public JDialogKundenArtikelDatenAndern(javax.swing.JDialog parent, boolean modal, List<LieferKundPrufer> listGtinAnderung, LieferKundPrufer kundPruferFamamk, LieferKundPrufer kundPrufer, String dbUrl, String gtinParam, String preisGrossBasis, String preisVarianten, String message) {
        super(parent, modal);
        initComponents();
        this.listGtinAnderung = listGtinAnderung;
        tableModel = (DefaultTableModel) jTable1.getModel();
        tableModel.setRowCount(0);
        populateTable(listGtinAnderung);
        daoInterface = new JlieferDao(dbUrl);
        this.kundPrufer = kundPrufer;
        this.kundPruferFamamk = kundPruferFamamk;
        this.gtinParam = gtinParam;
        this.preisGrossBasis = preisGrossBasis.replace(",", ".");
        this.preisVarianten = preisVarianten.replace(",", ".");
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        //jTextArea.setEnabled(false);
        jTextArea.setText(message);

    }

    private void populateTable(List<LieferKundPrufer> listGtinAnderung) {
        listGtinAnderung.stream().forEach(cnsmr
                -> {
            addRow(cnsmr);
        });
    }

    private void addRow(LieferKundPrufer kundPrufer) {
        rowData[0] = kundPrufer.getGtin();
        rowData[1] = kundPrufer.getKundenArtikelNummer();
        rowData[2] = kundPrufer.getFarbeNummer();
        rowData[3] = kundPrufer.getGroesse();
        rowData[4] = kundPrufer.getVarNummer();
        rowData[5] = kundPrufer.getPosGrPreis();
        rowData[6] = kundPrufer.getGtinPreis();
        tableModel.addRow(rowData);
    }

    public String getMeldung() {
        return meldung;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanelText = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Kunden-Artikel-daten ändern");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "GTIN", "Artk_Num", "Farb_Num", "Groesse", "var_Nummern", "Gr_Preis", "var_Preis"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("JA");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("NEIN");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTextLayout = new javax.swing.GroupLayout(jPanelText);
        jPanelText.setLayout(jPanelTextLayout);
        jPanelTextLayout.setHorizontalGroup(
            jPanelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );
        jPanelTextLayout.setVerticalGroup(
            jPanelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );

        jTextArea.setEditable(false);
        jTextArea.setColumns(20);
        jTextArea.setLineWrap(true);
        jTextArea.setRows(5);
        jScrollPane2.setViewportView(jTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jPanelText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //System.out.println("1"+"/"+kundPrufer.getKundNummer()+"/"+ kundPrufer.getKundenArtikelNummer()+"/"+kundPrufer.getFarbe()+"/"+kundPrufer.getGroesse()+"/"+kundPrufer.getVariante()+"/"+gtinParam+"/"+kundPruferFamamk.getPosGrId()+"/"+preisGrossBasis+"/"+preisVarianten);
        System.out.println("kundprufer : " + kundPrufer.getGroesse() + "-" + kundPrufer.getFarbe() + "" + kundPrufer.getVariante());
        meldung = daoInterface.anlegenAndern("1", kundPrufer.getKundNummer(), kundPrufer.getKundenArtikelNummer(), kundPrufer.getFarbe(), kundPrufer.getGroesse(), kundPrufer.getVariante(), gtinParam, kundPruferFamamk.getPosGrId(), preisGrossBasis, preisVarianten);
        System.out.println("melung in KundenArtikelDaten: " + meldung);
        String message = daoInterface.getMeldung("2", meldung);
        System.out.println("message in KundenArtikelDaten: " + message);
        String[] parts3 = message.split("--");
        String part1_3 = parts3[0]; // 004
        String part2_3 = parts3[1]; // 034556
        JOptionPane.showMessageDialog(null,
                part1_3,
                part2_3,
                JOptionPane.WARNING_MESSAGE);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogKundenArtikelDatenAndern.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogKundenArtikelDatenAndern.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogKundenArtikelDatenAndern.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogKundenArtikelDatenAndern.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JDialogKundenArtikelDatenAndern dialog = new JDialogKundenArtikelDatenAndern(new JDialog(), true, new ArrayList<>(), new LieferKundPrufer(), new LieferKundPrufer(), "", "", "", "", "");
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanelText;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea;
    // End of variables declaration//GEN-END:variables
}
