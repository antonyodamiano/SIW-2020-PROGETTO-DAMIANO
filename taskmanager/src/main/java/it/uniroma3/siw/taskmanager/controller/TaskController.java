package it.uniroma3.siw.taskmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TaskService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Autowired
    SessionData sessionData;

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = {"/projects/{projectId}/addTask"}, method = RequestMethod.GET)
    public String goToAddTask(Model model, @PathVariable Long projectId){
    	User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
        Project project = projectService.getProject(projectId);
        model.addAttribute("project",project);
        model.addAttribute("task", new Task());
        return "addTask";
    }

    @RequestMapping(value = {"/projects/{projectId}/addTask"}, method = RequestMethod.POST)
	public String addTask(Model model, @PathVariable Long projectId, @Validated @ModelAttribute("task") Task task){
    	User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
        Project project = projectService.getProject(projectId);
        task.setCompleted(false);
        project.addTask(task);

        projectService.saveProject(project);
        return "redirect:/projects/"+projectId;
    }

    @RequestMapping(value = {"/projects/{projectId}/task/{taskId}"}, method = RequestMethod.GET)
    public String goToTask(Model model, @PathVariable Long projectId, @PathVariable Long taskId){
    	User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
        Project project = projectService.getProject(projectId);
        Task task = taskService.getTask(taskId);
        List<Tag> tags = task.getTags();
        model.addAttribute("project", project);
        model.addAttribute("task", task);
        model.addAttribute("commentForm", new Comment());
        model.addAttribute("tags",tags);
        return "task";
    }

    @RequestMapping(value = { "/projects/{projectId}/task/{taskId}/deleteTask" }, method = RequestMethod.POST)
    public String removeProject(Model model, @PathVariable Long projectId,  @PathVariable Long taskId) {
    	User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		for(Tag t: taskService.getTask(taskId).getTags())
			t.deleteTask(taskService.getTask(taskId));
    	this.taskService.deleteTask(this.taskService.getTask(taskId));
    	return "redirect:/projects/"+projectId;
    }
    
    @RequestMapping(value = { "/task/{taskId}/updateTaskForm"}, method = RequestMethod.GET)
    public String goupdateTask(Model model, 
    						@PathVariable Long taskId) {
    	User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
    	Task taskToUpdate = this.taskService.getTask(taskId);
    	model.addAttribute("task", taskToUpdate);
    	return "taskUpdateForm";
    }
    
    @RequestMapping(value = {"/task/{taskId}/updateTaskForm"}, method = RequestMethod.POST)
    public String updateTask (Model model, @PathVariable Long taskId, @Validated @ModelAttribute("task") Task task){
    	User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
        Task currentTask = taskService.getTask(taskId);
        currentTask.setName(task.getName());
        currentTask.setDescription(task.getDescription());
        taskService.saveTask(currentTask);
        return "success";
    }
    
    @RequestMapping(value = {"/projects/{projectId}/task/{taskId}/assignTask"}, method = RequestMethod.GET)
    public String goToAssignTask(Model model, @PathVariable Long taskId,@PathVariable Long projectId){
    	User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
        Task task = taskService.getTask(taskId);
        Project project = projectService.getProject(projectId);
        model.addAttribute("task",task);
        model.addAttribute("usersList",project.getMembers());
        return "assignTask";
    }

    
    @RequestMapping(value = {"/task/{taskId}/assignTask/{userId}"}, method = RequestMethod.POST)
    public String AssignTask(Model model, @PathVariable Long taskId,@PathVariable Long userId){
    	User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
        Task task = taskService.getTask(taskId);
        task.setUser(userService.getUser(userId));
        taskService.saveTask(task);
        return "success";
    }



}