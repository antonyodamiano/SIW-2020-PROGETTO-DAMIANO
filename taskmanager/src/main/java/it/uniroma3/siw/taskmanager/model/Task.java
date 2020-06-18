package it.uniroma3.siw.taskmanager.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private boolean completed;
       
    @ManyToMany(mappedBy = "tasks")
    private List<Tag> tags;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<Comment> comments;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(updatable = false, nullable = false)
    private LocalDateTime creationTimestamp;

    @Column(nullable = false)
    private LocalDateTime lastUpdateTimestamp;

    public Task() {
    	this.comments = new ArrayList<>();
    	this.tags = new ArrayList<>();
    }

    public Task(String name,
                String description,
                boolean completed) {
        this.name = name;
        this.description = description;
        this.completed = completed;
    }    
    
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

    @PrePersist
    protected void onPersist() {
        this.creationTimestamp = LocalDateTime.now();
        this.lastUpdateTimestamp = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdateTimestamp = LocalDateTime.now();
    }
    
    public void addComment(Comment comment) {
    	this.comments.add(comment);
	}
    
    public void addTag(Tag tag) {
    	this.tags.add(tag);
    }
    
    public boolean deleteTag(Tag tag) {
    	return this.tags.remove(tag);
    }


    // GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public LocalDateTime getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(LocalDateTime lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return completed == task.completed &&
               Objects.equals(name, task.name) &&
                Objects.equals(creationTimestamp, task.creationTimestamp) &&
                Objects.equals(lastUpdateTimestamp, task.lastUpdateTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, completed, creationTimestamp, lastUpdateTimestamp);
    }
}
