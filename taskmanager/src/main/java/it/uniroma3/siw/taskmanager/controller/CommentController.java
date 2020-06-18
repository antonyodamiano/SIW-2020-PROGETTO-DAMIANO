package it.uniroma3.siw.taskmanager.controller;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.service.CommentService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class CommentController {

    @Autowired
    SessionData sessionData;

    @Autowired
    CommentService commentService;

    @Autowired
    TaskService taskService;

    @Autowired
    ProjectService projectService;

    
    //Inserisci commento
    @RequestMapping(value = {"/projects/{projectId}/task/{taskId}/addComment"},method = RequestMethod.POST)
    public String addComment (Model model, @PathVariable Long taskId, 
    						@PathVariable Long projectId,	
    						@Validated @ModelAttribute("commentForm") Comment comment){
        Task task = taskService.getTask(taskId);
        comment.setWriter(sessionData.getLoggedUser());
        task.addComment(comment);

        comment.setTask(task);
        taskService.saveTask(task);
        
        return ("redirect:/projects/" + projectId + "/task/" + taskId);
    }
    //cancella commento
    @RequestMapping(value = {"/projects/{projectId}/task/{taskId}/deleteComment/{commentId}"},method = RequestMethod.POST)
    public String deleteComment (Model model, @PathVariable("taskId") Long taskId,
    							@PathVariable Long projectId,
    							@PathVariable("commentId") Long commentId){
        Task task = taskService.getTask(taskId);
        Comment comment = commentService.getComment(commentId);
        task.getComments().remove(comment);
        commentService.deleteComment(comment);
        return ("redirect:/projects/" + projectId + "/task/" + taskId);
    }
}