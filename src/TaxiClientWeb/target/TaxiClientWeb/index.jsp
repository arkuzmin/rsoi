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
    <c:if test="${user != null}">      
      <div class="row-fluid">
            <ul class="thumbnails">
              <li class="span4">
                <div class="thumbnail">
                    <form action="makeorder" method="post">
                      <div class="caption">
                        <h3>Заказать такси <img src="img/order.png" width="50" height="50"></h3> 
                        <p>Закажите ближайшее к Вам такси, просто заполнив поля формы ниже.</p>
                        <hr>
                        <p><input placeholder=" Адрес" name="address"> <span	class="label label-important">x</span></p>
                        <p>
                            <b>Ожидаемое время доставки</b>
                        </p>
                        <p><input type="datetime-local" placeholder=" Время доставки" name="deltime"> <span	class="label label-important">x</span></p>
                        <p><input placeholder=" Макс. цена за километр" name="kmprice"></p>
                        <p><input placeholder=" Макс. цена за минуту" name="minprice"></p>
                        <p>
                        <p>
                            <b>Класс комфорта автомобиля</b>
                        </p>
                        <select name="comfort">
                          <option value="econom">Эконом</option>
                          <option value="comf">Комфорт</option>
                          <option value="business">Бизнес</option>
                        </select>
                        </p>
                        <p><input type="submit" class="btn btn-primary" value="Сделать заказ"></p>
                      </div>
                  </form>
                </div>
              </li>
              <li class="span4">
                <div class="thumbnail">
                  <form action="viewstatusr" method="post">
                    <div class="caption">
                      <h3>Статус заказа <img src="img/status.png" width="50" height="50"></h3>
                      <p>Просмотр статуса Вашего текущего заказа.</p>
                      <hr>
                      <p><input type="submit" class="btn btn-primary" value="Показать статус"></p>
                    </div>
                  </form>
                </div>
              </li>
              <li class="span4">
                <div class="thumbnail">
                  <form action="history" method="post">
                    <div class="caption">
                      <h3>История заказов <img src="img/history.png" width="50" height="50"></h3>
                      <p>Просмотр ранее осуществленных Вами заказов такси.</p>
                      <hr>
                      <p><input type="submit" class="btn btn-primary" value="Показать историю"></p>
                    </div>
                  </form>
                </div>
              </li>
            </ul>
          </div>
    </c:if>

    <c:if test="${user == null}">
      <div class="row-fluid">
            <ul class="thumbnails">
              <li class="span6">
                <div class="thumbnail">
                    <form action="makeorder" method="post">
                      <div class="caption">
                        <h3>Заказать такси <img src="img/order.png" width="64" height="64"></h3> 
                        <p>Закажите ближайшее к Вам такси, просто заполнив поля формы ниже.</p>
                        <hr>
                        <p><input placeholder=" Адрес" name="address"> <span	class="label label-important">Обязательно</span></p>
                        <p>
                            <b>Ожидаемое время доставки</b>
                        </p>
                        <p><input type="datetime-local" placeholder=" Время доставки" name="deltime"> <span	class="label label-important">Обязательно</span></p>
                        <p><input placeholder=" Макс. цена за километр" name="kmprice"></p>
                        <p><input placeholder=" Макс. цена за минуту" name="minprice"></p>
                        <p>
                        <p>
                            <b>Класс комфорта автомобиля</b>
                        </p>
                        <select name="comfort">
                          <option value="econom">Эконом</option>
                          <option value="comf">Комфорт</option>
                          <option value="business">Бизнес</option>
                        </select>
                        </p>
                        <p><input type="submit" class="btn btn-primary" value="Сделать заказ"></p>
                      </div>
                  </form>
                </div>
              </li>
              <li class="span6">
                <div class="thumbnail">
                  <form action="viewstatusnr" method="post">
                    <div class="caption">
                      <h3>Посмотреть статус <img src="img/status.png" width="64" height="64"></h3>
                      <p>Для просмотра текущего статуса Вашего заказа, введите код доступа в поле формы ниже.</p>
                      <hr>
                      <p><input placeholder=" Код доступа" name="code"> <span	class="label label-important">Обязательно</span></p>
                      <p><input type="submit" class="btn btn-primary" value="Статус"></p>
                    </div>
                  </form>
                </div>
              </li>
            </ul>
          </div>
    </c:if>
          
          
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
