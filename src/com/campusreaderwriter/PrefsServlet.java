package com.campusreaderwriter;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.campusreaderwriter.UserPrefs;

@SuppressWarnings("serial")
public class PrefsServlet extends HttpServlet {
    public void doPost(HttpServletRequest req,
                       HttpServletResponse resp)
        throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        UserPrefs userPrefs = UserPrefs.getPrefsForUser(user);

        try {
            String textInput = new String(req.getParameter("text_input"));
            userPrefs.setTextInput(textInput);
            userPrefs.save();   
            String emailInput = new String(req.getParameter("email_input"));
            userPrefs.setEmailInput(emailInput);
            userPrefs.save(); 
        } catch (NumberFormatException nfe) {
            // User entered a value that wasn't an integer.  Ignore for now.
        	resp.sendError(500, "Error occurred with new form");
        }

        resp.sendRedirect("/");
    }
}