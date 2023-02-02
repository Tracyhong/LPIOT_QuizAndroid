<?php

function getDbConnection()
{
    $db_host = "localhost";
    $db_uid = "root";
    $db_pass = "root";
    $db_name = "testandroid";
    $db_con = mysqli_connect($db_host, $db_uid, $db_pass, $db_name);
    mysqli_set_charset($db_con, 'utf8');

    return $db_con;
}

function getConfigKey($key)
{
    $db_con = getDbConnection();
    $sql = "SELECT * FROM config WHERE `key` = '$key'";
    $result = mysqli_query($db_con, $sql);

    if ($row = mysqli_fetch_assoc($result)) {
        mysqli_close($db_con);
        return $row['value'];
    }
    return '';
}

function checkServerKey($method = 'GET')
{
    $server_key = getConfigKey('server_key');
    if (!isset($server_key) || empty($server_key)) {
        echo '{"error" : "Server key not defined yet !!!"}';
        http_response_code(404);
        die(0);
    }

    if ($method == 'GET') {
        if (!isset($_GET['key']) || empty($_GET['key'])) {
            echo '{"error": "Key is required !!!"}';
            http_response_code(403);
            die(0);
        }
        $key = $_GET['key'];
    } else {
        if (!isset($_POST['key']) || empty($_POST['key'])) {
            echo '{"error": "Key is required !!!"}';
            http_response_code(403);
            die(0);
        }
        $key = $_POST['key'];
    }

    if ($key != $server_key) {
        echo '{"error" : "Wrong key !!!"}';
        http_response_code(403);
        die(0);
    }
}

function checkGuiKey()
{
    $gui_key = getConfigKey('gui_key');
    if (!isset($gui_key) || empty($gui_key)) {
        echo '{"error" : "GUI key not defined yet !!!"}';
        http_response_code(404);
        die(0);
    }

    if (!isset($_GET['gui']) || empty($_GET['gui'])) {
        echo '{"error": "Ooops! Your are not allowed to access to this page."}';
        http_response_code(403);
        die(0);
    }
    $gui = $_GET['gui'];

    if ($gui != $gui_key) {
        echo '{"error" : "Ooops! Your are not allowed to access to this page."}';
        http_response_code(403);
        die(0);
    }
}

function checkTempQrcode()
{
    $server_qrcode = getConfigKey('qrcode');
    if (!isset($server_qrcode) || empty($server_qrcode)) {
        echo '{"error" : "Server QR code not defined yet !!!"}';
        http_response_code(404);
        die(0);
    }

    if (!isset($_GET['qrcode']) || empty($_GET['qrcode'])) {
        echo '{"error": "QR code is required !!!"}';
        http_response_code(403);
        die(0);
    }

    $client_qrcode = $_GET['qrcode'];
    if ($client_qrcode != $server_qrcode) {
        echo '{"error" : "Wrong or expired QR code!!!"}';
        http_response_code(403);
        die(0);
    }
}

function checkImg()
{
    if (!isset($_FILES['img']['error']) || is_array($_FILES['img']['error'])) {
        echo '{"error": "Invalid request!!!"}';
        http_response_code(404);
        die(0);
    }

    switch ($_FILES['img']['error']) {
        case UPLOAD_ERR_OK:
            break;
        case UPLOAD_ERR_NO_FILE:
            echo '{"error": "No file sent!!!"}';
            http_response_code(404);
            die(0);
        case UPLOAD_ERR_INI_SIZE:
            die(0);
        case UPLOAD_ERR_FORM_SIZE:
            echo '{"error": "Exceeded filesize limit!!!"}';
            http_response_code(404);
            die(0);
        default:
            echo '{"error": "Unknown errors!!!"}';
            http_response_code(404);
            die(0);
    }

    // You should also check filesize here.
    if ($_FILES['img']['size'] > 500 * 1000) {
        echo '{"error": Exceeded filesize limit!!!"}';
        http_response_code(404);
        die(0);
    }

    $finfo = new finfo(FILEINFO_MIME_TYPE);
    if (false === $ext = array_search(
            $finfo->file($_FILES['img']['tmp_name']),
            array('jpg' => 'image/jpeg', 'png' => 'image/png', 'gif' => 'image/gif',),
            true
        )) {
        echo '{"error": Invalid file format!!!"}';
        http_response_code(404);
        die(0);
    }
}

function getPresenceByUserId($id)
{
    $db_con = getDbConnection();
    $sql = "SELECT * FROM presence a WHERE $id = a.id_user";
    $result = mysqli_query($db_con, $sql);
    mysqli_close($db_con);
    return ($row = mysqli_fetch_assoc($result));
}

function getUserById($id)
{
    $db_con = getDbConnection();
    $sql = "SELECT id, first_name, last_name, score FROM user WHERE id = $id";
    $result = mysqli_query($db_con, $sql);
    $output = null;
    if ($row = mysqli_fetch_assoc($result)) {
        $row['present'] = getPresenceByUserId($row['id']) ? 1 : 0;
        $output = $row;
    }
    mysqli_close($db_con);

    return $output;
}

function getUserById2($id)
{
    $db_con = getDbConnection();
    $sql = "SELECT * FROM user where id=$id";
    $result = mysqli_query($db_con, $sql);
    if ($row = mysqli_fetch_assoc($result)) {
        return $row;
    }
    mysqli_close($db_con);

    return null;
}

function getUserByToken($token)
{
    $db_con = getDbConnection();
    $sql = "SELECT user.id, first_name, last_name, score, token FROM user, token"
			." WHERE user.id = token.id_user AND token.token = '$token'";
    $result = mysqli_query($db_con, $sql);
    if ($row = mysqli_fetch_assoc($result)) {
        return $row;
    }
    mysqli_close($db_con);

    return null;
}

