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

public class CampusReaderWriterServlet extends HttpServlet
{
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException
  {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    String jScripts = "<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js\"></script><script type=\"text/javascript\" src=\"http://documentcloud.github.com/underscore/underscore-min.js\"></script><script type=\"text/javascript\" src=\"http://documentcloud.github.com/backbone/backbone.js\"></script>";
    jScripts = jScripts + "<script type='text/javascript'>assessmentModel = new Backbone.Model({    data: [ " + DBUtils.getLatestAssessmentQuestions().replace("\"name\"", "name").replace("\"value\"", "value").replace("\"text\"", "text") + " ]}); </script>";
    jScripts = jScripts + "\n<script src=\"js/jquery-te-1.0.5.min.js\"></script>\n<script src=\"js/csshttprequest.js\"></script>\n<script src=\"js/jquery.atd.js\"></script>\n<script src=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js\"></script>\n<script src=\"http://henlin.org/crwjs/script.js\"></script><script src=\"http://henlin.org/crwjs/assessment.js\"></script>\n<script src=\"http://henlin.org/crwjs/texttospeech.js\"></script>\n<script src=\"http://henlin.org/crwjs/jquery.contenteditable/jquery.contenteditable/shortcut.js\"></script>\n<script src=\"http://henlin.org/crwjs/jquery.contenteditable/jquery.contenteditable/jquery.contenteditable.js\"></script>";
    String assessmentModel = "";
    
    String cssFiles = "<style src=\"http://henlin.org/crwjs/jquery.contenteditable/jquery.contenteditable/jquery.contenteditable.css\"></style>\n<style type=\"text/css\">.input { font-size: 100%;  font-family: times; border: 1px solid black;padding: 2px;  margin: 2px; }</style>\n<style type=\"text/css\" media=\"screen\">\t\t#container{ width:600px; }  body {\tpadding: 20px;\n font-family:\"Helvetica Neue\",Helvetica,Arial,sans-serif;  background: -moz-linear-gradient(top, rgba(224,224,224,1) 0%, rgba(235,235,235,0) 36%, rgba(255,255,255,0) 100%); /* FF3.6+ */ background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(224,224,224,1)), color-stop(36%,rgba(235,235,235,0)), color-stop(100%,rgba(255,255,255,0)));  background: -webkit-linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* Chrome10+,Safari5.1+ */ background: -o-linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* Opera 11.10+ */ background: -ms-linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* IE10+ */ background: linear-gradient(top, rgba(224,224,224,1) 0%,rgba(235,235,235,0) 36%,rgba(255,255,255,0) 100%); /* W3C */ filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#e0e0e0', endColorstr='#00ffffff',GradientType=0 ); /* IE6-9 */ margin: 0;background-repeat: no-repeat;background-attachment: fixed;}\t</style>";

    String navBar = "";
    String tzForm = "";
    String textInput = "";
    String emails = "";
    String emailsraw = "";

    if (user == null) {
      navBar = "<p>Welcome! <a href=\"" + userService.createLoginURL("/") + 
        "\">Sign in or register</a> to customize.</p>";
      tzForm = "";
    }
    else
    {
      try
      {
        UserPrefs userPrefs = UserPrefs.getPrefsForUser(user);

        if (userPrefs != null) {
          textInput = userPrefs.getTextInput();
          emails = userPrefs.getEmailInput().replace(".", "").replace("@", "");
          emailsraw = userPrefs.getEmailInput();
        }

        navBar = "<p>Welcome, " + user.getEmail() + "! You can <a href=\"" + 
          userService.createLogoutURL("/") + 
          "\">sign out</a>.<br>You can edit the assessment questions <a href='http://henlin.org/crwjs/saveAssessmentQuestions.html'>here</a>.</p><div id=user style=\"visibility: hidden;\">" + user.getEmail().replace(".", "").replace("@", "") + "</div>";

        tzForm = "";
        int counter = 0;

        JLanguageTool grammarCheck = new JLanguageTool(Language.AMERICAN_ENGLISH);
        grammarCheck.activateDefaultPatternRules();
        grammarCheck.disableRule("EnglishUnpairedBracketsRule");
        List<RuleMatch> matches = grammarCheck.check(textInput);

        StringBuilder tempText = new StringBuilder(textInput);

        int ruleMatchCount = 0;
        for (RuleMatch match : matches) {
          String js = "$('body').append('<div id=\\'grammar_check_error_" + ruleMatchCount + "_corrections\\'></div>'); ";

          List<String> suggestedReplacements = match.getSuggestedReplacements();
          for (String suggestion : suggestedReplacements)
          {
          //  js = js + "$('#grammar_check_error_" + ruleMatchCount + "_corrections').append('<br><a href=# onclick=\\' $(\\'#grammar_error_" + ruleMatchCount + "\\').text(\\'" + suggestion + "\\')\\'>" + suggestion + "</a>'); function(event) {  $('#grammar_check_error_" + ruleMatchCount + "_corrections').css( {position:\\'absolute\\', top:event.pageY, left: event.pageX}); } ";
        	  js = js + " showGrammarCorrection(" + ruleMatchCount + ", '" + suggestion + "', event); ";
          }

          try
          {
            String tempErrorStartTag = "<u id=grammar_error_" + ruleMatchCount + " onclick=\"" + js + "\">";
            tempText = tempText.insert(match.getFromPos() + counter, tempErrorStartTag);
            counter += tempErrorStartTag.length();
            tempText = tempText.insert(match.getToPos() + counter, "</u>");
            counter += 4;
          }
          catch (Exception ex) {
            tzForm = tzForm + ex.toString();
          }

          ruleMatchCount++;
        }
        // tzForm = tzForm + "<script type='text/javascript'>assessmentModel = new Backbone.Model({    data: [ " + DBUtils.getLatestAssessmentQuestions().replace("\"name\"", "name").replace("\"value\"", "value").replace("\"text\"", "text") + " ]}); </script>";
        tzForm = tzForm + "<div id=\"assessment-template\" style=\"display: none;\"><li><input type=\"checkbox\" name=\"\" value=\"\"></li></div><div id=\"count\"></div><div id=\"container\" style=\"border-width: 1px; border-style: solid; border-color: black; background-color: #c7c7c7; margin: 4px; padding: 4px; width: 300px; text-align: center; z-index: 100; left: -500px; top: 200px; position: absolute;\"><form></form><button onclick=\"writeToDataStoreAndHide();\">Save</button></div>";
        tzForm = tzForm + "<div style=\"float: right; margin-top: 10px;\"><form style=\"float: left;\" id=dataform action=\"/prefs\" method=\"post\">Email: <input type=text name=email_input id=email_input /><input type=\"button\" style=\"float: right;\" value=\"Save Email To Group\" onclick=\"submitForm();\" /><input type=\"submit\" style=\"float: right;\" value=\"Clear Group\" /><br><div id=emails>" + 
          emails + 
          "</div><div id=emailsraw>" + emailsraw + "</div></div>" + 
          "<table>" + 
	          "<tr>" + 
		          "<td width=300 valign=top>" + 
		          "<input style=\"visibility: hidden;\" type=\"text\" id=\"in\" value=\"" + user.getEmail() + "\">" + 
		          "<br><a href=# onclick=\"hideDivs('#content_" + user.getEmail().replace(".", "").replace("@", "") + "');\">" + user.getEmail() + "</a><div id=" + user.getEmail().replace(".", "").replace("@", "") + ">" + 
		          "<div id=content_" + user.getEmail().replace(".", "").replace("@", "") + ">" + 
		          "</div></div><div id=groupusers></div>" + 
		          "</td>" + 
		          "<td valign=top>" + 
		          "<div style=\"margin-left:auto; margin-right:auto;\" id=container>" + 
		          "<textarea name=text_input id=text_input style=\"visibility: hidden;\"></textarea>" + 
		          "</form>" + 
		          "</div>" + 
		          "<input type=button value=\"Clear formatting\" onclick=\"stripFormHTML('#textinput');\" /> " + 
		          "<input type=button value=\"Clear selected formatting\" onclick=\"stripHTML();\" /> " + 
		          "<input type=button value=\"b\" onclick=\"boldText();\" /> " + 
		          "<input type=button value=\"i\" onclick=\"italicText();\" /> " + 
		          "<div width=400 style=\"background-color: white; border-color: black; border-width: 1px; border-style: solid; height: 500px;\" id='textinput' name='textinput' contenteditable>" + tempText + "</div>" + 
		          "<br>" + 
		          "<input type=\"button\" value=\"Save\" onclick=\"submitForm();\" />" + 
		          "</td>" + 
	          "</tr>" + 
          "</table>";
        
        // tzForm = tzForm + DBUtils.getLatestAssessmentQuestions();
      }
      catch (Exception ex) {
        tzForm = ex.toString();
      }

    }

    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();
    out.println("<html><head>");
    out.println(jScripts);
    out.println(cssFiles);
    if (user != null) {
      out.println("</head><body onload=\"getUserLibraries('#content_" + user.getEmail().replace(".", "").replace("@", "") + "'); \">");
    }
    else {
      out.println("</head><body>");
    }
    out.println(navBar);

    out.println(tzForm);
    out.println("</body></html>");
  }
}