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
import ru.arkuzmin.dais.model.Order;
import ru.arkuzmin.dais.model.Status;
import ru.arkuzmin.tcw.common.CommonUtils;

public class DAISCancelOrderServlet extends HttpServlet {

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
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			DAISWebServiceImplService service = new DAISWebServiceImplService();
			DAISWebService port = service.getDAISWebServiceImplPort();
			
			Status status = (Status) request.getSession().getAttribute("currentStatus");
			Order order = status.getOrder();
			
			boolean result = port.cancelOrder(order);
			
			
			if (result) {
				RequestDispatcher disp = getServletContext().getRequestDispatcher("/success.jsp");
				request.getSession().setAttribute("successMsg", "Операция отмены заказа успешно принята в обработку");
				disp.forward(request, response);
			}
			
			
		} catch (Exception e) {
			logger.error("Error", e);
			request.getSession().setAttribute("errorMsg", "Произошла ошибка при попытке отмены заказа. Удаленный сервер не отвечает");
			RequestDispatcher disp = getServletContext().getRequestDispatcher("/error.jsp");
			if (disp != null) {
				disp.forward(request, response);
			}
		}
	}
}