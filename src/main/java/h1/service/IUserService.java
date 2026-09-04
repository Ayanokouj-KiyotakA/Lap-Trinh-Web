package h1.service;

import h1.entity.AppUser;

public interface IUserService {
	AppUser login(String username, String password);

	AppUser findByUserName(String username);

	AppUser findByEmail(String email);

	AppUser findById(int id);

	void insert(AppUser user);

	void update(AppUser user);
}
