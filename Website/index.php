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
		$to = $_POST['emailAddress'];
		//echo "$to";
		$subject = "Musite Result";
		//$txt = "Hello world!";
		$headers = "From: ylskt3@mail.missouri.edu";

		if(mail($to, $subject, $output, $headers))
			echo "success";
		else
			echo "error";
			echo $to;
			echo $subject;
			//echo $output;
			echo $headers;
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

			<fieldset id="accessions">
			<legend>Accessions:</legend>
				<div class="inset">Input comma-separated accessions (limit of 100). <a id="uniprot_example" class="theme" href="files/uniprot_example.txt" target="_blank">example</a><br/>
				<select name="accession_type" id="accession_type" size="1">
				<option value="uniprot">UniProt</option>
				</select><strong>:</strong>
				
				<input type="text" name="accession_text" id="accession_text" size="40" />
				</div>
			</fieldset>

			<fieldset>
			<legend>Sequence:</legend>
			<div class="inset">Input protein sequence(s) in FASTA format (limit of 100). <a id="fasta_example" class="theme" href="files/fasta_example.txt" target="_blank">example</a><br/>
			<textarea name="inputSeq" id="sequence" class="resizable" rows="12" cols="100"></textarea></div><br>

			<legend>Upload Sequence file:</legend>
			<div class="inset">Input protein sequence(s) file in FASTA format (limit of 100). <a id="fasta_example" class="theme" href="files/fasta_example.txt" target="_blank">example</a><br/>
			<br>Your email: <input type="email" name="emailAddress" id="accession_text" size="39" /><br>
			<br>
			<label for="c">Upload File: </label>
			<input id="c" name="uploadedFile" type="file" aria-required="true"></div><br>

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
			</form>

			<fieldset>
			<legend id="mainLegend">RESULT:</legend>
			<fieldset>
			<textarea id="sequence" class="resizable" rows="25" cols="100"><?php echo "$output"; ?></textarea>
			</div>
			</fieldset>
			<form action="MAILTO:<?php echo $to; ?>" method="post" enctype="text/plain">
			Result:<br>
			<input type="text" name="name" value="<?php echo $output; ?>"><br>
			<input type="submit" value="Send">
			<input type="reset" value="Reset">
			</form>
		</div>
</div>
<p>
Please kindly cite the following paper if you use Musite or Musite.net in your study: <br/>
Jianjiong Gao, Jay J. Thelen, A. Keith Dunker, and Dong Xu. Musite: 
a Tool for Global Prediction of General and Kinase-Specific Phosphorylation Sites.
<i>Molecular & Cellular Proteomics</i>. 2010. 9(12):2586-600.	<br/>
<a href="http://www.ncbi.nlm.nih.gov/pubmed/20702892" target="_blank">PubMed Abstract</a>
&nbsp; &nbsp;<a href="http://www.mcponline.org/content/9/12/2586.abstract" target="_blank">MCP Abstract</a>
&nbsp; &nbsp;<a href="http://www.mcponline.org/content/9/12/2586.long" target="_blank">MCP Full Text</a>
</p>

</body>
</html>
