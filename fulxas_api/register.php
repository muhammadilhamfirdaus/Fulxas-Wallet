<?php
include 'koneksi.php'; // include file koneksi

// Tangkap data dari Android
$email = $_POST['email'];
$password = $_POST['password'];

// Simpan ke database
$sql = "INSERT INTO users (email, password) VALUES ('$email', '$password')";

if ($koneksi->query($sql) === TRUE) {
    echo "success";
} else {
    echo "error";
}

$koneksi->close();
?>
