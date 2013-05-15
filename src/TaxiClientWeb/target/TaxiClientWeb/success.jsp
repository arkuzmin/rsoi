<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="utf-8">
<link href="css/bootstrap.css" rel="stylesheet">
</head>
<body>
<div class="navbar">
  <div class="navbar-inner">
    <a class="brand" href="home.jsp"><img src="img/icon.png" class="img-rounded"></a>
    <c:if test="${user != null}">   
   <ul class="nav">
      <li class="active"><a href="index.jsp">Домой</a></li>
    </ul>
    </c:if>
  </div>
</div>
	<div class="container">
		<div class="hero-unit">
			<h1><img src="img/success.png" width ="64px" height="64px" class="img-rounded"> Операция успешно выполнена! </h1>
			<p><b>${successMsg}</b></p>
		</div>
	</div>
</body>
</html>
