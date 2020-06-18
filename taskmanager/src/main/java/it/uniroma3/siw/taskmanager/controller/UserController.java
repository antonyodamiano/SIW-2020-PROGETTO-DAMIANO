package it.uniroma3.siw.taskmanager.controller;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.CredentialsValidator;
import it.uniroma3.siw.taskmanager.controller.validation.UserValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.UserRepository;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserValidator userValidator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SessionData sessionData;
    
    @Autowired
    CredentialsValidator credentialsValidator;
    
    @Autowired
    UserService userService;
    
    @Autowired
    CredentialsService credentialsService;

    
    @RequestMapping(value = { "/home" }, method = RequestMethod.GET)
    public String home(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "home";
    }


    @RequestMapping(value = { "/users/me" }, method = RequestMethod.GET)
    public String me(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        Credentials credentials = sessionData.getLoggedCredentials();
        System.out.println(credentials.getPassword());
        model.addAttribute("user", loggedUser);
        model.addAttribute("credentials", credentials);

        return "userProfile";
    }


    @RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
    public String admin(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "admin";
    }
    
    @RequestMapping(value = { "/admin/users" }, method = RequestMethod.GET)
    public String userList(Model model) {
    	User loggedUser= sessionData.getLoggedUser();
    	List<Credentials> allCredentials = this.credentialsService.getAllCredentials();
    	model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("allCredentials", allCredentials);
    	return "allUsers";
    }
    
    @RequestMapping(value = { "/admin/users/{username}/delete" }, method = RequestMethod.POST)
    public String removeUser(Model model, @PathVariable String username) {
    	for (Project p: credentialsService.getCredentials(username).getUser().getVisibleProjects()) {
    		p.deleteMember( credentialsService.getCredentials(username).getUser());
    	}
    	this.credentialsService.deleteCredentials(username);
    	return "redirect:/admin/users";
    }
    
    @RequestMapping(value = {"/users/updateUser"},method = RequestMethod.GET)
    public String goUpdateUser(Model model) {
    	model.addAttribute("credentialsForm", sessionData.getLoggedCredentials());
    	model.addAttribute("userForm", sessionData.getLoggedUser());
    	return "userUpdate";
    }
    @RequestMapping(value = {"users/updateUser"}, method = RequestMethod.POST)
    public String modifyUser(Model model, @Validated @ModelAttribute("userForm") User user,
                             BindingResult userBindingResult,
                             @Validated @ModelAttribute("credentialsForm") Credentials credentials,
                             BindingResult credentialsBindingResult) {

        Credentials credentialsToUpdate = sessionData.getLoggedCredentials();
        User userToUpdate = sessionData.getLoggedUser();
        credentialsValidator.validate(credentials, credentialsBindingResult);
        userValidator.validate(user, userBindingResult);
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
        	credentialsToUpdate.setUserName(credentials.getUserName());
        	credentialsToUpdate.setPassword(credentials.getPassword());
        	userToUpdate.setFirstName(user.getFirstName());
        	userToUpdate.setLastName(user.getLastName());
        	sessionData.setLoggedUser(userToUpdate);
        	sessionData.setLoggedCredentials(credentialsToUpdate);
        	this.credentialsService.saveCredentials(credentialsToUpdate);
        	return "success";
        }
        return "userUpdate";
    }
}
