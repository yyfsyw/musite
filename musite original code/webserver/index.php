<?php
set_include_path('system'.PATH_SEPARATOR.'templates');
require_once('Template.php');
require_once('Database.php');
require_once('RedirectBrowserException.php');
$tmpl = new Template();
$tmpl->pageContent = '';
print $tmpl->build('page.tmpl');
?>