<?php
	if(isset($_POST['submitInput']))
	{
		$input = $_POST['inputSeq'];
		$selection = $_POST['selection'];
		//echo "$input";
		$myfile = fopen("input.txt", "w") or die("Unable to open file!");
		fwrite($myfile, $input);
		fclose($myfile);
		$output = shell_exec("java -jar musitePractice.jar $selection");
		//echo "$output";
	}
?>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>musite</title>
	<link type="text/css" href="css/custom/jquery-ui-1.8.2.custom.css" rel="stylesheet" />
	<link type="text/css" href="css/jquery.jqplot.css" rel="stylesheet" />
	<link type="text/css" href="css/site.css" rel="stylesheet" />
	<script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>
	<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.2.custom.min.js"></script>
	<script type="text/javascript" src="js/packed.js"></script>
	<script type="text/javascript" src="js/site.js"></script>
	<script type="text/javascript">
		var _gaq = _gaq || [];
		_gaq.push(['_setAccount', 'UA-17389493-1']);
		_gaq.push(['_trackPageview']);
	</script>
</head>
<body>
<div id="mainWrapper">
	<div class="clear">
		<h1 id="title" class="css_left"><a href="index.php"><img src="images/logo.png" alt="musite" /></a></h1>
		
			<form id="bioform" action="index.php" method="post" enctype="multipart/form-data">

			<div id="bioform-message"></div>

			<fieldset>

			<legend id="mainLegend">SUBMISSION:</legend>

			<fieldset>
			<legend>Sequence:</legend>
			<div class="inset">Input protein sequence(s) in FASTA format (limit of 100). <a id="fasta_example" class="theme" href="files/fasta_example.txt" target="_blank">example</a><br/>
			<textarea name="inputSeq" id="sequence" class="resizable" rows="12" cols="60"></textarea></div><br>

			<legend>Selections:</legend><br>
			<div class="inset">
			<label for="Ser">Ser</label>
			<input type="radio" name="selection" id="ser" value="S">
			<label for="Thr">Thr</label>
			<input type="radio" name="selection" id="thr" value="T">
			<label for="Tyr">Tyr</label>
			<input type="radio" name="selection" id="tyr" value="K">
			</div>
			</fieldset>

			<fieldset id="organisms">

			</fieldset>

			<fieldset>
				<div><button type="submit" name = "submitInput">Submit</button>
				<br>
				<br>
			</fieldset>

			</fieldset>
			<fieldset>
			<legend id="mainLegend">RESULT:</legend>
			<fieldset>
			<textarea id="sequence" class="resizable" rows="25" cols="60"><?php echo "$output"; ?></textarea>
			</div>
			</fieldset>

			</form>
		</div>
</div>


</body>
</html>
