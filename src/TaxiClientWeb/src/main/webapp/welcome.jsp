<html>
<head>
<meta charset="utf-8">
</head>
<body>
	<h2></h2>
	<form action="history" method="get">
		<input type="submit" value="История заказов">
	</form>
	<form action="register" method="post">
		<input type="text" placeholder="Имя" name="name"> 
		<input type="text" placeholder="Фамилия" name="lname"> 
		<input type="text" placeholder="Отчество" name="mname"> 
		<input type="text" placeholder="Логин" name="login"> 
		<input type="text" placeholder="Пароль" name="pwd"> 
		<input type="submit" value="Регистрация">
	</form>
	<form action="authorize" method="post">
		<input type="text" placeholder="Логин" name="login">
		<input type="text" placeholder="Пароль" name="pwd">
		<input type="submit" value="Авторизация">
	</form>
	<form action="makeorder" method="post">
		<input type="text" placeholder="Адрес" name="address">
		<input type="text" placeholder="Время доставки" name="deltime">
		<input type="text" placeholder="Макс. цена за километр" name="kmprice">
		<input type="text" placeholder="Макс. цена за минуту" name="minprice">
		<input type="text" placeholder="Класс комфорта" name="comfort">
		<input type="submit" value="Сделать заказ">
	</form>
</body>
</html>
