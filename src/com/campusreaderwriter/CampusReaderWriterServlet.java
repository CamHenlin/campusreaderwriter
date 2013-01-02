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
	String jScripts = "<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js\"></script>\n" +
						"<script src=\"js/jquery-te-1.0.5.min.js\"></script>\n" +
						"<script src=\"js/csshttprequest.js\"></script>\n" +
						"<script src=\"js/jquery.atd.js\"></script>\n" +
						"<script src=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js\"></script" +
					//	"<script src=\"js/jquery.wysiwyg.js\"></script>\n" +
					//	"<script src=\"js/wysiwyg.image.js\"></script>\n" +
					//	"<script src=\"js/wysiwyg.table.js\"></script>\n" +
					//	"<script src=\"js/default.js\"></script>\n" +
					//	"<script src=\"js/wysiwyg.link.js\"></script>\n" +
						"<script src=\"js/jquery.atd.textarea.js\"></script>\n" +
						"<script src=\"http://henlin.org/crwjs/script.js\"></script>\n" +
						"<script src=\"http://henlin.org/crwjs/texttospeech.js\"></script>\n" +
						"<script src=\"js/bootstrap.min.js\"></script>\n";
	String cssFiles = "<style src=\"css/main.css\"></style>\n" +
						"<style type=\"text/css\">.input { font-size: 100%;  font-family: times; border: 1px solid black;" +
						"padding: 2px;  margin: 2px; }</style>\n" +
						"<script type=\"text/javascript\">" +
					//	"(function($) {" +
					//	"	$(document).ready(function() {$('#text_input').wysiwyg({controls:\"bold,italic,|,undo,redo,image\"});});" +
					//	"})(jQuery);" +
						"</script>" +
						"<style type=\"text/css\" media=\"screen\">		#container{ width:600px; }" +
						"  body {	padding: 20px;\n" +
						" font-family:\"Helvetica Neue\",Helvetica,Arial,sans-serif; "+
						" background: -moz-linear-gradient(top, rgba(224,224,224,1) 0%, rgba(235,235,235,0) 36%, rgba(255,255,255,0) 100%); /* FF3.6+ */" +
	                    " background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(224,224,224,1)), color-stop(36%,rgba(235,235,235,0)), color-stop(100%,rgba(255,255,255,0))); "+ /* Chrome,Safari4+ */
	                    " background: -webkit-linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* Chrome10+,Safari5.1+ */"+
	                    " background: -o-linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* Opera 11.10+ */"+
	                    " background: -ms-linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* IE10+ */"+
	                    " background: linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* W3C */"+
	                    " filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#e0e0e0', endColorstr='#00ffffff',GradientType=0 ); /* IE6-9 */"+
	                    " margin: 0;"+
	                    "background-repeat: no-repeat;"+
	                    "background-attachment: fixed;"+
	                    "}" +
						"" +
						"	</style>";
	
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
	  tzForm = "<table><tr><td width=300>	<input type=\"text\" id=\"in\"><button onclick=\"getUserLibraries()\">get data</button><div id=temp></div></td><td valign=top><div style=\"margin-left:auto; margin-right:auto;\" id=container><form id=dataform action=\"/prefs\" method=\"post\">" +
		      "<textarea name=text_input id=text_input style=\"visibility: hidden;\"></textarea></div><div width=400 style=\"background-color: white; border-color: black; border-width: 1px; border-style: solid; height: 500px;\" id='textinput' name='textinput' contenteditable>" + textInput + "</div><br>" +
		      "<input type=\"button\" value=\"Save\" onclick=\"$(\"#text_input\").html($(\"#textinput\").html()); $(\"#dataform\").submit();\" />" +
		      "</form></div></td></tr></table>";
		
	  /* tzForm = "<table><tr><td width=300>	<input type=\"text\" id=\"in\"><button onclick=\"getUserLibraries()\">get data</button><div id=temp></div></td><td><div style=\"margin-left:auto; margin-right:auto;\" id=container><form action=\"/prefs\" method=\"post\">" +
	      "<textarea style=\"border-width: 2px; border-color: black;\" name=\"text_input\" class=\"input\" id=\"text_input\" rows=\"25\" cols=\"80\">" + textInput + "</textarea><br>" +
	      "<input type=\"submit\" value=\"Save\" /><a href=\"javascript:check()\" id=\"checkLink\">Check Text</a>" +
	      "</form><script>\n\n function check() {    AtD.checkTextAreaCrossAJAX('text_input', 'checkLink', 'Edit Text'); } </script></div></td></tr></table>"; */
	}
	
	resp.setContentType("text/html");
	PrintWriter out = resp.getWriter();
	out.println("<html><head>");
	out.println(jScripts);
	out.println(cssFiles);
	out.println("</head><body>");
	out.println(navBar);
	// out.println("<p>The text input was " + textInput);
	
	out.println(tzForm);
	out.println("</body></html>");
	}
}