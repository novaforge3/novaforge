<?php
$userLocale = $_GET["locale"];
$userGuideDir = "@DEFAULT_GUIDE@";
$userGuideChapterOneURL = "@CONTENT_URL@";
$userGuideDirLocale = $userGuideDir."_".$userLocale;

if(is_dir($userGuideDirLocale))
{
	$redirectURL = $userGuideDirLocale.$userGuideChapterOneURL;
}
else
{
	$redirectURL = $userGuideDir.$userGuideChapterOneURL;
}
?>
<!DOCTYPE html
  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:exsl="http://exslt.org/common"
	xmlns:ng="http://docbook.org/docbook-ng">
<head>
<link rel="shortcut icon" href="favicon.ico" />
<meta http-equiv="Refresh" content="0; URL=<?php echo $redirectURL; ?>" />
<title>NovaForge 3 user guide</title>
</head>
<body>
	If not automatically redirected, click here:
	<a href="<?php echo $redirectURL; ?>"></a>
</body>
</html>
