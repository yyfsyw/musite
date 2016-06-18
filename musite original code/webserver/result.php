<?php
set_include_path('system'.PATH_SEPARATOR.'templates'.PATH_SEPARATOR.'classes'.PATH_SEPARATOR.'backend');

//require_once('JsonEncDec.inc');
require_once('Template.php');
require_once('Database.php');
require_once('RedirectBrowserException.php');
require_once('execute.php');
require_once('JSON.php');

$json = new Services_JSON(SERVICES_JSON_LOOSE_TYPE);
$tmpl = new Template();

$tmpl->id = $_GET['id'];
$tmpl->seq = $_POST['sequence'];
$tmpl->org = $_GET['organism'].'.model';

$dataString = executeAndReturnFormatedObject($tmpl->seq, $tmpl->org, 'backend');
$tmpl->dataArray = $json->decode($dataString, true);

//var_export($tmpl->dataArray);
$tmpl->dataArray = $tmpl->dataArray['proteins'];
$tmpl->dataHeader = $tmpl->dataArray[0]['header'];

$HTML = $tmpl->build('result.tmpl');
$tmpl->pageContent = $HTML;

print $tmpl->pageContent;
?>