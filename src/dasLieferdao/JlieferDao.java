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
import model.Faktor;
import model.Kund;
import model.LieferKund;
import model.LieferKundPrufer;
import model.ParameterKund;
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
    boolean recorded= true;
    boolean updated=true;
    String exceptionRecord="";
    String exceptionUpdate="";

    private final String dburlProdukt;
    public JlieferDao(String dburlProdukt) {
        this.dburlProdukt = dburlProdukt;
    }

    @Override
    public String anlegenAndern(String indicator, String kundNummer, String kundArtNummer, String kundfarbe, String kundGroesse, String variante, String gtin, String posGrId, String grundPreis, String varPreis) {
    String ret = "";
        try {
            //String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '0', '1701000', '13', 'EL', ';001;002;061O;072;091;111C;111D;', '', '2230531' )";
            String proc = "SELECT GTIN_Kunde_KdArtNr_anlegen_aendern( '"+indicator+"', '"+kundNummer+"', '"+kundArtNummer+"', '"+kundfarbe+"', '"+kundGroesse+"', '"+variante+"', '"+gtin+"', '"+posGrId+"', '"+grundPreis+"', '"+varPreis+"' )";
            
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
    public List<LieferKundPrufer> getListGtinAnderung(String KdNr, String KdArtNr, String KdFarbe, String KdGroße, String KdVariante, String GTIN, String grundPreis,String varPreis) {
    List<LieferKundPrufer> listGtinAnderung = new ArrayList<>();
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
                    listGtinAnderung.add(kund);
                    
                }
                rs.close();
                cs.close();
                conProdukt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JlieferDao.class.getName()).log(Level.SEVERE, null, ex);
        }
//        kunds.stream().forEach(cnsmr->{
//            System.out.println(cnsmr.getKdArtNummer());
//    });
        return listGtinAnderung;
        
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
//        kunds.stream().forEach(cnsmr->{
//            System.out.println(cnsmr.getKdArtNummer());
//    });
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
                    
//                     if (rs.wasNull()) {
//                        System.out.println("was NULL");
//                    } else {
//                        System.out.println("not NULL");
//                    }
//                     String handel = rs.getString("Erfasser") != null ? rs.getString("Erfasser") : "-1";
//                     System.out.println(handel);
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
                    liefPrufer.setGroesse(rs.getString("Kd_Groesse") != null ? rs.getString("Kd_Groesse") : "");
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
    public List<VerfugbareMengenstaffeln> getListVerfugmeng(String indice, String kdNum, String artNum, String barbNum, String groesse) {
        List<VerfugbareMengenstaffeln> verfugbareMengenstaffelns = new ArrayList<>();
        String proc = "CALL GTIN_Preisermittlung ( '"+indice+"', '"+kdNum+"', '"+artNum+"', '"+barbNum+"', '"+groesse+"')";
            
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
    public String getMeldung(String vorgangNummer, String meldungNummer) {
        String ret = "";
        try {
            String proc = "CALL GTIN_Meldung('" + vorgangNummer + "','" + meldungNummer + "')";
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                    ret = rs.getString("Meldung_Text")+"--"+rs.getString("Vorgang_Text");
                    
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
    public VerwendeteMengenstaffel getVerMengen(String indice, String kdNum, String artNum, String barbNum, String groesse,String posGridId) {
    VerwendeteMengenstaffel verwendeteMengenstaffel = new VerwendeteMengenstaffel();
        VerwendetePreise verwendetePreise = new VerwendetePreise();
        VerwendeterGroßenzuschlag verwendeterGroßenzuschlag = new VerwendeterGroßenzuschlag();
    
   try {
            //String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '0', '1701000', '13', 'EL', ';001;002;061O;072;091;111C;111D;', '', '2230531' )";
            String proc = "CALL GTIN_Preisermittlung ( '"+indice+"', '"+kdNum+"', '"+artNum+"', '"+barbNum+"', '"+groesse+"')";
            
            Connection conProdukt = DriverManager.getConnection(dburlProdukt);
            Statement s = conProdukt.createStatement();
            try (ResultSet rs = s.executeQuery(proc)) {
                while (rs.next()) {
                  verwendeteMengenstaffel.setKundNummer(rs.getString("s_Kd_Nr"));
                  verwendeteMengenstaffel.setStufe(rs.getString("s_Stufe"));
                  verwendeteMengenstaffel.setTyp(rs.getString("s_Typ"));
                  verwendeteMengenstaffel.setStaffelNr(rs.getString("s_Staffel_Nr"));
                  verwendeteMengenstaffel.setMenge1(rs.getString("s_Menge_1"));
                  verwendeteMengenstaffel.setMenge2(rs.getString("s_Menge_2"));
                  verwendeteMengenstaffel.setMenge3(rs.getString("s_Menge_3"));
                  verwendeteMengenstaffel.setMenge4(rs.getString("s_Menge_4"));
                  verwendeteMengenstaffel.setMengeBetzeug(rs.getString("s_Mng_Bezug_GP"));
                  verwendeteMengenstaffel.setAnderung1(rs.getString("s_Aenderung_1"));
                  verwendeteMengenstaffel.setAnderung2(rs.getString("s_Aenderung_2"));
                  verwendeteMengenstaffel.setAnderung3(rs.getString("s_Aenderung_3"));
                  verwendeteMengenstaffel.setAnderung4(rs.getString("s_Aenderung_4"));
                  verwendetePreise.setBasisPreis(rs.getString("s_Basispreis_1"));
                  verwendetePreise.setVarianten(getPreisvariante(posGridId));
                  verwendeteMengenstaffel.setVerwendetePreise(verwendetePreise);
                  verwendeterGroßenzuschlag.setKundNummer(rs.getString("z_Kd_Nr"));
                  verwendeterGroßenzuschlag.setWgZuchlag(rs.getString("s_warengruppe_GZ"));
                  verwendeteMengenstaffel.setVerwendeterGroßenzuschlag(verwendeterGroßenzuschlag);
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

    private String getPreisvariante(String posGridId){
        String varPreis = "";
        try {
            //String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '0', '1701000', '13', 'EL', ';001;002;061O;072;091;111C;111D;', '', '2230531' )";
            String proc = "SELECT GTIN_Preisermittlung_Varianten_GrPosID ( '"+posGridId+"')";
            
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
    public String updateFaktor(String indice, String KdNr, String ArtikelNr, String Faktor, String runden, String NKS) {
      String ret = "";
        try {
            //String proc = "SELECT GTIN_Stammsatz_anlegen_aendern ( '0', '1701000', '13', 'EL', ';001;002;061O;072;091;111C;111D;', '', '2230531' )";
            String proc = "SELECT GTIN_ME_Faktor_anlegen_aendern( '"+indice+"', '"+KdNr+"', '"+ArtikelNr+"', '"+Faktor+"', '"+runden+"', '"+NKS+"')";
           
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
