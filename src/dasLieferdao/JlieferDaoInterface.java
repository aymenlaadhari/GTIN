/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dasLieferdao;

import java.util.List;
import model.ABDaten;
import model.Faktor;
import model.KopfDaten;
import model.Kund;
import model.LieferKund;
import model.LieferKundDoppel;
import model.LieferKundPrufer;
import model.ParameterKund;
import model.Status;
import model.VarPreis;
import model.VerfugbareGroßen;
import model.VerfugbareMengenstaffeln;
import model.VerwendeteMengenstaffel;

/**
 *
 * @author aladhari
 */
public interface JlieferDaoInterface {

    public List<String> getKundenNummers();

    public List<LieferKundPrufer> getListPrufers(String datum, KopfDaten kopfDaten);

    public ParameterKund getKundenParameter(String kundNummer);

    public String getLagerNr(String kdNr, String kdArtNr, String kdFarbe, String kdGroesse, String kdVariante);

    public String getException();

    public boolean updateTableGin(String kundnummer, String kdBest, String kdBesDate, String kundWunch, String erfasser, String erDatum, String kdPosActiv, List<LieferKund> lieferKunds, String id);

    public List<String> updateInFamak(String kundnummer, String kdBest, String kdBesDate, String kundWunch, List<LieferKund> lieferKunds);

    public boolean updateTablePrufen(String id, String posNr, String artikelNr, String aarbe, String groesse, String variante, String menge, String preis, String kommission);

    public String getPreisVariante(String posGridID);

    public String gtinStammsatzAnderung(String indicator, String ArtNr, String FarbNr, String Größe, String Varianten, String GTIN, String PosGrID);

    public String anlegenAndern(String indicator, String kundNummer, String kundArtNummer, String kundfarbe, String kundGroesse, String variante, String gtin, String posGrId, String grundPreis, String varPreis);

    public String getMeldung(String vorgangNummer, String meldung);

    public List<Kund> getListKundGtin(String gtin);

    public List<LieferKundPrufer> getListGtinAnderung(String KdNr, String KdArtNr, String KdFarbe, String KdGroße, String KdVariante, String GTIN, String grundPreis, String varPreis);

    public List<Faktor> getListFaktor(String kundNummer, String artNummer);

    public String updateFaktor(String indice, String KdNr, String ArtikelNr, String Faktor, String runden, String NKS);

    public VerwendeteMengenstaffel getVerMengen(String indice, String posGridId);

    public List<VerfugbareMengenstaffeln> getListVerfugmeng(String indice, String posGridId);

    public List<VerfugbareGroßen> getListverfugGroesse(String indice, String posGridId);

    public String erfassungManuelzuweisen(String indice, String id, String posGridId, String status);

    public String erfassungAbschliessen(String id, String posGrosId);

    public boolean erfassungVerarbeiten(KopfDaten kopfDaten);

    public List<VarPreis> getListVarPreis(String kdNummer, String kdArtNummer, String kdFarbe, String kdGroesse, String kdVariante, String menge, String lagerNummer);

    public String getMengenbezug(String kdNummer);
    
    public List<String> getIndexes();
    public List<String> getIndexInFamak();
    public String getOneIndexInFamak();
    public List<String> getFehlerIndexes();
    
    public List<LieferKundDoppel> getListDoppelErfassung(KopfDaten kopfDaten);
    public String erfassungAktualisieren(String id,String kdPosnr,String kdArtNr,String kdfarb,String kdGros,String kdVar,String kdKomm,String kdMeng);
    public String erfassungLoschen(String id);
    public List<LieferKundDoppel> getListDoppelErfassungZuruck();
    
    public ABDaten getabDaten(String posGridId);
    
    public List<LieferKundPrufer> getLieferKundSuche(String kdNummer,String kdArtNummer, String kdFarbe,String kdGroesse, String kdVariante);
    public String insertInFamak(String kundnummer, String kdBest, String kdBesDate, String kundWunch, LieferKund cnsmr);
    public boolean insertIntoDb(String kundnummer, String kdBest, String kdBesDate, String kundWunch, String erfasser, String erDatum, String kdPosActiv,LieferKund cnsmr, String id);
    
    public List<KopfDaten> getListKopfDaten(String kdNum, String kdBestNum, String kdBestDat);
    public List<LieferKund> getListLieferGenerated(String KdNum, String KdBestnum, String KdBestDatum, String status);
    public List<Status> getStatusListe();
    
    public boolean  speichern(String status,LieferKund lieferKund, KopfDaten daten, String posAktiv);
    public boolean famakVorbereiten(KopfDaten kopfDaten);
    public List<String> datenInfamakSchreiben(KopfDaten kopfDaten);
    public void getColumnName();
    public String returnError();
    public String removeLieferKund(String lieferKundID);
}
