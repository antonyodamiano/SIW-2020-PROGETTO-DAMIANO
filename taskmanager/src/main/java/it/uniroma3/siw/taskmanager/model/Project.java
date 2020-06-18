package it.uniroma3.siw.taskmanager.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private String description;

    @ManyToOne()
    private User owner;
    
    @ManyToMany(mappedBy = "visibleProjects")
    private List<User> members;

    @OneToMany(fetch = FetchType.EAGER,        
            cascade = CascadeType.ALL)  
    @JoinColumn(name="project_id")
    private List<Task> tasks;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Tag> tags;
    
    public Project() {
        this.members = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public Project(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }
    
    public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
	
	public boolean deleteTag(Tag tag) {
		return this.tags.remove(tag);	}

	public void addMember(User user) {
        if (!this.members.contains(user))
            this.members.add(user);
    }
	
	public boolean deleteMember(User user) {
		return this.members.remove(user);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {

        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(name, project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

	public void addTask(Task task) {
		if(!this.tasks.contains(task))
			this.tasks.add(task);
	}
}
