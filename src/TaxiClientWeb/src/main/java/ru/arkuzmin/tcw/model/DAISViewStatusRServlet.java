package ru.arkuzmin.tcw.model;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.arkuzmin.dais.model.DAISWebService;
import ru.arkuzmin.dais.model.DAISWebServiceImplService;
import ru.arkuzmin.dais.model.Status;
import ru.arkuzmin.dais.model.User;

public class DAISViewStatusRServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2933614912245685255L;
	
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
			
			User user = (User) request.getSession().getAttribute("user");
			Status status = port.getStatus(user.getGuid(), "R");
			request.getSession().setAttribute("currentStatus", status.getOrder() == null ? null : status);
			RequestDispatcher disp = getServletContext().getRequestDispatcher("/status.jsp");
			if (disp != null) {
				disp.forward(request, response);
			}

		} catch (Exception e) {
			request.getSession().setAttribute("errorMsg", "Не получить статус текущего заказа, удаленный сервер не отвечает.");
			RequestDispatcher disp = getServletContext().getRequestDispatcher("/error.jsp");
			if (disp != null) {
				disp.forward(request, response);
			}
		}
	}
}
