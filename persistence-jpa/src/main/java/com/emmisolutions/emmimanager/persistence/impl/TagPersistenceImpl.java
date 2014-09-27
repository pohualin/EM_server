package com.emmisolutions.emmimanager.persistence.impl;

import static com.emmisolutions.emmimanager.persistence.impl.specification.TagSpecifications.byGroupId;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;
import com.emmisolutions.emmimanager.persistence.TagPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TagRepository;

@Repository
public class TagPersistenceImpl implements TagPersistence{
	
	@Resource
	TagRepository tagRepository;
	
	@Override
	public Page<Tag> listTagsByGroupId(Pageable page, TagSearchFilter searchFilter){
		if (page == null) {
			// default pagination request if none
			page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
		}
		return tagRepository.findAll(where(byGroupId(searchFilter)), page);
	}

	@Override
	public Tag save(Tag tag){
		return tagRepository.save(tag);
	}

	@Override
	public Tag reload(Tag tag){
		return tagRepository.findOne(tag.getId());
	}

	@Override
	public List<Tag> createAll(List<Tag> createTagsList) {
		return tagRepository.save(createTagsList);
	}

}