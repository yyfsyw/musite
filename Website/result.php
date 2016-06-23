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

		if(!empty($to))
		{
			if(!mail($to, $subject, $output, $headers))
			echo "error";
		}
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
	<link rel="stylesheet" href="css/bootstrap.css">
			<link rel="stylesheet" href="css/bootstrap-theme.css">
			<link rel="stylesheet" href="css/AStyle.css">

			<script src="js/bootstrap.js"></script>
			<script src="js/npm.js"></script>

			<style>

					button#submitBtn{
							background-color: goldenrod;
							background-image: none;
							border-color: goldenrod;
							color: white;
					}
					#submitBtn:hover {
							background-color:darkgoldenrod;
							border-color: goldenrod;
					}


					#downloadButton{
						color: #fff;
						background-color: gold ;
						border-color: goldenrod;
							background-image: none;
					}
					#downloadButton:focus,
					#downloadButton.focus {
						color: #fff;
						background-color: goldenrod;
						border-color: darkgoldenrod;
					}
					#downloadButton:hover {
						color: #fff;
						background-color: goldenrod;
						border-color: darkgoldenrod;
					}

					input {
						background-color: beige;

					}
			</style>
</head>
<body>
				<div id="titleDiv">
            <img src="mu_logo.png" id="muLogo"/>
        </div>

        <!-- navbar -->
        <nav class="navbar navbar-inverse">
          <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
              <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar">faq</span>
                <span class="icon-bar">support</span>
                <span class="icon-bar">bug report</span>
                <span class="icon-bar">feature request</span>
              </button>
              <a class="navbar-brand" href="#">MUsite
                  <!--
                  <img id="logo" alt="Brand" src="http://primegens.org//images/mu_logo.png">
                -->
              </a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
              <ul class="nav navbar-nav">
                <li><a href="#">faq</a></li>
                <li><a href="#">support</a></li>
                  <li><a href="#">bug report</a></li>
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">feature request <span class="caret"></span></a>
                  <ul class="dropdown-menu">
                    <li><a href="#">Action</a></li>
                    <li><a href="#">Another action</a></li>
                    <li><a href="#">Something else here</a></li>
                    <li role="separator" class="divider"></li>
                    <li><a href="#">Separated link</a></li>
                    <li role="separator" class="divider"></li>
                    <li><a href="#">One more separated link</a></li>
                  </ul>
                </li>
              </ul>
              <form class="navbar-form navbar-right" role="search">
                <div class="form-group">
                  <input type="text" class="form-control" placeholder="Search">
                </div>
                <button type="submit" class="btn btn-primary" id="submitBtn">Submit</button>
              </form>

            </div><!-- /.navbar-collapse -->
          </div><!-- /.container-fluid -->
        </nav>
        <!-- end of navbar -->


        <div id="headWrapper">
            <div id="caption_title">
                General and kinase-specific protein phosphorylation system
            </div>
            <a href="#" class="btn btn-primary btn-lg" role="button" id="downloadButton" >Download MUsite</a>
        </div>

        <!--list menu-->
        <div id="contentWrapper">
						<div id="mainWrapper">
							<div class="clear">

									<fieldset>
									<legend id="mainLegend">RESULT:</legend>
									<fieldset>
									<textarea name="Result" id="sequence" class="resizable" rows="25" cols="100"><?php echo "$output"; ?></textarea>
									</fieldset>

									<p>&nbsp; &nbsp;</p>
									<button name="sendResult" type="submit" value="Send" class="btn btn-primary" id="submitBtn">Send Result</button>
									<button type="reset" value="Reset" class="btn btn-primary" id="submitBtn">Reset</button>

									<p>&nbsp; &nbsp;</p>
									<p>
									Please kindly cite the following paper if you use Musite or Musite.net in your study: <br/>
									Jianjiong Gao, Jay J. Thelen, A. Keith Dunker, and Dong Xu. Musite:
									a Tool for Global Prediction of General and Kinase-Specific Phosphorylation Sites.
									<i>Molecular & Cellular Proteomics</i>. 2010. 9(12):2586-600.	<br/>
									<a href="http://www.ncbi.nlm.nih.gov/pubmed/20702892" target="_blank">PubMed Abstract</a>
									&nbsp; &nbsp;<a href="http://www.mcponline.org/content/9/12/2586.abstract" target="_blank">MCP Abstract</a>
									&nbsp; &nbsp;<a href="http://www.mcponline.org/content/9/12/2586.long" target="_blank">MCP Full Text</a>
									</p>
								</div>
						</div>

        </div>
        <!--end of list menu-->

</body>
</html>
