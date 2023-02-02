<?php
session_start();

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
include('../common/lib.php');

if (!isset($_GET['theme']) || empty($_GET['theme'])) {
    echo '{"error": "theme is required !!!"}';
    http_response_code(403);
    die(0);
}
$theme = $_GET['theme'];
//print_r( "---------THEME    " );
//print_r($theme);

$question = getQuestion($theme);

//print_r("---------id    ");
//print_r($question[0]['id']);
$answer = getAnswer($question[0]['id']);
$reponse = getGoodResponse($question[0]['id']);
//print_r('--------------'.$question[0]['id'].'---------------');
$question['answer'] = $answer;
$question['reponse'] = $reponse;
//print_r("--------------".$)
echo json_encode($question);

http_response_code(200);

?>