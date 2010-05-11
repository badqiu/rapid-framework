package com.company.project.facade.converter;

import java.util.ArrayList;
import java.util.List;

import com.company.project.facade.dto.UserInfoDTO;
import com.company.project.model.UserInfo;

public class UserInfoServiceConverter {
	
	public static UserInfoDTO convert(UserInfo source) {
		UserInfoDTO target = new UserInfoDTO();
		
		target.setAge(source.getAge());
		return target;
	}

	public static UserInfo convert(UserInfoDTO source) {
		UserInfo target = new UserInfo();
		
		target.setAge(source.getAge());
		return target;		
	}

	public static List<UserInfoDTO> convertFrom(List<UserInfo> list) {
		List<UserInfoDTO> results = new ArrayList();
		for(UserInfo source : list) {
			results.add(convert(source));
		}
		return results;
	}

	public static List<UserInfo> convertFromDTO(List<UserInfoDTO> list) {
		List<UserInfo> results = new ArrayList();
		for(UserInfoDTO source : list) {
			results.add(convert(source));
		}
		return results;
	}
	
}
