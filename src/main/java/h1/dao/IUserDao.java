package h1.dao;

import h1.entity.AppUser;

public interface IUserDao {
	AppUser findByUserName(String username);

	void insert(AppUser user);
}
