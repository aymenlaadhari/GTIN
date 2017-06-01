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
public class Protokoll {

    private String origin, artNummer, farbNummer, groesse, varianten, posgridID, null1, null2, meldung, message, gtin;

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getArtNummer() {
        return artNummer;
    }

    public void setArtNummer(String artNummer) {
        this.artNummer = artNummer;
    }

    public String getFarbNummer() {
        return farbNummer;
    }

    public void setFarbNummer(String farbNummer) {
        this.farbNummer = farbNummer;
    }

    public String getGroesse() {
        return groesse;
    }

    public void setGroesse(String groesse) {
        this.groesse = groesse;
    }

    public String getVarianten() {
        return varianten;
    }

    public void setVarianten(String varianten) {
        this.varianten = varianten;
    }

    public String getPosgridID() {
        return posgridID;
    }

    public void setPosgridID(String posgridID) {
        this.posgridID = posgridID;
    }

    public String getNull1() {
        return null1;
    }

    public void setNull1(String null1) {
        this.null1 = null1;
    }

    public String getNull2() {
        return null2;
    }

    public void setNull2(String null2) {
        this.null2 = null2;
    }

    public String getMeldung() {
        return meldung;
    }

    public void setMeldung(String meldung) {
        this.meldung = meldung;
    }

}
