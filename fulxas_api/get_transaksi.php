<?php
include "koneksi.php";

$sql = "SELECT * FROM transaksi ORDER BY tanggal DESC, waktu DESC";
$result = $koneksi->query($sql);

$response = array();
$response["data"] = array();

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $item = array(
            "id" => $row["id"],
            "kategori" => $row["kategori"],
            "tanggal" => $row["tanggal"],
            "waktu" => $row["waktu"],
            "rekening" => $row["rekening"],
            "jumlah" => $row["jumlah"],
            "tipe" => $row["tipe"]
        );
        array_push($response["data"], $item);
    }
    $response["success"] = true;
} else {
    $response["success"] = false;
    $response["message"] = "Tidak ada data";
}

echo json_encode($response);
?>
