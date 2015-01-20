package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
/**
 * Specifications for TeamTags
 */
@Component
public class TeamTagSpecifications {

    /**
     * match on tagId within the provided TeamTagSearchFilter
     *
     * @param searchFilter to search
     * @return the specification as a filter predicate
     */
    public Specification<TeamTag> getOrs(final TeamTagSearchFilter searchFilter) {
        return new Specification<TeamTag>() {
            @Override
            public Predicate toPredicate(Root<TeamTag> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && !searchFilter.getTagSet().isEmpty()) {
                    Set<Tag> tagSet = searchFilter.getTagSet();
                    HashMap<Group, Set<Tag>> groupedTags = new HashMap<>();
                    Set<Tag> savedSet;
                    List<Predicate> tagPredicate = new ArrayList<>();
                    for (Tag tag : tagSet) {
                        savedSet = groupedTags.get(tag.getGroup());
                        if (savedSet == null) {
                            Set<Tag> newSet = new HashSet<>();
                            newSet.add(tag);
                            groupedTags.put(tag.getGroup(), newSet);
                        } else {
                            savedSet.add(tag);
                            groupedTags.put(tag.getGroup(), savedSet);
                        }
                        tagPredicate.add(cb.equal(root.get(TeamTag_.tag), tag));
                    }

                    // want to know Teams present in all of the groups
                    Map<Team, Set<Group>> teamSetMap = new HashMap<>();
                    for (Tag tag : tagSet) {
                        for (TeamTag teamTag : tag.getTeamTags()) {
                            Set<Group> aTeamsGroups = teamSetMap.get(teamTag.getTeam());
                            if (aTeamsGroups == null) {
                                aTeamsGroups = new HashSet<>();
                                teamSetMap.put(teamTag.getTeam(), aTeamsGroups);
                            }
                            aTeamsGroups.add(teamTag.getTag().getGroup());
                        }
                    }
                    Set<Team> goodTeams = new HashSet<>();
                    for (Map.Entry<Team, Set<Group>> teamSetEntry : teamSetMap.entrySet()) {
                        boolean teamGood = true;
                        for (Group group : groupedTags.keySet()) {
                            if (!teamSetEntry.getValue().contains(group)) {
                                teamGood = false;
                            }
                        }
                        if (teamGood) {
                            goodTeams.add(teamSetEntry.getKey());
                        }
                    }
                    if(goodTeams.size() ==0){
                        //if there are no teams return an empty search
                        return cb.equal(root.get(TeamTag_.tag), 0);
                    }

                    return cb.and(cb.or(tagPredicate.toArray(new Predicate[tagPredicate.size()])),
                            root.get(TeamTag_.team).in(goodTeams));
                } else if (searchFilter != null) {
                    return cb.equal(root.join(TeamTag_.team).join(Team_.client).get(Client_.id), searchFilter.getClientId());
                } else {
                    return null;
                }
            }
        };
    }

    /**
     * Ensures that the TeamTag team is in a particular status
     *
     * @param searchFilter used to find the status
     * @return the specification as a filter predicate
     */
    public Specification<TeamTag> isInStatus(final TeamTagSearchFilter searchFilter) {
        return new Specification<TeamTag>() {
            @Override
            public Predicate toPredicate(Root<TeamTag> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getStatus() !=null && !TeamTagSearchFilter.StatusFilter.ALL.equals(searchFilter.getStatus())) {
                    return cb.equal(root.get(TeamTag_.team).get(Team_.active), TeamSearchFilter.StatusFilter.ACTIVE_ONLY.toString().equals(searchFilter.getStatus().toString()));
                }
                return null;
            }
        };
    }
}
