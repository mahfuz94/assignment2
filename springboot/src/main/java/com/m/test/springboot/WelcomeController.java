package com.m.test.springboot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.m.test.model.User;
import com.m.test.springboot.config.SpringSecurityConfig;

@Controller
public class WelcomeController {
	
	@Autowired
	SpringSecurityConfig ss;
	
	static List<User> userList = new ArrayList<User>();;
	
	@Autowired
	HttpSession session;
	
	User user;
	

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
	
	@RequestMapping(value="/author/admin/adduser")
	public String addUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "addUser";
	}
	
	@RequestMapping(value="/author/admin/adduser", method=RequestMethod.POST)
	public String addPostUser(@ModelAttribute User user, Model model) {
		userList.add(user);
		this.user = user;
//		session.setAttribute("userList", userList);
		model.addAttribute("firstName", user.getFirstName());
		return "addUser";
	}
	
	@ModelAttribute("userList")
	public List<User> getListUser() {
		return userList;
	}
	
	@RequestMapping(value="/author/admin/viewalluser")
	public String viewUser(Model model) {
		//model.addAttribute("userLis", userList.get(1).getLastName());
		return "viewUser";
	}
	
	
	@RequestMapping(value="/author/admin/delete/{id}/user")
	public String deleteUser(@PathVariable int id) {
		userList.remove(id);
		return "redirect:/author/admin/viewalluser";
	}
	
	@RequestMapping(value="/author/admin/edit/{id}/user")
	public String editUser(@PathVariable int id, Model model) {
		user = userList.get(id);
		model.addAttribute("index", id);
		model.addAttribute("editUser", user);
		return "editUser";
	}
	
	@RequestMapping(value="/author/admin/update/{id}/user", method=RequestMethod.POST)
	public String updateUser(@PathVariable int id, @ModelAttribute User editUser, Model model) {
		//editUser = userList.get(id);
		user = editUser;
		userList.remove(id);
		userList.add(id, user);
		model.addAttribute("index", id);
		model.addAttribute("editUser", user);
		return "redirect:/author/admin/edit/"+id+"/user";
	}
	
	@RequestMapping("/admin")
	public ModelAndView admin() {
		ModelAndView mv = new ModelAndView("adminPanel");
		return mv;
	}
	
	@RequestMapping("/sortby/firstName")
	public String sortByFirstName() {
		Collections.sort(userList, new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
			}
			
		});
		return "redirect:/author/admin/viewalluser";
	}
	
	@RequestMapping("/sortby/lastName")
	public String sortByLastName() {
		Collections.sort(userList, new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o1.getLastName().compareToIgnoreCase(o2.getLastName());
			}
			
		});
		return "redirect:/author/admin/viewalluser";
	}
	
	@RequestMapping("/sortby/phoneNumber")
	public String sortByPhoneNumber() {
		Collections.sort(userList, new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o1.getPhoneNumber().compareToIgnoreCase(o2.getPhoneNumber());
			}
			
		});
		return "redirect:/author/admin/viewalluser";
	}
}
