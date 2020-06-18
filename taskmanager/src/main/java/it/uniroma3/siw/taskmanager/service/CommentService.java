package it.uniroma3.siw.taskmanager.service;


import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Transactional
    public Comment getComment(Long id){
        Optional<Comment> result = this.commentRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Comment saveComment(Comment comment){
        return this.commentRepository.save(comment);
    }

    @Transactional
    public List<Comment> getAllComments(){
        Iterable<Comment> iterable = this.commentRepository.findAll();
        List<Comment> comments = new ArrayList<Comment>();
        for (Comment c :
                iterable) {
            comments.add(c);
        }
        return comments;
    }

    @Transactional
    public void deleteComment(Comment comment){
        this.commentRepository.delete(comment);
    }


}