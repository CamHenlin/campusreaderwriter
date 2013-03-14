package com.campusreaderwriter;
import java.util.Date;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class AssessmentQuestionData {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String questions;
	private String user;
	@Persistent
	private Date assessmentDateTime;
	
	public AssessmentQuestionData(String user, String questions, Date assessmentDateTime) {
		super();
		this.user = user;
		this.questions = questions;
		this.assessmentDateTime = assessmentDateTime;
	}
	
	public Key getKey() {
		return key;
	}
	
	public void setKey(Key key) {
		this.key = key;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getQuestions() {
		return questions;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setQuestions(String questions) {
		this.questions = questions;
	}
	
	public Date getAssessmentDateTime() {
		return assessmentDateTime;
	}
	
	public void setAssessmentDateTime(Date assessmentDateTime) {
		this.assessmentDateTime = assessmentDateTime;
	}

}