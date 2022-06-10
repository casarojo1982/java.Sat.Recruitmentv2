package com.sevice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.model.User;

@Service
public class UserService {
	private static AtomicInteger counter = new AtomicInteger();

	private static List<User> Users;

	static {
		Users = populateUsers();
	}

	public User findUserById(int id) {
		for (User User : Users) {
			if (id == User.getId()) {
				return User;
			}
		}
		return null;
	}

	public void saveUser(User User) {
		User.setId(counter.incrementAndGet());
		Users.add(User);
	}

	public void updateUser(User User) {
		int index = Users.indexOf(User);
		Users.set(index, User);
	}

	public void deleteUserById(int id) {
		Iterator<User> it = Users.iterator();
		while (it.hasNext()) {
			User User = it.next();
			if (id == User.getId()) {
				it.remove();
			}
		}
	}

	public boolean isUserAvailable(User User) {
		return findUserById(User.getId()) != null;
	}

	private static List<User> populateUsers() {
		List<User> Users = new ArrayList<User>();
		Users.add(new User(counter.incrementAndGet(), "Juan","Juan@marmol.com","+5491154762312","Peru 2464","Normal",1234.00));
		Users.add(new User(counter.incrementAndGet(), "Franco","Franco.Perez@gmail.com","+534645213542","Alvear y Colombres","Premium",112234.00));
		Users.add(new User(counter.incrementAndGet(), "Agustina","Agustina@gmail.com","+534645213542","Garay y Otra Calle","SuperUser",112234.00));
		return Users;
	}

}
