package h1.dao;

import h1.entity.AppUser;

public interface IUserDao {
	AppUser findByUserName(String username);

	AppUser findById(int id);

	void insert(AppUser user);

	void update(AppUser user);
}
