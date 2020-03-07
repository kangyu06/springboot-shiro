package com.kangyu.service;

import com.kangyu.domain.User;

public interface UserService {

	public User findByName(String name);
	
	public User findById(Integer id);
}
