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

public class DAISViewStatusNRServlet extends HttpServlet {

	private static final long serialVersionUID = 3483165794267827943L;

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
			
			String code = request.getParameter("code");
			
			Status status = port.getStatus(code, "G");
			request.getSession().setAttribute("currentStatus", status);
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
