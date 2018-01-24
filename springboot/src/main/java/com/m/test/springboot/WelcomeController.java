package com.m.test.springboot;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.m.test.springboot.config.SpringSecurityConfig;

@Controller
public class WelcomeController {
	
	@Autowired
	SpringSecurityConfig ss;

	@RequestMapping("/")
	public ModelAndView welcome() {
		ModelAndView mv = new ModelAndView("welcome");
		return mv;
	}
	
	@RequestMapping("/about")
	public ModelAndView about() {
		ModelAndView mv = new ModelAndView("about");
		return mv;
	}
	
	@GetMapping("/login")
	public String login() {
		
		return "/login";
	}
	
	@RequestMapping(value="/author")
	public ModelAndView author() {
		ModelAndView mv ;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String role = auth.getAuthorities().toString();
		//mv.addObject(attributeName, attributeValue)
		if(auth.isAuthenticated()) {
			if(role.equals("[ROLE_ADMIN]")) {
				mv = new ModelAndView("adminPanel");
				mv.addObject("role", role);
				return mv;
			} 
			else if(role.equals("[ROLE_USER]")) {
				mv= new ModelAndView("userPanel");
				mv.addObject("role", role);
				return mv;
			}
//			else {
//				
//				mv= new ModelAndView("welcome");
//				mv.addObject("role", role+" from welcome page");
//				return mv;
//			}
			
		}
		
		return new ModelAndView("welcome");
		
		
	}
	
	@RequestMapping("/admin")
	public ModelAndView admin() {
		ModelAndView mv = new ModelAndView("adminPanel");
		return mv;
	}
}
