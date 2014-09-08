package com.emmisolutions.emmimanager.service.spring;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;
import com.emmisolutions.emmimanager.persistence.TagPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.service.TagService;

/**
 * 
 * Implementation of Tag service
 *
 */
@Service
public class TagServiceImpl implements TagService {

	@Resource
	TagPersistence tagPersistence;

	@Resource
	ClientService clientService;

	@Resource
	GroupService groupService;

	@Override
	public Page<Tag> list(TagSearchFilter searchFilter) {
		return tagPersistence.listTagsByGroupId(null, searchFilter);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Tag> list(Pageable pageable, TagSearchFilter searchFilter) {
		return tagPersistence.listTagsByGroupId(pageable, searchFilter);
	}

	@Override
	@Transactional(readOnly = true)
	public Tag reload(Tag tag) {
		if (tag == null || tag.getId() == null) {
			return null;
		}
		return tagPersistence.reload(tag);
	}

	@Override
	@Transactional
	public List<Tag> saveAllTagsForGroup(List<Tag> tags, Group group) {
		for (Tag tag : tags) {
			tag.setGroup(group);
		}
		return tagPersistence.createAll(tags);
	}

}
