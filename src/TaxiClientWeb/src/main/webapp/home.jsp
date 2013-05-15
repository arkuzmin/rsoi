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
    <c:if test="${user == null}">
    <ul class="nav pull-right">
				<a href="#loginModal"
					role="button" class="btn" data-toggle="modal">Войти
					в систему</a>
    </ul>
    </c:if>
  </div>
</div>
	<div class="container">
		<div class="hero-unit">
			<h1>Система заказа такси</h1>
			<p>Добро пожаловать в систему заказа такси!
			</p>
			<c:if test="${registered != null && user == null}">
				<p>
					<b>Регистрация успешно завершена, войдите в систему</b>.
				</p>
			</c:if>
			<p>
				<c:if test="${registered == null && user == null}">
					<a href="#regModal" role="button" class="btn btn-primary"
						data-toggle="modal">Зарегистрироваться</a> 
				</c:if>
        <form action="redirect" method="post">
          <c:if test="${registered == null && user == null}">
              <input type="submit" class="btn" value="Не регистрироваться">
              <input type="hidden" value="index" name="redirectTo">
          </c:if>
          </form>
                  <div class="row">
        <div class="span5 offset2">
        <img src="img/road.png" class="img-rounded">
        </div>
        <div class="span3">
      			<img src="img/taxi.png" class="img-rounded">
        </div>
      </div>
          </div>
			</p>

		

		<div id="regModal" class="modal hide fade" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<form action="register" method="post">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="myModalLabel">Регистрация</h3>
				</div>
				<div class="modal-body">
					<p>
						<input  placeholder=" Имя" name="name"> <span
							class="label label-important">Обязательно</span>
					</p>
					<p>
						<input  placeholder=" Фамилия" name="lname">
					</p>
					<p>
						<input  placeholder=" Отчество" name="mname">
					</p>
					<p>
						<input placeholder=" Логин" name="login"> <span
							class="label label-important">Обязательно</span>
					</p>
					<p>
						<input  placeholder=" Пароль" name="pwd"> <span
							class="label label-important">Обязательно</span>
					</p>
				</div>
				<div class="modal-footer">
					<button class="btn" data-dismiss="modal" aria-hidden="true">Закрыть</button>
					<input type="submit" class="btn btn-primary" value="Регистрация">
				</div>
			</form>
		</div>

		<div id="loginModal" class="modal hide fade" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<form action="authorize" method="post">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="myModalLabel">Вход в систему</h3>
				</div>
				<div class="modal-body">
					<p>
						<input placeholder=" Логин" name="login"> <span
							class="label label-important">Обязательно</span>
					</p>
					<input placeholder=" Пароль" name="pwd"> <span
						class="label label-important">Обязательно</span>
					</p>
				</div>
				<div class="modal-footer">
					<button class="btn" data-dismiss="modal" aria-hidden="true">Закрыть</button>
					<input type="submit" class="btn btn-primary" value="Войти">
				</div>
			</form>
		</div>
		<div>
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
