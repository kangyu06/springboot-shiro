package com.kangyu.mapper;

import com.kangyu.domain.User;

public interface UserMapper {

	public User findByNamee(String name);
	
	public User findById(Integer id);
}
