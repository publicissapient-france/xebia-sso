<html>
<head>
<title>Login Page</title>
<script language="Javascript"> 
function buildOpenIdUri() {
	// Use this URI to connect to the cloud instance
	//openIdUri=http://sso.aws.xebiatechevent.info:8095/openidserver/users/ielfatmi
    var openIdUri="http://localhost:8095/openidserver/users/"+document.getElementById("openid_identifier").value;
    document.getElementById("openid_identifier").value=openIdUri;
	alert("toto");
	document.forms["openid_form"].submit();
}
</script>
</head>
<body>
	<div align="center">
		<form id="openid_form" action="j_spring_openid_security_check"
			method="post">
			<input id="openid_identifier" name="openid_identifier" type="text" />
			<button id="openid_submit" type="submit" onclick="buildOpenIdUri()"
				maxlength="15">Sign-In with your Open ID</button>
		</form>
	</div>
</body>
</html>
