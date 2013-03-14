package com.campusreaderwriter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class DBUtils {
	public static final Logger _logger = Logger.getLogger(DBUtils.class.getName());
	
	//Currently we are hardcoding this list. But this could also be retrieved from
	//database
	public static String getAssessmentDataMasterList() throws Exception {
		return "question1,question2,question3";
	}
	
	/**
	* This method persists a record to the database.
	*/
	public static void saveAssessmentData(AssessmentData aD)
	throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(aD);
			_logger.log(Level.INFO, "assessment data saved.");
			} catch (Exception ex) {
			_logger.log(Level.SEVERE,
			"Could not save the assessment data. Reason : "
			+ ex.getMessage());
			throw ex;
		} finally {
			pm.close();
		}
	}
	
	
	/**
	* This method persists a record to the database.
	*/
	public static void saveAssessmentQuestionData(AssessmentQuestionData aD)
	throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(aD);
			_logger.log(Level.INFO, "assessment data saved.");
			} catch (Exception ex) {
			_logger.log(Level.SEVERE,
			"Could not save the assessment data. Reason : "
			+ ex.getMessage());
			throw ex;
		} finally {
			pm.close();
		}
	}
	
	/**
	* This method gets the count all health incidents in an area (Pincode/Zipcode) for the current month
	* @param healthIncident
	* @param pinCode
	* @return A Map containing the health incident name and the number of cases reported for it in the current month
	*/
	public static Map<String, Integer> getAssessmentDataForCurrentMonth(String text) {
		Map<String, Integer> _assessmentData = new HashMap<String, Integer>();
		
		PersistenceManager pm = null;
		
		//Get the current month and year
		Calendar c = Calendar.getInstance();
		int CurrentMonth = c.get(Calendar.MONTH);
		int CurrentYear = c.get(Calendar.YEAR);
		
		try {
			//Determine if we need to generate data for only one health Incident or ALL
			String[] assessmentTexts = {};
			if (text.equalsIgnoreCase("ALL")) {
				String strAssessmentData = getAssessmentDataMasterList();
				assessmentTexts = strAssessmentData.split(",");
			}
			else {
				assessmentTexts =  new String[]{text};
			}
			
			pm = PMF.get().getPersistenceManager();
			Query query = null;
			
			//For each health incident (i.e. Cold Flu Cough), retrieve the records
			
			for (int i = 0; i < assessmentTexts.length; i++) {
				int assessmentDataCount = 0;
				//Set the From and To Dates i.e. 1st of the month and 1st day of next month
				Calendar _cal1 = Calendar.getInstance();
				_cal1.set(CurrentYear, CurrentMonth, 1);
				Calendar _cal2 = Calendar.getInstance();
				_cal2.set(CurrentYear,CurrentMonth+1,1);
				
				List<AssessmentData> codes = null;
				
				codes = (List<AssessmentData>) query.executeWithArray(assessmentTexts[i], _cal1.getTime(), _cal2.getTime());
				
				//Iterate through the results and increment the count
				for (Iterator iterator = codes.iterator(); iterator.hasNext();) {
					AssessmentData _aD = (AssessmentData) iterator.next();
					assessmentDataCount++;
				}
				
				//Put the record in the Map data structure
				_assessmentData.put(assessmentTexts[i], new Integer(assessmentDataCount));
			}
			return _assessmentData;
			
		} catch (Exception ex) {
			return null;
		} finally {
			pm.close();
		}
	}
	
	/**
	* This method gets the count all health incidents in an area (Pincode/Zipcode) for the current month
	* @param healthIncident
	* @param pinCode
	* @return A Map containing the health incident name and the number of cases reported for it in the current month
	*/
	public static String getLatestAssessmentQuestions() {
		String _assessmentData = "";
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {	
			pm = PMF.get().getPersistenceManager();
			Query query = null;
			
			List<AssessmentQuestionData> questions = null;
			query = pm.newQuery(AssessmentQuestionData.class);
			query.setOrdering("assessmentDateTime");
					
			questions = (List<AssessmentQuestionData>) query.execute();
			
			//Iterate through the results and increment the count
			for (Iterator iterator = questions.iterator(); iterator.hasNext();) {
				AssessmentQuestionData _aD = (AssessmentQuestionData) iterator.next();
				_assessmentData = _aD.getQuestions();
			}
			
			
			
			return _assessmentData;
			
		} catch (Exception ex) {
			return null;
		} finally {
			pm.close();
		}
	}
}