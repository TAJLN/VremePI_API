package me.TAJLN.Vremenko;

public class Postaja {

    private int id;
    private String ime;
    private String kljuc;
    private int lastnik;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getIme(){
        return ime;
    }

    public void setIme(String ime){
        this.ime = ime;
    }

    public String getKljuc(){
        return kljuc;
    }

    public void setKljuc(String kljuc){
        this.kljuc = kljuc;
    }

    public int getLastnik(){
        return lastnik;
    }

    public void setLastnik(int lastnik){
        this.lastnik = lastnik;
    }
}
