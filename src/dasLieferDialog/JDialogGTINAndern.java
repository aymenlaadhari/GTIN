/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dasLieferDialog;

import dao.JlieferDao;
import dao.JlieferDaoInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Kund;
import model.LieferKundPrufer;

/**
 *
 * @author aladhari
 */
public class JDialogGTINAndern extends javax.swing.JDialog {

    private final List<Kund> kunds;
    private final DefaultTableModel tableModel;
    private final Object[] rowData = new Object[6];
    private final JlieferDaoInterface daoInterface;
    private final LieferKundPrufer kundPruferFamamk, kundPrufer;
    private String preisGrossBasis, preisVarianten,dbUrl, meldung;

    /**
     * Creates new form JDialogGTINAndern
     * @param parent
     * @param modal
     * @param meldung
     * @param kunds
     * @param dbUrl
     * @param kundPruferFamamk
     * @param kundPrufer
     * @param preisGrossBasis
     * @param preisVarianten

     */
    public JDialogGTINAndern(javax.swing.JDialog parent, boolean modal,List<Kund> kunds, String meldung, String dbUrl, LieferKundPrufer kundPruferFamamk, LieferKundPrufer kundPrufer, String preisGrossBasis, String preisVarianten) {
        super(parent,modal);
        initComponents();
        tableModel = (DefaultTableModel) jTableKd.getModel();
        tableModel.setRowCount(0);
        this.kunds = kunds;
        this.kundPruferFamamk = kundPruferFamamk;
        this.kundPrufer = kundPrufer;
        this.preisGrossBasis = preisGrossBasis.replace(",", ".");
        this.preisVarianten = preisVarianten.replace(",", ".");
        this.dbUrl= dbUrl;
        jLabelMeldung.setText(meldung);
        populateJtable(kunds);
        daoInterface = new JlieferDao(this.dbUrl);
    }

    private void addToTable(Kund kund)
    {
        rowData[0] = kund.getKdNummer();
        rowData[1] = kund.getKdArtNummer();
        rowData[2] = kund.getKdFarbe();
        rowData[3] = kund.getKdGrosse();
        rowData[4] = kund.getKdvariante();
        rowData[5] = kund.getKdName();
        //rowData[6] = lieferKund.getPreis();
     tableModel.addRow(rowData);
    }
    
