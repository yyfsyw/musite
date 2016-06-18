<?php
$value = empty($_GET['a'])? '' : $_GET['a'];

$data = file_get_contents('http://www.uniprot.org/uniprot/'.$value.'.fasta');
//print_r(explode("\n", $data));
print trim($data, "\n >");
?>