/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.awd.dao.UsersDao;
import com.awd.model.Authorities;
import com.awd.model.Roles;
import com.awd.model.Users;


/**
 * 实现SpringSecurity的UserDetailsService接口,实现获取用户Detail信息的回调函�?.
 * 
 * @author calvin
 */
@Transactional(readOnly = true)
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UsersDao usersDao;

	/**
	 * 获取用户Detail信息的回调函�?.
	 */
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {

		Users users = usersDao.findByUnique("loginName", userName);
		if (users == null)
			throw new UsernameNotFoundException("用户" + userName + " 不存�?");

		GrantedAuthority[] grantedAuths = obtainGrantedAuthorities(users);

		// mini-web中无以下属�??,暂时全部设为true.
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		org.springframework.security.userdetails.User userdetail = new org.springframework.security.userdetails.User(
				users.getLoginName(), users.getPassword(), enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, grantedAuths);

		return userdetail;
	}

	/**
	 * 获得用户�?有角色的权限.
	 */
	private GrantedAuthority[] obtainGrantedAuthorities(Users user) {
		Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
		for (Roles role : user.getRoles()) {
			for (Authorities authority : role.getAuthorities()) {
				authSet.add(new GrantedAuthorityImpl(authority.getName()));
			}
		}
		return authSet.toArray(new GrantedAuthority[authSet.size()]);
	}
}
