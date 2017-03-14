/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dasLieferDialog;

import dao.JlieferDao;
import dao.JlieferDaoInterface;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;
import model.Kund;
import model.LieferKundPrufer;

/**
 *
 * @author aladhari 
 */
public class JDialogGTIN extends javax.swing.JDialog {
    private final JlieferDaoInterface jlieferDaoInterface;
    private final String dbUrl;
    private final LieferKundPrufer kundPrufer,kundPruferFamak;
    private Float varianten, grossenBasis;
    /**
     * Creates new form JDialogGTIN
     * @param parent
     * @param modal
     * @param kundPruferIn
     * @param kundPruferFamakIn
     * @param preisVariante
     * @param dbUrl1
     */
    public JDialogGTIN(java.awt.Frame parent, boolean modal, LieferKundPrufer kundPruferIn, LieferKundPrufer kundPruferFamakIn, String preisVariante, String dbUrl1) {
        super(parent, modal);
        initComponents();
        this.dbUrl =  dbUrl1;
        this.kundPrufer = kundPruferIn;
        this.kundPruferFamak = kundPruferFamakIn;
        jlieferDaoInterface = new JlieferDao(dbUrl);
        jLabelkundNummer.setText(kundPrufer.getKundNummer());
        jTextFieldArtikelNummer.setText(kundPrufer.getKundenArtikelNummer());
        jTextFieldFarbe.setText(kundPrufer.getFarbe());
        jTextFieldGroesse.setText(kundPrufer.getGroesse());
        jTextFieldVariante.setText(kundPrufer.getVariante());
        
        jTextFieldGTIN.setText(kundPruferFamak.getGtin());
        jTextFieldGTIN.setEditable(false);
        
        jTextFieldFarbNum.setText(kundPruferFamak.getFarbeNummer());
        varianten = Float.parseFloat(preisVariante);
        grossenBasis = (Float.parseFloat(kundPruferFamak.getPosGrPreis())- varianten);
        jTextFieldPreisGrossBasis.setText(String.format("%.2f",grossenBasis));
        jTextFieldPreisVarianten.setText(String.format("%.2f", varianten));
        
        jTextFieldPreisGesamt.setText(String.format("%.2f",Float.parseFloat(preisVariante)+grossenBasis));
        jTextFieldPreisGesamt.setEditable(false);
        
        jTextFieldPosGrID.setText(kundPruferFamak.getPosGrId());
        jTextFieldPosGrID.setEditable(false);
        
        jTextFieldArtNummerFamak.setText(kundPruferFamak.getArtikel_Nr());
        jTextFieldGroesseFamak.setText(kundPruferFamak.getGroesse());
        jTextFieldVariantenFamak.setText(kundPruferFamak.getVarNummer());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelKundenAnlegen = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelkundNummer = new javax.swing.JLabel();
        jTextFieldArtikelNummer = new javax.swing.JTextField();
        jTextFieldFarbe = new javax.swing.JTextField();
        jTextFieldGroesse = new javax.swing.JTextField();
        jTextFieldVariante = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldPreisGrossBasis = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldPreisVarianten = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldPreisGesamt = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldArtNummerFamak = new javax.swing.JTextField();
        jTextFieldFarbNum = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldGroesseFamak = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldVariantenFamak = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldGTIN = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldPosGrID = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelKundenAnlegen.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Kunden Daten", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Kunden Nummer");

        jLabel2.setText("Farbe");

        jLabel3.setText("Groesse");

        jLabel4.setText("Variante");

        jLabel5.setText("ArtikNum.");

        jLabel6.setText("Preis");

