/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

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
import model.Kund;
import model.LieferKund;
import model.LieferKundPrufer;
import model.ParameterKund;

/**
 *
 * @author aladhari
 */
public class JlieferDao implements JlieferDaoInterface {
    boolean recorded= true;
    boolean updated=true;
    String exceptionRecord="";
    String exceptionUpdate="";

    private final String dburlProdukt;
    public JlieferDao(String dburlProdukt) {
        this.dburlProdukt = dburlProdukt;
    }

    @Override
    public String getException() {
        return exceptionRecord;
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
    public List<Kund> getListKundGtin(String gtin) {
        List<Kund> kunds = new ArrayList<>();
        String procName = "{CALL GTIN_Kunden_GTIN_Liste ('" +gtin+ "')}";
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
        kunds.stream().forEach(cnsmr->{
            System.out.println(cnsmr.getKdArtNummer());
    });
        return kunds;
        
    }

    @Override
    public List<LieferKundPrufer> getListPrugers(String datum) {
        List<LieferKundPrufer> kundPrufers = new ArrayList<>();
        String procName = "{CALL GTIN_Erfassungsdaten_pruefen ('" + datum + "')}";
        Connection conProdukt;
        try {
            conProdukt = DriverManager.getConnection(dburlProdukt);
            CallableStatement cs = conProdukt.prepareCall(procName);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    LieferKundPrufer liefPrufer = new LieferKundPrufer();
                    liefPrufer.setZeile(rs.getString("Zeile"));
                    liefPrufer.setTreffer(rs.getString("Treffer"));
                    liefPrufer.setKundNummer(rs.getString("Kd_Nr"));
                    liefPrufer.setBestNummer(rs.getString("Kd_Best_Nr"));
                    liefPrufer.setKdBestDatum(rs.getString("Kd_Best_Datum"));
                    liefPrufer.setKdWunchDatum(rs.getString("Kd_Wunsch_Datum"));
                    liefPrufer.setErfasser(rs.getString("Erfasser"));
                    liefPrufer.setErfassungsDatum(rs.getString("Erfassungs_Datum"));
                    liefPrufer.setPosiNummer(rs.getString("Kd_Pos_Nr"));
                    liefPrufer.setKundenArtikelNummer(rs.getString("Kd_Artikel_Nr"));
                    liefPrufer.setFarbe(rs.getString("Kd_Farbe"));
                    liefPrufer.setGroesse(rs.getString("Kd_Groesse"));
                    liefPrufer.setVariante(rs.getString("Kd_Variante"));
                    liefPrufer.setMenge(rs.getString("Kd_Menge"));
                    liefPrufer.setPreis(rs.getString("Kd_Menge"));
                    liefPrufer.setKommission(rs.getString("Kommission"));
                    liefPrufer.setKd_Pos_activ(rs.getString("Kd_Pos_aktiv"));
                    liefPrufer.setStatus(rs.getString("Status"));
                    liefPrufer.setArtikelId(rs.getString("Artikel_ID"));
                    liefPrufer.setArtikel_Nr(rs.getString("Artikel_Nr"));
                    liefPrufer.setFarbeNummer(rs.getString("Farb_Nr"));
                    liefPrufer.setGroesse(rs.getString("Groesse"));
                    liefPrufer.setVarNummer(rs.getString("Var_Nummern"));
                    liefPrufer.setGtin(rs.getString("GTIN"));
                    liefPrufer.setZielMenge(rs.getString("Ziel_Menge"));
                    liefPrufer.setPosGrId(rs.getString("Pos_Gr_ID"));
                    liefPrufer.setId(rs.getString("ID"));
                    liefPrufer.setLagerNum(rs.getString("Lager_Nr"));
                    liefPrufer.setGtinPreis(rs.getString("GTIN_Preis"));
                    liefPrufer.setPosGrPreis(rs.getString("Pos_Gr_Preis"));
                    liefPrufer.setKalkPreis(rs.getString("kalk_Preis"));
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
    public String getMeldung(String vorgangNummer, String meldungNummer) {
        String ret = "";
        try {
            String proc = "CALL GTIN_Meldung('1','" + meldungNummer + "')";
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString("Meldung_Text")+"--"+rs.getString("Vorgang_Text");
                    System.out.println(ret);
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
    public String gtinStammsatzAnderung(String indicator, String ArtNr, String FarbNr, String Gross, String Varianten, String GTIN, String PosGrID) {
     String ret = "";
        try {
            //String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '0', '1701000', '13', 'EL', ';001;002;061O;072;091;111C;111D;', '', '2230531' )";
            String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '"+indicator+"', '"+ArtNr+"', '"+FarbNr+"', '"+Gross+"', '"+Varianten+"', '"+GTIN+"', '"+PosGrID+"' )";
            
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
    public boolean updateTableGin(String kundnummer, String kdBest, String kdBesDate, String kundWunch, String erfasser, String erDatum, String kdPosActiv, List<LieferKund> lieferKunds) {
       
        lieferKunds.stream().forEach((cnsmr) ->
        {
            String procName = "{CALL GTIN_Erfassungsdaten_laden( '" + kundnummer 
                    + "', '" + kdBest + "', '" + kdBesDate + "', '" + kundWunch + "', '" + erfasser 
                    + "', '" + erDatum + "', '" + kdPosActiv + "' , '" + cnsmr.getPosiNummer() 
                    + "', '"+ cnsmr.getArtikel_Nr() + "', '" + cnsmr.getFarbe() + "', '" + cnsmr.getGroesse() 
                    + "', '"+ cnsmr.getVariante() + "', '" + cnsmr.getMenge() + "', '" + cnsmr.getPreis() 
                    + "', '"+ cnsmr.getKommission() + "')}";
            Connection conProdukt;
            try {
                conProdukt = DriverManager.getConnection(dburlProdukt);
                try (CallableStatement cs = conProdukt.prepareCall(procName)) {
                    if (kdBesDate==null) {
                       cs.setNull(3, java.sql.Types.DATE); 
                    }
                    if (kundWunch==null) {
                       cs.setNull(4, java.sql.Types.DATE); 
                    }
                    if (erDatum==null) {
                       cs.setNull(6, java.sql.Types.DATE); 
                    }
                    //cs.executeUpdate();
                    
                    cs.executeQuery();
                    cs.close();
                    conProdukt.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
                recorded= false;
                exceptionRecord=ex.getMessage();
            }
        });
        return recorded;
    }

    @Override
    public boolean updateTablePrufen(String id, String posNr, String artikelNr, String farbe, String groesse, String variante, String menge, String preis, String kommission) {
     String procName = "{CALL GTIN_Erfassungsdaten_aendern( '" + id 
                    + "', '" + posNr + "', '" + artikelNr + "', '" + farbe + "', '" + groesse 
                    + "', '" + variante + "', '" + menge + "' , '" + preis 
                    + "', '"+ kommission + "')}";
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
                updated= false;
                exceptionUpdate=ex.getMessage();
            } 
            return updated;
    }
}
