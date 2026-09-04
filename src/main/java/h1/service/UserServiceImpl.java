package h1.service;

import h1.dao.IUserDao;
import h1.dao.UserDao;
import h1.entity.AppUser;

public class UserServiceImpl implements IUserService {
	IUserDao userDao = new UserDao();

	@Override
	public AppUser login(String username, String password) {
		AppUser user = this.findByUserName(username);
		if (user != null && password.equals(user.getPassWord())) {
			return user;
		}
		return null;
	}

	@Override
	public AppUser findByUserName(String username) {
		return userDao.findByUserName(username);
	}

	@Override
	public AppUser findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public AppUser findById(int id) {
		return userDao.findById(id);
	}

	@Override
	public void insert(AppUser user) {
		userDao.insert(user);
	}

	@Override
	public void update(AppUser user) {
		userDao.update(user);
	}
}
