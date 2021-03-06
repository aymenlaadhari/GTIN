/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dasLiefer;

import dasLieferDialog.JDialogDoppelErfassung;
import dasLieferdao.JlieferDao;
import dasLieferdao.JlieferDaoInterface;
import dasLieferDialog.JDialogGTIN;
import dasLieferDialog.JDialogKundenbestellung;
import dasLieferDialog.JDialogListKopfDaten;
import dasLieferDialog.JDialogStatus;
import dasLieferDialog.JDialogSuchen;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import model.KopfDaten;
import model.LieferKund;
import model.LieferKundDoppel;
import model.LieferKundPrufer;
import model.ParameterKund;
import model.Status;
import model.VarPreis;
import model.VerwendeteMengenstaffel;
import util.JTextFieldAutoCompletion;
import util.TableCellListener;

/**
 *
 * @author aladhari
 */
public class MainFrame extends javax.swing.JFrame {

    private final DefaultTableModel tableModel, tableModelPrufer, tableModelPreislIste;
    private JlieferDaoInterface jlieferDaoInterface;
    private List<String> nummers;
    private Properties systemPropertie;
    private final JTextFieldAutoCompletion kundNummerComp;
    private ParameterKund parameterKund;
    private final Object[] rowDataMuster = new Object[13];
    private final Object[] rowDataPrufung = new Object[31];
    private final Object[] rowDataPreisListe = new Object[5];
    private boolean changedJtextPosition;
    private int count, increment;
    private float summe;
    private String suchen, ersetzen;
    private String statusIn = "";
    private List<LieferKund> liefkunds;
    private List<LieferKundPrufer> liefPrufers;
    private final JDialog dlgProgress;
    private String dbUrl;
    private boolean ctrlPressed;
    JPopupMenu popupMenu, popupMenueingaben;
    private LieferKundPrufer kundPruferFamak, kundPrufer;
    private VerwendeteMengenstaffel verwendeteMengenstaffel;
    private final ListSelectionModel listSelectionModel;
    private final TableCellListener tclLieferKund;
    private TableCellListener tclLiefKund;
    Integer indexPos;
    private KopfDaten kopfDaten;
    boolean result;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        initilize();
        loadPropertie("installation");
        initializeDataBase();
        kopfDaten = new KopfDaten();
        tableModel = (DefaultTableModel) jTableAllgemein.getModel();
        tableModelPrufer = (DefaultTableModel) jTablePrufer.getModel();
        tableModelPrufer.setRowCount(0);
        jTablePrufer.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTablePrufer.setPreferredScrollableViewportSize(Toolkit.getDefaultToolkit().getScreenSize());
        tableModelPreislIste = (DefaultTableModel) jTablePreisListe.getModel();
        tableModelPreislIste.setRowCount(0);
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
        jCheckBoxPreisSofort.setSelected(true);
        jLabelSpeichern1und2.setText("<html>Speichern1<br>Speichern2</html>");
        jLabelggf.setText("<html>(ggf.903<br>öffnen)</html>");
        jLabel22.setText("<html>913 öffnen, prüfen<br>(ändern, speichern)</html>");
        jLabel18.setText("<html>Prozess 900<br>Prozess 0</html>");
        jLabel20.setText("<html>Prüfung in<br>GTIN überprüfen</html>");
        jLabel28.setText("<html>Speicher0<br>ohne Prüf.</html>");
        jLabel30.setText("<html>Daten in Famak<br>eingeben.</html>");
        
        jLabelSpeichern1und2.setOpaque(true);
        jLabelggf.setOpaque(true);
        jLabel22.setOpaque(true);
        jLabel28.setOpaque(true);
        jLabel30.setOpaque(true);
        jLabel20.setOpaque(true);

        popupMenu = new JPopupMenu();
        popupMenueingaben = new JPopupMenu();
        JMenuItem menuItemAdd = new JMenuItem("GTIN prüfen");
        JMenuItem menuItemAddSuche = new JMenuItem("Suchen");
        menuItemAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String preisVariante = jlieferDaoInterface.getPreisVariante(kundPruferFamak.getPosGrId());

