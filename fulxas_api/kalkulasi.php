<?php
header('Content-Type: application/json');
include 'koneksi.php'; // Pastikan file ini benar dan koneksi berhasil

$query = "SELECT tipe, SUM(jumlah) AS total FROM transaksi GROUP BY tipe";
$result = mysqli_query($koneksi, $query);

$data = [];

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

echo json_encode($data);
?>
