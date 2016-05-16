<?php
$file = 'Result '.date('d-m-y H-i-s').'.txt';
$text = empty($_POST['result_text'])? '' : $_POST['result_text'];

header("Cache-Control: no-cache, must-revalidate");
header("Pragma: no-cache");
header("Content-type: text/plain");
header("Content-disposition: attachment; filename=".$file);

print $text;
?>