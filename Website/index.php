<!DOCTYPE html>
<html>
<head>

<body>
<h1>Input</h1>
<form action="index.php" method="post">
	<textarea rows="4" cols="50" name = "inputSq"></textarea>
	<div class="row">
     	<button type="submit" name = "submitInput">Submit</button>
  	</div>
</form>

<h1>Output</h1>
<textarea rows="100" cols="50">
<?php

if(isset($_POST['submitInput']))
{
	$input = $_POST['inputSq'];
	//echo "$input";
	$output = shell_exec("java -jar musitePractice.jar artifica_data.txt $input 1");
	echo "$output";
}

?>
</textarea>

</head>
</body>
</html>