    private void populateJtable(List<Kund> kunds)
    {
        kunds.stream().forEach(cnsmr ->{
            addToTable(cnsmr);
        });
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
        jTableKd = new javax.swing.JTable();
        jLabelMeldung = new javax.swing.JLabel();
        jButtonJa = new javax.swing.JButton();
        jButtonNein = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("GTIN ändern");

        jTableKd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kd_Nr", "Kd_Art_Nr", "Kd_Farbe", "Kd_Groesse", "Kd_Variante", ""
            }
        ));
        jScrollPane1.setViewportView(jTableKd);

        jButtonJa.setText("JA");
        jButtonJa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJaActionPerformed(evt);
            }
        });

        jButtonNein.setText("NEIN");
        jButtonNein.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNeinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelMeldung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(jButtonJa, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(jButtonNein, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(278, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabelMeldung, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonJa)
                    .addComponent(jButtonNein))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonJaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJaActionPerformed
        // TODO add your handling code here:
        //System.out.println("1"+"/"+kundPrufer.kundPrufer.getArtikel_Nr()+"/"+ kundPrufer.getFarbeNummer()+"/"+kundPrufer.getGroesse()+"/"+kundPrufer.getVarNummer()+"/"+kundPrufer.getGtin()+"/"+kundPrufer.getPosGrId());
        meldung = daoInterface.gtinStammsatzAnderung("1", kundPruferFamamk.getArtikel_Nr(), kundPruferFamamk.getFarbeNummer(), kundPruferFamamk.getGroesse(), kundPruferFamamk.getVarNummer(), kundPruferFamamk.getGtin(), kundPruferFamamk.getPosGrId());
        System.out.println("meldung: " + meldung);
        //String meldung2;
        String message = daoInterface.getMeldung("1", meldung);
        System.out.println(message);
        dispose();
        //String message2;

//        if (((meldung.equals("0") || meldung.equals("1")) && !kundPruferFamamk.getGtin().equals("")) || meldung.length() > 4) {
//            String gtinParam;
//            if (meldung.length() > 4) {
//                gtinParam = meldung;
//            } else {
//                gtinParam = kundPruferFamamk.getGtin();
//            }
//            meldung2 = daoInterface.anlegenAndern("0", kundPrufer.getKundNummer(), kundPrufer.getKundenArtikelNummer(), kundPrufer.getFarbe(), kundPrufer.getGroesse(), kundPrufer.getVariante(), gtinParam, kundPruferFamamk.getPosGrId(), preisGrossBasis, preisVarianten);
//            String indcator = "";
//            //message2 = daoInterface.getMeldung("2", meldung2);
//            System.out.println("meldung2:" + meldung2);
//            
//            
//            if (meldung2.length()>4) {
//               indcator="E";
//               message2 = daoInterface.getMeldung("2", indcator);
//            }else
//            {
//                message2 = daoInterface.getMeldung("2", meldung2);
//                System.out.println("message2:" + message2);
//            }
//            String[] parts2 = message2.split("--");
//            String part1_1; // 004
//            if (indcator.equals("")) {
//                part1_1 = parts2[0];
//            }else
//            {
//                part1_1 = parts2[0]+" "+meldung2;
//            }
//            String part2_1 = parts2[1]; // 034556
//            switch (meldung2) {
//                case "30":
//                    System.out.println(kundPrufer.getKundNummer()+"--"+ kundPrufer.getKundenArtikelNummer()+"--"+ kundPrufer.getFarbe()+"--"+ kundPrufer.getGroesse()+"--"+ kundPrufer.getVariante()+"--"+gtinParam);
//                    List<LieferKundPrufer> listGtinAnderung = daoInterface.getListGtinAnderung(kundPrufer.getKundNummer(), kundPrufer.getKundenArtikelNummer(), kundPrufer.getFarbe(), kundPrufer.getGroesse(), kundPrufer.getVariante(), gtinParam);
//                    JDialogKundenArtikelDatenAndern artikelDatenAndern = new JDialogKundenArtikelDatenAndern(this, true, listGtinAnderung, kundPruferFamamk, kundPrufer, this.dbUrl, gtinParam, preisGrossBasis, preisVarianten);
//                    artikelDatenAndern.setVisible(true);
//                    break;
//                case "38":
//                    int reply = JOptionPane.showConfirmDialog(null, message2, "Actung", JOptionPane.YES_NO_OPTION);
//                    if (reply == JOptionPane.YES_OPTION) {
//                        String meldung3 = daoInterface.anlegenAndern("1", kundPrufer.getKundNummer(), kundPrufer.getKundenArtikelNummer(), kundPrufer.getFarbe(), kundPrufer.getGroesse(), kundPrufer.getVariante(), gtinParam, kundPruferFamamk.getPosGrId(), preisGrossBasis, preisVarianten);
//                        String message3 = daoInterface.getMeldung("2", meldung3);
//                        System.out.println("meldung3:" + meldung3);
//                        System.out.println("message3:" + message3);
//                        
////                   String[] parts3 = message3.split("--");
////                   String part1_3 = parts3[0]; // 004
////                   String part2_3 = parts3[1]; // 034556
////                   JOptionPane.showMessageDialog(null,
////                    part1_3,
////                    part2_3,
////                    JOptionPane.WARNING_MESSAGE);
//                    }   break;
//                default:
//                    JOptionPane.showMessageDialog(null,
//                            part1_1,
//                            part2_1,
//                            JOptionPane.WARNING_MESSAGE);
//                    break;
//            }

//        } else {
//            String[] parts = message.split("--");
//            String part1 = parts[0]; // 004
//            String part2 = parts[1]; // 034556
//            JOptionPane.showMessageDialog(null,
//                    part1,
//                    part2,
//                    JOptionPane.WARNING_MESSAGE);
//        }
    }//GEN-LAST:event_jButtonJaActionPerformed

    private void jButtonNeinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNeinActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButtonNeinActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogGTINAndern.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogGTINAndern.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogGTINAndern.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogGTINAndern.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogGTINAndern dialog = new JDialogGTINAndern(new JDialog(),true,new ArrayList<>(),"","",new LieferKundPrufer(),new LieferKundPrufer(),"","");
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
    private javax.swing.JButton jButtonJa;
    private javax.swing.JButton jButtonNein;
    private javax.swing.JLabel jLabelMeldung;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableKd;
    // End of variables declaration//GEN-END:variables
}
