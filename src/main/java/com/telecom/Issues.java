package com.telecom;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class Issues implements Serializable {

	static final long serialVersionUID = 1L;

	private HashMap<Integer,Issue> list;

	public Issues() {
	}

	public HashMap<Integer,Issue> getList() {
		return this.list;
	}

	public void setList(HashMap<Integer,Issue> list) {
		this.list = list;
	}
	
	public void removeIssue(Integer issueId) {
	    list.remove(issueId);
	}



	public Issues(HashMap<Integer,Issue> list) {
		this.list = list;
	}

}