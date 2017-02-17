/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Date;
import java.util.List;
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
    public List<LieferKundPrufer> getListPrugers(String datum);
    public ParameterKund getKundenParameter(String kundNummer);
    public String getLagerNr(String kdNr,String kdArtNr,String kdFarbe,String kdGroesse,String kdVariante);
    public String getException();
    public boolean updateTableGin(String kundnummer, String kdBest, String kdBesDate,String kundWunch, String erfasser, String erDatum,String kdPosActiv, List<LieferKund> lieferKunds);
    public boolean updateTablePrufen(String id, String posNr, String artikelNr, String aarbe, String groesse, String variante,String menge, String preis, String kommission);
    public String getPreisVariante(String posGridID);
    public String gtinStammsatzAnderung(String indicator, String ArtNr, String FarbNr,String Größe,String Varianten,String GTIN,String PosGrID);
}
