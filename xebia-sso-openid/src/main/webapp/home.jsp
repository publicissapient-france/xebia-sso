<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<body>
	<div align="center">

		<h2>Application name: xebia-sso-openid : Demo</h2>
		<p>
		<h1>Welcome</h1>
		<sec:authentication property='principal.name' />
		</p>
		<h3>Personel Information</h3>
		<p>Email:<sec:authentication property='principal.email' />
		<h3>Technical Information</h3>
		<p>Your principal object is....: <%= request.getUserPrincipal() %>
		</p>
		<p>
			<a href="j_spring_security_logout">Logout</a>
	</div>
</body>
</html>