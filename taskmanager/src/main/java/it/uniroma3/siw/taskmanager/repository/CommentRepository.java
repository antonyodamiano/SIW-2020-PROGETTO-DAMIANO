package it.uniroma3.siw.taskmanager.repository;

import it.uniroma3.siw.taskmanager.model.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment,Long> {

}