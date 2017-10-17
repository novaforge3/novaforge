package org.novaforge.forge.plugins.quality.sonar.ws.models;

import java.util.ArrayList;
import java.util.List;

public class Users {

	private List<User> users = new ArrayList<User>();

	private Users(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public boolean isEmpty() {

		return this.users.isEmpty();
	}

	public User getFirst() {

		User ret = null;

		if (!this.users.isEmpty()) {

			ret = this.users.get(0);
		}
		return ret;
	}
}
