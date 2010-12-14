
<!-- This is the project specific website template -->
<!-- It can be changed as liked or replaced by other content -->

<?php

$domain=ereg_replace('[^\.]*\.(.*)$','\1',$_SERVER['HTTP_HOST']);
$group_name=ereg_replace('([^\.]*)\..*$','\1',$_SERVER['HTTP_HOST']);
$themeroot='http://r-forge.r-project.org/themes/rforge/';

echo '<?xml version="1.0" encoding="UTF-8"?>';
?>
<!DOCTYPE html
	PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en   ">

  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><?php echo $group_name; ?></title>
	<link href="<?php echo $themeroot; ?>styles/estilo1.css" rel="stylesheet" type="text/css" />
	<link href="css/messages.css" rel="stylesheet" type="text/css" />
  </head>

<body>

<!-- R-Forge Logo -->
<table border="0" width="100%" cellspacing="0" cellpadding="0">
<tr><td>
<!-- <a href="http://r-forge.r-project.org/"><img src="http://<?php echo $themeroot; ?>images/logo.png" border="0" alt="R-Forge Logo" /> </a> </td> </tr> -->
<a href="http://r-forge.r-project.org/"><img src="http://r-forge.r-project.org/themes/rforge/images/logo.png" border="0" alt="R-Forge Logo" /> </a> </td> </tr>
</table>


<!-- get project title  -->
<!-- own website starts here, the following may be changed as you like -->

<?php if ($handle=fopen('http://'.$domain.'/export/projtitl.php?group_name='.$group_name,'r')){
$contents = '';
while (!feof($handle)) {
	$contents .= fread($handle, 8192);
}
fclose($handle);
echo $contents; } ?>

<!-- end of project description -->
<h3>Getting Started</h3>

<h4>Prerequisites:</h4>
<p>
speedR depends on Java Runtime Edition 6. To install Java, please go to <a href="http://java.com" target="blank">java.com</a>
</p>

<h4>Installation:</h4>
<p>Please run this code in R to install speedR: 
<!-- HTML generated using hilite.me --><div style="background: #ffffff; overflow:auto;width:55%;color:black;background:white;border:solid gray;border-width:.1em .1em .1em .8em;padding:.2em .6em;"><pre style="margin: 0; line-height: 125%">install.packages(<span style="color: #a31515">&quot;speedR&quot;</span>)
</pre></div>

</p>

<h4>Running speedR:</h4>
<p>Once speedR and its dependiecies are installed, run the following code to start the speedR's GUI:
<!-- HTML generated using hilite.me --><div style="background: #ffffff; overflow:auto;width:55%;color:black;background:white;border:solid gray;border-width:.1em .1em .1em .8em;padding:.2em .6em;"><pre style="margin: 0; line-height: 125%">library(speedR)
speedR()
</pre></div>
<div class="info">On certain Mac OS X systems (like Snow Leopard), it is impossible to start AWT from rJava started
from R.app or from the terminal. Since speedR depends on AWT, it will not run. The solution is to
use R and speedR from within JGR <a href="http://www.rforge.net/JGR" target="blank">http://www.rforge.net/JGR</a>.</div>
</p>

<h3>Using speedR</h3>
<p>speedR has 4 sections as marked in the following figure.</p>
<p><img src="img/speedR-GUI-sections.png"></p>

<p>
<ol style="line-height:150%;">
	<li> Object browser: Displays all data objects from the R workspace. </li>
	<li> Table panel: Data objects from the object browser can be displayed here. </li>
	<li> Filter panel: Filter expressions (see the section "Filter Expressions") can be written here. </li>
	<li> Code panel: Generated R code will be shown here.</li>
</ol>	
</p>

<h4>Object Browser</h4>
<p>
<table width="55%" height="100%" cellpadding="3" cellspacing="3" border="0">
<tr><td align="left" valign="top"><img src="img/data_browser_win7.png"></td></tr>
<tr>
<td align="left" valign="top" style="line-height:150%;">speedR allows easy access to data in the R workspace by providing an interface to open data from R and to return modified data to R.
 R objects can easily be accessed via the built-in object browser where all matrices like objects can be opened by simply clicking them. 
 The content of the object browser can be refreshed by clicking the "Refresh" button. The following figure displays the object browser window. 
 Leaf nodes on the tree represent data in the current R workspace. Double clicking on a node makes the data available via the filtering-table.</td></tr>
</table>
</p>

