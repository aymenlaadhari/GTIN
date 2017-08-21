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
public class KopfDaten {
    private String kdNum, kdName,ort, kdBestnum, kdBestDatum, erfasser,erfassDatum,status,bemerkung,kdWunchDat;

    public String getKdNum() {
        return kdNum;
    }

    public void setKdNum(String kdNum) {
        this.kdNum = kdNum;
    }

    public String getKdBestnum() {
        return kdBestnum;
    }

    public void setKdBestnum(String kdBestnum) {
        this.kdBestnum = kdBestnum;
    }



    public String getKdName() {
        return kdName;
    }

    public void setKdName(String kdName) {
        this.kdName = kdName;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getErfasser() {
        return erfasser;
    }

    public void setErfasser(String erfasser) {
        this.erfasser = erfasser;
    }

    public String getErfassDatum() {
        return erfassDatum;
    }

    public void setErfassDatum(String erfassDatum) {
        this.erfassDatum = erfassDatum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    public String getKdWunchDat() {
        return kdWunchDat;
    }

    public void setKdWunchDat(String kdWunchDat) {
        this.kdWunchDat = kdWunchDat;
    }

    public String getKdBestDatum() {
        return kdBestDatum;
    }

    public void setKdBestDatum(String kdBestDatum) {
        this.kdBestDatum = kdBestDatum;
    }
    
}
