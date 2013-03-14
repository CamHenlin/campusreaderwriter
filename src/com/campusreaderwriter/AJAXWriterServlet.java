package com.campusreaderwriter;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.rules.RuleMatch;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;


public class AJAXWriterServlet extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();

		try
		{
			UserPrefs userPrefs = UserPrefs.getPrefsForUser(user);
			out.println("{");
			String jsonOut = "";
			if (req.getParameter("text") != null) {
				jsonOut += "\"text\" : { \"text\" : \"" + userPrefs.getTextInput() + "\"},";
			}
			if (req.getParameter("emails") != null) {
				jsonOut += "\"emails\" : { \"emails\" : \"" + userPrefs.getEmailInput() + "\"},";
			}
			jsonOut.substring(0, jsonOut.length()-1);
			out.println(jsonOut + "}");
			
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		} 
		catch (Exception ex) 
		{
			out.println(ex.toString());
	    }
		
	}
}