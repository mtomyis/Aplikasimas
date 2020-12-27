package com.tomtomy.aplikasimas.model;

public class modelmas {
    String no, tgl, jam, jam_Selesai, user, kategori, keterangan, solusi, status;

    public modelmas(String no, String tgl, String jam, String jam_Selesai, String user, String kategori, String keterangan, String solusi, String status) {
        this.no = no;
        this.tgl = tgl;
        this.jam = jam;
        this.jam_Selesai = jam_Selesai;
        this.user = user;
        this.kategori = kategori;
        this.keterangan = keterangan;
        this.solusi = solusi;
        this.status = status;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getJam_Selesai() {
        return jam_Selesai;
    }

    public void setJam_Selesai(String jam_Selesai) {
        this.jam_Selesai = jam_Selesai;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getSolusi() {
        return solusi;
    }

    public void setSolusi(String solusi) {
        this.solusi = solusi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
