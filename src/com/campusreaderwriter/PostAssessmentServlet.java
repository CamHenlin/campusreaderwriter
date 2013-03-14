package com.campusreaderwriter;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
@SuppressWarnings("serial")
public class PostAssessmentServlet extends HttpServlet {
	public static final Logger _logger = Logger.getLogger(PostAssessmentServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		resp.setContentType("text/plain");
		String strResponse = "";
		String text = "";

		try {
		
			//DO ALL YOUR REQUIRED VALIDATIONS HERE AND THROW EXCEPTION IF NEEDED
			
			text = (String)req.getParameter("text");

			String strRecordStatus = "ACTIVE";
			
			Date date = new Date();
			UserService userService = UserServiceFactory.getUserService();
			User loggedInUser = userService.getCurrentUser();
			String user = loggedInUser.getEmail();
			
			AssessmentData aD = new AssessmentData(user, text, date);
			DBUtils.saveAssessmentData(aD);
			strResponse = "assessment data stored.";
		}
		catch (Exception ex) {
			_logger.severe("Error: " + ex.getMessage());
			strResponse = "Reason : " + ex.getMessage();
		}
		resp.getWriter().println(strResponse);
	}
}