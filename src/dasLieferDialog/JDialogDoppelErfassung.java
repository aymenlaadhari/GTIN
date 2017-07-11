/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dasLieferDialog;

import dasLieferdao.JlieferDao;
import dasLieferdao.JlieferDaoInterface;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import model.LieferKundDoppel;
import util.TableCellListener;

/**
 *
 * @author aladhari
 */
public class JDialogDoppelErfassung extends javax.swing.JDialog {

    private final Object[] rowDataDoppel = new Object[15];
    private final DefaultTableModel tableModel;
    private List<LieferKundDoppel> kundDoppelsIn;
    private final ListSelectionModel listSelectionModel;
    private final TableCellListener tclLieferKund;
    private TableCellListener tclLiefKund;
    private JlieferDaoInterface jlieferDaoInterface;

    /**
     * Creates new form NewJDialogDoppelErfassung
     *
     * @param parent
     * @param modal
     * @param kundDoppels
     * @param dburlProdukt
     */
    public JDialogDoppelErfassung(java.awt.Frame parent, boolean modal, List<LieferKundDoppel> kundDoppels, String dburlProdukt) {
        super(parent, modal);
        initComponents();
        jlieferDaoInterface = new JlieferDao(dburlProdukt);
        tableModel = (DefaultTableModel) jTable.getModel();
        kundDoppelsIn = kundDoppels;
        populateJtable(kundDoppelsIn);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable.setPreferredScrollableViewportSize(Toolkit.getDefaultToolkit().getScreenSize());
        resizeColumnWidth(jTable);
        listSelectionModel = jTable.getSelectionModel();

        //ColumnsAutoSizer.sizeColumnsToFit(jTable);
        tclLieferKund = new TableCellListener(jTable, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //boolean changeArtnum = false, changeFarb = false, changeVar = false;
                tclLiefKund = (TableCellListener) e.getSource();
                switch (tclLiefKund.getColumn()) {

                    case 2:
                        kundDoppelsIn.get(tclLiefKund.getRow()).setKdPosNummer(tclLiefKund.getNewValue().toString());
                        break;
                    case 3:
                        kundDoppelsIn.get(tclLiefKund.getRow()).setKdArtNummer(tclLiefKund.getNewValue().toString());

                        break;

                    case 4:
                        kundDoppelsIn.get(tclLiefKund.getRow()).setKdFarbe(tclLiefKund.getNewValue().toString());

                        break;

                    case 5:
                        kundDoppelsIn.get(tclLiefKund.getRow()).setKdGroesse(tclLiefKund.getNewValue().toString());

                        break;

                    case 6:
                        kundDoppelsIn.get(tclLiefKund.getRow()).setKdVariante(tclLiefKund.getNewValue().toString());

                        break;

                    case 7:

                        kundDoppelsIn.get(tclLiefKund.getRow()).setKommission(tclLiefKund.getNewValue().toString());
                        break;

                    case 8:
                        kundDoppelsIn.get(tclLiefKund.getRow()).setKdMenge(tclLiefKund.getNewValue().toString());

                        break;
                }
                //populateJtablePreisListe();
                //refreshTableLiefKund(tclLiefKund.getRow(), changeArtnum, changeFarb, changeVar);
            }
        });
    }

    private void populateJtable(List<LieferKundDoppel> kundDoppels) {
        kundDoppelsIn = kundDoppels;
        tableModel.setRowCount(0);
        kundDoppels.stream().forEach(cnsmr -> {
            addToTable(cnsmr);
            tableModel.addRow(rowDataDoppel);
        });
    }

    private void addToTable(LieferKundDoppel kundDoppel) {
        rowDataDoppel[0] = kundDoppel.getKdNummer();
        rowDataDoppel[1] = kundDoppel.getKdBestNummer();
        rowDataDoppel[2] = kundDoppel.getKdPosNummer();
        rowDataDoppel[3] = kundDoppel.getKdArtNummer();
        rowDataDoppel[4] = kundDoppel.getKdFarbe();
        rowDataDoppel[5] = kundDoppel.getKdGroesse();
        rowDataDoppel[6] = kundDoppel.getKdVariante();
        rowDataDoppel[7] = kundDoppel.getKommission();
        rowDataDoppel[8] = kundDoppel.getKdMenge();
        rowDataDoppel[9] = kundDoppel.getStatus();
        rowDataDoppel[10] = kundDoppel.getErfasser();
        rowDataDoppel[11] = kundDoppel.getErfassungDatum();
        rowDataDoppel[12] = kundDoppel.getId();
        rowDataDoppel[13] = kundDoppel.getPosGroId();
        rowDataDoppel[14] = kundDoppel.getBemerkung();
    }

    private void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }

            //columnModel.getColumn(2).setPreferredWidth(232);
            columnModel.getColumn(column).setPreferredWidth(width);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jButtonmarkierteAktualisieren = new javax.swing.JButton();
        jButtonmarkierteLöschen = new javax.swing.JButton();
        jButtonstatusSetzen = new javax.swing.JButton();
        jButtonerneut = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Kd_Nr", "Kd_Best_Nr", "Kd_Pos_Nr", "Kd_Artikel_Nr", "Kd_Farbe", "Kd_Groesse", "Kd_Variante", "Kommission", "Kd_Menge", "Status", "Erfasser", "Erfassungs_Datum", "ID", "Pos_Gr_ID", "Bemerkung"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, true, true, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable);

        jButtonmarkierteAktualisieren.setText("markierte Zeilen aktualisieren");
        jButtonmarkierteAktualisieren.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonmarkierteAktualisierenActionPerformed(evt);
            }
        });

        jButtonmarkierteLöschen.setText("markierte Zeilen löschen");
        jButtonmarkierteLöschen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonmarkierteLöschenActionPerformed(evt);
            }
        });

        jButtonstatusSetzen.setText("Status setzen");
        jButtonstatusSetzen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonstatusSetzenActionPerformed(evt);
            }
        });

        jButtonerneut.setText("Erneut prüfen");
        jButtonerneut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonerneutActionPerformed(evt);
            }
        });

        jLabel1.setText("Nach Änderung [Status zurück setzen] und [Erneut prüfen] drücken, um das Ergebnis der Änderungen angezeigt zu bekommen");

        jLabel2.setText("bzw.");

        jLabel3.setText(">>");

        jLabel4.setText(">>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonmarkierteAktualisieren)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)
                        .addComponent(jButtonmarkierteLöschen)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonstatusSetzen)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel4)
                        .addGap(32, 32, 32)
                        .addComponent(jButtonerneut)))
                .addContainerGap(369, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonmarkierteAktualisieren)
                    .addComponent(jButtonmarkierteLöschen)
                    .addComponent(jButtonstatusSetzen)
                    .addComponent(jButtonerneut)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonstatusSetzenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonstatusSetzenActionPerformed
        // TODO add your handling code here:
        populateJtable(jlieferDaoInterface.getListDoppelErfassungZuruck());
    }//GEN-LAST:event_jButtonstatusSetzenActionPerformed

    private void jTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int[] rows = jTable.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                //MusterArtikel musterArtikel = musterArtikels.get(rows[i] - i);
                LieferKundDoppel kundDoppel = kundDoppelsIn.get(rows[i] - i);
                tableModel.removeRow(rows[i] - i);
                kundDoppelsIn.remove(kundDoppel);
            }
            if (jTable.getRowCount() == 0) {
                //musterArtikels = new ArrayList<>();
                //selectedMusterArtikel = new MusterArtikel();
                tableModel.setRowCount(0);
                kundDoppelsIn.clear();
                tableModel.setRowCount(0);

                //initilizeFelder();
            }

            //refreshPosition(jTable.getRowCount());
            tableModel.fireTableDataChanged();

        }
    }//GEN-LAST:event_jTableKeyPressed

    private void jButtonmarkierteAktualisierenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonmarkierteAktualisierenActionPerformed
        // TODO add your handling code here:
        int[] rows = jTable.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            LieferKundDoppel kundDoppel = kundDoppelsIn.get(rows[i]);
            String meldung = jlieferDaoInterface.erfassungAktualisieren(kundDoppel.getId(), kundDoppel.getKdPosNummer(), kundDoppel.getKdArtNummer(), kundDoppel.getKdFarbe(), kundDoppel.getKdGroesse(), kundDoppel.getKdVariante(), kundDoppel.getKommission(), kundDoppel.getKdMenge());
            JOptionPane.showMessageDialog(null, meldung, "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButtonmarkierteAktualisierenActionPerformed

    private void jButtonerneutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonerneutActionPerformed
        // TODO add your handling code here:
        populateJtable(jlieferDaoInterface.getListDoppelErfassung());
    }//GEN-LAST:event_jButtonerneutActionPerformed

    private void jButtonmarkierteLöschenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonmarkierteLöschenActionPerformed
        // TODO add your handling code here:
        int[] rows = jTable.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            LieferKundDoppel kundDoppel = kundDoppelsIn.get(rows[i] - i);
            String meldung = jlieferDaoInterface.erfassungLoschen(kundDoppel.getId());
            JOptionPane.showMessageDialog(null, meldung, "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        populateJtable(jlieferDaoInterface.getListDoppelErfassungZuruck());
    }//GEN-LAST:event_jButtonmarkierteLöschenActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogDoppelErfassung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogDoppelErfassung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogDoppelErfassung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogDoppelErfassung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogDoppelErfassung dialog = new JDialogDoppelErfassung(new javax.swing.JFrame(), true, new ArrayList<>(), "");
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
    private javax.swing.JButton jButtonerneut;
    private javax.swing.JButton jButtonmarkierteAktualisieren;
    private javax.swing.JButton jButtonmarkierteLöschen;
    private javax.swing.JButton jButtonstatusSetzen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
}
