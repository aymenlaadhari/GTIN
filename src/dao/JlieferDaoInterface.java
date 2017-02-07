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
}
