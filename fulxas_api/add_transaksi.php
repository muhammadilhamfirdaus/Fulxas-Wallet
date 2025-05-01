<?php
// Koneksi ke database
$host = "127.0.0.1:3307"; // atau IP server database
$username = 'root'; // username database
$password = ''; // password database
$database = "fulxas_db"; // nama database yang digunakan
$conn = new mysqli($host, $username, $password, $database);

// Cek koneksi
if ($conn->connect_error) {
    die("Koneksi gagal: " . $conn->connect_error);
}

// Mendapatkan data dari request POST
$kategori = $_POST['kategori'];
$tanggal = $_POST['tanggal'];
$waktu = $_POST['waktu'];
$rekening = $_POST['rekening'];
$jumlah = $_POST['jumlah'];
$tipe = $_POST['tipe']; // "Pendapatan" atau "Pengeluaran"

// Query untuk menambahkan data
$query = "INSERT INTO transaksi (kategori, tanggal, waktu, rekening, jumlah, tipe) 
          VALUES ('$kategori', '$tanggal', '$waktu', '$rekening', '$jumlah', '$tipe')";

if ($conn->query($query) === TRUE) {
    echo json_encode(['status' => 'success', 'message' => 'Transaksi berhasil ditambahkan']);
} else {
    echo json_encode(['status' => 'error', 'message' => 'Gagal menambahkan transaksi']);
}

$conn->close();
?>
