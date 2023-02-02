<?php
session_start();

header('Content-Type: application/json');
include('../common/lib.php');

checkServerKey();

$token = $_GET['token'];
if ($token != 'd341bf3069c4866272b03e601eb55472') {
    echo '{"error": "You are not allowed to perform this action !!!"}';
    http_response_code(403);

    die(0);
}

$users = getAllUsers(true);

if ($users == null) {
    echo '{"info": "Empty list of users !!!"}';
    http_response_code(204);
    die(0);
}
print(json_encode($users));
http_response_code(200);

