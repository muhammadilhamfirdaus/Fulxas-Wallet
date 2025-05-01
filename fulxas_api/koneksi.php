<?php
$host = "127.0.0.1:3307";
$user = "root";
$pass = "";
$db   = "fulxas_db"; // ganti dengan nama database kamu

$koneksi = new mysqli($host, $user, $pass, $db);

if ($koneksi->connect_error) {
    die("Koneksi gagal: " . $koneksi->connect_error);
}
?>