                JDialogGTIN dialogGTIN = new JDialogGTIN(MainFrame.this, true, kundPrufer, kundPruferFamak, preisVariante, dbUrl);
                dialogGTIN.setVisible(true);
            }
        });
        menuItemAddSuche.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                if (!liefkunds.get(jTableAllgemein.getSelectedRow()).getId().isEmpty()) {
                    JDialogSuchen dialogSuchen = new JDialogSuchen(MainFrame.this, liefkunds.get(jTableAllgemein.getSelectedRow()), jTextFieldKdNr.getText(), dbUrl);
                    dialogSuchen.setVisible(true);

                }
            }
        });

        popupMenu.add(menuItemAdd);
        popupMenueingaben.add(menuItemAddSuche);
        ctrlPressed = false;

        listSelectionModel = jTableAllgemein.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {

                        if (jCheckBoxPreisSofort.isSelected()) {
                            populateJtablePreisListe();
                        }
                    }
                }
            }
        });
        tclLieferKund = new TableCellListener(jTableAllgemein, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean changeArtnum = false, changeFarb = false, changeVar = false;
                tclLiefKund = (TableCellListener) e.getSource();
                switch (tclLiefKund.getColumn()) {

                    case 0:
                        liefkunds.get(tclLiefKund.getRow()).setPosiNummer(tclLiefKund.getNewValue().toString());
                        liefkunds.get(tclLiefKund.getRow()).setUbergabe("X");
                        changeArtnum = true;
                        break;
                        
                    case 1:
                        liefkunds.get(tclLiefKund.getRow()).setArtikel_Nr(tclLiefKund.getNewValue().toString());
                        liefkunds.get(tclLiefKund.getRow()).setUbergabe("X");
                        changeArtnum = true;
                        break;

                    case 2:
                        liefkunds.get(tclLiefKund.getRow()).setFarbe(tclLiefKund.getNewValue().toString());
                        liefkunds.get(tclLiefKund.getRow()).setUbergabe("X");
                        changeFarb = true;
                        break;

                    case 3:
                        liefkunds.get(tclLiefKund.getRow()).setGroesse(tclLiefKund.getNewValue().toString());
                        liefkunds.get(tclLiefKund.getRow()).setUbergabe("X");
                        break;

                    case 4:
                        liefkunds.get(tclLiefKund.getRow()).setVariante(tclLiefKund.getNewValue().toString());
                        liefkunds.get(tclLiefKund.getRow()).setUbergabe("X");
                        changeVar = true;
                        break;

                    case 5:

                        liefkunds.get(tclLiefKund.getRow()).setMenge(tclLiefKund.getNewValue().toString());
                        liefkunds.get(tclLiefKund.getRow()).setUbergabe("X");
                        changeVar = true;
                        break;

                    case 6:
                        if (tclLiefKund.getNewValue().toString().contains(",")) {
                            String replaceAll = tclLiefKund.getNewValue().toString().replaceAll(",", ".");
                            liefkunds.get(tclLiefKund.getRow()).setPreis(replaceAll);
                        } else {
                            liefkunds.get(tclLiefKund.getRow()).setPreis(tclLiefKund.getNewValue().toString());
                        }
                        liefkunds.get(tclLiefKund.getRow()).setUbergabe("X");
                        changeVar = true;
                        break;

                    case 7:
                        liefkunds.get(tclLiefKund.getRow()).setKommission(tclLiefKund.getNewValue().toString());
                        liefkunds.get(tclLiefKund.getRow()).setUbergabe("X");
                        changeVar = true;
                        break;

                    case 8:
                        liefkunds.get(tclLiefKund.getRow()).setLagerNum(tclLiefKund.getNewValue().toString());
                        liefkunds.get(tclLiefKund.getRow()).setUbergabe("X");
                        changeVar = true;
                        break;

                    case 9:
                        liefkunds.get(tclLiefKund.getRow()).setSumme(tclLiefKund.getNewValue().toString());

                        break;
                }
                refreshTableLiefKund(tclLiefKund.getRow(), changeArtnum, changeFarb, changeVar);
            }
        });
        jTextFieldKdBestNr.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent de) {
                kopfDaten.setKdBestnum(jTextFieldKdBestNr.getText());
                jTextFieldKdBestNrCopi.setText(jTextFieldKdBestNr.getText());
                 }

            @Override
            public void insertUpdate(DocumentEvent de) {
               kopfDaten.setKdBestnum(jTextFieldKdBestNr.getText());
               jTextFieldKdBestNrCopi.setText(jTextFieldKdBestNr.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
            
                //kopfDaten.setKdBestnum(jTextFieldKdBestNr.getText());
            }
        });
        
        
    }
    
    private void checkKopfDaten(){
        if (kopfDaten == null) {
            kopfDaten = new KopfDaten();
        }
        kopfDaten.setKdNum(jTextFieldKdNr.getText());
        kopfDaten.setKdBestnum(jTextFieldKdBestNr.getText());
        kopfDaten.setKdBestDatum(new SimpleDateFormat("dd.MM.yyyy").format(jXDatePickerKdBestDat.getDate()));
        kopfDaten.setErfassDatum(new SimpleDateFormat("dd.MM.yyyy").format(jXDatePickerErfassung.getDate()));
        kopfDaten.setKdWunchDat(new SimpleDateFormat("dd.MM.yyyy").format(jXDatePickerWunch.getDate()));
        kopfDaten.setErfasser(jTextFieldErfasser.getText());
        kopfDaten.setKdBestnum(jTextFieldKdBestNr.getText());
        statusIn = kopfDaten.getStatus();
        
    }

    private void selecteItems() {
        kundPrufer = new LieferKundPrufer();
        kundPruferFamak = new LieferKundPrufer();

        int[] rows = jTablePrufer.getSelectedRows();
        if (!"".equals(liefPrufers.get(rows[0]).getPosGrId()) && liefPrufers.get(rows[0]).getId().equals("")) {
            kundPruferFamak = liefPrufers.get(rows[0]);

        } else if (!liefPrufers.get(rows[0]).getId().equals("")) {
            kundPrufer = liefPrufers.get(rows[0]);
        }

        if (!"".equals(liefPrufers.get(rows[1]).getPosGrId()) && liefPrufers.get(rows[1]).getId().equals("")) {
            kundPruferFamak = liefPrufers.get(rows[1]);

        } else if (!liefPrufers.get(rows[1]).getId().equals("")) {
            kundPrufer = liefPrufers.get(rows[1]);
        }
    }

    private void initializeDataBase() {
        dbUrl = "jdbc:sqlanywhere:uid=" + systemPropertie.getProperty("uid")
                + ";pwd=" + systemPropertie.getProperty("pwd")
                + ";eng=" + systemPropertie.getProperty("eng")
                + ";database=" + systemPropertie.getProperty("database")
                + ";links=" + systemPropertie.getProperty("links")
                + "(host=" + systemPropertie.getProperty("host") + ")";
        jlieferDaoInterface = new JlieferDao(dbUrl);

        try {
            nummers = jlieferDaoInterface.getKundenNummers();

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

    private void initilize() {
        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
        Date dateFromOld = Date.from(instant);
        jXDatePickerErfassung.setDate(dateFromOld);
        jXDatePickerBisDatum.setDate(dateFromOld);
        jTextFieldErfasser.setText(System.getProperty("user.name"));
        jXDatePickerKdBestDat.getEditor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    String dateBest = "1900/01/01";
                    if (jXDatePickerKdBestDat.getDate() != null) {
                        dateBest = format.format(jXDatePickerKdBestDat.getDate());
                    }
                    List<KopfDaten> kopfDatens = jlieferDaoInterface.getListKopfDaten(jTextFieldKdNr.getText(), jTextFieldKdBestNr.getText(), dateBest);
                    if (!kopfDatens.isEmpty()) {
                        JDialogListKopfDaten dialogListKopfDaten = new JDialogListKopfDaten(MainFrame.this, true, kopfDatens);
                        dialogListKopfDaten.setVisible(true);
                        if (dialogListKopfDaten.getSelectedKopfDatenIn()!= null) {
                        
                        kopfDaten = dialogListKopfDaten.getSelectedKopfDatenIn();
                        statusIn = kopfDaten.getStatus();
                        if (kopfDaten != null) {
                            DateFormat df = new SimpleDateFormat("dd.MM.yyyy"); 
                            Date dateWunsch = null;
                            Date dateErfass = null;
                            try {
                                dateWunsch = df.parse(kopfDaten.getKdWunchDat());
                                dateErfass = df.parse(kopfDaten.getErfassDatum());
                            } catch (ParseException ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            jXDatePickerWunch.setDate(dateWunsch);
                            jXDatePickerErfassung.setDate(dateErfass);
                            
                            List<LieferKund> lieferKundsIn = jlieferDaoInterface.getListLieferGenerated(kopfDaten.getKdNum(), kopfDaten.getKdBestnum(), kopfDaten.getKdBestDatum(), kopfDaten.getStatus());
                            liefkunds = lieferKundsIn;
                            
                            populateTableGenarate(lieferKundsIn);
                        } else {
                            jXDatePickerWunch.requestFocus();
                        }
                    }

                    } else {
                        jXDatePickerWunch.requestFocus();
                    }

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
        jXDatePickerErfassung.getEditor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    jTextFieldKposAktiv.requestFocus();
                }
            }
        });

        liefkunds = new ArrayList<>();
        liefPrufers = new ArrayList<>();

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
            increment = Integer.valueOf(parameterKund.getPos_Zaehler());
        } else {
            increment = 1;
        }

        if (!parameterKund.getArtikel_Nr().equals("J")) {
            jTextFieldKdArtNr.setEnabled(false);
        }
        if (!parameterKund.getFarbe().equals("J")) {

            jTextFieldKdFarbe.setEnabled(false);
        }
        if (!parameterKund.getGroesse().equals("J")) {

            jTextFieldKdGroesse.setEnabled(false);
        }
        if (!parameterKund.getVariante().equals("J")) {

            jTextFieldKdVariant.setEnabled(false);
        }
        if (!parameterKund.getMenge().equals("J")) {

            jTextFieldMenge.setEnabled(false);
        }
        if (!parameterKund.getPreis().equals("J")) {

            jTextFieldkdPreis.setEnabled(false);
        }
        if (!parameterKund.getKommission().equals("J")) {

            jTextFieldKommission.setEnabled(false);
        }
        if (parameterKund.getSuchen() == null) {
            suchen = "§§";
        } else {
            suchen = parameterKund.getSuchen();
        }

        ersetzen = parameterKund.getErsetzen();
        jTextFieldKdPosNr.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent de) {
                changedJtextPosition = true;

            }

            @Override
            public void insertUpdate(DocumentEvent de) {
                changedJtextPosition = true;
                count = (Integer.valueOf(jTextFieldKdPosNr.getText()));

            }

            @Override
            public void removeUpdate(DocumentEvent de) {

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
                    if (text.equals("")) {
                        return true;
                    } else {
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
                    if (text.equals("")) {
                        return true;
                    } else {
                        jc.setBackground(Color.RED);
                        return false;
                    }

                }
            }
        });
    }

    private void addToTable(LieferKund lieferKund, boolean changeInthtable) {

        if (jTableAllgemein.getRowCount() == 0) {

            count = (Integer.valueOf(jTextFieldKdPosNr.getText()));
        }

        if (changeInthtable) {

            rowDataMuster[0] = lieferKund.getPosiNummer();

        } else {

            if (changedJtextPosition) {
                rowDataMuster[0] = count++;
            } else if ((Integer.valueOf(jTextFieldKdPosNr.getText()) % 10) == 0) {

                if ((Integer.valueOf(jTextFieldKdPosNr.getText()) % 100) == 0) {
                    rowDataMuster[0] = (jTableAllgemein.getRowCount() + increment) * 100;
                } else {
                    rowDataMuster[0] = (jTableAllgemein.getRowCount() + increment) * 10;
                }
            } else {
                rowDataMuster[0] = (jTableAllgemein.getRowCount() + increment);
            }
            liefkunds.get(liefkunds.size() - 1).setPosiNummer(String.valueOf(rowDataMuster[0]));
        }
        rowDataMuster[1] = lieferKund.getArtikel_Nr();
        rowDataMuster[2] = lieferKund.getFarbe();
        rowDataMuster[3] = lieferKund.getGroesse();
        rowDataMuster[4] = lieferKund.getVariante();
        rowDataMuster[5] = lieferKund.getMenge();
        rowDataMuster[6] = lieferKund.getPreis();
        rowDataMuster[7] = lieferKund.getKommission();
        rowDataMuster[8] = lieferKund.getLagerNum();
        rowDataMuster[9] = lieferKund.getSumme();
        rowDataMuster[10] = lieferKund.getStatus();
        rowDataMuster[11] = lieferKund.getUbergabe();
        rowDataMuster[12] = lieferKund.getId();
    }

    private void addToTableNormal(LieferKund lieferKund) {
        rowDataMuster[0] = lieferKund.getPosiNummer();
        rowDataMuster[1] = lieferKund.getArtikel_Nr();
        rowDataMuster[2] = lieferKund.getFarbe();
        rowDataMuster[3] = lieferKund.getGroesse();
        rowDataMuster[4] = lieferKund.getVariante();
        rowDataMuster[5] = lieferKund.getMenge();
        rowDataMuster[6] = lieferKund.getPreis();
        rowDataMuster[7] = lieferKund.getKommission();
        rowDataMuster[8] = lieferKund.getLagerNum();
        rowDataMuster[9] = lieferKund.getSumme();
        rowDataMuster[10] = lieferKund.getStatus();
        rowDataMuster[11] = lieferKund.getUbergabe();
        rowDataMuster[12] = lieferKund.getId();
    }

    private void populateJtable() {

        LieferKund lieferKund = new LieferKund();
        statusIn = "";
        if ((!liefkunds.isEmpty()) && jTextFieldKdArtNr.getText().equals("")) {
            lieferKund.setArtikel_Nr(liefkunds.get(liefkunds.size() - 1).getArtikel_Nr());
        } else if (jTextFieldKdArtNr.getText().contains(suchen) && suchen != null) {
            String replaceAll = jTextFieldKdArtNr.getText().replaceAll(suchen, ersetzen);
            lieferKund.setArtikel_Nr(replaceAll);
        } else {
            lieferKund.setArtikel_Nr(jTextFieldKdArtNr.getText());
        }
        if ((!liefkunds.isEmpty()) && jTextFieldKdFarbe.getText().equals("") && jTextFieldKdArtNr.getText().equals("")) {
            lieferKund.setFarbe(liefkunds.get(liefkunds.size() - 1).getFarbe());
        } else {
            lieferKund.setFarbe(jTextFieldKdFarbe.getText());
        }
        lieferKund.setGroesse(jTextFieldKdGroesse.getText());
        lieferKund.setVariante(jTextFieldKdVariant.getText());
        lieferKund.setMenge(jTextFieldMenge.getText());

        if ((!liefkunds.isEmpty()) && jTextFieldkdPreis.getText().equals("") && jTextFieldKdArtNr.getText().equals("")) {
            lieferKund.setPreis(liefkunds.get(liefkunds.size() - 1).getPreis());
        } else if (jTextFieldkdPreis.getText().contains(",")) {
            String replaceAll = jTextFieldkdPreis.getText().replaceAll(",", ".");
            lieferKund.setPreis(replaceAll);
        } else {
            lieferKund.setPreis(jTextFieldkdPreis.getText());
        }
        if ((!liefkunds.isEmpty()) && jTextFieldKommission.getText().equals("") && jTextFieldKdArtNr.getText().equals("")) {
            lieferKund.setKommission(liefkunds.get(liefkunds.size() - 1).getKommission());
        } else {
            lieferKund.setKommission(jTextFieldKommission.getText());
        }

        lieferKund.setLagerNum(jlieferDaoInterface.getLagerNr(jTextFieldKdNr.getText(), lieferKund.getArtikel_Nr(), lieferKund.getFarbe(), lieferKund.getGroesse(), lieferKund.getVariante()));
        lieferKund.setUbergabe("X");
        System.out.println("statusIn before check= "+statusIn);
        checkKopfDaten();
        System.out.println("statusIn= "+statusIn);
        lieferKund.setStatus(statusIn);
        if (liefkunds.isEmpty()) {
            lieferKund.setSumme(lieferKund.getMenge());
        }
        
        liefkunds.add(lieferKund);

        String kdArtNum = lieferKund.getArtikel_Nr();
        String kdFarbe = lieferKund.getFarbe();
        String kdVariante = lieferKund.getVariante();
        ArrayList<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < liefkunds.size(); i++) {
            if (liefkunds.get(i).getArtikel_Nr().equals(kdArtNum) && liefkunds.get(i).getFarbe().equals(kdFarbe) && liefkunds.get(i).getVariante().equals(kdVariante)) {
                indexList.add(i);
            }
        }
        addToTable(lieferKund, false);
        tableModel.addRow(rowDataMuster);
        indexPos++;
        jTextFieldKdPosNr.setText(String.valueOf(indexPos));

