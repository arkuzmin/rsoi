package ru.arkuzmin.tcw.model;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.arkuzmin.dais.model.DAISWebService;
import ru.arkuzmin.dais.model.DAISWebServiceImplService;
import ru.arkuzmin.dais.model.User;

public class DAISRegisterServlet extends HttpServlet {

	private static final long serialVersionUID = -6184770725730430352L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	/**
	 * Выполняет запрос к диспетчерской системе для регистрации нового пользователя.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			DAISWebServiceImplService service = new DAISWebServiceImplService();
			DAISWebService port = service.getDAISWebServiceImplPort();
			boolean result;
			
			User user = new User();
			user.setName(request.getParameter("name"));
			user.setLname(request.getParameter("lname"));
			user.setMname(request.getParameter("mname"));
			user.setLogin(request.getParameter("login"));
			user.setPwd(request.getParameter("pwd"));
			
			result = port.registerNewUser(user);
			
			if (result) {
				request.getSession().setAttribute("registered", true);
				RequestDispatcher disp = getServletContext().getRequestDispatcher("/home.jsp");
				if (disp != null) {
					disp.forward(request, response);
				}
			} else {
				request.getSession().setAttribute("errorMsg", "Не удалось завершить регистрацию, попробуйте использовать другой логин!");
				RequestDispatcher disp = getServletContext().getRequestDispatcher("/error.jsp");
				if (disp != null) {
					disp.forward(request, response);
				}
			}

		} catch (Exception e) {
			request.getSession().setAttribute("errorMsg", "Не удалось завершить регистрацию, удаленный сервер не отвечает.");
			RequestDispatcher disp = getServletContext().getRequestDispatcher("/error.jsp");
			if (disp != null) {
				disp.forward(request, response);
			}
		}
	}
}
