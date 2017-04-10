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
public class LieferKund {
    
    private String posiNummer="",Kd_Pos_activ="", Pos_Zaehler="", Artikel_Nr="", Farbe="", Groesse="", Variante="", Menge="", Preis="", Kommission="",lagerNum="", summe="0";

    public String getSumme() {
        return summe;
    }

    public void setSumme(String summe) {
        this.summe = summe;
    }

    
    public String getKd_Pos_activ() {
        return Kd_Pos_activ;
    }

    public void setKd_Pos_activ(String Kd_Pos_activ) {
        this.Kd_Pos_activ = Kd_Pos_activ;
    }

    public String getPos_Zaehler() {
        return Pos_Zaehler;
    }

    public void setPos_Zaehler(String Pos_Zaehler) {
        this.Pos_Zaehler = Pos_Zaehler;
    }

    public String getArtikel_Nr() {
        return Artikel_Nr;
    }

    public void setArtikel_Nr(String Artikel_Nr) {
        this.Artikel_Nr = Artikel_Nr;
    }

    public String getFarbe() {
        return Farbe;
    }

    public void setFarbe(String Farbe) {
        this.Farbe = Farbe;
    }

    public String getGroesse() {
        return Groesse;
    }

    public void setGroesse(String Groesse) {
        this.Groesse = Groesse;
    }

    public String getVariante() {
        return Variante;
    }

    public void setVariante(String Variante) {
        this.Variante = Variante;
    }

    public String getMenge() {
        return Menge;
    }

    public void setMenge(String Menge) {
        this.Menge = Menge;
    }

    public String getPreis() {
        return Preis;
    }

    public void setPreis(String Preis) {
        this.Preis = Preis;
    }

    public String getKommission() {
        return Kommission;
    }

    public void setKommission(String Kommission) {
        this.Kommission = Kommission;
    }

    public String getLagerNum() {
        return lagerNum;
    }

    public void setLagerNum(String lagerNum) {
        this.lagerNum = lagerNum;
    }

    public String getPosiNummer() {
        return posiNummer;
    }

    public void setPosiNummer(String posiNummer) {
        this.posiNummer = posiNummer;
    }

    @Override
    public String toString() {
        return "LieferKund{" + "posiNummer=" + posiNummer + ", Kd_Pos_activ=" + Kd_Pos_activ + ", Pos_Zaehler=" + Pos_Zaehler + ", Artikel_Nr=" + Artikel_Nr + ", Farbe=" + Farbe + ", Groesse=" + Groesse + ", Variante=" + Variante + ", Menge=" + Menge + ", Preis=" + Preis + ", Kommission=" + Kommission + ", lagerNum=" + lagerNum + '}';
    }
}
