package com.campusreaderwriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class CampusReaderWriterServlet extends HttpServlet {
	public void doGet(HttpServletRequest req,
            HttpServletResponse resp)
	throws IOException {
	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSSSS");
	
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	String jQuery = "<script src=\"jquery.min.js\"></script>";
	String bootStrap = "<script src=\"js/bootstrap.min.js\"></script>";
	String bootStrapCss = "<style src=\"css/bootstrap.css\"></style>";
	String mainCss = "<style src=\"css/main.css\"></style>";
	String navBar;
	String tzForm;
	String textInput = "";
	if (user == null) {
	  navBar = "<p>Welcome! <a href=\"" + userService.createLoginURL("/") +
	           "\">Sign in or register</a> to customize.</p>";
	  tzForm = "";
	  fmt.setTimeZone(new SimpleTimeZone(0, ""));
	
	} else {
	  UserPrefs userPrefs = UserPrefs.getPrefsForUser(user);
	  int tzOffset = 0;
	  
	  if (userPrefs != null) {
	      tzOffset = userPrefs.getTzOffset();
	      textInput = userPrefs.getTextInput();
	  }
	
	  navBar = "<p>Welcome, " + user.getNickname() + "! You can <a href=\"" +
	           userService.createLogoutURL("/") +
	           "\">sign out</a>.</p>";
	  tzForm = "<form action=\"/prefs\" method=\"post\">" +
	      "<label for=\"tz_offset\">" +
	      "Timezone offset from UTC (can be negative):" +
	      "</label>" +
	      "<input name=\"tz_offset\" id=\"tz_offset\" type=\"text\" size=\"4\" " +
	      "value=\"" + tzOffset + "\" />" +
	      "<input name=\"text_input\" id=\"text_input\" type=\"textarea\" rows=\"5\" cols=\"10\" />" +
	      "<input type=\"submit\" value=\"Set\" />" +
	      "</form>";
	  fmt.setTimeZone(new SimpleTimeZone(tzOffset * 60 * 60 * 1000, ""));
	}
	
	resp.setContentType("text/html");
	PrintWriter out = resp.getWriter();
	out.println(jQuery);
	out.println(bootStrap);
	out.println(bootStrapCss);
	out.println(mainCss);
	out.println(navBar);
	out.println("<p>The time is: " + fmt.format(new Date()) + "</p>");
	out.println("<p>The text input was " + textInput);
	
	out.println(tzForm);
	}
}