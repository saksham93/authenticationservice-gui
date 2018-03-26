package com.nagarro.authenticationgui.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@Controller
@SessionAttributes("name")
public class LoginController {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private EurekaClient eurekaClient;

	@Value("${service.user.authentication}")
	private String userAuthenticationServiceId;

	@Value("${WEB_AUTHENTICATION_SERVICE_ID}")
	private String webServiceId;

	@GetMapping(value = "/login")
	public String showLoginPage(ModelMap model) {
		return "login";
	}

	@PostMapping(value = "/login")
	public String showWelcomePage(ModelMap model, @RequestParam String name, @RequestParam String password) {
		Application application = eurekaClient.getApplication(userAuthenticationServiceId);
		InstanceInfo instanceInfo = application.getInstances().get(0);
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		Map<String, String> vars = new HashMap<>();
		String url = "http://" + webServiceId + ":" + instanceInfo.getPort() + "/authenticate/";
		System.out.println("URL" + url);
		Boolean isPresent = restTemplate.postForObject(url, user, Boolean.class, vars);
		if (isPresent) {
			model.put("name", "Authenticated");
		} else {
			model.put("name", "Invalid Credentials");
		}
		model.put("password", password);
		return "welcome";
	}
}