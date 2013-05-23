package ru.arkuzmin.dais.model;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import ru.arkuzmin.dais.dto.Order;
import ru.arkuzmin.dais.dto.Status;
import ru.arkuzmin.dais.dto.User;

@WebService
@SOAPBinding(style = Style.DOCUMENT)
public interface DAISWebService {
	@WebMethod
	public boolean registerNewUser(User newUser);
	@WebMethod
	public List<Order> getUserHistory(User user);
	@WebMethod
	public User authorizeUser(String login, String pwd);
	@WebMethod
	public boolean addUserApplication(User user, Order order);
	@WebMethod
	public boolean confirmApplication(Order order, boolean confirm);
	@WebMethod
	public Status getStatus(String guid, String userIdentifier) ;
	@WebMethod
	public String addGuestApplication(Order order);
}
