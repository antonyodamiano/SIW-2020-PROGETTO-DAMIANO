package it.uniroma3.siw.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Tag {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 20)
	private String name;
	
	@Column(nullable = false, length = 20)
	private String color;
	
	@Column(nullable = false, length = 200)
	private String description;
	
	@ManyToMany
	@JoinTable(name = "TAG_TASK",
	joinColumns = @JoinColumn(name = "tag_ID"),
	inverseJoinColumns = @JoinColumn(name = "task_ID"))
	private List<Task> tasks;

	public Tag() {
		this.tasks = new ArrayList<>();
	}

	public Tag(String name, String color, String description) {
		this();
		this.name = name;
		this.color = color;
		this.description = description;
	}

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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}
	
	public boolean deleteTask(Task task) {
		return this.tasks.remove(task);
	}
	
	
	
	
	
	
	
	
	
}
