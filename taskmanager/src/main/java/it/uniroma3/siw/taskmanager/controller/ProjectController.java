package it.uniroma3.siw.taskmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.ProjectValidator;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProjectValidator projectValidator;
	
	@Autowired
	SessionData sessionData;
	//visualizzaare i miei progetti
	@RequestMapping(value = {"/projects" }, method = RequestMethod.GET)
	public String myProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		List<Project> projectsList = projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "myProjects";
	}
	//visualizzare un progetto
	@RequestMapping(value = {"/projects/{projectId}"}, method = RequestMethod.GET)
	public String project(Model model,@PathVariable Long projectId) {
		Project project = projectService.getProject(projectId);
		if (project == null)
			return "redirect:/projects";
		List<User> members = userService.getMembers(project);
		User loggedUser = sessionData.getLoggedUser();
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		model.addAttribute("tasks", project.getTasks());
		
		return "project";
	}
	//creare un progetto GET
	@RequestMapping(value = {"/projects/add"}, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser= sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectForm", new Project());
		return "addProject";
	}
	//creare un progetto POST
	@RequestMapping(value = {"/projects/add"}, method = RequestMethod.POST)
	public String createProject(@Validated @ModelAttribute("projectForm") Project project,
								BindingResult projectBindingResult,
								Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		projectValidator.validate(project, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			this.projectService.saveProject(project);
			return "redirect:/projects/" + project.getId();
		}
		
		return "addProject";
	}
	//condividere un progetto get
	@RequestMapping(value = {"/projects/{projectId}/shareProject"}, method = RequestMethod.GET)
	public String shareProjectForm(Model model,@PathVariable Long projectId) {
		Project project = projectService.getProject(projectId);
		List<User> userAlreadyMembers = project.getMembers();
		User loggedUser = sessionData.getLoggedUser();
		userAlreadyMembers.add(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		sessionData.setProject(project);
		model.addAttribute("usersList", this.userService.removeUserFromAllUsers(userAlreadyMembers));
		return "shareProject";
	}
	// condividere un progetto post
	@RequestMapping(value = { "/shareProject/{userId}"}, method = RequestMethod.POST)
	public String shareProjectWithUser(Model model,@PathVariable Long userId) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		User userToAdd = this.userService.getUser(userId);
		Project projectToAdd = sessionData.getProject();
		projectToAdd.addMember(userToAdd);
		userToAdd.getVisibleProjects().add(projectToAdd);
		this.projectService.saveProject(projectToAdd);
		this.userService.saveUser(userToAdd);
		return "success";
	}
	//visualizzare i progetti condivisi con me
	@RequestMapping(value = {"/visibleProject"}, method = RequestMethod.GET)
	public String visibleProject(Model model) {
		User loggedUser = this.userService.getUser(sessionData.getLoggedUser().getId());
		List<Project> visibleProject = loggedUser.getVisibleProjects();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("visibleProject", visibleProject);
		return "visibleProject";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/delete" }, method = RequestMethod.POST)
    public String removeProject(Model model, @PathVariable Long projectId) {
		for (User u: userService.getAllUsers()) {
			u.deleteProject(this.projectService.getProject(projectId));
		}
    	this.projectService.deleteProject(this.projectService.getProject(projectId));
    	return "redirect:/projects";
    }
	
	@RequestMapping(value = { "/projects/{projectId}/updateForm" }, method = RequestMethod.GET)
    public String updateProjectForm(Model model, @PathVariable Long projectId) {
    	User loggedUser = sessionData.getLoggedUser();
    	model.addAttribute("project", this.projectService.getProject(projectId));
    	model.addAttribute("loggedUser", loggedUser);
    	return "updateForm";
    }
	
	
	@RequestMapping(value = {"/projects/{projectId}/updateForm"}, method = RequestMethod.POST)
	public String updateProject(@Validated @ModelAttribute("project") Project project,
								BindingResult projectBindingResult,@PathVariable Long projectId,
								Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		Project projectUp = this.projectService.getProject(projectId);
		
		projectValidator.validate(project, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			projectUp.setDescription(project.getDescription());
			projectUp.setName(project.getName());
			this.projectService.saveProject(projectUp);
			return "redirect:/projects/" + projectId;
		}
		return "updateForm";
	}
}
