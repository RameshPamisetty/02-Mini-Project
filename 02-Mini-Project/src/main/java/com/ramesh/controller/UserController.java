package com.ramesh.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ramesh.bindings.LoginForm;
import com.ramesh.bindings.RegisterForm;
import com.ramesh.bindings.ResetPwdForm;
import com.ramesh.constants.AppConstants;
import com.ramesh.entity.User;
import com.ramesh.props.AppProps;
import com.ramesh.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AppProps props;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("login", new LoginForm());
		return "index";
	}

	@PostMapping("/login")
	public String loginCheck(@ModelAttribute("login") LoginForm login, Model model) {
		User user = userService.login(login);

		if (user == null) {
			model.addAttribute(AppConstants.ERROR_MSG, props.getMessages().get("invalidLogin"));
			return "index";
		}

		if (user.getPwdUpdated().equals("NO")) {
			ResetPwdForm formObj = new ResetPwdForm();
			formObj.setUserId(user.getUserId());

			// model.addAttribute("userId", user.getUserId());
			model.addAttribute("resetPwd", formObj);
			return "resetPwd";
		}
		return "redirect:dashboard";
	}

	@PostMapping("/updatePwd")
	public String updatePwd(@ModelAttribute("resetPwd") ResetPwdForm resetPwd, Model model) {

		if (!resetPwd.getNewPwd().equals(resetPwd.getConfirmPwd())) {
			model.addAttribute(AppConstants.ERROR_MSG, props.getMessages().get("invalidPwds"));
			return "resetPwd";
		}

		boolean status = userService.resetPwd(resetPwd);
		if (status) {
			return "redirect:dashboard";
		}
		model.addAttribute(AppConstants.ERROR_MSG, props.getMessages().get("pwdUpdatedFailed"));
		return "resetPwd";
	}

	@GetMapping("/register")
	public String loadRegisterPage(Model model) {

		model.addAttribute("registerForm", new RegisterForm());
		Map<Integer, String> countries = userService.getCountries();
		model.addAttribute("countries", countries);
		return "register";
	}

	@ResponseBody
	@GetMapping("/getStates")
	public Map<Integer, String> getStates(@RequestParam("countryId") Integer countryId) {
		return userService.getStates(countryId);
	}

	@ResponseBody
	@GetMapping("/getCities")
	public Map<Integer, String> getCities(@RequestParam("stateId") Integer stateId) {
		return userService.getCities(stateId);
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute RegisterForm registerForm, Model model) {
		boolean saveUser = userService.saveUser(registerForm);
		if (saveUser) {
			model.addAttribute(AppConstants.SUCC_MSG, props.getMessages().get("regSuccess"));
		}else {
			model.addAttribute(AppConstants.ERROR_MSG, props.getMessages().get("regFailed"));	
		}
		Map<Integer, String> countries = userService.getCountries();
		model.addAttribute("countries", countries);
		return "register";
	}

}
