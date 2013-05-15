package ru.arkuzmin.tcw.model;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ru.arkuzmin.dais.model.DAISWebService;
import ru.arkuzmin.dais.model.DAISWebServiceImplService;
import ru.arkuzmin.dais.model.User;
import ru.arkuzmin.tcw.common.CommonUtils;

public class DAISAuthorizeServlet extends HttpServlet {

	private static final long serialVersionUID = 1777792322731387797L;
	
	private static final Logger logger = CommonUtils.getLogger();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	/**
	 * Выполняет запрос к диспетчерской системе для получения истории заказов пользователя.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			DAISWebServiceImplService service = new DAISWebServiceImplService();
			DAISWebService port = service.getDAISWebServiceImplPort();
			String login = request.getParameter("login");
			String pwd = request.getParameter("pwd");
			User user = port.authorizeUser(login, pwd);
			
			request.getSession().setAttribute("user", user);
			
			if (user == null) {
				request.getSession().setAttribute("errorMsg", "Не удалось войти в систему, некорректный логин или пароль.");
				RequestDispatcher disp = getServletContext().getRequestDispatcher("/index.jsp");
				if (disp != null) {
					disp.forward(request, response);
				}
			} else {
				RequestDispatcher disp = getServletContext().getRequestDispatcher("/index.jsp");
				if (disp != null) {
					disp.forward(request, response);
				}
			}
			
		} catch (Exception e) {
			logger.error("Error", e);
			request.getSession().setAttribute("errorMsg", "Не удалось войти в систему. Удаленный сервер не отвечает");
			RequestDispatcher disp = getServletContext().getRequestDispatcher("/error.jsp");
			if (disp != null) {
				disp.forward(request, response);
			}
		}
	}
	
}
