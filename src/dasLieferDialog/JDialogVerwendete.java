/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dasLieferDialog;

import dasLieferdao.JlieferDao;
import dasLieferdao.JlieferDaoInterface;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import model.LieferKundPrufer;
import model.Protokoll;
import model.Varianten;
import model.VerfugbareGroßen;
import model.VerfugbareMengenstaffeln;
import model.VerwendeteMengenstaffel;
import util.TableCellListener;

/**
 *
 * @author aladhari
 */
public class JDialogVerwendete extends javax.swing.JDialog {

    private final Object[] rowData = new Object[7];
    private final Object[] rowDataVarianten = new Object[3];
    private final Object[] rowDataVerGroesse = new Object[13];
    private final DefaultTableModel tableModel, tablemodelvarianten, tableModelGroessen;
    private List<VerfugbareGroßen> verfugbareGroßens;
    private VerfugbareGroßen großen;
    private final LieferKundPrufer kundPruferFamakIn;
    private final JlieferDaoInterface jlieferDaoInterface;
    private final String preisVariante;
    private final List<Protokoll> protokolls;
    private final ListSelectionModel listSelectionModel;
    private TableCellListener tclGroesses, tclGroess;
    private final String kundNum;

    /**
     * Creates new form JDialogVerwendete
     *
     * @param parent
     * @param modal
     * @param verwendeteMengenstaffel
     * @param verfugbareMengenstaffelns
     * @param verfugbareGroßensIn
     * @param kundPruferFamak
     * @param dburlProdukt
     * @param preisVar
     * @param kundNummer
     *
     */
    public JDialogVerwendete(JDialog parent, boolean modal, VerwendeteMengenstaffel verwendeteMengenstaffel, List<VerfugbareMengenstaffeln> verfugbareMengenstaffelns, List<VerfugbareGroßen> verfugbareGroßensIn, LieferKundPrufer kundPruferFamak, String dburlProdukt, String preisVar, String kundNummer) {
        super(parent, modal);
        initComponents();
        tableModel = (DefaultTableModel) jTable1.getModel();
        tablemodelvarianten = (DefaultTableModel) jTableVariaten.getModel();
        tableModelGroessen = (DefaultTableModel) jTableverfugGroesse.getModel();
        this.verfugbareGroßens = new ArrayList<>();
        this.verfugbareGroßens = verfugbareGroßensIn;
        this.kundPruferFamakIn = kundPruferFamak;
        this.preisVariante = preisVar;
        protokolls = new ArrayList<>();
        this.kundNum = kundNummer;
        jTextFieldAnderung1.setText(verwendeteMengenstaffel.getAnderung1());
        jTextFieldAnderung2.setText(verwendeteMengenstaffel.getAnderung2());
        jTextFieldAnderung3.setText(verwendeteMengenstaffel.getAnderung3());
        jTextFieldAnderung4.setText(verwendeteMengenstaffel.getAnderung4());
       // jTextFieldKundNumGros.setText(verwendeteMengenstaffel.getVerwendeterGroßenzuschlag().getKundNummer());
        jTextFieldKundNumGros.setText(kundNum);
        //jTextFieldKundumMeng.setText(verwendeteMengenstaffel.getKundNummer());
        jTextFieldKundumMeng.setText(kundNum);
        jTextFieldMenge1.setText(verwendeteMengenstaffel.getMenge1());
        jTextFieldMenge2.setText(verwendeteMengenstaffel.getMenge2());
        jTextFieldMenge3.setText(verwendeteMengenstaffel.getMenge3());
        jTextFieldMenge4.setText(verwendeteMengenstaffel.getMenge4());
        jTextFieldPos.setText(verwendeteMengenstaffel.getMengeBetzeug());
        jTextFieldStafNr.setText(verwendeteMengenstaffel.getStaffelNr());
        jTextFieldStuf.setText(verwendeteMengenstaffel.getStufe());
        jTextFieldTyp.setText(verwendeteMengenstaffel.getTyp());
        jTextFieldVarianten.setText(verwendeteMengenstaffel.getVerwendetePreise().getVarianten().replace(".", ","));
        jTextFieldWGZ.setText(verwendeteMengenstaffel.getVerwendeterGroßenzuschlag().getWgZuchlag());
        jTextFieldbasisPreisVerw.setText(verwendeteMengenstaffel.getVerwendetePreise().getBasisPreis().replace(".", ","));
        tableModel.setRowCount(0);
        tablemodelvarianten.setRowCount(0);
        tableModelGroessen.setRowCount(0);
        populateJtableMengen(verfugbareMengenstaffelns);
        populateJtablevarianten(verwendeteMengenstaffel.getVariantens());
        populateTableGroessen(this.verfugbareGroßens);
        jlieferDaoInterface = new JlieferDao(dburlProdukt);
        listSelectionModel = jTableverfugGroesse.getSelectionModel();
        tclGroesses = new TableCellListener(jTableverfugGroesse, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tclGroess = (TableCellListener) e.getSource();
                switch (tclGroess.getColumn()) {
                    case 0:
                        verfugbareGroßens.get(tclGroess.getRow()).setKdArtNum(tclGroess.getNewValue().toString());
                        break;
                    case 1:
                        verfugbareGroßens.get(tclGroess.getRow()).setKdFarbe(tclGroess.getNewValue().toString());

                        break;
                    case 2:
                        verfugbareGroßens.get(tclGroess.getRow()).setKdGrosse(tclGroess.getNewValue().toString());

                        break;
                    case 3:
                        verfugbareGroßens.get(tclGroess.getRow()).setKdVariante(tclGroess.getNewValue().toString());

                        break;
                    case 4:
                        verfugbareGroßens.get(tclGroess.getRow()).setSort(tclGroess.getNewValue().toString());

                        break;
                    case 5:
                        verfugbareGroßens.get(tclGroess.getRow()).setGroesse(tclGroess.getNewValue().toString());

                        break;
                    case 6:
                        verfugbareGroßens.get(tclGroess.getRow()).setKd1(tclGroess.getNewValue().toString());

                        break;
                    case 7:
                        verfugbareGroßens.get(tclGroess.getRow()).setGz(tclGroess.getNewValue().toString());

                        break;
                    case 8:
                        verfugbareGroßens.get(tclGroess.getRow()).setGp1(tclGroess.getNewValue().toString());

                        break;
                    case 9:
                        verfugbareGroßens.get(tclGroess.getRow()).setGp2(tclGroess.getNewValue().toString());

                        break;
                    case 10:
                        verfugbareGroßens.get(tclGroess.getRow()).setGp3(tclGroess.getNewValue().toString());

                        break;
                    case 11:
                        verfugbareGroßens.get(tclGroess.getRow()).setGp4(tclGroess.getNewValue().toString());

                        break;
                    case 12:
                        verfugbareGroßens.get(tclGroess.getRow()).setStaffelNum(tclGroess.getNewValue().toString());

                        break;

                }
            }
        });
    }
    
    private void populateJtableMengen(List<VerfugbareMengenstaffeln> verfugbareMengenstaffelns) {
        verfugbareMengenstaffelns.stream().forEach(cnsmr -> {
            addTotableMengen(cnsmr);
        });
    }

    private void addTotableMengen(VerfugbareMengenstaffeln verfugbareMengenstaffeln) {
        rowData[0] = verfugbareMengenstaffeln.getStufe();
        rowData[1] = verfugbareMengenstaffeln.getTyp();
        rowData[2] = verfugbareMengenstaffeln.getMenge1();
        rowData[3] = verfugbareMengenstaffeln.getMenge2();
        rowData[4] = verfugbareMengenstaffeln.getMenge3();
        rowData[5] = verfugbareMengenstaffeln.getMenge4();
        rowData[6] = verfugbareMengenstaffeln.getStaffelNr();
        tableModel.addRow(rowData);

    }

    private void populateJtablevarianten(List<Varianten> variantens) {
        variantens.stream().forEach(cnsmr -> {
            addTotableVarianten(cnsmr);
        });
    }

    private void addTotableVarianten(Varianten varianten) {
        rowDataVarianten[0] = varianten.getNummer();
        rowDataVarianten[1] = varianten.getBezeichung();
        rowDataVarianten[2] = varianten.getAufpreis();
        tablemodelvarianten.addRow(rowDataVarianten);
    }

    private void populateTableGroessen(List<VerfugbareGroßen> verfugbareGroßens) {
        tableModelGroessen.setRowCount(0);
        verfugbareGroßens.stream().forEach(cnsmr -> {
            addTotableGroessen(cnsmr);
        });
    }

    private void addTotableGroessen(VerfugbareGroßen großen) {
        rowDataVerGroesse[0] = großen.getKdArtNum();
        rowDataVerGroesse[1] = großen.getKdFarbe();
        rowDataVerGroesse[2] = großen.getKdGrosse();
        rowDataVerGroesse[3] = großen.getKdVariante();
        rowDataVerGroesse[4] = großen.getSort();
        rowDataVerGroesse[5] = großen.getGroesse();
        rowDataVerGroesse[6] = großen.getKd1();
        rowDataVerGroesse[7] = großen.getGz();
        rowDataVerGroesse[8] = großen.getGp1();
        rowDataVerGroesse[9] = großen.getGp2();
        rowDataVerGroesse[10] = großen.getGp3();
        rowDataVerGroesse[11] = großen.getGp4();
        rowDataVerGroesse[12] = großen.getStaffelNum();
        tableModelGroessen.addRow(rowDataVerGroesse);
    }

    private List<VerfugbareGroßen> getSelectedGrossen() {
        List<VerfugbareGroßen> großens = new ArrayList<>();
        int[] selection;
        selection = jTableverfugGroesse.getSelectedRows();
        for (int i = 0; i < selection.length; i++) {
            VerfugbareGroßen verfugbareGroßen = verfugbareGroßens.get(selection[i]);
            großens.add(verfugbareGroßen);
        }
        großens.stream().forEach(cnsmr -> {
            System.out.println("KD-farbe: " + cnsmr.getKdFarbe());
        });
        return großens;
    }

    private void insertIntoList(String origin, String artNum, String farbNum, String groesse, String varNum, String gtin, String posGridID, String grPreis, String varPreis, String meldung, String message) {
        Protokoll protokoll = new Protokoll();
        protokoll.setOrigin(origin);
        protokoll.setArtNummer(artNum);
        protokoll.setFarbNummer(farbNum);
        protokoll.setGroesse(groesse);
        protokoll.setVarianten(varNum);
        protokoll.setGtin(gtin);
        protokoll.setPosgridID(posGridID);
        protokoll.setNull1(grPreis);
        protokoll.setNull2(varPreis);
        protokoll.setMeldung(meldung);
        protokoll.setMessage(message);
        protokolls.add(protokoll);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldPos = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldKundumMeng = new javax.swing.JTextField();
        jTextFieldStuf = new javax.swing.JTextField();
        jTextFieldTyp = new javax.swing.JTextField();
        jTextFieldStafNr = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldMenge1 = new javax.swing.JTextField();
        jTextFieldMenge2 = new javax.swing.JTextField();
        jTextFieldMenge3 = new javax.swing.JTextField();
        jTextFieldMenge4 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jTextFieldAnderung1 = new javax.swing.JTextField();
        jTextFieldAnderung2 = new javax.swing.JTextField();
        jTextFieldAnderung3 = new javax.swing.JTextField();
        jTextFieldAnderung4 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jTextFieldKundNumGros = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextFieldWGZ = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jTextFieldbasisPreisVerw = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jTextFieldVarianten = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableVariaten = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableverfugGroesse = new javax.swing.JTable();
        jButtonMarkierte = new javax.swing.JButton();
        jButtonGroessenberechnen = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Verwendete Mengenstaffel", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel17.setText("Mengenbezug (Größe / Position):");

        jTextFieldPos.setEditable(false);
        jTextFieldPos.setText(" ");

        jLabel18.setText("Kunden-Nr.");

        jTextFieldKundumMeng.setEditable(false);
        jTextFieldKundumMeng.setText(" ");
        jTextFieldKundumMeng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldKundumMengActionPerformed(evt);
            }
        });

        jTextFieldStuf.setEditable(false);
        jTextFieldStuf.setText(" ");

        jTextFieldTyp.setEditable(false);
        jTextFieldTyp.setText(" ");

        jTextFieldStafNr.setEditable(false);
        jTextFieldStafNr.setText(" ");

        jLabel19.setText("Stufe");

        jLabel20.setText("Typ");

        jLabel21.setText("Staffel-Nr");

        jTextFieldMenge1.setEditable(false);
        jTextFieldMenge1.setText(" ");

        jTextFieldMenge2.setEditable(false);
        jTextFieldMenge2.setText(" ");

        jTextFieldMenge3.setEditable(false);
        jTextFieldMenge3.setText(" ");

        jTextFieldMenge4.setEditable(false);
        jTextFieldMenge4.setText(" ");

        jLabel22.setText("Menge 1");

        jLabel23.setText("Menge 2");

        jLabel24.setText("Menge 3");

        jLabel25.setText("Menge 4");

        jTextFieldAnderung1.setEditable(false);
        jTextFieldAnderung1.setText(" ");

        jTextFieldAnderung2.setEditable(false);
        jTextFieldAnderung2.setText(" ");

        jTextFieldAnderung3.setEditable(false);

        jTextFieldAnderung4.setEditable(false);
        jTextFieldAnderung4.setText(" ");

        jLabel26.setText("Änderung1");

        jLabel27.setText("Änderung2");

        jLabel28.setText("Änderung3");

        jLabel29.setText("Änderung 4");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(123, 123, 123)
                        .addComponent(jTextFieldPos, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel18)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                        .addComponent(jLabel20)
                        .addGap(59, 59, 59)
                        .addComponent(jLabel21)
                        .addGap(29, 29, 29))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jTextFieldAnderung1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldAnderung2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldAnderung3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldAnderung4))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldMenge1, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel22))
                                    .addComponent(jTextFieldKundumMeng))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jTextFieldMenge2, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                                .addComponent(jTextFieldStuf))
                                            .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(jLabel27)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jTextFieldTyp, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextFieldMenge3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                                .addComponent(jLabel28)
                                                .addGap(19, 19, 19))))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel24)
                                        .addGap(26, 26, 26))))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel26)))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextFieldStafNr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addComponent(jTextFieldMenge4))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel29))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldPos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldKundumMeng, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldStuf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTyp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldStafNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldMenge1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMenge2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMenge3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMenge4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldAnderung1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldAnderung2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldAnderung3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldAnderung4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Verwendeter Größenzuschlag", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTextFieldKundNumGros.setEditable(false);
        jTextFieldKundNumGros.setText(" ");

        jLabel30.setText("Kunden-Nr.:");

        jTextFieldWGZ.setEditable(false);
        jTextFieldWGZ.setText(" ");

        jLabel31.setText("WG-Zuschlag");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel30)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldKundNumGros, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldWGZ, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldKundNumGros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jTextFieldWGZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Verwendete Preise", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel32.setText("Basispreis:");

        jTextFieldbasisPreisVerw.setText(" ");

        jLabel33.setText("Varianten:");

        jTextFieldVarianten.setText(" ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel32)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldbasisPreisVerw, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldVarianten, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldbasisPreisVerw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jTextFieldVarianten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Verfügbare Mengenstaffeln", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Stufe", "Typ", "Menge1", "Menge2", "Menge3", "Menge4", "Staffel_Nr"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Varianten", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTableVariaten.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nr", "Bezeichnung", "Aufpreis"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableVariaten);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Verfügbare Größen", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTableverfugGroesse.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kd-Art-Nr", "Kd-Farbe", "Kd-Größe", "Kd-Variante", "Sort", "Größe", "Kd_1", "GZ", "GP1", "GP2", "GP3", "GP4", "Staffel_Nr"
            }
        ));
        jScrollPane3.setViewportView(jTableverfugGroesse);

        jButtonMarkierte.setText("Markierte Zeilen Verarbeiten");
        jButtonMarkierte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMarkierteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(363, 363, 363)
                .addComponent(jButtonMarkierte)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonMarkierte)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButtonGroessenberechnen.setText("Größen-Preise berechnen");
        jButtonGroessenberechnen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGroessenberechnenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonGroessenberechnen))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonGroessenberechnen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldKundumMengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldKundumMengActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldKundumMengActionPerformed

    private void jButtonMarkierteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMarkierteActionPerformed
        // TODO add your handling code here:
        protokolls.clear();
        System.out.println("selectedGroessen: " + getSelectedGrossen().size());
        getSelectedGrossen().stream().forEach(cnsmr -> {
            String meldung1 = jlieferDaoInterface.gtinStammsatzAnderung("2", kundPruferFamakIn.getArtikel_Nr(), kundPruferFamakIn.getFarbeNummer(), cnsmr.getGroesse(), kundPruferFamakIn.getVarNummer(), "", kundPruferFamakIn.getPosGrId());
            System.out.println(meldung1);
            String message;
            String indicator = meldung1;
            if (meldung1.length() > 4) {

                if (meldung1.contains("-")) {
                    indicator = "E-";
                } else {
                    indicator = "E";
                }
                message = jlieferDaoInterface.getMeldung("1", indicator);
                insertIntoList("Dastex", kundPruferFamakIn.getArtikel_Nr(), kundPruferFamakIn.getFarbeNummer(), cnsmr.getGroesse(), kundPruferFamakIn.getVarNummer(), kundPruferFamakIn.getGtin(), kundPruferFamakIn.getPosGrId(), "", "", meldung1, message);
            } else {
                message = jlieferDaoInterface.getMeldung("1", indicator);
                insertIntoList("Dastex", kundPruferFamakIn.getArtikel_Nr(), kundPruferFamakIn.getFarbeNummer(), cnsmr.getGroesse(), kundPruferFamakIn.getVarNummer(), kundPruferFamakIn.getGtin(), kundPruferFamakIn.getPosGrId(), "", "", meldung1, message);

            }

            if (meldung1.length() > 4) {
                //System.out.println("In table:" + jTextFieldKundumMeng.getText() + "---" + cnsmr.getKdFarbe() + "---" + cnsmr.getKdGrosse() + "---" + cnsmr.getKdVariante());
                String meldung2 = jlieferDaoInterface.anlegenAndern("0", kundPruferFamakIn.getKundNummer(), cnsmr.getKdArtNum(), cnsmr.getKdFarbe(), cnsmr.getKdGrosse(), cnsmr.getKdVariante(), meldung1, kundPruferFamakIn.getPosGrId(), cnsmr.getGp1(), preisVariante);
                message = jlieferDaoInterface.getMeldung("2", meldung2);
                insertIntoList(kundNum, cnsmr.getKdArtNum(), cnsmr.getKdFarbe(), cnsmr.getGroesse(), cnsmr.getKdVariante(), kundPruferFamakIn.getGtin(), kundPruferFamakIn.getPosGrId(), cnsmr.getGp1(), preisVariante, meldung1, message);

            }

        });

        JDialogProtokoll dialogProtokoll = new JDialogProtokoll(this, true, protokolls);
        dialogProtokoll.setVisible(true);

        //jlieferDaoInterface.gtinStammsatzAnderung("0", kundPruferFamakIn.getArtikel_Nr(), kundPruferFamakIn.getFarbeNummer(), getSelectedGrossen().get(0).getGroesse(), kundPruferFamakIn.getVariante(), kundPruferFamakIn.getGtin(), kundPruferFamakIn.getPosGrId());

    }//GEN-LAST:event_jButtonMarkierteActionPerformed

    private void jButtonGroessenberechnenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGroessenberechnenActionPerformed
        // TODO add your handling code here:
        double basisPreis = Double.valueOf(jTextFieldbasisPreisVerw.getText().replace(",", "."));
        DecimalFormat df = new DecimalFormat("0.00");

        verfugbareGroßens.stream().forEach(cnsmr -> {
            if (cnsmr.getGz().isEmpty()) {
                cnsmr.setGz("0");
            }
            double gz = Double.parseDouble(cnsmr.getGz());
            String step1 = df.format(basisPreis * (100 + gz) / 100).replace(",", ".");
            String step2 = df.format((100 + Double.parseDouble(jTextFieldAnderung1.getText())) / 100).replace(",", ".");
            //System.out.println("step1: "+step1+"; step2: "+step2);
            double result = Double.parseDouble(step1) * Double.parseDouble(step2);
            System.out.println("result: " + df.format(result));
            cnsmr.setGp1(df.format(result).replace(",", "."));
        }); 
        populateTableGroessen(verfugbareGroßens);
    }//GEN-LAST:event_jButtonGroessenberechnenActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogVerwendete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogVerwendete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogVerwendete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogVerwendete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JDialogVerwendete dialog = new JDialogVerwendete(new JDialog(), true, new VerwendeteMengenstaffel(), new ArrayList<>(), new ArrayList<>(), new LieferKundPrufer(), "", "","");
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
    private javax.swing.JButton jButtonGroessenberechnen;
    private javax.swing.JButton jButtonMarkierte;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableVariaten;
    private javax.swing.JTable jTableverfugGroesse;
    private javax.swing.JTextField jTextFieldAnderung1;
    private javax.swing.JTextField jTextFieldAnderung2;
    private javax.swing.JTextField jTextFieldAnderung3;
    private javax.swing.JTextField jTextFieldAnderung4;
    private javax.swing.JTextField jTextFieldKundNumGros;
    private javax.swing.JTextField jTextFieldKundumMeng;
    private javax.swing.JTextField jTextFieldMenge1;
    private javax.swing.JTextField jTextFieldMenge2;
    private javax.swing.JTextField jTextFieldMenge3;
    private javax.swing.JTextField jTextFieldMenge4;
    private javax.swing.JTextField jTextFieldPos;
    private javax.swing.JTextField jTextFieldStafNr;
    private javax.swing.JTextField jTextFieldStuf;
    private javax.swing.JTextField jTextFieldTyp;
    private javax.swing.JTextField jTextFieldVarianten;
    private javax.swing.JTextField jTextFieldWGZ;
    private javax.swing.JTextField jTextFieldbasisPreisVerw;
    // End of variables declaration//GEN-END:variables
}
