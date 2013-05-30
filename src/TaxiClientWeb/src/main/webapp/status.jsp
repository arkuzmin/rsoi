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
   <ul class="nav">
      <li class="active"><a href="index.jsp">Домой</a></li>
    </ul>
  </div>
</div>
	<div class="container">
		<div class="hero-unit">
			<h2><img src="img/status2.png" width ="64px" height="64px" class="img-rounded"> Статус текущего заказа </h2>
			<c:if test="${currentStatus != null}">   
				<p><b>Статус: </b>${currentStatus.order.orderStatus}</p>
				<hr>
				<h3>Детали заказа</h3>
				<hr>
				<p><b>Адрес: </b>${currentStatus.order.address}</p>
				<p><b>Время доставки: </b>${currentStatus.order.deliveryTime}</p>
				<p><b>Цена за минуту: </b>${currentStatus.order.minPrice}</p>
				<p><b>Цена за километр: </b>${currentStatus.order.kmPrice}</p>
				<p><b>Класс комофорта: </b>${currentStatus.order.comfortClass}</p>
				<hr>
				<c:if test="${currentStatus.order.orderStatus != 'UNKNOWN' && currentStatus.order.orderStatus != 'canceled' && currentStatus.order.orderStatus != 'completed'}"> 
					<form action="cancel" method="post">
						<input type="submit" class="btn" value="Отменить заказ">
					</form>
				</c:if>
			</c:if>
			<c:if test="${currentStatus == null}"> 
				<h3>Вы еще не соврешали заказов в системе!</h3>
			</c:if>
		</div>
	</div>
</body>
</html>