        jTextFieldPreisGrossBasis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldPreisGrossBasisKeyPressed(evt);
            }
        });

        jLabel7.setText("Größenbasis");

        jLabel8.setText("Varianten");

        jTextFieldPreisVarianten.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldPreisVariantenKeyPressed(evt);
            }
        });

        jLabel9.setText("Gesamt");

        jTextFieldPreisGesamt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPreisGesamtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelKundenAnlegenLayout = new javax.swing.GroupLayout(jPanelKundenAnlegen);
        jPanelKundenAnlegen.setLayout(jPanelKundenAnlegenLayout);
        jPanelKundenAnlegenLayout.setHorizontalGroup(
            jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelKundenAnlegenLayout.createSequentialGroup()
                .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelKundenAnlegenLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelKundenAnlegenLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelKundenAnlegenLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel5))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldArtikelNummer, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldFarbe)
                    .addComponent(jTextFieldGroesse)
                    .addComponent(jTextFieldVariante)
                    .addComponent(jLabelkundNummer, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelKundenAnlegenLayout.createSequentialGroup()
                        .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jTextFieldPreisGrossBasis, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldPreisVarianten))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 14, Short.MAX_VALUE)
                        .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldPreisGesamt, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelKundenAnlegenLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(9, 9, 9)))))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanelKundenAnlegenLayout.setVerticalGroup(
            jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelKundenAnlegenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabelkundNummer, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldArtikelNummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldFarbe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldGroesse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldVariante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelKundenAnlegenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldPreisGrossBasis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldPreisVarianten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldPreisGesamt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dastex Daten", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel10.setText("GTIN (Famak, KdNr.)");

        jLabel11.setText("ArtikNum.");

        jLabel12.setText("Größe");

        jLabel13.setText("FarbNum.");

        jLabel14.setText("Varianten");

        jLabel15.setText("GTIN");

        jLabel16.setText("PosGrID");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextFieldFarbNum)
                                .addGap(24, 24, 24)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldGroesseFamak, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldArtNummerFamak)
                            .addComponent(jTextFieldVariantenFamak)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextFieldGTIN)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16)
                                .addGap(2, 2, 2)
                                .addComponent(jTextFieldPosGrID, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10)))
                .addGap(37, 37, 37))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldArtNummerFamak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldFarbNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldGroesseFamak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldVariantenFamak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldGTIN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jTextFieldPosGrID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jButton1.setText("Übernehmen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Abbrechen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelKundenAnlegen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jButton1)
                .addGap(32, 32, 32)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelKundenAnlegen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldPreisGesamtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPreisGesamtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPreisGesamtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //System.out.println("1"+"/"+kundPrufer.getKundenArtikelNummer()+"/"+ kundPrufer.getFarbeNummer()+"/"+kundPrufer.getGroesse()+"/"+kundPrufer.getVarNummer()+"/"+kundPrufer.getGtin()+"/"+kundPrufer.getPosGrId());
        String meldung = jlieferDaoInterface.gtinStammsatzAnderung("0", jTextFieldArtNummerFamak.getText(), jTextFieldFarbNum.getText(), jTextFieldGroesseFamak.getText(), jTextFieldVariantenFamak.getText(), jTextFieldGTIN.getText(), jTextFieldPosGrID.getText());
        String indicator="";
        String message;
        
        System.out.println(meldung);
        
        if (meldung.length()> 4) {
            if (meldung.contains("-")) {
                indicator = "E-";
            } else
            {
                indicator = "E";
            }
                
                message = jlieferDaoInterface.getMeldung("1", indicator);
                System.out.println(message);
        }else
            {
              message = jlieferDaoInterface.getMeldung("1", meldung);  
            }
        

        String[] parts = message.split("--");
        String part1;
        if (indicator.equals("")) {
            part1 = parts[0];
        }else
        {
          part1 = parts[0]+" "+meldung;  
        }
       
        String part2 = parts[1]; 
        
        
//
        if (meldung.equals("10")) {

            List<Kund> kunds = jlieferDaoInterface.getListKundGtin(jTextFieldGTIN.getText());
            kundPrufer.setFarbe(jTextFieldFarbe.getText());
            kundPrufer.setGroesse(jTextFieldGroesse.getText());
            kundPrufer.setVarNummer(jTextFieldVariante.getText());
            JDialogGTINAndern dialogGTINAndern = new JDialogGTINAndern(this,true, kunds, part1, dbUrl, kundPruferFamak, kundPrufer,jTextFieldPreisGrossBasis.getText(), jTextFieldPreisVarianten.getText());
            dialogGTINAndern.setVisible(true);
            meldung = dialogGTINAndern.getMeldung();
            System.out.println(meldung);
            System.out.println("we are here");

        }else {
            JOptionPane.showMessageDialog(null,
                    part1,
                    part2,
                    JOptionPane.WARNING_MESSAGE);
        }
        
        if (((meldung.equals("0") || meldung.equals("1")) && !jTextFieldGTIN.getText().equals("")) || meldung.length() > 4) {
            String gtinParam;
            String message2;
            if (meldung.length() > 4) {
                gtinParam = meldung;
            } else {
                gtinParam = jTextFieldGTIN.getText();
            }
            String preisGrossBasis = jTextFieldPreisGrossBasis.getText().replace(",", ".");
            String preisVarianten = jTextFieldPreisVarianten.getText().replace(",", ".");
            kundPrufer.setFarbe(jTextFieldFarbe.getText());
            kundPrufer.setGroesse(jTextFieldGroesse.getText());
            kundPrufer.setVarNummer(jTextFieldVariante.getText());
            String meldung2 = jlieferDaoInterface.anlegenAndern("0", kundPrufer.getKundNummer(), kundPrufer.getKundenArtikelNummer(), jTextFieldFarbe.getText(), jTextFieldGroesse.getText(), kundPrufer.getVariante(), gtinParam, jTextFieldPosGrID.getText(), preisGrossBasis, preisVarianten);
            String indcator = "";
            message2 = jlieferDaoInterface.getMeldung("2", meldung2);
            System.out.println("meldung2:" + meldung2);
            
            
            if (meldung2.length()>4) {
               indcator="E";
               message2 = jlieferDaoInterface.getMeldung("2", indcator);
            }else
            {
                message2 = jlieferDaoInterface.getMeldung("2", meldung2);
                System.out.println("message2:" + message2);
            }
            String[] parts2 = message2.split("--");
            String part1_1; // 004
            if (indcator.equals("")) {
                part1_1 = parts2[0];
            }else
            {
                part1_1 = parts2[0]+" "+meldung2;
            }
            String part2_1 = parts2[1]; // 034556
            switch (meldung2) {
                case "30":
                    //System.out.println(kundPrufer.getKundNummer()+"-"+ kundPrufer.getKundenArtikelNummer()+"-"+ kundPrufer.getFarbe()+"-"+ kundPrufer.getGroesse()+"-"+ kundPrufer.getVariante()+"-"+gtinParam);
                   kundPrufer.setFarbe(jTextFieldFarbe.getText());
                   kundPrufer.setGroesse(jTextFieldGroesse.getText());
                   kundPrufer.setVarNummer(jTextFieldVariante.getText());
                    List<LieferKundPrufer> listGtinAnderung = jlieferDaoInterface.getListGtinAnderung(kundPrufer.getKundNummer(), kundPrufer.getKundenArtikelNummer(), jTextFieldFarbe.getText(), jTextFieldGroesse.getText(), kundPrufer.getVariante(), gtinParam);
                   
                    JDialogKundenArtikelDatenAndern artikelDatenAndern = new JDialogKundenArtikelDatenAndern(this, true, listGtinAnderung, kundPruferFamak, kundPrufer, this.dbUrl, gtinParam, jTextFieldPreisGrossBasis.getText(), jTextFieldPreisVarianten.getText());
                    artikelDatenAndern.setVisible(true);
                    //meldung = artikelDatenAndern.getMeldung();
                    break;
                case "38":
                    int reply = JOptionPane.showConfirmDialog(null, message2, "Actung", JOptionPane.YES_NO_OPTION);
                    
                    if (reply == JOptionPane.YES_OPTION) {
                        String meldung3 = jlieferDaoInterface.anlegenAndern("1", kundPrufer.getKundNummer(), kundPrufer.getKundenArtikelNummer(), jTextFieldFarbe.getText(), jTextFieldGroesse.getText(), jTextFieldVariante.getText(), gtinParam, jTextFieldPosGrID.getText(), preisGrossBasis, preisVarianten);
                        String message3 = jlieferDaoInterface.getMeldung("2", meldung3);
                        System.out.println("meldung3:" + meldung3);
                        System.out.println("message3:" + message3);
                        
                   String[] parts3 = message3.split("--");
                   String part1_3 = parts3[0]; // 004
                   String part2_3 = parts3[1]; // 034556
                   JOptionPane.showMessageDialog(null,
                    part1_3,
                    part2_3,
                    JOptionPane.WARNING_MESSAGE);
                    }   break;
                default:
                    JOptionPane.showMessageDialog(null,
                            part1_1,
                            part2_1,
                            JOptionPane.WARNING_MESSAGE);
                    break;
            }
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextFieldPreisGrossBasisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPreisGrossBasisKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()== KeyEvent.VK_ENTER) {
            varianten = Float.parseFloat(jTextFieldPreisVarianten.getText().replace(",", "."));
            grossenBasis = (Float.parseFloat(jTextFieldPreisGrossBasis.getText().replace(",", ".")));
            jTextFieldPreisGesamt.setText(String.format("%.2f",varianten+grossenBasis));
        }
    }//GEN-LAST:event_jTextFieldPreisGrossBasisKeyPressed

    private void jTextFieldPreisVariantenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPreisVariantenKeyPressed
        // TODO add your handling code here:
          if (evt.getKeyCode()== KeyEvent.VK_ENTER) {
            varianten = Float.parseFloat(jTextFieldPreisVarianten.getText().replace(",", "."));
            grossenBasis = (Float.parseFloat(jTextFieldPreisGrossBasis.getText().replace(",", ".")));
            jTextFieldPreisGesamt.setText(String.format("%.2f",varianten+grossenBasis));
        }
    }//GEN-LAST:event_jTextFieldPreisVariantenKeyPressed

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
            java.util.logging.Logger.getLogger(JDialogGTIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogGTIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogGTIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogGTIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogGTIN dialog = new JDialogGTIN(new javax.swing.JFrame(), true, new LieferKundPrufer(), new LieferKundPrufer(),"","");
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelkundNummer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelKundenAnlegen;
    private javax.swing.JTextField jTextFieldArtNummerFamak;
    private javax.swing.JTextField jTextFieldArtikelNummer;
    private javax.swing.JTextField jTextFieldFarbNum;
    private javax.swing.JTextField jTextFieldFarbe;
    private javax.swing.JTextField jTextFieldGTIN;
    private javax.swing.JTextField jTextFieldGroesse;
    private javax.swing.JTextField jTextFieldGroesseFamak;
    private javax.swing.JTextField jTextFieldPosGrID;
    private javax.swing.JTextField jTextFieldPreisGesamt;
    private javax.swing.JTextField jTextFieldPreisGrossBasis;
    private javax.swing.JTextField jTextFieldPreisVarianten;
    private javax.swing.JTextField jTextFieldVariante;
    private javax.swing.JTextField jTextFieldVariantenFamak;
    // End of variables declaration//GEN-END:variables
}
