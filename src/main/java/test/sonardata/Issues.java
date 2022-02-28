package test.sonardata;

import java.util.List;

public class Issues {

	private String total ;
	private String p ;
	private String ps ;
	private String effortTotal ;
	private List<String> facets ;
	
	private Paging paging ;
	
	private List<Issue> issues ;
	private List<Component> components ;
	
	public Issues() {}

	public String getTotal() {
		return total;
	}

	public String getP() {
		return p;
	}

	public String getPs() {
		return ps;
	}

	public String getEffortTotal() {
		return effortTotal;
	}

	public List<String> getFacets() {
		return facets;
	}

	public Paging getPaging() {
		return paging;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public List<Component> getComponents() {
		return components;
	}
}