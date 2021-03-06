<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
	<head>
		<title>The time is</title>
		<style type=text/css media=screen>		#container{ width:600px; } 
						  body {	padding: 20px; 
						 font-family:Helvetica Neue,Helvetica,Arial,sans-serif; 
						 background: -moz-linear-gradient(top, rgba(224,224,224,1) 0%, rgba(235,235,235,0) 36%, rgba(255,255,255,0) 100%); /* FF3.6 */ 
	                     background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(224,224,224,1)), color-stop(36%,rgba(235,235,235,0)), color-stop(100%,rgba(255,255,255,0)));  /* Chrome,Safari4 */
	                     background: -webkit-linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* Chrome10,Safari5.1 */
	                     background: -o-linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* Opera 11.10 */
	                     background: -ms-linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* IE10 */
	                     background: linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* W3C */
	                     filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#e0e0e0', endColorstr='#00ffffff',GradientType=0 ); /* IE6-9 */
	                     margin: 0;
	                    background-repeat: no-repeat;
	                    background-attachment: fixed;
	                    } 
	                    </style>
						 
	</head>
	<body>
		<c:choose>
			<c:when test="${user != null}">
				<p>
					Welcome, ${user.email}!
					You can <a href="${logoutUrl}">sign out</a>
				</p>
			</c:when>
			<c:otherwise>
				<p>
					Welcome!
					<a href="${loginUrl}"> sign in or register</a> to customzie
				</p>
			</c:otherwise>
		</c:choose>
		<p>the time is ${currentTime}</p>
	</body>
</html>
	