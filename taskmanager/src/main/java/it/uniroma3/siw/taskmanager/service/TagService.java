package it.uniroma3.siw.taskmanager.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	TagRepository tagRepository;
	
	@Transactional
	public Tag getTag(long id) {
        Optional<Tag> result = this.tagRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Tag saveTag(Tag Tag) {
        return this.tagRepository.save(Tag);
    }

    @Transactional
    public void deleteTag(Tag Tag) {
        this.tagRepository.delete(Tag);
    }
}
