package com.kangyu.service.impl;

import com.kangyu.domain.User;
import com.kangyu.mapper.UserMapper;
import com.kangyu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

	//注入Mapper接口
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User findByName(String name) {
		return userMapper.findByNamee(name);
	}

	@Override
	public User findById(Integer id) {
		return userMapper.findById(id);
	}

	
}
