/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dasLieferdao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.ABDaten;
import model.Faktor;
import model.Kund;
import model.LieferKund;
import model.LieferKundDoppel;
import model.LieferKundPrufer;
import model.ParameterKund;
import model.VarPreis;
import model.Varianten;
import model.VerfugbareGroßen;
import model.VerfugbareMengenstaffeln;
import model.VerwendeteMengenstaffel;
import model.VerwendetePreise;
import model.VerwendeterGroßenzuschlag;

/**
 *
 * @author aladhari
 */
public class JlieferDao implements JlieferDaoInterface {

    private final String dburlProdukt;

    String exceptionRecord = "";
    String exceptionUpdate = "";
    boolean verify, proceed = false;
    List<String> indexes = new ArrayList<>(), infamakIndexes = new ArrayList<>(), fehlerIndexes = new ArrayList<>();
    String indexFamak;
    boolean recorded = true;
    boolean updated = true;
    List<String> listMeldung;

    public JlieferDao(String dburlProdukt) {
        this.dburlProdukt = dburlProdukt;
    }

    @Override
    public String anlegenAndern(String indicator, String kundNummer, String kundArtNummer, String kundfarbe, String kundGroesse, String variante, String gtin, String posGrId, String grundPreis, String varPreis) {
        String ret = "";
        try {
            //System.out.println("in dao: '"+indicator+"', '"+kundNummer+"', '"+kundArtNummer+"', '"+kundfarbe+"', '"+kundGroesse+"', '"+variante);
            // String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '0', '1040', '13', 'EL', ';001;002;061O;072;091;111C;111D;', '', '2230531' )";
            //String proc = "SELECT GTIN_Kunde_KdArtNr_anlegen_aendern( '0', '104008', 'null', 'null', 'null', null, '"+gtin+"', '"+posGrId+"', '"+grundPreis+"', '"+varPreis+"' )";
            String proc = "SELECT GTIN_Kunde_KdArtNr_anlegen_aendern( '" + indicator + "', '" + kundNummer + "', '" + kundArtNummer + "', '" + kundfarbe + "', '" + kundGroesse + "', '" + variante + "', '" + gtin + "', '" + posGrId + "', '" + grundPreis + "', '" + varPreis + "' )";

            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString(1);
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public String erfassungAbschliessen(String id, String posGrosId) {
        String ret = "";
        try {
            String proc = "SELECT GTIN_Erfassungsdaten_manuell_abschliessen('" + id + "','" + posGrosId + "')";
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString(1);
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public String erfassungAktualisieren(String id,String kdPosnr,String kdArtNr,String kdfarb,String kdGros,String kdVar,String kdKomm,String kdMeng) {
    String ret = "";
        try {
            String proc = "CALL GTIN_DoppelErfassung_Zeile_aktualisieren('" + id + "','" + kdPosnr + "','" + kdArtNr + "','" + kdfarb + "','" + kdGros + "','" + kdVar + "','" + kdKomm + "','" + kdMeng + "')";
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString(1);
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;    
    }

    @Override
    public String erfassungLoschen(String id) {
        
    String ret = "";
        try {
            String proc = "CALL GTIN_DoppelErfassung_Zeile_loeschen('" + id + "')";
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString(1);
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret; 
    }

    @Override
    public String erfassungManuelzuweisen(String indice, String id, String posGridId, String status) {
        //System.out.println(indice + "','" + id + "','" + posGridId + "','" + status );
        String ret = "";
        try {
            String proc = "SELECT GTIN_Erfassungsdaten_manuell_zuweisen('" + indice + "','" + id + "','" + posGridId + "','" + status + "')";
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString(1);
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public boolean erfassungVerarbeiten() {
        String ret = "";
        try {
            String proc = "CALL GTIN_Erfassungsdaten_verarbeiten('')";

            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString(1);
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;

        }
        return ret.equals("TRUE");
    }

    @Override
    public String getException() {
        return exceptionRecord;
    }

    @Override
    public List<String> getFehlerIndexes() {
        return fehlerIndexes;
    }

    @Override
    public List<String> getIndexInFamak() {
        return infamakIndexes;
    }

    @Override
    public String getOneIndexInFamak() {
       return indexFamak;
    }
    

    @Override
    public List<String> getIndexes() {
        return indexes;
    }

    @Override
    public List<String> getKundenNummers() {
        List<String> nummers = new ArrayList<>();
        String procName = "{call angebot_kundendaten('%','%','%','%')}";
        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(procName);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {

                    nummers.add(rs.getString("Nr"));
                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nummers;
    }

    @Override
    public ParameterKund getKundenParameter(String kundNummer) {
        ParameterKund parameterKund = new ParameterKund();
        try (
                // Connect to Sybase Database
                Connection conProdukt = DriverManager.getConnection(dburlProdukt);
                Statement statementPro = conProdukt.createStatement();
                ResultSet rs = statementPro.executeQuery("select * from GTIN_Erfassungs_Parameter where Kd_Nr = '" + kundNummer + "'");) {

            while (rs.next()) {

                parameterKund.setArtikel_Nr(rs.getString("Artikel_Nr"));

                parameterKund.setErsetzen(rs.getString("Ersetzen"));

                parameterKund.setFarbe(rs.getString("Farbe"));

                parameterKund.setGroesse(rs.getString("Groesse"));

                parameterKund.setKd_Pos_activ(rs.getString("Kd_Pos_aktiv"));

                parameterKund.setKommission(rs.getString("Kommission"));

                parameterKund.setMenge(rs.getString("Menge"));

                parameterKund.setPos_Zaehler(rs.getString("Pos_Zaehler"));

                parameterKund.setPreis(rs.getString("Preis"));

                parameterKund.setSuchen(rs.getString("Suchen"));

                parameterKund.setVariante(rs.getString("Variante"));

                rs.close();
                statementPro.close();
                conProdukt.close();

            }
        } catch (Exception e) {
        }
        return parameterKund;
    }

    @Override
    public String getLagerNr(String kdNr, String kdArtNr, String kdFarbe, String kdGroesse, String kdVariante) {
        String ret = "";
        try {
            String proc = "SELECT GTIN_Kunden_Lagerartikel_pruefen('" + kdNr + "','" + kdArtNr + "','" + kdFarbe + "','" + kdGroesse + "','" + kdVariante + "')";
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    if (rs.getString(1) == null) {
                        ret = "";
                    } else {
                        ret = rs.getString(1);
                    }
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public List<LieferKundDoppel> getListDoppelErfassung() {
        List<LieferKundDoppel> listLiefDoppel = new ArrayList<>();
        String procName = "{CALL GTIN_DoppelErfassung_verarbeiten()}";
        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(procName);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    LieferKundDoppel kundDoppel = new LieferKundDoppel();
                    //verfugbareGroßen.setGp1(rs.getString("GP1") != null ? rs.getString("GP1") : "");
                    kundDoppel.setKdNummer(rs.getString(1) != null ? rs.getString(1) : "");
                    kundDoppel.setKdBestNummer(rs.getString(2)!= null ? rs.getString(2) : "");
                    kundDoppel.setKdPosNummer(rs.getString(3)!= null ? rs.getString(3) : "");
                    kundDoppel.setKdArtNummer(rs.getString(4)!= null ? rs.getString(4) : "");
                    kundDoppel.setKdFarbe(rs.getString(5)!= null ? rs.getString(5) : "");
                    kundDoppel.setKdGroesse(rs.getString(6)!= null ? rs.getString(6) : "");
                    kundDoppel.setKdVariante(rs.getString(7)!= null ? rs.getString(7) : "");
                    kundDoppel.setKommission(rs.getString(8)!= null ? rs.getString(8) : "");
                    kundDoppel.setKdMenge(rs.getString(9)!= null ? rs.getString(9) : "");
                    kundDoppel.setStatus(rs.getString(10)!= null ? rs.getString(10) : "");
                    kundDoppel.setErfasser(rs.getString(11)!= null ? rs.getString(11) : "");
                    kundDoppel.setErfassungDatum(rs.getString(12)!= null ? rs.getString(12) : "");
                    kundDoppel.setId(rs.getString(13)!= null ? rs.getString(13) : "");
                    kundDoppel.setPosGroId(rs.getString(14)!= null ? rs.getString(14) : "");
                    kundDoppel.setBemerkung(rs.getString(15)!= null ? rs.getString(15) : "");
                    
                    listLiefDoppel.add(kundDoppel);

                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listLiefDoppel;
    }

    @Override
    public List<LieferKundDoppel> getListDoppelErfassungZuruck() {
    List<LieferKundDoppel> listLiefDoppel = new ArrayList<>();
        String procName = "{CALL GTIN_DoppelErfassung_Status_zuruecksetzen ()}";
        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(procName);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    LieferKundDoppel kundDoppel = new LieferKundDoppel();
                    kundDoppel.setKdNummer(rs.getString(1)!= null ? rs.getString(1) : "");
                    kundDoppel.setKdBestNummer(rs.getString(2)!= null ? rs.getString(2) : "");
                    kundDoppel.setKdPosNummer(rs.getString(3)!= null ? rs.getString(3) : "");
                    kundDoppel.setKdArtNummer(rs.getString(4)!= null ? rs.getString(4) : "");
                    kundDoppel.setKdFarbe(rs.getString(5)!= null ? rs.getString(5) : "");
                    kundDoppel.setKdGroesse(rs.getString(6)!= null ? rs.getString(6) : "");
                    kundDoppel.setKdVariante(rs.getString(7)!= null ? rs.getString(7) : "");
                    kundDoppel.setKommission(rs.getString(8)!= null ? rs.getString(8) : "");
                    kundDoppel.setKdMenge(rs.getString(9)!= null ? rs.getString(9) : "");
                    kundDoppel.setStatus(rs.getString(10)!= null ? rs.getString(10) : "");
                    kundDoppel.setErfasser(rs.getString(11)!= null ? rs.getString(11) : "");
                    kundDoppel.setErfassungDatum(rs.getString(12)!= null ? rs.getString(12) : "");
                    kundDoppel.setId(rs.getString(13)!= null ? rs.getString(13) : "");
                    kundDoppel.setPosGroId(rs.getString(14)!= null ? rs.getString(14) : "");
                    kundDoppel.setBemerkung(rs.getString(15)!= null ? rs.getString(15) : "");
                    
                    listLiefDoppel.add(kundDoppel);
                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listLiefDoppel;    
    }

    @Override
    public List<Faktor> getListFaktor(String kundNummer, String artNummer) {
        List<Faktor> listfaktor = new ArrayList<>();
        String procName = "{CALL GTIN_ME_Faktor_Liste ('" + kundNummer + "','" + artNummer + "')}";
        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(procName);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Faktor faktor = new Faktor();
                    if (kundNummer.equals("000000")) {
                        faktor.setMe(rs.getString(1));
                    } else {
                        faktor.setZehler(rs.getString(1) != null ? rs.getString(1) : "");
                        faktor.setMe(rs.getString(2) != null ? rs.getString(2) : "");
                        faktor.setFaktor(rs.getString(3) != null ? rs.getString(3) : "");
                        faktor.setRunde(rs.getString(4) != null ? rs.getString(4) : "");
                        faktor.setNks(rs.getString(5) != null ? rs.getString(5) : "");

                    }
                    listfaktor.add(faktor);
                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listfaktor;
    }

    @Override
    public List<LieferKundPrufer> getListGtinAnderung(String KdNr, String KdArtNr, String KdFarbe, String KdGroße, String KdVariante, String GTIN, String grundPreis, String varPreis) {
        List<LieferKundPrufer> listGtinAnderung = new ArrayList<>();

        //System.out.println("getListGtinAnderung parameter: " + KdNr + "','" + KdArtNr + "','" + KdFarbe + "','" + KdGroße + "','" + KdVariante + "','" + GTIN + "','" + grundPreis + "','" + varPreis);
        String procName = "{CALL GTIN_Kunde_KdArtNr_Liste ('" + KdNr + "','" + KdArtNr + "','" + KdFarbe + "','" + KdGroße + "','" + KdVariante + "','" + GTIN + "','" + grundPreis + "','" + varPreis + "')}";
        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(procName);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    LieferKundPrufer kund = new LieferKundPrufer();

                    kund.setKundenArtikelNummer(rs.getString(2));
                    kund.setFarbeNummer(rs.getString(3));
                    kund.setGroesse(rs.getString(4));
                    kund.setVarNummer(rs.getString(5));
                    kund.setGtin(rs.getString(1));
                    kund.setPosGrPreis(rs.getString(6));
                    kund.setGtinPreis(rs.getString(7));
                    listGtinAnderung.add(kund);

                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listGtinAnderung;
    }

    @Override
    public List<Kund> getListKundGtin(String gtin) {
        List<Kund> kunds = new ArrayList<>();
        String procName = "{CALL GTIN_Kunden_GTIN_Liste ('" + gtin + "')}";
        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(procName);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Kund kund = new Kund();
                    kund.setKdNummer(rs.getString(1));
                    kund.setKdArtNummer(rs.getString(2));
                    kund.setKdFarbe(rs.getString(3));
                    kund.setKdGrosse(rs.getString(4));
                    kund.setKdvariante(rs.getString(5));
                    kund.setKdName(rs.getString(6));
                    kunds.add(kund);
                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return kunds;
    }

    @Override
    public List<LieferKundPrufer> getListPrufers(String datum) {
        List<LieferKundPrufer> kundPrufers = new ArrayList<>();
        String procName = "{CALL GTIN_Erfassungsdaten_pruefen ('" + datum + "')}";
        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(procName);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    LieferKundPrufer liefPrufer = new LieferKundPrufer();

                    liefPrufer.setZeile(rs.getString("Zeile") != null ? rs.getString("Zeile") : "");
                    liefPrufer.setTreffer(rs.getString("Treffer") != null ? rs.getString("Treffer") : "");
                    liefPrufer.setKundNummer(rs.getString("Kd_Nr") != null ? rs.getString("Kd_Nr") : "");
                    liefPrufer.setBestNummer(rs.getString("Kd_Best_Nr") != null ? rs.getString("Kd_Best_Nr") : "");
                    liefPrufer.setKdBestDatum(rs.getString("Kd_Best_Datum") != null ? rs.getString("Kd_Best_Datum") : "");
                    liefPrufer.setKdWunchDatum(rs.getString("Kd_Wunsch_Datum") != null ? rs.getString("Kd_Wunsch_Datum") : "");
                    liefPrufer.setErfasser(rs.getString("Erfasser") != null ? rs.getString("Erfasser") : "");
                    liefPrufer.setErfassungsDatum(rs.getString("Erfassungs_Datum") != null ? rs.getString("Erfassungs_Datum") : "");
                    liefPrufer.setPosiNummer(rs.getString("Kd_Pos_Nr") != null ? rs.getString("Kd_Pos_Nr") : "");
                    liefPrufer.setKundenArtikelNummer(rs.getString("Kd_Artikel_Nr") != null ? rs.getString("Kd_Artikel_Nr") : "");
                    liefPrufer.setFarbe(rs.getString("Kd_Farbe") != null ? rs.getString("Kd_Farbe") : "");
                    liefPrufer.setKdgroesse(rs.getString("Kd_Groesse") != null ? rs.getString("Kd_Groesse") : "");
                    liefPrufer.setVariante(rs.getString("Kd_Variante") != null ? rs.getString("Kd_Variante") : "");
                    liefPrufer.setMenge(rs.getString("Kd_Menge") != null ? rs.getString("Kd_Menge") : "");
                    liefPrufer.setPreis(rs.getString("Kd_Preis") != null ? rs.getString("Kd_Preis") : "");
                    liefPrufer.setKommission(rs.getString("Kommission") != null ? rs.getString("Kommission") : "");
                    liefPrufer.setKd_Pos_activ(rs.getString("Kd_Pos_aktiv") != null ? rs.getString("Kd_Pos_aktiv") : "");
                    liefPrufer.setStatus(rs.getString("Status") != null ? rs.getString("Status") : "");
                    liefPrufer.setArtikelId(rs.getString("Artikel_ID") != null ? rs.getString("Artikel_ID") : "");
                    liefPrufer.setArtikel_Nr(rs.getString("Artikel_Nr") != null ? rs.getString("Artikel_Nr") : "");
                    liefPrufer.setFarbeNummer(rs.getString("Farb_Nr") != null ? rs.getString("Farb_Nr") : "");
                    liefPrufer.setGroesse(rs.getString("Groesse") != null ? rs.getString("Groesse") : "");
                    liefPrufer.setVarNummer(rs.getString("Var_Nummern") != null ? rs.getString("Var_Nummern") : "");
                    liefPrufer.setGtin(rs.getString("GTIN") != null ? rs.getString("GTIN") : "");
                    liefPrufer.setZielMenge(rs.getString("Ziel_Menge") != null ? rs.getString("Ziel_Menge") : "");
                    liefPrufer.setPosGrId(rs.getString("Pos_Gr_ID") != null ? rs.getString("Pos_Gr_ID") : "");
                    liefPrufer.setId(rs.getString("ID") != null ? rs.getString("ID") : "");
                    liefPrufer.setLagerNum(rs.getString("Lager_Nr") != null ? rs.getString("Lager_Nr") : "");
                    liefPrufer.setGtinPreis(rs.getString("GTIN_Preis") != null ? rs.getString("GTIN_Preis") : "");
                    liefPrufer.setPosGrPreis(rs.getString("Pos_Gr_Preis") != null ? rs.getString("Pos_Gr_Preis") : "");
                    liefPrufer.setKalkPreis(rs.getString("kalk_Preis") != null ? rs.getString("kalk_Preis") : "");
                    kundPrufers.add(liefPrufer);
                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kundPrufers;
    }

    @Override
    public List<VarPreis> getListVarPreis(String kdNummer, String kdArtNummer, String kdFarbe, String kdGroesse, String kdVariante, String menge, String lagerNummer) {
        List<VarPreis> varPreises = new ArrayList<>();
        String proc = "CALL GTIN_Varianten_KdArtNr_Liste( '" + kdNummer + "', '" + kdArtNummer + "', '" + kdFarbe + "', '" + kdGroesse + "', '" + kdVariante + "', '" + menge + "', '" + lagerNummer + "')";
        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(proc);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    VarPreis varPreis = new VarPreis();
                    varPreis.setZeile(rs.getString("Zeile"));
                    varPreis.setVkPreis(rs.getString("VkPreis"));

                    varPreis.setVarText(rs.getString("VarText"));
                    varPreis.setVarNummer(rs.getString("VarNr"));
                    varPreis.setKdPreis(rs.getString("KdPreis"));
                    varPreises.add(varPreis);
                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return varPreises;
    }

    @Override
    public List<VerfugbareMengenstaffeln> getListVerfugmeng(String indice, String posGrossID) {
        List<VerfugbareMengenstaffeln> verfugbareMengenstaffelns = new ArrayList<>();
        String proc = "CALL GTIN_Preisermittlung_Basisdaten_GrPosID ( '" + indice + "', '" + posGrossID + "')";

        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(proc);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    VerfugbareMengenstaffeln mengenstaffeln = new VerfugbareMengenstaffeln();
                    mengenstaffeln.setStufe(rs.getString("Stufe"));
                    mengenstaffeln.setTyp(rs.getString("Typ"));
                    mengenstaffeln.setMenge1(rs.getString("Menge1"));
                    mengenstaffeln.setMenge2(rs.getString("Menge2"));
                    mengenstaffeln.setMenge3(rs.getString("Menge3"));
                    mengenstaffeln.setMenge4(rs.getString("Menge4"));
                    verfugbareMengenstaffelns.add(mengenstaffeln);
                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return verfugbareMengenstaffelns;

    }

    @Override
    public List<VerfugbareGroßen> getListverfugGroesse(String indice, String posGrosId) {
        List<VerfugbareGroßen> verfugbareGroßens = new ArrayList<>();
        String proc = "CALL GTIN_Preisermittlung_Basisdaten_GrPosID ( '" + indice + "', '" + posGrosId + "')";

        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(proc);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    VerfugbareGroßen verfugbareGroßen = new VerfugbareGroßen();
                    verfugbareGroßen.setGp1(rs.getString("GP1") != null ? rs.getString("GP1") : "");
                    verfugbareGroßen.setGp2(rs.getString("GP2") != null ? rs.getString("GP2") : "");
                    verfugbareGroßen.setGp3(rs.getString("GP3") != null ? rs.getString("GP3") : "");
                    verfugbareGroßen.setGp4(rs.getString("GP4") != null ? rs.getString("GP4") : "");
                    verfugbareGroßen.setGroesse(rs.getString("Groesse") != null ? rs.getString("Groesse") : "");
                    verfugbareGroßen.setGz(rs.getString("GZ") != null ? rs.getString("GZ") : "");
                    verfugbareGroßen.setKd1(rs.getString("Kd_1") != null ? rs.getString("Kd_1") : "");
                    verfugbareGroßen.setKdArtNum(rs.getString("Kd-Art-Nr") != null ? rs.getString("Kd-Art-Nr") : "");
                    verfugbareGroßen.setKdFarbe(rs.getString("Kd-Farbe") != null ? rs.getString("Kd-Farbe") : "");
                    verfugbareGroßen.setKdGrosse(rs.getString("Kd-Größe") != null ? rs.getString("Kd-Größe") : "");
                    verfugbareGroßen.setKdVariante(rs.getString("Kd-Variante") != null ? rs.getString("Kd-Variante") : "");
                    verfugbareGroßen.setSort(rs.getString("Sort") != null ? rs.getString("Sort") : "");
                    verfugbareGroßen.setStaffelNum(rs.getString("Staffel_Nr") != null ? rs.getString("Staffel_Nr") : "");
                    verfugbareGroßens.add(verfugbareGroßen);
                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("verfugbareGroßens lengh: "+verfugbareGroßens.size());
        return verfugbareGroßens;
    }

    @Override
    public String getMeldung(String vorgangNummer, String meldungNummer) {
        String ret = "";
        try {
            String proc = "CALL GTIN_Meldung('" + vorgangNummer + "','" + meldungNummer + "')";
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString("Meldung_Text") + "--" + rs.getString("Vorgang_Text");

                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public String getMengenbezug(String kdNummer) {
        String ret = "";
        try (
                // Connect to Sybase Database
                Connection conProdukt = DriverManager.getConnection(dburlProdukt);
                Statement statementPro = conProdukt.createStatement();
                ResultSet rs = statementPro.executeQuery("SELECT Mng_Bezug_GP FROM GTIN_Preis_Parameter WHERE Kd_Nr = '" + kdNummer + "'");) {

            while (rs.next()) {

                ret = rs.getString(1);
                //System.out.println(ret);
                rs.close();
                statementPro.close();
                conProdukt.close();

            }
        } catch (Exception e) {
        }

        return ret;
    }

    @Override
    public String getPreisVariante(String posGridID) {
        String ret = "";
        try {
            String proc = "SELECT GTIN_Preisermittlung_Varianten_GrPosID('" + posGridID + "')";
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString(1);
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public VerwendeteMengenstaffel getVerMengen(String indice, String posGridId) {
        VerwendeteMengenstaffel verwendeteMengenstaffel = new VerwendeteMengenstaffel();
        VerwendetePreise verwendetePreise = new VerwendetePreise();
        VerwendeterGroßenzuschlag verwendeterGroßenzuschlag = new VerwendeterGroßenzuschlag();

        try {
            //String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '0', '1701000', '13', 'EL', ';001;002;061O;072;091;111C;111D;', '', '2230531' )";
            String proc = "CALL GTIN_Preisermittlung_Basisdaten_GrPosID ( '" + indice + "', '" + posGridId + "')";

            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    verwendeteMengenstaffel.setKundNummer(rs.getString("s_Kd_Nr")!= null ? rs.getString("s_Kd_Nr") : "");
                    //System.out.println(verwendeteMengenstaffel.getKundNummer());
                    verwendeteMengenstaffel.setStufe(rs.getString("s_Stufe")!= null ? rs.getString("s_Stufe") : "");
                    verwendeteMengenstaffel.setTyp(rs.getString("s_Typ")!= null ? rs.getString("s_Typ") : "");
                    verwendeteMengenstaffel.setStaffelNr(rs.getString("s_Staffel_Nr")!= null ? rs.getString("s_Staffel_Nr") : "");
                    verwendeteMengenstaffel.setMenge1(rs.getString("s_Menge_1")!= null ? rs.getString("s_Menge_1") : "");
                    verwendeteMengenstaffel.setMenge2(rs.getString("s_Menge_2")!= null ? rs.getString("s_Menge_2") : "");
                    verwendeteMengenstaffel.setMenge3(rs.getString("s_Menge_3")!= null ? rs.getString("s_Menge_3") : "");
                    verwendeteMengenstaffel.setMenge4(rs.getString("s_Menge_4")!= null ? rs.getString("s_Menge_4") : "");
                    verwendeteMengenstaffel.setMengeBetzeug(rs.getString("s_Mng_Bezug_GP")!= null ? rs.getString("s_Mng_Bezug_GP") : "");
                    verwendeteMengenstaffel.setAnderung1(rs.getString("s_Aenderung_1")!= null ? rs.getString("s_Aenderung_1") : "");
                    verwendeteMengenstaffel.setAnderung2(rs.getString("s_Aenderung_2")!= null ? rs.getString("s_Aenderung_2") : "");
                    verwendeteMengenstaffel.setAnderung3(rs.getString("s_Aenderung_3")!= null ? rs.getString("s_Aenderung_3") : "");
                    verwendeteMengenstaffel.setAnderung4(rs.getString("s_Aenderung_4")!= null ? rs.getString("s_Aenderung_4") : "");
                    
                    verwendetePreise.setBasisPreis(rs.getString("s_Basispreis_1")!= null ? rs.getString("s_Basispreis_1") : "");
                    verwendetePreise.setVarianten(getPreisvariante(posGridId));
                    verwendeteMengenstaffel.setVerwendetePreise(verwendetePreise);
                    verwendeterGroßenzuschlag.setKundNummer(rs.getString("z_Kd_Nr")!= null ? rs.getString("z_Kd_Nr") : "");
                    verwendeterGroßenzuschlag.setWgZuchlag(rs.getString("s_warengruppe_GZ")!= null ? rs.getString("s_warengruppe_GZ") : "");
                    verwendeteMengenstaffel.setVerwendeterGroßenzuschlag(verwendeterGroßenzuschlag);
                    verwendeteMengenstaffel.setVariantens(getListVarianten(posGridId));
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verwendeteMengenstaffel;
    }

    @Override
    public ABDaten getabDaten(String posGridId) {
    ABDaten aBDaten = new ABDaten();
    try {
            //String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '0', '1701000', '13', 'EL', ';001;002;061O;072;091;111C;111D;', '', '2230531' )";
            String proc = "CALL GTIN_Famak_AB_Daten_nennen_aus_PosGrID('" + posGridId + "')";

            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    aBDaten.setAb(rs.getString("AB"));
                    aBDaten.setGrNr(rs.getString("GrNr"));
                    aBDaten.setPos(rs.getString("Pos"));
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    return aBDaten;
    }

    @Override
    public String gtinStammsatzAnderung(String indicator, String ArtNr, String FarbNr, String Gross, String Varianten, String GTIN, String PosGrID) {
        String ret = "";
        try {
            String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '" + indicator + "', '" + ArtNr + "', '" + FarbNr + "', '" + Gross + "', '" + Varianten + "', '" + GTIN + "', '" + PosGrID + "' )";

            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString(1);
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public String updateFaktor(String indice, String KdNr, String ArtikelNr, String Faktor, String runden, String NKS) {
        String ret = "";
        try {
            //String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '0', '1701000', '13', 'EL', ';001;002;061O;072;091;111C;111D;', '', '2230531' )";
            String proc = "SELECT GTIN_ME_Faktor_anlegen_aendern( '" + indice + "', '" + KdNr + "', '" + ArtikelNr + "', '" + Faktor + "', '" + runden + "', '" + NKS + "')";

            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString(1);
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public List<String> updateInFamak(String kundnummer, String kdBest, String kdBesDate, String kundWunch, List<LieferKund> lieferKunds) {

        listMeldung = new ArrayList<>();
        lieferKunds.stream().forEach((cnsmr)
                -> {
            String procName = "SELECT GTIN_Erfassungsdaten_in_Famak_schreiben( '" + kundnummer
                    + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + cnsmr.getPosiNummer()
                    + "', '" + cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "' , '" + cnsmr.getGroesse()
                    + "', '" + cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getSumme() + "', '" + cnsmr.getLagerNum() + "', '" + cnsmr.getKommission() + "')";
           
            
            if (cnsmr.getId().equals("0")) {
                try {
                    Connection conProdukt = DriverManager.getConnection(dburlProdukt);
                    Statement s = conProdukt.createStatement();
                    try (ResultSet rs = s.executeQuery(procName)) {

                        while (rs.next()) {
                            //listMeldung.add(cnsmr.getPosiNummer()+"-"+rs.getString(1)+"//"+cnsmr.hashCode());
                            listMeldung.add(rs.getString(1)+"//"+cnsmr.hashCode());
                            if (rs.getString(1).startsWith("F")) {
                              fehlerIndexes.add(cnsmr.getPosiNummer());
                            }else{
                               infamakIndexes.add(cnsmr.getPosiNummer());
                               indexFamak = cnsmr.getPosiNummer();
                            }  
                        }

                        rs.close();
                        s.close();
                        conProdukt.close();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
                    recorded = false;
                    exceptionRecord = ex.getMessage();
                }
            }

        });
        return listMeldung;
    }
    
    @Override
    public String insertInFamak(String kundnummer, String kdBest, String kdBesDate, String kundWunch, LieferKund cnsmr){
        String meldung = "";
        String procName = "SELECT GTIN_Erfassungsdaten_in_Famak_schreiben( '" + kundnummer
                    + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + cnsmr.getPosiNummer()
                    + "', '" + cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "' , '" + cnsmr.getGroesse()
                    + "', '" + cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getSumme() + "', '" + cnsmr.getLagerNum() + "', '" + cnsmr.getKommission() + "')";
            if (cnsmr.getId().equals("0")) {
                try {
                    Connection conProdukt = DriverManager.getConnection(dburlProdukt);
                    Statement s = conProdukt.createStatement();
                    try (ResultSet rs = s.executeQuery(procName)) {
//                    if (kdBesDate==null) {
//                       rs.setNull(3, java.sql.Types.DATE); 
//                       rs.
//                    }
//                    if (kundWunch==null) {
//                       cs.setNull(4, java.sql.Types.DATE); 
//                    }
                        while (rs.next()) {
                            listMeldung.add(cnsmr.getPosiNummer()+"-"+rs.getString(1));
                            if (rs.getString(1).startsWith("F")) {
                              fehlerIndexes.add(cnsmr.getPosiNummer());
                            }else{
                               infamakIndexes.add(cnsmr.getPosiNummer()); 
                            }  
                        }

                        rs.close();
                        s.close();
                        conProdukt.close();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
                    recorded = false;
                    exceptionRecord = ex.getMessage();
                }
            }
        return meldung;
    }

    @Override
    public boolean updateTableGin(String kundnummer, String kdBest, String kdBesDate, String kundWunch, String erfasser, String erDatum, String kdPosActiv, List<LieferKund> lieferKunds, String id) {

        lieferKunds.stream().forEach(cnsmr -> {
            
            String procName;
            switch (id) {

                case "0":
                    if (cnsmr.getStatus().equals("0") && cnsmr.getUbergabe().equals("0") && cnsmr.getId().equals("0")) {
                        procName = "{CALL GTIN_Erfassungsdaten_speichern( '" + kundnummer
                                + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + erfasser
                                + "', '" + erDatum + "', '" + kdPosActiv + "' , '" + cnsmr.getPosiNummer()
                                + "', '" + cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "', '" + cnsmr.getGroesse()
                                + "', '" + cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getPreis()
                                + "', '" + cnsmr.getKommission() + "', '" + "" + "', '" + "" + "', '" + "" + "')}";
                        executeQuery(kdBesDate, kundWunch, erDatum, procName, cnsmr);
                    } else {
                        recorded = false;
                    }
                    break;

                case "1":
                    if (cnsmr.getStatus().equals("0")) {
                        procName = "{CALL GTIN_Erfassungsdaten_speichern( '" + kundnummer
                                + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + erfasser
                                + "', '" + erDatum + "', '" + kdPosActiv + "' , '" + cnsmr.getPosiNummer()
                                + "', '" + cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "', '" + cnsmr.getGroesse()
                                + "', '" + cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getPreis()
                                + "', '" + cnsmr.getKommission() + "', '" + "901" + "', '" + "" + "', '" + cnsmr.getStatus() + "')}";

                        executeQuery(kdBesDate, kundWunch, erDatum, procName, cnsmr);

                    } else {
                        recorded = false;
                    }
                    break;

                case "2":
                    if (cnsmr.getUbergabe().equals("0")) {
                        procName = "{CALL GTIN_Erfassungsdaten_speichern( '" + kundnummer
                                + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + erfasser
                                + "', '" + erDatum + "', '" + kdPosActiv + "' , '" + cnsmr.getPosiNummer()
                                + "', '" + cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "', '" + cnsmr.getGroesse()
                                + "', '" + cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getPreis()
                                + "', '" + cnsmr.getKommission() + "', '" + "902" + "', '" + "" + "', '" + cnsmr.getUbergabe() + "')}";
                        if (!cnsmr.getStatus().equals("0")) {
                            int dialogResult = JOptionPane.showConfirmDialog(
                                    null,
                                    "Die Daten wurden bereits für \"Speichern 1\" verwendet, wollen Sie fortfahren?",
                                    "Achtung",
                                    JOptionPane.YES_NO_OPTION);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                // Saving code here
                                executeQuery(kdBesDate, kundWunch, erDatum, procName, cnsmr);
                            } else {
                                recorded = false;
                            }
                        } else {
                            executeQuery(kdBesDate, kundWunch, erDatum, procName, cnsmr);
                        }

                    } else {
                        recorded = false;
                    }
                    break;

                case "3":
                    if (cnsmr.getStatus().equals("0")) {

                        procName = "{CALL GTIN_Erfassungsdaten_speichern( '" + kundnummer
                                + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + erfasser
                                + "', '" + erDatum + "', '" + kdPosActiv + "' , '" + cnsmr.getPosiNummer()
                                + "', '" + cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "', '" + cnsmr.getGroesse()
                                + "', '" + cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getPreis()
                                + "', '" + cnsmr.getKommission() + "', '" + "900" + "', '" + cnsmr.getMeldungFamak() + "', '" + cnsmr.getId() + "')}";

                        executeQuery(kdBesDate, kundWunch, erDatum, procName, cnsmr);
                    } else {
                        recorded = false;
                    }
                    break;
            }
        });
        return recorded;
    }

    @Override
    public boolean insertIntoDb(String kundnummer, String kdBest, String kdBesDate, String kundWunch, String erfasser, String erDatum, String kdPosActiv,LieferKund cnsmr, String id){
        String procName;
            switch (id) {

            case "0":
                if (cnsmr.getStatus().equals("0") && cnsmr.getUbergabe().equals("0") && cnsmr.getId().equals("0")) {
                    procName = "{CALL GTIN_Erfassungsdaten_speichern( '" + kundnummer
                            + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + erfasser
                            + "', '" + erDatum + "', '" + kdPosActiv + "' , '" + cnsmr.getPosiNummer()
                            + "', '" + cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "', '" + cnsmr.getGroesse()
                            + "', '" + cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getPreis()
                            + "', '" + cnsmr.getKommission() + "', '" + "" + "', '" + "" + "', '" + "" + "')}";
                    executeQuery(kdBesDate, kundWunch, erDatum, procName, cnsmr);
                } else {
                    recorded = false;
                }
                break;

            case "1":
                if (cnsmr.getStatus().equals("0")) {
                    procName = "{CALL GTIN_Erfassungsdaten_speichern( '" + kundnummer
                            + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + erfasser
                            + "', '" + erDatum + "', '" + kdPosActiv + "' , '" + cnsmr.getPosiNummer()
                            + "', '" + cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "', '" + cnsmr.getGroesse()
                            + "', '" + cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getPreis()
                            + "', '" + cnsmr.getKommission() + "', '" + "901" + "', '" + "" + "', '" + cnsmr.getStatus() + "')}";

                    executeQuery(kdBesDate, kundWunch, erDatum, procName, cnsmr);

                } else {
                    recorded = false;
                }
                break;

            case "2":
                if (cnsmr.getUbergabe().equals("0")) {
                    procName = "{CALL GTIN_Erfassungsdaten_speichern( '" + kundnummer
                            + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + erfasser
                            + "', '" + erDatum + "', '" + kdPosActiv + "' , '" + cnsmr.getPosiNummer()
                            + "', '" + cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "', '" + cnsmr.getGroesse()
                            + "', '" + cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getPreis()
                            + "', '" + cnsmr.getKommission() + "', '" + "902" + "', '" + "" + "', '" + cnsmr.getUbergabe() + "')}";
                    if (!cnsmr.getStatus().equals("0")) {
                        int dialogResult = JOptionPane.showConfirmDialog(
                                null,
                                "Die Daten wurden bereits für \"Speichern 1\" verwendet, wollen Sie fortfahren?",
                                "Achtung",
                                JOptionPane.YES_NO_OPTION);
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            // Saving code here
                            executeQuery(kdBesDate, kundWunch, erDatum, procName, cnsmr);
                        } else {
                            recorded = false;
                        }
                    } else {
                        executeQuery(kdBesDate, kundWunch, erDatum, procName, cnsmr);
                    }

                } else {
                    recorded = false;
                }
                break;

            case "3":

                if (cnsmr.getStatus().equals("0")) {

                    procName = "{CALL GTIN_Erfassungsdaten_speichern( '" + kundnummer
                            + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + erfasser
                            + "', '" + erDatum + "', '" + kdPosActiv + "' , '" + cnsmr.getPosiNummer()
                            + "', '" + cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "', '" + cnsmr.getGroesse()
                            + "', '" + cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getPreis()
                            + "', '" + cnsmr.getKommission() + "', '" + "900" + "', '" + cnsmr.getMeldungFamak() + "', '" + cnsmr.getId() + "')}";

                    executeQuery(kdBesDate, kundWunch, erDatum, procName, cnsmr);
                } else {
                    recorded = false;
                }
                break;
        }
        return recorded;
    }

    @Override
    public boolean updateTablePrufen(String id, String posNr, String artikelNr, String farbe, String groesse, String variante, String menge, String preis, String kommission) {
        String procName = "{CALL GTIN_Erfassungsdaten_aendern( '" + id
                + "', '" + posNr + "', '" + artikelNr + "', '" + farbe + "', '" + groesse
                + "', '" + variante + "', '" + menge + "' , '" + preis
                + "', '" + kommission + "')}";
        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            try (CallableStatement cs = conProdukt.prepareCall(procName)) {

                cs.executeQuery();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
            updated = false;
            exceptionUpdate = ex.getMessage();
        }
        return updated;
    }

    private boolean executeQuery(String kdBesDate, String kundWunch, String erDatum, String procName, LieferKund cnsmr) {
        boolean liefRecorded;
        try {
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            try (CallableStatement cs = conProdukt.prepareCall(procName)) {
                if (kdBesDate == null) {
                    cs.setNull(3, java.sql.Types.DATE);
                }
                if (kundWunch == null) {
                    cs.setNull(4, java.sql.Types.DATE);
                }
                if (erDatum == null) {
                    cs.setNull(6, java.sql.Types.DATE);
                }
                //cs.executeUpdate();

                cs.executeQuery();
                cs.close();
                conProdukt.close();
                indexes.add(cnsmr.getPosiNummer());
            }
           recorded = true;
           liefRecorded = true;
           
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
            recorded = false;
            liefRecorded = false;
            exceptionRecord = ex.getMessage();
        }
        
        return liefRecorded;
    }

    private List<Varianten> getListVarianten(String posGridId) {
        List<Varianten> variantens = new ArrayList<>();
        String proc = "CALL GTIN_Varianten_Position_Liste ( '" + posGridId + "')";

        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(proc);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Varianten varianten = new Varianten();
                    varianten.setAufpreis(rs.getString("Aufpreis"));
                    varianten.setBezeichung(rs.getString("Bezeichnung"));
                    varianten.setNummer(rs.getString("Nr"));

                    variantens.add(varianten);
                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return variantens;

    }

    private String getPreisvariante(String posGridId) {
        String varPreis = "";
        try {
            //String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '0', '1701000', '13', 'EL', ';001;002;061O;072;091;111C;111D;', '', '2230531' )";
            String proc = "SELECT GTIN_Preisermittlung_Varianten_GrPosID ( '" + posGridId + "')";

            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    varPreis = rs.getString(1);
                }
                rs.close();
                s.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return varPreis;
    }
}
