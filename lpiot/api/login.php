<?php
session_start();

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
include('../common/lib.php');

checkServerKey();
if (!isset($_GET['username']) || empty($_GET['username'])) {
    echo '{"error": "username is required !!!"}';
    http_response_code(403);
    die(0);
}

if (!isset($_GET['password']) || empty($_GET['password'])) {
    echo '{"error": "password is required !!!"}';
    http_response_code(403);
    die(0);
}

$username = $_GET['username'];
$password = $_GET['password'];
getUserByLogin($username, $password);
?>