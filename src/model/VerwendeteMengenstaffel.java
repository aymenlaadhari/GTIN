/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author aladhari
 */
public class VerwendeteMengenstaffel extends Mengenstaffel {

    private String kundNummer, mengeBetzeug, anderung1, anderung2, anderung3, anderung4;
    private VerwendetePreise verwendetePreise;
    private VerwendeterGroßenzuschlag verwendeterGroßenzuschlag;
    private List<Varianten> variantens;

    public List<Varianten> getVariantens() {
        return variantens;
    }

    public void setVariantens(List<Varianten> variantens) {
        this.variantens = variantens;
    }

    public VerwendetePreise getVerwendetePreise() {
        return verwendetePreise;
    }

    public void setVerwendetePreise(VerwendetePreise verwendetePreise) {
        this.verwendetePreise = verwendetePreise;
    }

    public VerwendeterGroßenzuschlag getVerwendeterGroßenzuschlag() {
        return verwendeterGroßenzuschlag;
    }

    public void setVerwendeterGroßenzuschlag(VerwendeterGroßenzuschlag verwendeterGroßenzuschlag) {
        this.verwendeterGroßenzuschlag = verwendeterGroßenzuschlag;
    }

    public String getKundNummer() {
        return kundNummer;
    }

    public void setKundNummer(String kundNummer) {
        this.kundNummer = kundNummer;
    }

    public String getMengeBetzeug() {
        return mengeBetzeug;
    }

    public void setMengeBetzeug(String mengeBetzeug) {
        this.mengeBetzeug = mengeBetzeug;
    }

    public String getAnderung1() {
        return anderung1;
    }

    public void setAnderung1(String anderung1) {
        this.anderung1 = anderung1;
    }

    public String getAnderung2() {
        return anderung2;
    }

    public void setAnderung2(String anderung2) {
        this.anderung2 = anderung2;
    }

    public String getAnderung3() {
        return anderung3;
    }

    public void setAnderung3(String anderung3) {
        this.anderung3 = anderung3;
    }

    public String getAnderung4() {
        return anderung4;
    }

    public void setAnderung4(String anderung4) {
        this.anderung4 = anderung4;
    }

}
