/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.LieferKund;
import model.ParameterKund;

/**
 *
 * @author aladhari
 */
public class JlieferDao implements JlieferDaoInterface {

    private final String dburlProdukt;

    public JlieferDao(String dburlProdukt) {
        this.dburlProdukt = dburlProdukt;
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
                System.out.println(rs.getString("Artikel_Nr"));
                parameterKund.setErsetzen(rs.getString("Ersetzen"));
                System.out.println(rs.getString("Ersetzen"));
                parameterKund.setFarbe(rs.getString("Farbe"));
                System.out.println(rs.getString("Farbe"));
                parameterKund.setGroesse(rs.getString("Groesse"));
                System.out.println(rs.getString("Groesse"));
                parameterKund.setKd_Pos_activ(rs.getString("Kd_Pos_aktiv"));
                System.out.println(rs.getString("Kd_Pos_aktiv"));
                parameterKund.setKommission(rs.getString("Kommission"));
                System.out.println(rs.getString("Kommission"));
                parameterKund.setMenge(rs.getString("Menge"));
                System.out.println(rs.getString("Menge"));
                parameterKund.setPos_Zaehler(rs.getString("Pos_Zaehler"));
                System.out.println(rs.getString("Pos_Zaehler"));
                parameterKund.setPreis(rs.getString("Preis"));
                System.out.println(rs.getString("Preis"));
                parameterKund.setSuchen(rs.getString("Suchen"));
                System.out.println(rs.getString("Suchen"));
                parameterKund.setVariante(rs.getString("Variante"));
                System.out.println(rs.getString("Variante"));
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
    public void updateTableGin(String kundnummer, String kdBest, String kdBesDate, String kundWunch, String erfasser, String erDatum, String kdPosActiv, List<LieferKund> lieferKunds) {
       
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
            }
        });
          
    }
    
   

}
