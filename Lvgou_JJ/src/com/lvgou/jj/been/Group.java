package com.lvgou.jj.been;

import java.util.ArrayList;
import java.util.List;

public class Group {

	private String GroupName;
	private List<User> users = new ArrayList<User>();

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
