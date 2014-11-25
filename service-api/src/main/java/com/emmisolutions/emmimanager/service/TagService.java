package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TagSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Tag Service API
 */
public interface TagService {

    /**
     * Returns a Page of Tags based on the search filter with default Page size
     *
     * @param tagSearchFilter the search filter
     * @return a page of tags
     */
    Page<Tag> list(TagSearchFilter tagSearchFilter);

    /**
     * Returns a Page of Tag based on the search filter
     *
     * @param pageable        the pagination spec
     * @param tagSearchFilter the search filter
     * @return page of tags
     */
    Page<Tag> list(Pageable pageable, TagSearchFilter tagSearchFilter);

    /**
     * Reloads a tag from persistence
     *
     * @param tag to reload
     * @return the tag or null if one can't be found
     */
    Tag reload(Tag tag);

    /**
     * Associate Tag to the Group and save
     *
     * @param tags  to be pushed to the group
     * @param group to be saved
     * @return saved tag list
     */
    List<Tag> saveAllTagsForGroup(List<Tag> tags, Group group);

}
