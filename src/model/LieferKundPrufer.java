/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author aladhari
 */
public class LieferKundPrufer extends LieferKund{
    private String bestNummer="",zeile="", treffer="", kundNummer="",kdBestDatum="", kdWunchDatum="", erfasser="", erfassungsDatum="", kdPosAktiv="", status="", artikelId="", kundenArtikelNummer="", farbeNummer="",kdgroesse="", varNummer="", gtin="",zielMenge="", posGrId="", id="", lagerNummer="",gtinPreis="",posGrPreis="",kalkPreis="" ;

    public String getZeile() {
        return zeile;
    }

    public void setZeile(String zeile) {
        this.zeile = zeile;
    }

    public String getTreffer() {
        return treffer;
    }

    public void setTreffer(String treffer) {
        this.treffer = treffer;
    }

    public String getKundNummer() {
        return kundNummer;
    }

    public void setKundNummer(String kundNummer) {
        this.kundNummer = kundNummer;
    }

    public String getKdBestDatum() {
        return kdBestDatum;
    }

    public void setKdBestDatum(String kdBestDatum) {
        this.kdBestDatum = kdBestDatum;
    }

    public String getKdWunchDatum() {
        return kdWunchDatum;
    }

    public void setKdWunchDatum(String kdWunchDatum) {
        this.kdWunchDatum = kdWunchDatum;
    }

    public String getErfasser() {
        return erfasser;
    }

    public void setErfasser(String erfasser) {
        this.erfasser = erfasser;
    }

    public String getErfassungsDatum() {
        return erfassungsDatum;
    }

    public void setErfassungsDatum(String erfassungsDatum) {
        this.erfassungsDatum = erfassungsDatum;
    }

    public String getKdPosAktiv() {
        return kdPosAktiv;
    }

    public void setKdPosAktiv(String kdPosAktiv) {
        this.kdPosAktiv = kdPosAktiv;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArtikelId() {
        return artikelId;
    }

    public void setArtikelId(String artikelId) {
        this.artikelId = artikelId;
    }

    public String getKundenArtikelNummer() {
        return kundenArtikelNummer;
    }

    public void setKundenArtikelNummer(String kundenArtikelNummer) {
        this.kundenArtikelNummer = kundenArtikelNummer;
    }

   

    public String getFarbeNummer() {
        return farbeNummer;
    }

    public void setFarbeNummer(String farbeNummer) {
        this.farbeNummer = farbeNummer;
    }

    public String getKdgroesse() {
        return kdgroesse;
    }

    public void setKdgroesse(String kdgroesse) {
        this.kdgroesse = kdgroesse;
    }

    

    public String getVarNummer() {
        return varNummer;
    }

    public void setVarNummer(String varNummer) {
        this.varNummer = varNummer;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getZielMenge() {
        return zielMenge;
    }

    public void setZielMenge(String zielMenge) {
        this.zielMenge = zielMenge;
    }

    public String getPosGrId() {
        return posGrId;
    }

    public void setPosGrId(String posGrId) {
        this.posGrId = posGrId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLagerNummer() {
        return lagerNummer;
    }

    public void setLagerNummer(String lagerNummer) {
        this.lagerNummer = lagerNummer;
    }

    public String getBestNummer() {
        return bestNummer;
    }

    public void setBestNummer(String bestNummer) {
        this.bestNummer = bestNummer;
    }

    public String getGtinPreis() {
        return gtinPreis;
    }

    public void setGtinPreis(String gtinPreis) {
        this.gtinPreis = gtinPreis;
    }

    public String getPosGrPreis() {
        return posGrPreis;
    }

    public void setPosGrPreis(String posGrPreis) {
        this.posGrPreis = posGrPreis;
    }

    public String getKalkPreis() {
        return kalkPreis;
    }

    public void setKalkPreis(String kalkPreis) {
        this.kalkPreis = kalkPreis;
    }

    @Override
    public String toString() {
        return "LieferKundPrufer{" + "bestNummer=" + bestNummer + ", zeile=" + zeile + ", treffer=" + treffer + ", kundNummer=" + kundNummer + ", kdBestDatum=" + kdBestDatum + ", kdWunchDatum=" + kdWunchDatum + ", erfasser=" + erfasser + ", erfassungsDatum=" + erfassungsDatum + ", kdPosAktiv=" + kdPosAktiv + ", status=" + status + ", artikelId=" + artikelId + ", kundenArtikelNummer=" + kundenArtikelNummer + ", farbeNummer=" + farbeNummer + ", kdgroesse=" + kdgroesse + ", varNummer=" + varNummer + ", gtin=" + gtin + ", zielMenge=" + zielMenge + ", posGrId=" + posGrId + ", id=" + id + ", lagerNummer=" + lagerNummer + ", gtinPreis=" + gtinPreis + ", posGrPreis=" + posGrPreis + ", kalkPreis=" + kalkPreis + '}';
    }
    
}
