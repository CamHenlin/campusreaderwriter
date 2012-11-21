package com.campusreaderwriter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class CampusReaderWriterServlet extends HttpServlet {
	public void doGet(HttpServletRequest req,
            HttpServletResponse resp) 
	throws IOException {
	
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	String jScripts = "<script src=\"js/jquery.min.js\"></script>\n" +
						"<script src=\"js/jquery-te-1.0.5.min.js\"></script>\n" +
						"<script src=\"js/csshttprequest.js\"></script>\n" +
						"<script src=\"js/jquery.atd.js\"></script>\n" +
						"<script src=\"js/jquery.atd.textarea.js\"></script>\n" +
						"<script src=\"js/bootstrap.min.js\"></script>\n";
	String cssFiles = "<style src=\"css/bootstrap.css\"></style>\n" + 
						"<style src=\"css/main.css\"></style>\n" +
						"<style src=\"css/atd.css\"></style>\n" +
						"<style src=\"css/jquery-te-Style.css\"></style>" +
						"<style type=\"text/css\">.input { font-size: 100%; width: 400px; height: 200px; font-family: times; border: 1px solid black;" +
						"padding: 2px;  margin: 2px; }</style>\n";
	
	String navBar;
	String tzForm;   
	String textInput = "";
	if (user == null) {
	  navBar = "<p>Welcome! <a href=\"" + userService.createLoginURL("/") +
	           "\">Sign in or register</a> to customize.</p>";
	  tzForm = "";
	  
	
	} else {
	  UserPrefs userPrefs = UserPrefs.getPrefsForUser(user);
	 
	  if (userPrefs != null) {
	      textInput = userPrefs.getTextInput();
	  }
	
	  navBar = "<p>Welcome, " + user.getNickname() + "! You can <a href=\"" +
	           userService.createLogoutURL("/") +
	           "\">sign out</a>.</p>";
	  tzForm = "<form action=\"/prefs\" method=\"post\">" +
	      "<textarea style=\"border-width: 1px; border-color: black;\" name=\"text_input\" class=\"input\" id=\"text_input\" rows=\"20\" cols=\"25\">" + textInput + "</textarea><br>" +
	      "<input type=\"submit\" value=\"Save\" /><a href=\"javascript:check()\" id=\"checkLink\">Check Text</a>" +
	      "</form><script>$('text_input').jqte();\n\n function check() {    AtD.checkTextAreaCrossAJAX('text_input', 'checkLink', 'Edit Text'); } </script>";
	}
	
	resp.setContentType("text/html");
	PrintWriter out = resp.getWriter();
	out.println("<html><head>");
	out.println(jScripts);
	out.println(cssFiles);
	out.println("</head><body bgcolor=\"#ccc\">");
	out.println(navBar);
	// out.println("<p>The text input was " + textInput);
	
	out.println(tzForm);
	out.println("</body></html>");
	}
}