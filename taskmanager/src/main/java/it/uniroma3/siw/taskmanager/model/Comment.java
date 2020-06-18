package it.uniroma3.siw.taskmanager.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String description;
	
	@ManyToOne
	private Task task;
	
	@ManyToOne
	private User writer;
	
	@Column(updatable = false, nullable = false)
    private LocalDateTime creationTimestamp;

	public Comment() {
	}

	public Comment(String title, String description) {
		this();
		this.description = description;
	}
	
	@PrePersist
    protected void onPersist() {
        this.creationTimestamp = LocalDateTime.now();
    }

	public LocalDateTime getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(LocalDateTime creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}
	
}
