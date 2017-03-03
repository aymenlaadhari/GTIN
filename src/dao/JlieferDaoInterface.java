/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Date;
import java.util.List;
import model.Kund;
import model.LieferKund;
import model.LieferKundPrufer;
import model.ParameterKund;

/**
 *
 * @author aladhari
 */
public interface JlieferDaoInterface 
{
    public List<String> getKundenNummers();
    public List<LieferKundPrufer> getListPrufers(String datum);
    public ParameterKund getKundenParameter(String kundNummer);
    public String getLagerNr(String kdNr,String kdArtNr,String kdFarbe,String kdGroesse,String kdVariante);
    public String getException();
    public boolean updateTableGin(String kundnummer, String kdBest, String kdBesDate,String kundWunch, String erfasser, String erDatum,String kdPosActiv, List<LieferKund> lieferKunds);
    public boolean updateTablePrufen(String id, String posNr, String artikelNr, String aarbe, String groesse, String variante,String menge, String preis, String kommission);
    public String getPreisVariante(String posGridID);
    public String gtinStammsatzAnderung(String indicator, String ArtNr, String FarbNr,String Größe,String Varianten,String GTIN,String PosGrID);
    public String anlegenAndern(String indicator, String kundNummer, String kundArtNummer,String kundfarbe, String kundGroesse, String variante, String gtin,String posGrId, String grundPreis, String varPreis );
    public String getMeldung(String vorgangNummer, String meldung);
    public List<Kund> getListKundGtin(String gtin);
    public List<LieferKundPrufer> getListGtinAnderung(String KdNr,String KdArtNr,String KdFarbe,String KdGroße,String KdVariante,String GTIN);
   }