//        indexList.stream().forEach(cnsmr -> {
//            tableModel.setValueAt(summe, cnsmr, 9);
//
//        });

        jTableAllgemein.setRowSelectionInterval(jTableAllgemein.getRowCount() - 1, jTableAllgemein.getRowCount() - 1);
        jTableAllgemein.scrollRectToVisible(new Rectangle(jTableAllgemein.getCellRect(jTableAllgemein.getRowCount() - 1, 0, true)));
    }

    private void populateTableGenarate(List<LieferKund> lieferKunds) {
        tableModel.setRowCount(0);
        lieferKunds.stream().forEach(cnsmr -> {
            addToTableNormal(cnsmr);
            tableModel.addRow(rowDataMuster);
        });
        
        jTableAllgemein.setRowSelectionInterval(0,0);
        jTableAllgemein.scrollRectToVisible(new Rectangle(jTableAllgemein.getCellRect(0, 0, true)));
        indexPos = Integer.valueOf(jTextFieldKdPosNr.getText());
    }

    private void refreshTable(List<LieferKund> lieferKunds) {
        tableModel.setRowCount(0);
        lieferKunds.stream().forEach(cnsmr -> {
            addToTableNormal(cnsmr);
            tableModel.addRow(rowDataMuster);
        });
    }

    private void refreshTableLiefKund(int position, boolean changeArtnum, boolean changeFarb, boolean changeVariante) {

        String kdArtNum = liefkunds.get(position).getArtikel_Nr();
        String kdFarbe = liefkunds.get(position).getFarbe();
        String kdVariante = liefkunds.get(position).getVariante();
        ArrayList<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < liefkunds.size(); i++) {
            if (liefkunds.get(i).getArtikel_Nr().equals(kdArtNum) && liefkunds.get(i).getFarbe().equals(kdFarbe) && liefkunds.get(i).getVariante().equals(kdVariante)) {
                indexList.add(i);
            }
        }
//        summe = 0;
//        indexList.stream().forEach(cnsmr -> {
//            String menge = liefkunds.get(cnsmr).getMenge();
//            if (!menge.isEmpty()) {
//                summe = summe + Float.parseFloat(liefkunds.get(cnsmr).getMenge());
//            } else {
//                summe = summe + 0;
//            }
//        });
        
