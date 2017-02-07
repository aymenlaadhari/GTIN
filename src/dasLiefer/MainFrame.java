/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dasLiefer;

import dao.JlieferDao;
import dao.JlieferDaoInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import model.LieferKund;
import model.ParameterKund;
import util.JTextFieldAutoCompletion;

/**
 *
 * @author aladhari
 */
public class MainFrame extends javax.swing.JFrame {
    private final DefaultTableModel tableModel;
    private JlieferDaoInterface jlieferDaoInterface;
    private List<String> nummers;
    private Properties systemPropertie;
    private final JTextFieldAutoCompletion kundNummerComp;
    private ParameterKund parameterKund;
    private final Object[] rowDataMuster = new Object[9];
    private boolean changed=false;
    private int count, increment;
    private String suchen, ersetzen;
    private List<LieferKund> liefkunds;
    private final JDialog dlgProgress;
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        initilize();
        loadPropertie("installation");
        initializeDataBase();
        tableModel = (DefaultTableModel) jTable.getModel();
        //nummers = new ArrayList<>();
        kundNummerComp = new JTextFieldAutoCompletion(jTextFieldKdNr, nummers);
        kundNummerComp.setDataCompletion(nummers);
        dlgProgress = new JDialog(this, "Bitte warten...", true);//true means that the dialog created is modal
        JLabel lblStatus = new JLabel("bearbeiten..."); // this is just a label in which you can indicate the state of the processing
        JProgressBar pbProgress = new JProgressBar(0, 100);
        pbProgress.setIndeterminate(true); //we'll use an indeterminate progress bar
        dlgProgress.add(BorderLayout.NORTH, lblStatus);
        dlgProgress.add(BorderLayout.CENTER, pbProgress);
        dlgProgress.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // prevent the user from closing the dialog
        dlgProgress.setSize(300, 90);
        dlgProgress.setResizable(false);
        dlgProgress.setLocationRelativeTo(getParent());
    }
    private void initializeDataBase() {

        String dburlProdukt = "jdbc:sqlanywhere:uid=" + systemPropertie.getProperty("uid")
                + ";pwd=" + systemPropertie.getProperty("pwd")
                + ";eng=" + systemPropertie.getProperty("eng")
                + ";database=" + systemPropertie.getProperty("database")
                + ";links=" + systemPropertie.getProperty("links")
                + "(host=" + systemPropertie.getProperty("host") + ")";
        jlieferDaoInterface = new JlieferDao(dburlProdukt);
        
        try {
            nummers = jlieferDaoInterface.getKundenNummers();
            System.out.println("the size is: "+nummers.size());
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error accessing Database : " + e.getMessage(),
                    "Inane error",
                    JOptionPane.ERROR_MESSAGE);
        }
        
       
    }
    private void initiliseFelder() {
        
        jTextFieldKdArtNr.setText("");
        jTextFieldKdFarbe.setText("");
        jTextFieldKdGroesse.setText("");
        jTextFieldKdVariant.setText("");
        jTextFieldMenge.setText("");
        jTextFieldkdPreis.setText("");
        jTextFieldKommission.setText("");
        jTextFieldKdArtNr.requestFocus();
        
    }
    private void initilize(){
        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
        Date dateFromOld = Date.from(instant);
        jXDatePickerToday.setDate(dateFromOld);
        jTextFieldErfasser.setText(System.getProperty("user.name"));
        jXDatePickerKdBestDat.getEditor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    jXDatePickerWunch.requestFocus();
                }
            }
        });
        jXDatePickerWunch.getEditor().addKeyListener(new KeyAdapter() {
             @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    jTextFieldErfasser.requestFocus();
                }
            }
});
        jXDatePickerToday.getEditor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    jTextFieldKposAktiv.requestFocus();
                }
            }
        });
        liefkunds= new ArrayList<>();
    }
    private Properties loadPropertie(String propertieName) {
        try {
            systemPropertie = new Properties();
            InputStream in = new FileInputStream("L:\\Bilder\\Angebote\\" + propertieName + ".properties");
            systemPropertie.load(in);
        } catch (IOException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.toString());
        }
        return systemPropertie;
    }
    private void configureTable(ParameterKund parameterKund) {

        jTextFieldKposAktiv.setEnabled(true);
        jTextFieldKdArtNr.setEnabled(true);
        jTextFieldKdFarbe.setEnabled(true);
        jTextFieldKdGroesse.setEnabled(true);
        jTextFieldKdVariant.setEnabled(true);
        jTextFieldMenge.setEnabled(true);
        jTextFieldkdPreis.setEnabled(true);
        jTextFieldKommission.setEnabled(true);
        jTextFieldKposAktiv.setText(parameterKund.getKd_Pos_activ());
        if (!parameterKund.getPos_Zaehler().equals("")) {
           increment=Integer.valueOf(parameterKund.getPos_Zaehler()); 
        }else{
            increment=1;
        }
        
        if (parameterKund.getArtikel_Nr().equals("N")) {
            jTextFieldKdArtNr.setEnabled(false);
        }
        if (parameterKund.getFarbe().equals("N")) {

            jTextFieldKdFarbe.setEnabled(false);
        }
        if (parameterKund.getGroesse().equals("N")) {

            jTextFieldKdGroesse.setEnabled(false);
        }
        if (parameterKund.getVariante().equals("N")) {

            jTextFieldKdVariant.setEnabled(false);
        }
        if (parameterKund.getMenge().equals("N")) {

            jTextFieldMenge.setEnabled(false);
        }
        if (parameterKund.getPreis().equals("N")) {

            jTextFieldkdPreis.setEnabled(false);
        }
        if (parameterKund.getKommission().equals("N")) {

            jTextFieldKommission.setEnabled(false);
        }
        suchen = parameterKund.getSuchen();
        ersetzen = parameterKund.getErsetzen();
        jTextFieldKdPosNr.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent de) {
                changed=true;
                System.out.println("changed");
               }

            @Override
            public void insertUpdate(DocumentEvent de) {
                changed=true;
                count = (Integer.valueOf(jTextFieldKdPosNr.getText()));
                System.out.println("changed");
               }

            @Override
            public void removeUpdate(DocumentEvent de) {
//                changed=true;
//                count = (Integer.valueOf(jTextFieldKdPosNr.getText()));
                System.out.println("changed");
                }
        }); 
        jTextFieldMenge.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent jc) {
                String text = ((JTextField) jc).getText();
                try {
                    BigDecimal value = new BigDecimal(text);
                    jc.setBackground(null);
                    return ((value.scale() <= Math.abs(4)));
                } catch (NumberFormatException e) {
                    if(text.equals(""))
                    {
                        return true;
                    }else
                    {
                    jc.setBackground(Color.RED);
                    return false; 
                    }
                   
                }
            }
        });
        jTextFieldKommission.setInputVerifier(new InputVerifier() {
             @Override
            public boolean verify(JComponent jc) {
                String text = ((JTextField) jc).getText();
                try {
                    BigDecimal value = new BigDecimal(text);
                    jc.setBackground(null);
                    return ((value.scale() <= Math.abs(4)));
                } catch (NumberFormatException e) {
                    if(text.equals(""))
                    {
                        return true;
                    }else
                    {
                    jc.setBackground(Color.RED);
                    return false; 
                    }
                   
                }
            }
        });
    }
    private void addToTable(LieferKund lieferKund) {
        if (jTable.getRowCount() == 0) {
            count = (Integer.valueOf(jTextFieldKdPosNr.getText()));
        }
        if (!changed) {
            if ((Integer.valueOf(jTextFieldKdPosNr.getText()) % 10) == 0) {

                if ((Integer.valueOf(jTextFieldKdPosNr.getText()) % 100) == 0) {
                    rowDataMuster[0] = (jTable.getRowCount() + increment) * 100;
                } else {
                    rowDataMuster[0] = (jTable.getRowCount() + increment) * 10;
                }
            } else {
                rowDataMuster[0] = (jTable.getRowCount() + increment);
            }
        } else {

            rowDataMuster[0] = count++;
        }
       
        rowDataMuster[1] = lieferKund.getArtikel_Nr();
        rowDataMuster[2] = lieferKund.getFarbe();
        rowDataMuster[3] = lieferKund.getGroesse();
        rowDataMuster[4] = lieferKund.getVariante();
        rowDataMuster[5] = lieferKund.getMenge();
        rowDataMuster[6] = lieferKund.getPreis();
        rowDataMuster[7] = lieferKund.getKommission();
        rowDataMuster[8] = lieferKund.getLagerNum();
        
    }
    private void populateJtable(){
        LieferKund lieferKund = new LieferKund();
        //lieferKund.setKd_Pos_activ(jTextFieldKposAktiv.getText());
        
        if ((!liefkunds.isEmpty()) && jTextFieldKdArtNr.getText().equals("")) {
            lieferKund.setArtikel_Nr(liefkunds.get(liefkunds.size() - 1).getArtikel_Nr());
        } else if (jTextFieldKdArtNr.getText().contains(suchen)) {
            String replaceAll = jTextFieldKdArtNr.getText().replaceAll(suchen, ersetzen);
            lieferKund.setArtikel_Nr(replaceAll);
        } else {
            lieferKund.setArtikel_Nr(jTextFieldKdArtNr.getText());
        }
        
        
        
        if ((!liefkunds.isEmpty()) && jTextFieldKdFarbe.getText().equals("")&& jTextFieldKdArtNr.getText().equals("")) {
           lieferKund.setFarbe(liefkunds.get(liefkunds.size() - 1).getFarbe()); 
        }else
        {
           lieferKund.setFarbe(jTextFieldKdFarbe.getText());  
        }
        
        
        lieferKund.setGroesse(jTextFieldKdGroesse.getText());
        lieferKund.setVariante(jTextFieldKdVariant.getText());
        lieferKund.setMenge(jTextFieldMenge.getText());
        
        
        
         if ((!liefkunds.isEmpty()) && jTextFieldkdPreis.getText().equals("")&& jTextFieldKdArtNr.getText().equals("")) {
           lieferKund.setPreis(liefkunds.get(liefkunds.size() - 1).getPreis()); 
        }else if(jTextFieldkdPreis.getText().contains(","))
        {
            String replaceAll = jTextFieldkdPreis.getText().replaceAll(",", ".");
           lieferKund.setPreis(replaceAll); 
        }
         
         
        
         if ((!liefkunds.isEmpty()) && jTextFieldKommission.getText().equals("")&& jTextFieldKdArtNr.getText().equals("")) {
           lieferKund.setKommission(liefkunds.get(liefkunds.size() - 1).getKommission()); 
        }else
        {
            lieferKund.setKommission(jTextFieldKommission.getText()); 
        }
         
         
        lieferKund.setLagerNum(jlieferDaoInterface.getLagerNr(jTextFieldKdNr.getText(), jTextFieldKdArtNr.getText(), jTextFieldKdFarbe.getText(),jTextFieldKdGroesse.getText(),jTextFieldKdVariant.getText()));

        addToTable(lieferKund);
        liefkunds.add(lieferKund);
        tableModel.addRow(rowDataMuster);
        jTable.setRowSelectionInterval(jTable.getRowCount() - 1, jTable.getRowCount() - 1);
        jTable.scrollRectToVisible(new Rectangle(jTable.getCellRect(jTable.getRowCount() - 1, 0, true)));
        System.out.println(jTable.getValueAt(jTable.getSelectedRow(), 0).toString());
        //jTextFieldKdPosNr.setText(jTable.getValueAt(jTable.getSelectedRow(), 0).toString());
       
    }
    private void insertIntoDb(){
        
         SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
             boolean recorded;
            @Override
            protected Void doInBackground() throws Exception {

                 SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        //Date date = new Date();
        String dateBest="1900/01/01";
        String dateTod="1900/01/01";
        String wunchDat="1900/01/01";

        if (jXDatePickerKdBestDat.getDate() != null) {
            dateBest = format.format(jXDatePickerKdBestDat.getDate());
            System.out.println(dateBest);
        }
        if (jXDatePickerKdBestDat.getDate() != null) {
            dateTod = format.format(jXDatePickerKdBestDat.getDate());
        }
        if (jXDatePickerWunch.getDate() != null) {
            wunchDat = format.format(jXDatePickerWunch.getDate());
        }
                // TODO add your handling code here:
                try {
                        recorded=jlieferDaoInterface.updateTableGin(jTextFieldKdNr.getText(), jTextFieldKdBestNr.getText(), dateBest, wunchDat, jTextFieldErfasser.getText(), dateTod, jTextFieldKposAktiv.getText(), liefkunds);

                } catch (Exception ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
                return null;
            }

            @Override
            protected void done() {
                dlgProgress.dispose();//close the modal dialog
                //JOptionPane.showMessageDialog(null, "Successeful recorded");
                if (recorded) {
                    JOptionPane.showMessageDialog(null, "Successeful recorded");
                    
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Error when recording "+jlieferDaoInterface.getException(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        sw.execute();
        dlgProgress.setVisible(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldKdNr = new javax.swing.JTextField();
        jTextFieldKdBestNr = new javax.swing.JTextField();
        jTextFieldErfasser = new javax.swing.JTextField();
        jTextFieldKposAktiv = new javax.swing.JTextField();
        jXDatePickerToday = new org.jdesktop.swingx.JXDatePicker();
        jXDatePickerKdBestDat = new org.jdesktop.swingx.JXDatePicker();
        jXDatePickerWunch = new org.jdesktop.swingx.JXDatePicker();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldKdPosNr = new javax.swing.JTextField();
        jTextFieldKdArtNr = new javax.swing.JTextField();
        jTextFieldKdFarbe = new javax.swing.JTextField();
        jTextFieldKdGroesse = new javax.swing.JTextField();
        jTextFieldKdVariant = new javax.swing.JTextField();
        jTextFieldMenge = new javax.swing.JTextField();
        jTextFieldkdPreis = new javax.swing.JTextField();
        jTextFieldKommission = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jButtonAdd = new javax.swing.JButton();
        jButtonSaveInDb = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Kopfdaten", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("KdNr");

        jLabel2.setText("KdBestNr");

        jLabel3.setText("KdBestDat");

        jLabel4.setText("KdWunschDat");

        jLabel5.setText("Erfasser");

        jLabel6.setText("ErfDatum");

        jLabel7.setText("KdPos_aktiv");

        jTextFieldKdNr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldKdNrKeyPressed(evt);
            }
        });

        jTextFieldKdBestNr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldKdBestNrKeyPressed(evt);
            }
        });

        jTextFieldErfasser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldErfasserKeyPressed(evt);
            }
        });

        jTextFieldKposAktiv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldKposAktivKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jTextFieldKdNr, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(jTextFieldKdBestNr, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jXDatePickerKdBestDat, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(59, 59, 59)
                        .addComponent(jLabel3)
                        .addGap(65, 65, 65)))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jXDatePickerWunch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jTextFieldErfasser, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(59, 59, 59)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jXDatePickerToday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(33, 33, 33)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldKposAktiv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldKdNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKdBestNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldErfasser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKposAktiv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jXDatePickerToday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jXDatePickerKdBestDat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jXDatePickerWunch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Positions-Daten", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel8.setText("KdPosNr");

        jLabel9.setText("KdArtNr");

        jLabel10.setText("KdFarbe");

        jLabel11.setText("KdGroesse");

        jLabel12.setText("Kdvariant");

        jLabel13.setText("KdMenge");

        jLabel14.setText("KdPreis");

        jLabel15.setText("Kommission");

        jTextFieldKdPosNr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldKdPosNrMouseClicked(evt);
            }
        });
        jTextFieldKdPosNr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldKdPosNrKeyPressed(evt);
            }
        });

        jTextFieldKdArtNr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldKdArtNrMouseClicked(evt);
            }
        });
        jTextFieldKdArtNr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldKdArtNrKeyPressed(evt);
            }
        });

        jTextFieldKdFarbe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldKdFarbeMouseClicked(evt);
            }
        });
        jTextFieldKdFarbe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldKdFarbeKeyPressed(evt);
            }
        });

        jTextFieldKdGroesse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldKdGroesseMouseClicked(evt);
            }
        });
        jTextFieldKdGroesse.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldKdGroesseKeyPressed(evt);
            }
        });

        jTextFieldKdVariant.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldKdVariantMouseClicked(evt);
            }
        });
        jTextFieldKdVariant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldKdVariantKeyPressed(evt);
            }
        });

        jTextFieldMenge.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldMengeMouseClicked(evt);
            }
        });
        jTextFieldMenge.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldMengeKeyPressed(evt);
            }
        });

        jTextFieldkdPreis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldkdPreisMouseClicked(evt);
            }
        });
        jTextFieldkdPreis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldkdPreisKeyPressed(evt);
            }
        });

        jTextFieldKommission.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldKommissionMouseClicked(evt);
            }
        });
        jTextFieldKommission.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldKommissionKeyPressed(evt);
            }
        });

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "KdPosNr", "KdArtNr", "KdFarbe", "KdGroesse", "Kdvariant", "KdMenge", "KdPreis", "Kommission", "Lager-Nr"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, true, false
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

        jButtonAdd.setText("---v");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel8)
                        .addGap(77, 77, 77)
                        .addComponent(jLabel9)
                        .addGap(71, 71, 71)
                        .addComponent(jLabel10))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jTextFieldKdPosNr, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jTextFieldKdArtNr, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldKdFarbe, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldKdGroesse, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jTextFieldKdVariant, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jTextFieldMenge, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jTextFieldkdPreis, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 29, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addGap(65, 65, 65)
                        .addComponent(jLabel13)
                        .addGap(66, 66, 66)
                        .addComponent(jLabel14)
                        .addGap(61, 61, 61)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextFieldKommission, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAdd))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel15)))
                .addContainerGap(89, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldKdPosNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKdArtNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKdFarbe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKdGroesse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKdVariant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMenge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldkdPreis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKommission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAdd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(198, 198, 198))
        );

        jButtonSaveInDb.setText("Hinfügen");
        jButtonSaveInDb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveInDbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(456, 456, 456)
                .addComponent(jButtonSaveInDb, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSaveInDb, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldKdNrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdNrKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            initiliseFelder();
              jTextFieldKdBestNr.setText("");
        
        jXDatePickerWunch.setDate(null);
        jXDatePickerKdBestDat.setDate(null);
            tableModel.setRowCount(0);
           parameterKund= jlieferDaoInterface.getKundenParameter(jTextFieldKdNr.getText());
            configureTable(parameterKund);
            jTextFieldKdPosNr.setText(parameterKund.getPos_Zaehler());
            jTextFieldKdBestNr.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldKdNrKeyPressed

    private void jTextFieldKdPosNrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKdPosNrMouseClicked
        // TODO add your handling code here:
         if (evt.getClickCount() == 2) {
             jTextFieldKdPosNr.setEnabled(true);
             jTextFieldKdPosNr.requestFocus();
         }
    }//GEN-LAST:event_jTextFieldKdPosNrMouseClicked

    private void jTextFieldKdArtNrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKdArtNrMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
             jTextFieldKdArtNr.setEnabled(true);
             jTextFieldKdArtNr.requestFocus();
         }
    }//GEN-LAST:event_jTextFieldKdArtNrMouseClicked

    private void jTextFieldKdFarbeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKdFarbeMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
             jTextFieldKdFarbe.setEnabled(true);
             jTextFieldKdFarbe.requestFocus();
         }
    }//GEN-LAST:event_jTextFieldKdFarbeMouseClicked

    private void jTextFieldKdGroesseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKdGroesseMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
             jTextFieldKdGroesse.setEnabled(true);
             jTextFieldKdGroesse.requestFocus();
         }
    }//GEN-LAST:event_jTextFieldKdGroesseMouseClicked

    private void jTextFieldKdVariantMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKdVariantMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
             jTextFieldKdVariant.setEnabled(true);
             jTextFieldKdVariant.requestFocus();
         }
    }//GEN-LAST:event_jTextFieldKdVariantMouseClicked

    private void jTextFieldMengeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldMengeMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
             jTextFieldMenge.setEnabled(true);
             jTextFieldMenge.requestFocus();
         }
    }//GEN-LAST:event_jTextFieldMengeMouseClicked

    private void jTextFieldkdPreisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldkdPreisMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
             jTextFieldkdPreis.setEnabled(true);
             jTextFieldkdPreis.requestFocus();
         }
    }//GEN-LAST:event_jTextFieldkdPreisMouseClicked

    private void jTextFieldKommissionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKommissionMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
             jTextFieldKommission.setEnabled(true);
             jTextFieldKommission.requestFocus();
         }
    }//GEN-LAST:event_jTextFieldKommissionMouseClicked

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO add your handling code here:
        populateJtable();
        initiliseFelder();
        configureTable(parameterKund);
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int[] rows = jTable.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                //MusterArtikel musterArtikel = musterArtikels.get(rows[i] - i);
                tableModel.removeRow(rows[i] - i);
                //musterArtikels.remove(musterArtikel);
            }
            if (jTable.getRowCount() == 0) {
                //musterArtikels = new ArrayList<>();
                //selectedMusterArtikel = new MusterArtikel();
                tableModel.setRowCount(0);
                //initilizeFelder();
                
            }

            //refreshPosition(jTable.getRowCount());
            tableModel.fireTableDataChanged();

        }
    }//GEN-LAST:event_jTableKeyPressed

    private void jTextFieldKommissionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKommissionKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextFieldKommission.isEnabled()) {
                populateJtable();
                initiliseFelder();
                configureTable(parameterKund);
            }
        }
        
    }//GEN-LAST:event_jTextFieldKommissionKeyPressed

    private void jTextFieldkdPreisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldkdPreisKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextFieldKommission.isEnabled()) {
                jTextFieldKommission.requestFocus();
            } else {
                populateJtable();
                initiliseFelder();
                configureTable(parameterKund);
            }

        }
    }//GEN-LAST:event_jTextFieldkdPreisKeyPressed

    private void jTextFieldMengeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMengeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextFieldkdPreis.isEnabled()) {
                jTextFieldkdPreis.requestFocus();
            } else if (jTextFieldKommission.isEnabled()) {
                jTextFieldKommission.requestFocus();
            } else {
                populateJtable();
                initiliseFelder();
                configureTable(parameterKund);
            }
        }
    }//GEN-LAST:event_jTextFieldMengeKeyPressed

    private void jTextFieldKdVariantKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdVariantKeyPressed
        // TODO add your handling code here:
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
          if (jTextFieldMenge.isEnabled()) {
                jTextFieldMenge.requestFocus();
            } else if (jTextFieldkdPreis.isEnabled()) {
                jTextFieldkdPreis.requestFocus();
            } else if (jTextFieldKommission.isEnabled()) {
                jTextFieldKommission.requestFocus();
            } else {
                populateJtable();
                initiliseFelder();
                configureTable(parameterKund);
            }
        }
    }//GEN-LAST:event_jTextFieldKdVariantKeyPressed

    private void jTextFieldKdGroesseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdGroesseKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextFieldKdVariant.isEnabled()) {
                jTextFieldKdVariant.requestFocus();
            } else if (jTextFieldMenge.isEnabled()) {
                jTextFieldMenge.requestFocus();
            } else if (jTextFieldkdPreis.isEnabled()) {
                jTextFieldkdPreis.requestFocus();
            } else if (jTextFieldKommission.isEnabled()) {
                jTextFieldKommission.requestFocus();
            } else {
                populateJtable();
                initiliseFelder();
                configureTable(parameterKund);
            }
        }
    }//GEN-LAST:event_jTextFieldKdGroesseKeyPressed

    private void jTextFieldKdFarbeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdFarbeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextFieldKdGroesse.isEnabled()) {
                jTextFieldKdGroesse.requestFocus();
            } else if (jTextFieldKdVariant.isEnabled()) {
                jTextFieldKdVariant.requestFocus();
            } else if (jTextFieldMenge.isEnabled()) {
                jTextFieldMenge.requestFocus();
            } else if (jTextFieldkdPreis.isEnabled()) {
                jTextFieldkdPreis.requestFocus();
            } else if (jTextFieldKommission.isEnabled()) {
                jTextFieldKommission.requestFocus();
            } else {
                populateJtable();
                initiliseFelder();
                configureTable(parameterKund);
            }
        }
    }//GEN-LAST:event_jTextFieldKdFarbeKeyPressed

    private void jTextFieldKdArtNrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdArtNrKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextFieldKdFarbe.isEnabled()) {
                jTextFieldKdFarbe.requestFocus();
                //populateJtable();
                //initiliseFelder();
            } else if (jTextFieldKdGroesse.isEnabled()) {
                jTextFieldKdGroesse.requestFocus();
            } else if (jTextFieldKdVariant.isEnabled()) {
                jTextFieldKdVariant.requestFocus();
            } else if (jTextFieldMenge.isEnabled()) {
                jTextFieldMenge.requestFocus();
            } else if (jTextFieldkdPreis.isEnabled()) {
                jTextFieldkdPreis.requestFocus();
            } else if (jTextFieldKommission.isEnabled()) {
                jTextFieldKommission.requestFocus();
            } else {
                populateJtable();
                initiliseFelder();
                configureTable(parameterKund);
            }
        }
    }//GEN-LAST:event_jTextFieldKdArtNrKeyPressed

    private void jTextFieldKdPosNrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdPosNrKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextFieldKdPosNr.isEnabled()) {
                jTextFieldKdArtNr.requestFocus();
                //populateJtable();
                //initiliseFelder();
            }
        }
    }//GEN-LAST:event_jTextFieldKdPosNrKeyPressed

    private void jTextFieldErfasserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldErfasserKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jXDatePickerToday.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldErfasserKeyPressed

    private void jTextFieldKdBestNrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdBestNrKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jXDatePickerKdBestDat.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldKdBestNrKeyPressed

    private void jTextFieldKposAktivKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKposAktivKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldKdArtNr.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldKposAktivKeyPressed

    private void jButtonSaveInDbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveInDbActionPerformed
        // TODO add your handling code here:
        insertIntoDb();
    }//GEN-LAST:event_jButtonSaveInDbActionPerformed

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonSaveInDb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextFieldErfasser;
    private javax.swing.JTextField jTextFieldKdArtNr;
    private javax.swing.JTextField jTextFieldKdBestNr;
    private javax.swing.JTextField jTextFieldKdFarbe;
    private javax.swing.JTextField jTextFieldKdGroesse;
    private javax.swing.JTextField jTextFieldKdNr;
    private javax.swing.JTextField jTextFieldKdPosNr;
    private javax.swing.JTextField jTextFieldKdVariant;
    private javax.swing.JTextField jTextFieldKommission;
    private javax.swing.JTextField jTextFieldKposAktiv;
    private javax.swing.JTextField jTextFieldMenge;
    private javax.swing.JTextField jTextFieldkdPreis;
    private org.jdesktop.swingx.JXDatePicker jXDatePickerKdBestDat;
    private org.jdesktop.swingx.JXDatePicker jXDatePickerToday;
    private org.jdesktop.swingx.JXDatePicker jXDatePickerWunch;
    // End of variables declaration//GEN-END:variables
}
