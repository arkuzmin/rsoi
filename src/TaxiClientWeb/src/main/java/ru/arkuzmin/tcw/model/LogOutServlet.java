package ru.arkuzmin.tcw.model;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogOutServlet extends HttpServlet {

	private static final long serialVersionUID = -3778513605008460979L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Enumeration<String> attr = session.getAttributeNames();
		while (attr.hasMoreElements()) {
			session.removeAttribute(attr.nextElement());
		}
		RequestDispatcher disp = getServletContext().getRequestDispatcher("/home.jsp");
		if (disp != null) {
			disp.forward(request, response);
		}
	}
}
