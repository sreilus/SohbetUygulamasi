package com.example.muhammet.sohbetuygulamasi.Models;

public class Kullanicilar {
    public Kullanicilar() {
    }

    @Override
    public String toString() {
        return "Kullanicilar{" +
                "dogumTarih='" + dogumTarih + '\'' +
                ", egitim='" + egitim + '\'' +
                ", hakkimda='" + hakkimda + '\'' +
                ", isim='" + isim + '\'' +
                ", resim='" + resim + '\'' +
                '}';
    }

    private String dogumTarih,egitim,hakkimda,isim,resim;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    private Object state;

    public String getDogumTarih() {
        return dogumTarih;
    }

    public void setDogumTarih(String dogumTarih) {
        this.dogumTarih = dogumTarih;
    }

    public String getEgitim() {
        return egitim;
    }

    public void setEgitim(String egitim) {
        this.egitim = egitim;
    }

    public String getHakkimda() {
        return hakkimda;
    }

    public void setHakkimda(String hakkimda) {
        this.hakkimda = hakkimda;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public Kullanicilar(String dogumTarih, String egitim, String hakkimda, String isim, String resim,Object state) {
        this.dogumTarih = dogumTarih;
        this.egitim = egitim;
        this.hakkimda = hakkimda;
        this.isim = isim;
        this.resim = resim;
        this.state=state;
    }
}
