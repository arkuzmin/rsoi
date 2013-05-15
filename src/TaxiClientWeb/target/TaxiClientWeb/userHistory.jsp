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
    <c:if test="${user == null}">
    <ul class="nav pull-right">
				<a href="#loginModal"
					role="button" class="btn" data-toggle="modal">Войти
					в систему</a>
    </ul>
    </c:if>
    <c:if test="${user == null}">
        <h6>Вы вошли как незарегистрированный пользователь</h6>
    </c:if>
    <c:if test="${user != null}">
        <ul class="nav pull-right">
				<a href="#logoutModal"
					role="button" class="btn" data-toggle="modal">Выйти из системы</a>
    </ul>
        <h6>Добро пожаловать, <c:out value="${user.name}"/></h6>

    </c:if>
  </div>
</div>
  <div class="container">
	<c:if test="${userHistory != null}">
		<table class="table table-bordered">
              <tr class="info">
                <td><b>Адрес</b></td>
                <td><b>Время доставки</b></td>
                <td><b>Цена за км.</b></td>
                <td><b>Цена за мин.</b></td>
                <td><b>Класс комфорта</b></td>
                <td><b>Время заказа</b></td>
                <td><b>Статус заказа</b></td>
              </tr>
			<c:forEach items="${userHistory}" var="order">
				<tr>
					<td>${order.address}</td>
					<td><c:out value="${order.deliveryTime}"/></td>
					<td><c:out value="${order.kmPrice}"/></td>
					<td><c:out value="${order.minPrice}"/></td>
          <td><c:out value="${order.orderStatus}"/></td>
					<td><c:out value="${order.orderDt}"/></td>
					<td><c:out value="${order.orderStatus}"/></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<c:if test="${userHistory == null}">
		<h3>Вы еще не совершали заказы в системе!</h3>
	</c:if>
	<form action="redirect" method="post">
		<input type="submit" class="btn" value="Назад">
		<input type="hidden" value="index" name="redirectTo">
	</form>
  <div id="logoutModal" class="modal hide fade" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<form action="logout" method="post">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="myModalLabel">Выход из системы</h3>
				</div>
				<div class="modal-body">
					<p>
						<h4>Закончить текущую сессию?</h4>
					</p>
				</div>
				<div class="modal-footer">
					<button class="btn" data-dismiss="modal" aria-hidden="true">Закрыть</button>
					<input type="submit" class="btn btn-primary" value="Выйти">
				</div>
			</form>
		</div>
  </div>
  <script src="js/jquery.js"></script>
			<script src="js/bootstrap-transition.js"></script>
			<script src="js/bootstrap-alert.js"></script>
			<script src="js/bootstrap-modal.js"></script>
			<script src="js/bootstrap-dropdown.js"></script>
			<script src="js/bootstrap-scrollspy.js"></script>
			<script src="js/bootstrap-tab.js"></script>
			<script src="js/bootstrap-tooltip.js"></script>
			<script src="js/bootstrap-popover.js"></script>
			<script src="js/bootstrap-button.js"></script>
			<script src="js/bootstrap-collapse.js"></script>
			<script src="js/bootstrap-carousel.js"></script>
			<script src="js/bootstrap-typeahead.js"></script>
</body>
</html>