package ru.arkuzmin.tcw.model;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ru.arkuzmin.dais.model.DAISWebService;
import ru.arkuzmin.dais.model.DAISWebServiceImplService;
import ru.arkuzmin.dais.model.Order;
import ru.arkuzmin.dais.model.User;
import ru.arkuzmin.tcw.common.CommonUtils;

/**
 * Получение истории заказов пользователя.
 * @author ArKuzmin
 *
 */
public class DAISGetHistoryServlet extends HttpServlet {

	private static final long serialVersionUID = -9199203518160847279L;
	
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
			User user = (User)request.getSession().getAttribute("user");
			List<Order> orders = port.getUserHistory(user);
			
			if (orders.isEmpty()) {
				orders = null;
			}
			
			request.getSession().setAttribute("userHistory", orders);
			
			RequestDispatcher disp = getServletContext().getRequestDispatcher("/userHistory.jsp");
			if (disp != null) {
				disp.forward(request, response);
			}
		} catch (Exception e) {
			logger.error("Error", e);
			request.getSession().setAttribute("errorMsg", "Произошла ошибка при чтении истории заказов. Удаленный сервер не отвечает");
			RequestDispatcher disp = getServletContext().getRequestDispatcher("/error.jsp");
			if (disp != null) {
				disp.forward(request, response);
			}
		}
	}
}
