package it.uniroma3.siw.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TagService;
import it.uniroma3.siw.taskmanager.service.TaskService;

@Controller
public class TagController {
	
	@Autowired
	TagService tagService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	ProjectService projectService;
	
	

	@RequestMapping(value = {"/projects/{projectId}/addTag"}, method = RequestMethod.GET)
	public String toaddTagProject(Model model, @PathVariable Long projectId) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", projectService.getProject(projectId));
		model.addAttribute("tag", new Tag());
		return "addTag";
	}
	
	@RequestMapping(value = {"/projects/{projectId}/addTag"}, method = RequestMethod.POST)
	public String addTagProject(Model model, @PathVariable Long projectId, @Validated @ModelAttribute("tag") Tag tag){
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
        Project project = projectService.getProject(projectId);
        project.addTag(tag);
        projectService.saveProject(project);
        return "redirect:/projects/"+projectId;
    }
	
	@RequestMapping(value = {"/task/{taskId}/addTagTask"}, method = RequestMethod.GET)
	public  String toaddTagTask(Model model,@PathVariable Long taskId) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("task", taskService.getTask(taskId));
		model.addAttribute("tag",new Tag());
		return "addTag";
	}
	@RequestMapping(value = {"/task/{taskId}/addTagTask"}, method = RequestMethod.POST)
	public String addTagTask(Model model, @PathVariable Long taskId, @Validated @ModelAttribute("tag") Tag tag){
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
        Task task = taskService.getTask(taskId);
        task.addTag(tag);
        tag.addTask(task);
        tagService.saveTag(tag);
        taskService.saveTask(task);
        return "success";
    }
}
