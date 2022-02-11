package test;

import java.util.List;

public class Issue {

	private String key ;
	private String rule ;
	private String severity ;
	private String component ;
	private String project ;
	private String line ;
	private String hash ;
	private List<?> flows  ;
	public String getKey() {
		return key;
	}

	public String getRule() {
		return rule;
	}

	public String getSeverity() {
		return severity;
	}

	public String getComponent() {
		return component;
	}

	public String getProject() {
		return project;
	}

	public String getLine() {
		return line;
	}

	public String getHash() {
		return hash;
	}

	public List<?> getFlows() {
		return flows;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getEffort() {
		return effort;
	}

	public String getDebt() {
		return debt;
	}

	public String getAuthor() {
		return author;
	}

	public List<?> getTags() {
		return tags;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public String getType() {
		return type;
	}

	public String getScope() {
		return scope;
	}

	public String getQuickFixAvailable() {
		return quickFixAvailable;
	}

	public String getResolution() {
		return resolution;
	}

	public String getCloseDate() {
		return closeDate;
	}

	public TextRange getTextRange() {
		return textRange;
	}

	private String  status ;
	private String message ;
	private String effort ;
	private String debt ;
	private String author ;
	private List<?> tags ;
	private String creationDate ;
	private String updateDate ;
	private String type ;
	private String scope ;
	private String quickFixAvailable ;
	private String resolution ;
	private String closeDate ;
		
	
	private TextRange textRange ;
	
	public Issue() {}
	
	
}
