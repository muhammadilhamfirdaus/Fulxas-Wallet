<?php
include 'koneksi.php'; // koneksi ke database

$query = "SELECT kategori, tipe, SUM(jumlah) AS total FROM transaksi GROUP BY kategori, tipe";
$result = mysqli_query($koneksi, $query);

$data = array();

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

echo json_encode($data);
?>
