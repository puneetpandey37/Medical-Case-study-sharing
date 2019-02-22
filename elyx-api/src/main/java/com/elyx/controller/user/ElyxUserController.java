package com.elyx.controller.user;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elyx.bo.user.UserService;
import com.elyx.model.user.DoctorSpecialization;
import com.elyx.model.user.User;
import com.elyx.model.user.UserDetail;

@Controller
@RequestMapping("/users")
public class ElyxUserController {
	
	UserService userService;
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/detail",method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public UserDetail getUser(@RequestParam(value = "id", required = false) Long id) {
		return userService.getUser(id);
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<User> getUsers() {
		return userService.getUsers(null);
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public User updateUser(@RequestBody User user) {
		return userService.updateUser(user);
	}
	
	@RequestMapping(value = "/specialization",method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public EnumSet<DoctorSpecialization> getDoctorSpecialization() {
		return userService.getDoctorSpecialization();
	}
	
	@RequestMapping(value = "/search",method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<User> searchUser(@RequestParam("input") String input) {
		return userService.searchUser(input);
	}
}
