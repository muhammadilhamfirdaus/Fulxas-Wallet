<?php
// Set headers to allow cross-origin requests (CORS) for your app
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

// Get the data sent from the Android app
$data = json_decode(file_get_contents("php://input"));

// Koneksi ke database
$host = "127.0.0.1:3307"; // Sesuaikan dengan port MySQL Anda
$user = "root";           // Ganti dengan username MySQL Anda
$pass = "";               // Ganti dengan password MySQL Anda
$dbname = "fulxas_db";    // Nama database Anda

// Create connection
$conn = new mysqli($host, $user, $pass, $dbname);

// Check the connection
if ($conn->connect_error) {
    die(json_encode(['status' => 'error', 'message' => 'Database connection failed: ' . $conn->connect_error]));
}

// Retrieve the email and password from the input
$email = $data->email;
$password = $data->password;

// Prepare the SQL query to check if the user exists
$query = "SELECT * FROM users WHERE email = ?";
$stmt = $conn->prepare($query);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

// Check if the user exists
if ($result->num_rows > 0) {
    $user = $result->fetch_assoc();
    
    // Verifikasi password (tanpa hash)
    if ($password === $user['password']) {
        echo json_encode(['status' => 'success', 'message' => 'Login successful', 'user' => $user]);
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Incorrect password']);
    }
} else {
    echo json_encode(['status' => 'error', 'message' => 'User not found']);
}

// Close the connection
$conn->close();
?>
