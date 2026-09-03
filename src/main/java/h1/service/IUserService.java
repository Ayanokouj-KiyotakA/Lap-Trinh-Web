package h1.service;

import h1.entity.AppUser;

public interface IUserService {
	AppUser login(String username, String password);

	AppUser findByUserName(String username);

	AppUser findById(int id);

	void update(AppUser user);
}