function getUserByUsername($username)
{
    $db_con = getDbConnection();
    $sql = "SELECT id, first_name, last_name, score FROM user WHERE username = '$username'";
    $result = mysqli_query($db_con, $sql);
    $output = null;
    if ($row = mysqli_fetch_assoc($result)) {
        return $row;
    }
    mysqli_close($db_con);

    return null;
}

function getUserByLogin($username, $password)
{
    $db_con = getDbConnection();
    $sql = "SELECT id, first_name, last_name, score FROM user WHERE username = '$username' and `password` = '$password'";
    $result = mysqli_query($db_con, $sql);
    $output = null;

    if ($row = mysqli_fetch_assoc($result)) {
        $row['present'] = getPresenceByUserId($row['id']) ? 1 : 0;
        $output = $row;
    }

    if ($output == null) {
        echo '{"info" : "Incorrect login !!!"}';
        http_response_code(400);
    } else {
        print(json_encode($output));
        http_response_code(200);
    }
    mysqli_close($db_con);
}

function createOrUpdatePresence($friend_id, $type, $qrcode = null, $img = null)
{
    $db_con = getDbConnection();

    $presence = getPresenceByUserId($friend_id);
    if (!isset($presence) || empty($presence)) {
        $sql = "INSERT INTO `presence` (`id_user`, `time`, `type`, `qr_code`) "
            ."VALUES($friend_id, now(),'$type', '$qrcode')";
    } else {
        $sql = "UPDATE `presence` SET `time`=now(), `qr_code`='$qrcode' WHERE  `id_user`=$friend_id";
    }
    return $result = mysqli_query($db_con, $sql);
}

function deletePresence($student_id = null)
{
    $db_con = getDbConnection();
    if ($student_id == null) {
        $sql = "DELETE FROM `presence`";
    } else {
        $sql = "DELETE FROM `presence` WHERE `id_user`=$student_id;";
    }

    // TODO delete image files
    $result = mysqli_query($db_con, $sql);
    mysqli_close($db_con);

    return $result;
}

function getAllUsers($with_details = false)
{
    $db_con = getDbConnection();
    $sql = "SELECT id, first_name, last_name, score FROM user";
    $result = mysqli_query($db_con, $sql);
    $output = null;
    while ($row = mysqli_fetch_assoc($result)) {
        if (!$with_details) {
            $row['present'] = getPresenceByUserId($row['id']) ? 1 : 0;
        } else{
            $row['presence'] = getPresenceByUserId($row['id']);
        }
        $output[] = $row;
    }
    mysqli_close($db_con);

    return $output;
}

function getNBQuestion()
{
	$db_con = getDbConnection();
    $sql = "SELECT COUNT(*) FROM question WHERE is_mult=1;";
	
    $result = mysqli_query($db_con, $sql);
	$row = mysqli_fetch_array($result);
	return $row[0];
}
/*
function getQuestion($id_question)
{
	$db_con = getDbConnection();
    $sql = "SELECT * FROM question WHERE id=$id_question;";
    $result = mysqli_query($db_con, $sql);
	$rows = array();
	while($r = mysqli_fetch_assoc($result)) {
		array_push($rows, $r);
	}
	mysqli_close($db_con);

    return $rows;
}*/

function getQuestion($id_question)
{
	$db_con = getDbConnection();
    $var="'arrdt_" . $id_question."'";
    //print_r($var);
    $sql = "SELECT * FROM question WHERE theme=$var;";
    //print_r("---------- SQL       " . $sql . "  -------------");
    $result = mysqli_query($db_con, $sql);
    //print_r($result);
	$rows = array();
	while($r = mysqli_fetch_assoc($result)) {
		array_push($rows, $r);
	}
	mysqli_close($db_con);

    return $rows;
}

function getAnswer($id_question)
{
    $db_con = getDbConnection();
    $sql = "SELECT texte FROM `answer` WHERE id_question=$id_question;";

    $result = mysqli_query($db_con, $sql);
	$rows = array();
	while($r = mysqli_fetch_assoc($result)) {
		array_push($rows, $r);
	}
	mysqli_close($db_con);

    return $rows;
}

function getGoodResponse($id_question)
{
    $db_con = getDbConnection();
    $sql = "SELECT texte FROM `answer` WHERE id_question=$id_question AND is_right=1;";

    $result = mysqli_query($db_con, $sql);
	$rows = array();
	while($r = mysqli_fetch_assoc($result)) {
		array_push($rows, $r);
	}
	mysqli_close($db_con);
    return $rows;
}

function generateRandomString($length = 10)
{
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}

function updateQrcodeKey($qrcode)
{
    $db_con = getDbConnection();
    $sql = "UPDATE `config` SET `value`='$qrcode' WHERE  `key`='qrcode'";
    return mysqli_query($db_con, $sql);
}

function updateQrcodeImage($filePath, $qrcode)
{
    QRcode::png($qrcode, $filePath, QR_ECLEVEL_L, 15, 1);
    return true;
}

//function createDuel($key, $from_id, $to_id)
//{
//
//    if ($key != getConfigKey('server_key')) {
//        return false;
//    }
//
//    $db_con = getDbConnection();
//    $sql = "INSERT INTO `duel` (`from_id`, `to_id`) VALUES ($from_id, $to_id);";
//
//    if (mysqli_query($db_con, $sql) == true) {
//        return true;
//    } else {
//        return false;
//    }
//    mysqli_close($db_con);
//}

?>