<h3>Importing Files</h3>
<p>
<table width="55%" height="100%" cellpadding="3" cellspacing="3" border="0">
<tr><td align="left" valign="top"><img src="img/importbutton_win7.png"></td></tr>
<tr>
<td align="left" valign="top" style="line-height:150%;">speedR enables data import from a wide range of sources such as Excel (all versions), open
office .calc, .csv and .txt files. Unknown data formats will be treated like text files. The
importer can be opened by clicking the importer shortcut button located on the right top
corner of the "Object Browser" window (see figure above).</td></tr>
</table>
</p>
<p>
<table width="55%" height="100%" cellpadding="3" cellspacing="3" border="0">
<tr>
<td align="left" valign="top">Data import consists of a two step wizard "select data" and "edit table", the two figures below respectively. </td></tr>
<tr>
<td align="left" valign="top"><img src="img/importdock_win7.png"></td>
</tr>
<tr>
<td align="left" valign="top"><img src="img/importdock2_win7.png"></td>
</tr>
</table>
</p>

<h3>Filtering</h3>
<p>
<table width="60%" height="100%" cellpadding="3" cellspacing="3" border="0">
<tr>
<td align="left" valign="top" style="line-height:150%;">
The speedR filter engine (see figure below) accepts multiple filter levels which are hierarchically applied to the data, starting with the first filter level. 
A filter level is defined from all filter settings of the same line in the filter editor. Each filter level is assigned to an editable default color; data matching that 
filter level criteria are labeled with that filter level color. Hit counts for each filter level are displayed left to the corresponding filter level in the "Show" column 
(see figure below). The filtered table can be added to the R workspace or exported to the file system by clicking the export button in the upper right corner (see figure below),
the user will be prompted for a variable or file name, respectively. speedR is equipped with a simple expression syntax which makes it easy to write powerful filter 
conditions. Depending on the data type, proper filter expressions are offered in a dropdown menu when clicking a cell in the filter editor.
</td></tr>
<tr>
<td align="left" valign="top"><img src="img/filterdock_win7.png"></td>
</tr>
</table>
</p>

<p>
<table width="60%" height="100%" cellpadding="3" cellspacing="3" border="0">
<tr>
<td align="left" valign="top">
While writing the filter expressions, speedR translates these filter expressions into an equivalent R function (see figure below). 
This function uses only function from base package and doesn't depend on speedR.
</td></tr>
<tr>
<td align="left" valign="top"><img src="img/filtercodedock_win7.png"></td>
</tr>
</table>
</p>

<h4> Filter Expressions</h4>
<p style="line-height:150%;">
speedR is equipped with a simple expression syntax which makes it easy to write powerful filter conditions. Depending on the data type, 
proper filter expressions are offered in a dropdown menu when clicking a cell in the filter editor.

<ul style="line-height:150%;">
	<li> <strong>'<' [number]:</strong>  Less than a <i>number</i>
	<li> <strong>'>' [number]:</strong>  Greater than a <i>number</i>
	<li> <strong>'<=' [number]:</strong> Less than or equals to a <i>number</i>
	<li> <strong>'>=' [number]:</strong> Greater than or equals a <i>number</i>
	<li> <strong>[number] 'to' [number]:</strong> Defines an intervall. Ex. <i>5</i> <strong>to</strong> <i>10</i>.
	<li> <strong>'is.na' :</strong> Selects a row if the cell value is  <i>NA</i>
	<li> <strong>'contains' [string]:</strong> Selects cells having strings which contains given character set.
	<li> <strong>'equals'  [string]:</strong> The cells are only than selected if cell value is <i>equals</i> to the given string. Note that this is case-sensitive.
	<li> <strong>'not' [expression]:</strong> <i>not</i> predicate can be used together with any type of expressions to inverse the sub-expression result.
	<li> <strong>[expression] 'and' [expression]:</strong> Expressions may be bound with <i>and</i> keyword.
	<li> <strong>[expression] 'or' [expression]:</strong> Expressions may be bound with <i>or</i> keyword.
	<li> <strong> ():</strong> Parenthesis can be used to groups the expressions. Ex.  <strong>is.na or (< 5 and > 2 )</strong>
</ul> 
</p>

<h4> Filter Examples </h4>
<p>
<ul>
 <li> < 5 or = 8 </li>
 <li>contains "set" and not equal "setosa"</li>
 <li> (>= 3 and < 9) or ( > 20 and not = 32 )</li>
 </ul>
</p>

<h3> speedR on R-Forge</h3>
<p> The <strong>project summary page:</strong> you can find <a href="http://<?php echo $domain; ?>/projects/<?php echo $group_name; ?>/"><strong>here:</strong></a>. </p>

</body>
</html>
