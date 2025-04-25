package com.kelompok3.fulxas.models;

public class Transaksi {
    public int id;
    public String kategori, tanggal, waktu, rekening, tipe;
    public double jumlah;

    public Transaksi(int id, String kategori, String tanggal, String waktu,
                     String rekening, double jumlah, String tipe) {
        this.id = id;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.rekening = rekening;
        this.jumlah = jumlah;
        this.tipe = tipe;
    }
}
