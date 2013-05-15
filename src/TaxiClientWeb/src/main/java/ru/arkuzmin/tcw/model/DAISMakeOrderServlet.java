package ru.arkuzmin.tcw.model;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import ru.arkuzmin.dais.model.DAISWebService;
import ru.arkuzmin.dais.model.DAISWebServiceImplService;
import ru.arkuzmin.dais.model.Order;
import ru.arkuzmin.dais.model.User;
import ru.arkuzmin.tcw.common.CommonUtils;
import ru.arkuzmin.tcw.common.Globals;

public class DAISMakeOrderServlet extends HttpServlet {

	private static final long serialVersionUID = 8962275087654693034L;
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
			
			// Формируем заказ
			Order order = new Order();
			// Адрес
			order.setAddress(request.getParameter("address"));
			// Время доставки
			String deltime = request.getParameter("deltime");
			deltime = deltime.replaceAll("T", " ");
			
			try {
				Date deltimedt = Globals.dateTime.parse(deltime);
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(deltimedt);
				XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
				order.setDeliveryTime(cal);
			} catch (DatatypeConfigurationException e) {
				logger.error("Error", e);
			} catch (ParseException e) {
				logger.error("Error", e);
			}
			// Цена за километр
			String kmprice = request.getParameter("kmprice");
			// Цена за минуту
			String minprice = request.getParameter("minprice");
			if (kmprice != null && !"".equals(minprice)) {
				order.setKmPrice(Double.parseDouble(kmprice));
			}
			if (minprice != null && !"".equals(minprice)) {
				order.setMinPrice(Double.parseDouble(minprice));
			}
			
			// Класс комфорта
			String comf = request.getParameter("comfort");
			order.setComfortClass(comf);
			
			User user = (User) request.getSession().getAttribute("user");
			
			boolean result = port.addUserApplication(user, order);	
			
			if (result) {
				request.getSession().setAttribute("successMsg", "Ваша заявка отправлена на обработку. Вы можете посмотреть текущий статус заявки из главного меню.");
				RequestDispatcher disp = getServletContext().getRequestDispatcher("/success.jsp");
				if (disp != null) {
					disp.forward(request, response);
				}
			} else {
				request.getSession().setAttribute("errorMsg", "При регистрации заказа произошла ошибка! Проверьте правильность заполнения параметров.");
				RequestDispatcher disp = getServletContext().getRequestDispatcher("/error.jsp");
				if (disp != null) {
					disp.forward(request, response);
				}
			}
			

		} catch (Exception e) {
			logger.error("Error", e);
			request.getSession().setAttribute("errorMsg", "Не удалось сделать заказ, удаленный сервер не отвечает.");
			RequestDispatcher disp = getServletContext().getRequestDispatcher("/error.jsp");
			if (disp != null) {
				disp.forward(request, response);
			}
		}
	}
}