//        indexList.stream().forEach(cnsmr -> {
//            liefkunds.get(cnsmr).setSumme(String.valueOf(summe));
//        });
//        liefkunds.get(position).setSumme(String.valueOf(summe));
        //liefkunds.notifyAll();
        tableModel.setRowCount(0);
        String newCount = String.valueOf(Integer.valueOf(liefkunds.get(liefkunds.size() - 1).getPosiNummer()) + 1);
        jTextFieldKdPosNr.setText(newCount);
        liefkunds.stream().forEach(cnsmr -> {
            addToTable(cnsmr, true);
            //addToTableNormal(cnsmr);
            tableModel.addRow(rowDataMuster);
        });
    }

    private List<Status> getStatus(){
        List<Status> statuses = jlieferDaoInterface.getStatusListe();
        return statuses;
    }
    
    private void insertIntoDb(String id) {

        SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
            boolean recorded;

            @Override
            protected Void doInBackground() throws Exception {

                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                //Date date = new Date();
                String dateBest = "1900/01/01";
                String dateTod = "1900/01/01";
                String wunchDat = "1900/01/01";

                if (jXDatePickerKdBestDat.getDate() != null) {
                    dateBest = format.format(jXDatePickerKdBestDat.getDate());

                }
                if (jXDatePickerKdBestDat.getDate() != null) {
                    dateTod = format.format(jXDatePickerErfassung.getDate());
                }
                if (jXDatePickerWunch.getDate() != null) {
                    wunchDat = format.format(jXDatePickerWunch.getDate());
                }
                // TODO add your handling code here:
                try {
                    recorded = jlieferDaoInterface.updateTableGin(jTextFieldKdNr.getText(), jTextFieldKdBestNr.getText(), dateBest, wunchDat, jTextFieldErfasser.getText(), dateTod, jTextFieldKposAktiv.getText(), liefkunds, id);

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
                    jlieferDaoInterface.getIndexes().forEach(cnsmr -> {
                        liefkunds.stream().forEach(liefKund -> {
                            if (liefKund.getPosiNummer().equals(cnsmr)) {
                                switch (id) {
                                    case "0":
                                        liefKund.setStatus("1");
                                        liefKund.setUbergabe("1");
                                        liefKund.setId("1");
                                        break;
                                    case "1":
                                        liefKund.setStatus("1");
                                        break;
                                    case "2":
                                        liefKund.setUbergabe("1");
                                        break;
                                    case "3":
                                        liefKund.setStatus("1");
                                        break;
                                }
                            }
                        });

                    });

                    refreshTable(liefkunds);

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Keine Verarbeitung vorgenommen " + jlieferDaoInterface.getException(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        sw.execute();
        dlgProgress.setVisible(true);
    }

    private void insertIntoFamak() {
        SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
            //boolean recorded;
            List<String> list;
            List<String> meldungs = new ArrayList<>();

            @Override
            protected Void doInBackground() throws Exception {

                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                //Date date = new Date();
                String dateBest = "1900/01/01";
                String dateTod = "1900/01/01";
                String wunchDat = "1900/01/01";

                if (jXDatePickerKdBestDat.getDate() != null) {
                    dateBest = format.format(jXDatePickerKdBestDat.getDate());

                }
                if (jXDatePickerKdBestDat.getDate() != null) {
                    dateTod = format.format(jXDatePickerKdBestDat.getDate());
                }
                if (jXDatePickerWunch.getDate() != null) {
                    wunchDat = format.format(jXDatePickerWunch.getDate());
                }
                // TODO add your handling code here:
                try {
                    list = jlieferDaoInterface.updateInFamak(jTextFieldKdNr.getText(), jTextFieldKdBestNr.getText(), dateBest, wunchDat, liefkunds);
//                    list.stream().forEach(cnsmr -> {
//
//                    });
//
//                    liefkunds.stream().forEach(cnsmr -> {
//
//                    });

                    list.stream().forEach(cnsmr -> {

                        String[] parts = cnsmr.split("//");
                        if (parts.length != 0) {
                            String part1 = parts[0];
                            String part2 = parts[1];
                            meldungs.add(part1);
                            for (LieferKund kund : liefkunds) {
                                if (kund.hashCode() == Integer.valueOf(part2)) {
                                    kund.setMeldungFamak(part1);
                                    break;
                                }
                            }

                            // liefkunds.get(Integer.valueOf(part2)).setMeldungFamak(part1);
                        }
                    });
                } catch (Exception ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);

                }


                return null;
            }

            @Override
            protected void done() {
                dlgProgress.dispose();//close the modal dialog

                JOptionPane.showMessageDialog(null,
                        new JScrollPane(new JList(meldungs.toArray())), "Meldung von Famak", 1);
                jlieferDaoInterface.getIndexInFamak().forEach(cnsmr -> {
                    liefkunds.stream().forEach(liefKund -> {

                        if (liefKund.getPosiNummer().equals(cnsmr)) {
                            //liefKund.setStatus("1");
                            liefKund.setId("1");
                        }
                    });
                });

                insertIntoDb("3");
                //refreshTable(liefkunds);
            }
        };
        sw.execute();
        dlgProgress.setVisible(true);
    }

    private void populateJtablePrufung() {
        tableModelPrufer.setRowCount(0);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    liefPrufers = jlieferDaoInterface.getListPrufers(format.format(jXDatePickerBisDatum.getDate()), kopfDaten);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Error getting DATA  " + jlieferDaoInterface.getException() + "," + e.getMessage() + ", date or database error",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }

            @Override
            protected void done() {
                dlgProgress.dispose();
                //populateJtablePrufung(liefPrufers);
                liefPrufers.stream().forEach((cnsmr)
                        -> {
                    addToPrufungTable(cnsmr);
                    tableModelPrufer.addRow(rowDataPrufung);
                });
            }
        };
        sw.execute();
        dlgProgress.setVisible(true);

    }

    private void addToPrufungTable(LieferKundPrufer liefPrufer) {
        rowDataPrufung[0] = liefPrufer.getZeile();
        rowDataPrufung[1] = liefPrufer.getTreffer();
        rowDataPrufung[2] = liefPrufer.getKundNummer();
        rowDataPrufung[3] = liefPrufer.getBestNummer();
        rowDataPrufung[4] = liefPrufer.getKdBestDatum();
        rowDataPrufung[5] = liefPrufer.getKdWunchDatum();
        rowDataPrufung[6] = liefPrufer.getErfasser();
        rowDataPrufung[7] = liefPrufer.getErfassungsDatum();
        rowDataPrufung[8] = liefPrufer.getPosiNummer();
        rowDataPrufung[9] = liefPrufer.getKundenArtikelNummer();
        rowDataPrufung[10] = liefPrufer.getFarbe();
        rowDataPrufung[11] = liefPrufer.getKdgroesse();
        rowDataPrufung[12] = liefPrufer.getVariante();
        rowDataPrufung[13] = liefPrufer.getMenge();
        rowDataPrufung[14] = liefPrufer.getPreis();
        rowDataPrufung[15] = liefPrufer.getKommission();
        rowDataPrufung[16] = liefPrufer.getKd_Pos_activ();
        rowDataPrufung[17] = liefPrufer.getStatus();
        rowDataPrufung[18] = liefPrufer.getArtikelId();
        rowDataPrufung[19] = liefPrufer.getArtikel_Nr();
        rowDataPrufung[20] = liefPrufer.getFarbeNummer();
        rowDataPrufung[21] = liefPrufer.getGroesse();
        rowDataPrufung[22] = liefPrufer.getVarNummer();
        rowDataPrufung[23] = liefPrufer.getGtin();
        rowDataPrufung[24] = liefPrufer.getZielMenge();
        rowDataPrufung[25] = liefPrufer.getPosGrId();
        rowDataPrufung[26] = liefPrufer.getId();
        rowDataPrufung[27] = liefPrufer.getLagerNum();
        rowDataPrufung[28] = liefPrufer.getGtinPreis();
        rowDataPrufung[29] = liefPrufer.getPosGrPreis();
        rowDataPrufung[30] = liefPrufer.getKalkPreis();
    }

    private void populateJtablePreisListe() {
        tableModelPreislIste.setRowCount(0);
        int selectedRow = jTableAllgemein.getSelectedRow();
        LieferKund lieferKund = liefkunds.get(selectedRow);
        List<VarPreis> varPreises;
        if (jTextFieldMengenBezeug.getText().equals("P")) {
            varPreises = jlieferDaoInterface.getListVarPreis(jTextFieldKdNr.getText(), lieferKund.getArtikel_Nr(), lieferKund.getFarbe(), lieferKund.getGroesse(), lieferKund.getVariante(), lieferKund.getSumme(), lieferKund.getLagerNum());

        } else {
            varPreises = jlieferDaoInterface.getListVarPreis(jTextFieldKdNr.getText(), lieferKund.getArtikel_Nr(), lieferKund.getFarbe(), lieferKund.getGroesse(), lieferKund.getVariante(), lieferKund.getMenge(), lieferKund.getLagerNum());
        }

        varPreises.stream().forEach(cnsmr -> {
            addToTablePreisListe(cnsmr);
            tableModelPreislIste.addRow(rowDataPreisListe);
            
        });
        for (int i = 0; i < tableModelPreislIste.getRowCount(); i++) {
            
            if (jTablePreisListe.getValueAt(i, 1)!= null) {
                
             switch (jTablePreisListe.getValueAt(i, 1).toString()) {
            case "DIFF":
                jTablePreisListe.getSelectionModel().addSelectionInterval(i, i);
                jTablePreisListe.setSelectionBackground(Color.decode("#ef6462"));
                     break;
//            case "OK":  
//                jTablePreisListe.getSelectionModel().addSelectionInterval(i, i);
//                jTablePreisListe.setSelectionBackground(Color.decode("#00AF33"));
//                     break;
//            case "1× 0":  
//                jTablePreisListe.getSelectionModel().addSelectionInterval(i, i);
//                jTablePreisListe.setSelectionBackground(Color.decode("#f9df7f"));
//                     break;
            
        }

        }
            
//            if (jTablePreisListe.getValueAt(i, 2)!= null && jTablePreisListe.getValueAt(i, 2).toString().contains("•••")){
//                jTablePreisListe.getSelectionModel().addSelectionInterval(i, i);
//                jTablePreisListe.setSelectionBackground(Color.decode("#f9df7f"));
//            }
        }
        jTablePreisListe.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resizeColumnWidth(jTablePreisListe);
    }

    private void addToTablePreisListe(VarPreis varPreis) {
        rowDataPreisListe[0] = varPreis.getZeile();
        rowDataPreisListe[1] = varPreis.getVarNummer();
        rowDataPreisListe[2] = varPreis.getVarText();
        rowDataPreisListe[3] = varPreis.getKdPreis();
        rowDataPreisListe[4] = varPreis.getVkPreis();
    }

    private void selectInTheTable() {
        String pruf1 = jTablePrufer.getValueAt(jTablePrufer.getSelectedRow(), 0).toString();
        String pruf2 = "";
        if (jTablePrufer.getValueAt(jTablePrufer.getSelectedRow(), 1) != null) {
            pruf2 = jTablePrufer.getValueAt(jTablePrufer.getSelectedRow(), 1).toString();
        }
        for (int i = 0; i < jTablePrufer.getRowCount(); i++) {
            if (pruf1.equals(jTablePrufer.getValueAt(i, 1)) || pruf2.equals(jTablePrufer.getValueAt(i, 0))) {
                jTablePrufer.getSelectionModel().addSelectionInterval(i, i);
                jTablePrufer.setSelectionBackground(Color.decode("#00AF33"));
            }
        }
        selecteItems();
    }

    private void erfassungManuelle() {
        String meldung3 = jlieferDaoInterface.erfassungManuelzuweisen("0", kundPrufer.getId(), kundPruferFamak.getPosGrId(), kundPrufer.getStatus());
        String message = jlieferDaoInterface.getMeldung("3", meldung3);
        String[] parts3 = message.split("--");
        String part1 = parts3[0]; // 004
        String part2 = parts3[1]; // 034556
        JOptionPane.showMessageDialog(null,
                part1,
                part2,
                JOptionPane.WARNING_MESSAGE);
    }

    private void erfassungManuelleteilmenge() {
        String meldung3 = jlieferDaoInterface.erfassungManuelzuweisen("1", kundPrufer.getId(), kundPruferFamak.getPosGrId(), kundPrufer.getStatus());
        String message = jlieferDaoInterface.getMeldung("3", meldung3);
        String[] parts3 = message.split("--");
        String part1 = parts3[0]; // 004
        String part2 = parts3[1]; // 034556
        JOptionPane.showMessageDialog(null,
                part1,
                part2,
                JOptionPane.WARNING_MESSAGE);
    }

    private void erfassungAbschliesen() {
        String meldung3 = jlieferDaoInterface.erfassungAbschliessen(kundPrufer.getId(), kundPruferFamak.getPosGrId());
        String message = jlieferDaoInterface.getMeldung("4", meldung3);
        String[] parts3 = message.split("--");
        String part1 = parts3[0]; // 004
        String part2 = parts3[1]; // 034556
        JOptionPane.showMessageDialog(null,
                part1,
                part2,
                JOptionPane.WARNING_MESSAGE);
    }

    private void erfassungVerarbeiten() {
        if (jlieferDaoInterface.erfassungVerarbeiten(kopfDaten)) {
            JOptionPane.showMessageDialog(null,
                    "Erledigt",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Fehler aufgetreten",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doppelErfassungPrüfen() {
        List<LieferKundDoppel> kundDoppels = jlieferDaoInterface.getListDoppelErfassung(kopfDaten);
        JDialogDoppelErfassung dialogDoppelErfassung = new JDialogDoppelErfassung(this, true, kundDoppels, dbUrl, kopfDaten);
        dialogDoppelErfassung.setVisible(true);
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
    
    private void speichern(String status,LieferKund cnsmr, KopfDaten daten, String posAktiv){
        result = jlieferDaoInterface.speichern(status,cnsmr, daten, posAktiv);
      
    }

    private boolean famakVorbereiten(){
        return jlieferDaoInterface.famakVorbereiten(kopfDaten);
    }
    
    private void inFamakSchreiben(KopfDaten kopfDaten){
        List<String> meldungs = jlieferDaoInterface.datenInfamakSchreiben(kopfDaten);
         JOptionPane.showMessageDialog(null,
                        new JScrollPane(new JList(meldungs.toArray())), "Meldung von Famak", 1);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
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
        jXDatePickerErfassung = new org.jdesktop.swingx.JXDatePicker();
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
        jTableAllgemein = new javax.swing.JTable();
        jButtonAdd = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabelggf = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabelSpeichern1und2 = new javax.swing.JLabel();
        jButtonDoppelErfasung = new javax.swing.JButton();
        jButtonSpeichern1 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jButtonInFamak = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTablePreisListe = new javax.swing.JTable();
        jCheckBoxPreisSofort = new javax.swing.JCheckBox();
        jTextFieldMengenBezeug = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jButtonSpeichern = new javax.swing.JButton();
        jButtonSpeichernUnter = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePrufer = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jButtonVerarbeiten = new javax.swing.JButton();
        jButtonLaden = new javax.swing.JButton();
        jXDatePickerBisDatum = new org.jdesktop.swingx.JXDatePicker();
        jLabel16 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jButtonManZu = new javax.swing.JButton();
        jButtonteilManZu = new javax.swing.JButton();
        jButtonAbOZu = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jTextFieldKdNrCopie = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jTextFieldKdBestNrCopi = new javax.swing.JTextField();
        jXDatePickerKdBestDatCopi = new org.jdesktop.swingx.JXDatePicker();
        jLabel33 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(4000, 1500));

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(2500, 1000));

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
                    .addComponent(jXDatePickerErfassung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
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
                    .addComponent(jXDatePickerErfassung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jTableAllgemein.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "KdPosNr", "KdArtNr", "KdFarbe", "KdGroesse", "Kdvariant", "KdMenge", "KdPreis", "Kommission", "Lager-Nr", "Art-Summe", "Status", "Ubergabe", "ID"
            }
        ));
        jTableAllgemein.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAllgemeinMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTableAllgemeinMouseReleased(evt);
            }
        });
        jTableAllgemein.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableAllgemeinKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableAllgemein);
        if (jTableAllgemein.getColumnModel().getColumnCount() > 0) {
            jTableAllgemein.getColumnModel().getColumn(0).setPreferredWidth(5);
            jTableAllgemein.getColumnModel().getColumn(1).setResizable(false);
            jTableAllgemein.getColumnModel().getColumn(3).setPreferredWidth(10);
            jTableAllgemein.getColumnModel().getColumn(5).setPreferredWidth(20);
            jTableAllgemein.getColumnModel().getColumn(6).setPreferredWidth(20);
            jTableAllgemein.getColumnModel().getColumn(7).setPreferredWidth(10);
            jTableAllgemein.getColumnModel().getColumn(8).setPreferredWidth(5);
            jTableAllgemein.getColumnModel().getColumn(9).setPreferredWidth(5);
            jTableAllgemein.getColumnModel().getColumn(10).setPreferredWidth(2);
            jTableAllgemein.getColumnModel().getColumn(11).setPreferredWidth(2);
            jTableAllgemein.getColumnModel().getColumn(12).setPreferredWidth(2);
        }

        jButtonAdd.setText("---v");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel19.setText("Prozess 900");

        jLabelggf.setBackground(new java.awt.Color(255, 255, 255));
        jLabelggf.setText("oder");

        jLabel23.setText(">>");

        jLabel24.setText(">>");

        jLabel25.setText(">>");

        jLabelSpeichern1und2.setBackground(new java.awt.Color(255, 255, 255));

        jButtonDoppelErfasung.setText("Doppel-Erfassung prüfen");
        jButtonDoppelErfasung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDoppelErfasungActionPerformed(evt);
            }
        });

        jButtonSpeichern1.setText("Famak vorbereiten");
        jButtonSpeichern1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSpeichern1ActionPerformed(evt);
            }
        });

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("jLabel22");

        jLabel26.setText(">>");

        jButtonInFamak.setText("Daten in Famak schreiben");
        jButtonInFamak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInFamakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSpeichern1und2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDoppelErfasung)
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelggf)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSpeichern1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonInFamak, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelSpeichern1und2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonDoppelErfasung)
                                    .addComponent(jLabelggf)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel25)
                                    .addComponent(jButtonSpeichern1)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel26)
                                    .addComponent(jButtonInFamak, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel19)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel18.setText("Prozess 0");

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("in Famak eingeben ");

        jButton5.setText("Erfassungsdaten verarbeiten");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel21.setText(">> ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addGap(5, 5, 5)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel20)
                    .addComponent(jButton5)
                    .addComponent(jLabel21))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel27.setText("Prozess 0");

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("jLabel28");

        jLabel29.setText(">>");

        jLabel30.setBackground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("jLabel30");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addGap(31, 31, 31)
                .addComponent(jLabel28)
                .addGap(41, 41, 41)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(jLabel30)
                .addGap(28, 28, 28))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldKdPosNr, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(78, 78, 78)
                                .addComponent(jLabel9))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jTextFieldKdArtNr, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jTextFieldKdFarbe, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(jLabel10)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldKdGroesse, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(62, 62, 62)
                                .addComponent(jLabel13)
                                .addGap(13, 13, 13))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextFieldKdVariant, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addComponent(jTextFieldMenge, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldkdPreis, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel14)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldKommission, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonAdd)
                                .addGap(25, 25, 25))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel15)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 141, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Variante-Preis-Liste"));

        jTablePreisListe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Zeile", "VarNr", "VarText", "KdPreis", "VKPreis"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTablePreisListe);

        jCheckBoxPreisSofort.setText("Preis sofort anzeigen");

        jTextFieldMengenBezeug.setEditable(false);

        jLabel17.setText("Mengenbezeug(Größe/Position):");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jCheckBoxPreisSofort)
                        .addGap(82, 82, 82)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldMengenBezeug, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxPreisSofort)
                    .addComponent(jLabel17)
                    .addComponent(jTextFieldMengenBezeug, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jButtonSpeichern.setText("Speichern");
        jButtonSpeichern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSpeichernActionPerformed(evt);
            }
        });

        jButtonSpeichernUnter.setText("Speichern Unter");
        jButtonSpeichernUnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSpeichernUnterActionPerformed(evt);
            }
        });

        jButton3.setText("Öffnen");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonSpeichern, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSpeichernUnter, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(662, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jButtonSpeichern, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonSpeichernUnter, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(350, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Daten Eintragen", jPanel3);

        jTablePrufer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Zeile", "Treffer", "Kd_Nr", "Kd_Best_Nr", "Kd_Best_Datum", "Kd_Wunsch_Datum", "Erfasser", "Erfassungs_Datum#", "Kd_Pos_Nr", "Kd_Artikel_Nr", "Kd_Farbe", "Kd_Groesse", "Kd_Variante", "Kd_Menge", "Kd_Preis", "Kommission", "Kd_Pos_Aktiv", "Status", "Artikel_ID", "Artikel_Nr", "Farbe_Nr", "Groesse", "Var_Nummern", "GTIN", "Ziel_Menge", "Pos_Gr_ID", "ID", "Lager_Nr", "GTIN_Preis", "Pos_Gr_Preis", "Kalk_Preis"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablePrufer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePruferMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTablePruferMouseReleased(evt);
            }
        });
        jTablePrufer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTablePruferKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTablePrufer);

        jButtonVerarbeiten.setText("Erfassungsdaten verarbeiten");
        jButtonVerarbeiten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerarbeitenActionPerformed(evt);
            }
        });

        jButtonLaden.setText("Daten Laden");
        jButtonLaden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLadenActionPerformed(evt);
            }
        });

        jLabel16.setText("Bis Datum:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jButtonLaden, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jXDatePickerBisDatum, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(jButtonVerarbeiten))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(236, 236, 236)
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jXDatePickerBisDatum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonVerarbeiten, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonLaden, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Manuelle Verarbeitung", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jButtonManZu.setText("Manuell zuweisen");
        jButtonManZu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonManZuActionPerformed(evt);
            }
        });

        jButtonteilManZu.setText("Teilmenge manuell zuweisen");
        jButtonteilManZu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonteilManZuActionPerformed(evt);
            }
        });

        jButtonAbOZu.setText("Abschließen ohne Zuweisung");
        jButtonAbOZu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAbOZuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonteilManZu, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                    .addComponent(jButtonManZu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jButtonAbOZu)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonManZu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonteilManZu))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jButtonAbOZu)))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jLabel31.setText("KdNr");

        jLabel32.setText("KdBestNr");

        jLabel33.setText("BestDatum");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(307, 307, 307)
                        .addComponent(jLabel31)
                        .addGap(143, 143, 143)
                        .addComponent(jLabel32)
                        .addGap(159, 159, 159)
                        .addComponent(jLabel33))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(246, 246, 246)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(266, 266, 266)
                        .addComponent(jTextFieldKdNrCopie, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(jTextFieldKdBestNrCopi, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(jXDatePickerKdBestDatCopi, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(135, 135, 135)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1100, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel31)
                                .addComponent(jLabel32))
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldKdNrCopie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldKdBestNrCopi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jXDatePickerKdBestDatCopi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(431, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("GTIN überprüfen", jPanel4);

        jScrollPane4.setViewportView(jTabbedPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1939, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLadenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLadenActionPerformed
        // TODO add your handling code here:
        populateJtablePrufung();
    }//GEN-LAST:event_jButtonLadenActionPerformed

    private void jTablePruferKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTablePruferKeyPressed
        // TODO add your handling code here:
        ctrlPressed = evt.isControlDown();

    }//GEN-LAST:event_jTablePruferKeyPressed

    private void jTablePruferMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePruferMouseReleased

        int rowindex = jTablePrufer.getSelectedRow();
        if (rowindex < 0) {
            return;
        }
        if (evt.isPopupTrigger() && evt.getComponent() instanceof JTable) {
            popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTablePruferMouseReleased

    private void jTablePruferMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePruferMouseClicked
        // TODO add your handling code here:
        if (tableModelPrufer.getRowCount() != 0) {
            if (evt.getClickCount() == 2) {
                if (!liefPrufers.get(jTablePrufer.getSelectedRow()).getId().isEmpty()) {
                    JDialogKundenbestellung dialogKundenbestellung = new JDialogKundenbestellung(this, true, liefPrufers.get(jTablePrufer.getSelectedRow()), dbUrl);
                    dialogKundenbestellung.setVisible(true);
                    if (dialogKundenbestellung.isRefresh()) {
                        populateJtablePrufung();
                    }
                }

            } else if (evt.getButton() == MouseEvent.BUTTON3) {
                } else {
                selectInTheTable();
            }

        }
    }//GEN-LAST:event_jTablePruferMouseClicked

    private void jButtonSpeichernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSpeichernActionPerformed
        // TODO add your handling code here:
        checkKopfDaten();

        if (statusIn.equals("")) {
            JDialogStatus dialogStatus = new JDialogStatus(MainFrame.this, true, getStatus());
            dialogStatus.setVisible(true);
            Status status = dialogStatus.getSelectedStatus();
            statusIn = status.getCode();
        }

        liefkunds.stream().forEach(cnsmr -> {
            speichern(statusIn, cnsmr, kopfDaten, jTextFieldKposAktiv.getText());
        });

        if (result) {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "successuful recorded");
            tableModel.setRowCount(0);
            liefkunds.clear();
            statusIn = "";
            jTextFieldKdPosNr.setText("");
            indexPos = 1;
        } else {
            String error = jlieferDaoInterface.returnError();
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Error when recording: " + error,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        //insertIntoDb("0");
    }//GEN-LAST:event_jButtonSpeichernActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO add your handling code here:
        populateJtable();
        initiliseFelder();
        configureTable(parameterKund);
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jTableAllgemeinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableAllgemeinKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {

            if (jTableAllgemein.getRowCount() != 0) {
                int[] rows = jTableAllgemein.getSelectedRows();
                String phrase = "";
                List<String> listMeldungRemove = new ArrayList<>();
                if (rows.length == 1) {
                    phrase = "Möchten Sie die markierte Zeile löschen?";
                } else if (rows.length > 1) {
                    phrase = "Möchten Sie die markierte Zeilen löschen?";
                }
                int dialogResult = JOptionPane.showConfirmDialog(null, phrase, "Actung", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {

                    for (int i = 0; i < rows.length; i++) {
                        //MusterArtikel musterArtikel = musterArtikels.get(rows[i] - i);
                        LieferKund lieferKund = liefkunds.get(rows[i] - i);
                        tableModel.removeRow(rows[i] - i);
                        liefkunds.remove(lieferKund);
                        String removeMeldung = jlieferDaoInterface.removeLieferKund(lieferKund.getId());
                        listMeldungRemove.add(removeMeldung);
                        
                    }
                    
                    JOptionPane.showMessageDialog(null,
                        new JScrollPane(new JList(listMeldungRemove.toArray())), "Hinweise", 1);
                    if (jTableAllgemein.getRowCount() == 0) {
                        //musterArtikels = new ArrayList<>();
                        //selectedMusterArtikel = new MusterArtikel();
                        tableModel.setRowCount(0);
                        liefkunds.clear();
                        tableModelPreislIste.setRowCount(0);
                        statusIn="";
                        
                    }
                    tableModel.fireTableDataChanged();
                }
            }

        }
    }//GEN-LAST:event_jTableAllgemeinKeyPressed

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

    private void jTextFieldKommissionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKommissionMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            jTextFieldKommission.setEnabled(true);
            jTextFieldKommission.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldKommissionMouseClicked

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

    private void jTextFieldkdPreisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldkdPreisMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            jTextFieldkdPreis.setEnabled(true);
            jTextFieldkdPreis.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldkdPreisMouseClicked

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

    private void jTextFieldMengeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldMengeMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            jTextFieldMenge.setEnabled(true);
            jTextFieldMenge.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldMengeMouseClicked

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

    private void jTextFieldKdVariantMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKdVariantMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            jTextFieldKdVariant.setEnabled(true);
            jTextFieldKdVariant.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldKdVariantMouseClicked

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

    private void jTextFieldKdGroesseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKdGroesseMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            jTextFieldKdGroesse.setEnabled(true);
            jTextFieldKdGroesse.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldKdGroesseMouseClicked

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

    private void jTextFieldKdFarbeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKdFarbeMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            jTextFieldKdFarbe.setEnabled(true);
            jTextFieldKdFarbe.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldKdFarbeMouseClicked

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

    private void jTextFieldKdArtNrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKdArtNrMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            jTextFieldKdArtNr.setEnabled(true);
            jTextFieldKdArtNr.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldKdArtNrMouseClicked

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

    private void jTextFieldKdPosNrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldKdPosNrMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            jTextFieldKdPosNr.setEnabled(true);
            jTextFieldKdPosNr.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldKdPosNrMouseClicked

    private void jTextFieldKposAktivKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKposAktivKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldKdArtNr.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldKposAktivKeyPressed

    private void jTextFieldErfasserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldErfasserKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jXDatePickerErfassung.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldErfasserKeyPressed

    private void jTextFieldKdBestNrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdBestNrKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jXDatePickerKdBestDat.requestFocus();
            jTextFieldKdBestNrCopi.setText(jTextFieldKdBestNr.getText());
        }
    }//GEN-LAST:event_jTextFieldKdBestNrKeyPressed

    private void jTextFieldKdNrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKdNrKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            initiliseFelder();
            jTextFieldKdBestNr.setText("");
            jTextFieldKdBestNrCopi.setText(jTextFieldKdBestNr.getText());
            jTextFieldKdNrCopie.setText(jTextFieldKdNr.getText());
            liefkunds.clear();
            jXDatePickerWunch.setDate(null);
            jXDatePickerKdBestDat.setDate(null);
            jXDatePickerKdBestDatCopi.setDate(null);
            tableModel.setRowCount(0);
            liefkunds.clear();
            parameterKund = jlieferDaoInterface.getKundenParameter(jTextFieldKdNr.getText());
            configureTable(parameterKund);
            jTextFieldKdPosNr.setText(parameterKund.getPos_Zaehler());
            jTextFieldKdBestNr.requestFocus();
            jTextFieldMengenBezeug.setText(jlieferDaoInterface.getMengenbezug(jTextFieldKdNr.getText()));
            tableModelPreislIste.setRowCount(0);
            indexPos = Integer.valueOf(jTextFieldKdPosNr.getText());
            kopfDaten = new KopfDaten();
            statusIn = "";
            jTextFieldKdNrCopie.setText(jTextFieldKdNr.getText());
        }
    }//GEN-LAST:event_jTextFieldKdNrKeyPressed

    private void jButtonVerarbeitenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerarbeitenActionPerformed
        // TODO add your handling code here:
        erfassungVerarbeiten();
    }//GEN-LAST:event_jButtonVerarbeitenActionPerformed

    private void jButtonManZuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonManZuActionPerformed
        // TODO add your handling code here:
        erfassungManuelle();
    }//GEN-LAST:event_jButtonManZuActionPerformed

    private void jButtonteilManZuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonteilManZuActionPerformed
        // TODO add your handling code here:
        erfassungManuelleteilmenge();
    }//GEN-LAST:event_jButtonteilManZuActionPerformed

    private void jButtonAbOZuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAbOZuActionPerformed
        // TODO add your handling code here:
        erfassungAbschliesen();
    }//GEN-LAST:event_jButtonAbOZuActionPerformed

    private void jTableAllgemeinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAllgemeinMouseClicked
        // TODO add your handling code here:

        populateJtablePreisListe();

    }//GEN-LAST:event_jTableAllgemeinMouseClicked

    private void jButtonInFamakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInFamakActionPerformed
        // TODO add your handling code here:
        checkKopfDaten();
       
        inFamakSchreiben(kopfDaten);
        //insertIntoFamak();
    }//GEN-LAST:event_jButtonInFamakActionPerformed

    private void jButtonSpeichern1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSpeichern1ActionPerformed
        // TODO add your handling code here:
        if (famakVorbereiten()) {
            JOptionPane.showMessageDialog(MainFrame.this,
    "Successuful");
        }else{
            JOptionPane.showMessageDialog(MainFrame.this,
    "Eroor when processing.",
    "Error",
    JOptionPane.ERROR_MESSAGE);
        }

       // insertIntoDb("1");
    }//GEN-LAST:event_jButtonSpeichern1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String dateBest = "1900/01/01";
        if (jXDatePickerKdBestDat.getDate() != null) {
            dateBest = format.format(jXDatePickerKdBestDat.getDate());
        }
        List<KopfDaten> kopfDatens = jlieferDaoInterface.getListKopfDaten(jTextFieldKdNr.getText(), jTextFieldKdBestNr.getText(), dateBest);
        if (!kopfDatens.isEmpty()) {
            JDialogListKopfDaten dialogListKopfDaten = new JDialogListKopfDaten(MainFrame.this, true, kopfDatens);
            dialogListKopfDaten.setVisible(true);
            kopfDaten = dialogListKopfDaten.getSelectedKopfDatenIn();

            if (kopfDaten != null) {
                statusIn = kopfDaten.getStatus();
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                Date dateWunsch = null;
                Date dateErfass = null;
                Date bestell = null;
                try {
                    dateWunsch = df.parse(kopfDaten.getKdWunchDat());
                    dateErfass = df.parse(kopfDaten.getErfassDatum());
                    bestell = df.parse(kopfDaten.getKdBestDatum());
                } catch (ParseException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                jXDatePickerWunch.setDate(dateWunsch);
                jXDatePickerErfassung.setDate(dateErfass);
                jXDatePickerKdBestDat.setDate(bestell);
                jXDatePickerKdBestDatCopi.setDate(bestell);
                jTextFieldKdNr.setText(kopfDaten.getKdNum());
                jTextFieldKdBestNr.setText(kopfDaten.getKdBestnum());
                
                jTextFieldKdBestNrCopi.setText(jTextFieldKdBestNr.getText());
                parameterKund = jlieferDaoInterface.getKundenParameter(jTextFieldKdNr.getText());
                configureTable(parameterKund);
                jTextFieldKposAktiv.setText(parameterKund.getKd_Pos_activ());
                List<LieferKund> lieferKundsIn = jlieferDaoInterface.getListLieferGenerated(kopfDaten.getKdNum(), kopfDaten.getKdBestnum(), kopfDaten.getKdBestDatum(), kopfDaten.getStatus());
                liefkunds = lieferKundsIn;

                jTextFieldKdPosNr.setText(String.valueOf(Integer.parseInt(lieferKundsIn.get(lieferKundsIn.size() - 1).getPosiNummer()) + 1));

                populateTableGenarate(lieferKundsIn);
            } else {
                jXDatePickerWunch.requestFocus();
            }

        } else {
            jXDatePickerWunch.requestFocus();
        }
        // insertIntoDb("2"); 
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButtonDoppelErfasungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDoppelErfasungActionPerformed
        // TODO add your handling code here:
       
        Optional<LieferKund> lieferKundFirst
                = liefkunds.stream().filter(obj -> obj.getUbergabe().equals("X")).findFirst();
        if (lieferKundFirst != null) {
            int reply = JOptionPane.showConfirmDialog(null, "Die Änderungen wurden noch nicht gespeichert. Wollen Sie fortfahren?", "Actung", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                doppelErfassungPrüfen();
            }

        }

    }//GEN-LAST:event_jButtonDoppelErfasungActionPerformed

    private void jButtonSpeichernUnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSpeichernUnterActionPerformed
        // TODO add your handling code here:
        JDialogStatus dialogStatus = new JDialogStatus(MainFrame.this, true, getStatus());
        dialogStatus.setVisible(true);
        if (dialogStatus.getSelectedStatus() != null) {
            
        
        Status status = dialogStatus.getSelectedStatus();
        checkKopfDaten();
        liefkunds.stream().forEach(cnsmr -> {
            cnsmr.setStatus(status.getCode());
            statusIn = status.getCode();
            speichern(statusIn,cnsmr, kopfDaten, jTextFieldKposAktiv.getText());
        });
        if (result) {
            
            JOptionPane.showMessageDialog(MainFrame.this,
                    "successuful recorded");
            tableModel.setRowCount(0);
            liefkunds.clear();
            statusIn="";
            jTextFieldKdPosNr.setText("");
            indexPos=1;
        } else {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Eroor when recording",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
       }
        //erfassungVerarbeiten();
    }//GEN-LAST:event_jButtonSpeichernUnterActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        erfassungVerarbeiten();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTableAllgemeinMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAllgemeinMouseReleased
        // TODO add your handling code here:
        int rowindex = jTableAllgemein.getSelectedRow();
        if (rowindex < 0) {
            return;
        }
        if (evt.isPopupTrigger() && evt.getComponent() instanceof JTable) {
            popupMenueingaben.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTableAllgemeinMouseReleased

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
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonAbOZu;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonDoppelErfasung;
    private javax.swing.JButton jButtonInFamak;
    private javax.swing.JButton jButtonLaden;
    private javax.swing.JButton jButtonManZu;
    private javax.swing.JButton jButtonSpeichern;
    private javax.swing.JButton jButtonSpeichern1;
    private javax.swing.JButton jButtonSpeichernUnter;
    private javax.swing.JButton jButtonVerarbeiten;
    private javax.swing.JButton jButtonteilManZu;
    private javax.swing.JCheckBox jCheckBoxPreisSofort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelSpeichern1und2;
    private javax.swing.JLabel jLabelggf;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableAllgemein;
    private javax.swing.JTable jTablePreisListe;
    private javax.swing.JTable jTablePrufer;
    private javax.swing.JTextField jTextFieldErfasser;
    private javax.swing.JTextField jTextFieldKdArtNr;
    private javax.swing.JTextField jTextFieldKdBestNr;
    private javax.swing.JTextField jTextFieldKdBestNrCopi;
    private javax.swing.JTextField jTextFieldKdFarbe;
    private javax.swing.JTextField jTextFieldKdGroesse;
    private javax.swing.JTextField jTextFieldKdNr;
    private javax.swing.JTextField jTextFieldKdNrCopie;
    private javax.swing.JTextField jTextFieldKdPosNr;
    private javax.swing.JTextField jTextFieldKdVariant;
    private javax.swing.JTextField jTextFieldKommission;
    private javax.swing.JTextField jTextFieldKposAktiv;
    private javax.swing.JTextField jTextFieldMenge;
    private javax.swing.JTextField jTextFieldMengenBezeug;
    private javax.swing.JTextField jTextFieldkdPreis;
    private org.jdesktop.swingx.JXDatePicker jXDatePickerBisDatum;
    private org.jdesktop.swingx.JXDatePicker jXDatePickerErfassung;
    private org.jdesktop.swingx.JXDatePicker jXDatePickerKdBestDat;
    private org.jdesktop.swingx.JXDatePicker jXDatePickerKdBestDatCopi;
    private org.jdesktop.swingx.JXDatePicker jXDatePickerWunch;
    // End of variables declaration//GEN-END:variables
}
