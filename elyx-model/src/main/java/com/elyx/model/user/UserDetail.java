package com.elyx.model.user;

import java.util.List;

import com.elyx.model.cases.Case;

public class UserDetail {

	private User me;
	private List<User> users;
	private List<Case> toMeCases;
	private List<Case> myCases;
	public User getMe() {
		return me;
	}
	public void setMe(User me) {
		this.me = me;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<Case> getToMeCases() {
		return toMeCases;
	}
	public void setToMeCases(List<Case> toMeCases) {
		this.toMeCases = toMeCases;
	}
	public List<Case> getMyCases() {
		return myCases;
	}
	public void setMyCases(List<Case> myCases) {
		this.myCases = myCases;
	}
	
}
