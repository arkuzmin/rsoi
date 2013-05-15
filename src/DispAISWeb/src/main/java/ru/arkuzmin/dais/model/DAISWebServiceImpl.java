package ru.arkuzmin.dais.model;

import java.util.List;

import javax.jws.WebService;

import ru.arkuzmin.dais.dao.ApplicationDAO;
import ru.arkuzmin.dais.dao.UserDAO;
import ru.arkuzmin.dais.dto.Order;
import ru.arkuzmin.dais.dto.User;

@WebService(endpointInterface = "ru.arkuzmin.dais.model.DAISWebService")
public class DAISWebServiceImpl implements DAISWebService {
	
	@Override
	public boolean registerNewUser(User newUser) {
		boolean result = false;
		UserDAO dao = new UserDAO();
		result = dao.addNewUser(newUser);
		return result;
	}

	@Override
	public List<Order> getUserHistory(User user) {
		ApplicationDAO dao = new ApplicationDAO();
		List<Order> history = dao.getHistory(user);
		return history;
	}
	
	@Override
	public User authorizeUser(String login, String pwd) {
		User user = null;
		UserDAO dao = new UserDAO();
		user = dao.getUserByLogin(login, pwd);
		return user;
	}

	@Override
	public boolean addUserApplication(User user, Order order) {
		boolean result = false;
		ApplicationDAO dao = new ApplicationDAO();
		result = dao.addUserApplication(user, order);
		return result;
	}

	@Override
	public boolean confirmApplication(Order order, boolean confirm) {
		boolean result = false;
		ApplicationDAO dao = new ApplicationDAO();
		result = dao.confirmApplication(order, confirm);
		return result;
	}

}
