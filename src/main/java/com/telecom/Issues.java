package com.telecom;

import java.io.Serializable;
import java.util.List;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class Issues implements Serializable {

	static final long serialVersionUID = 1L;

	private List<Issue> list;

	private Integer lastIssueId;

	public Issues() {
	}

	public List<Issue> getList() {
		return this.list;
	}

	public void setList(List<Issue> list) {
		this.list = list;
	}

	public Integer getLastIssueId() {
		return this.lastIssueId;
	}

	public void setLastIssueId(Integer lastIssueId) {
		this.lastIssueId = lastIssueId;
	}

	public Issues(List<Issue> list,
			Integer lastIssueId) {
		this.list = list;
		this.lastIssueId = lastIssueId;
	}

}