package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.model.Client_;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the specification class that allows for filtering of Client objects.
 */
public class ClientSpecifications {

    private ClientSpecifications() {
    }

    /**
     * EM-12: Client types multiple words, separated only by spaces (no other delimiters needed),
     * all client names containing all of those words (in any order) should show up.
     *
     * The difficult part of this is the *all* requirement. Due to the fact that the number of
     * permutations starts to explode at 5!, this implementation limits the permutation generator
     * at 4! (24 permutations). If there are more than 4 search terms coming into the filter,
     * a sliding set of four terms will be used. E.g. User searches for '1 2 3 4 5 6'. The
     * terms which will be searched will be:
     *      all_permutations('1 2 3 4') + all_permutations('2 3 4 5') + all_permutations('3 4 5 6')
     *
     * So a sliding group of four terms will be used; all permutations within each group.
     *
     * @param searchFilter to be found
     * @return the specification as a filter predicate
     */
    public static Specification<Client> hasNames(final ClientSearchFilter searchFilter) {
        return new Specification<Client>() {
            @Override
            public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (searchFilter != null && !CollectionUtils.isEmpty(searchFilter.getNames())) {
                    for (String name : searchFilter.getNames()) {
                        for (String permutation : findPermutations(name)) {
                            predicates.add(cb.like(root.get(Client_.normalizedName), permutation));
                        }
                    }
                    return cb.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                return null;
            }
        };
    }

    private static List<String> findPermutations(String searchTerm) {
        List<String> permutations = new ArrayList<>();
        List<List<String>> batches = new ArrayList<>();
        List<String> searchTerms = new ArrayList<>();
        Collections.addAll(searchTerms, StringUtils.split(normalizeName(searchTerm), " "));

        // make sure we only do permutations of up to 4 terms at a time
        List<String> aSubList = fourAtATime(searchTerms);
        while (aSubList != null) {
            batches.add(new ArrayList<>(aSubList));
            aSubList = fourAtATime(searchTerms);
        }
        batches.add(searchTerms);
        for (List<String> batch : batches) {
            permute(batch, 0, permutations);
        }
        return permutations;
    }

    private static List<String> fourAtATime(List<String> truncateMe) {
        List<String> ret = null;
        if (truncateMe.size() > 4) {
            ret = new ArrayList<>(truncateMe.subList(0, 4));
            truncateMe.remove(0);
        }
        return ret;
    }

    /**
     * Creates all of the permutations of a List of Strings and then populates
     * the output List with a like delimited (%) list of the permutations
     *
     * @param toPermute the starting list
     * @param k         the starting point of the permutations
     * @param output    populated with like friendly strings
     */
    private static void permute(List<String> toPermute, int k, List<String> output) {
        for (int i = k; i < toPermute.size(); i++) {
            Collections.swap(toPermute, i, k);
            permute(toPermute, k + 1, output);
            Collections.swap(toPermute, k, i);
        }
        if (k == toPermute.size() - 1) {
            output.add("%" + StringUtils.join(toPermute, "%") + "%");
        }
    }

    private static String normalizeName(String name) {
        String normalizedName = StringUtils.trimToEmpty(StringUtils.lowerCase(name));
        if (StringUtils.isNotBlank(normalizedName)) {
            // do regex
            normalizedName = normalizedName.replaceAll("[^a-z0-9 ]*", "");
        }
        return normalizedName;
    }

    /**
     * Ensures that the Client is in a particular status
     *
     * @param searchFilter used to find the status
     * @return the specification as a filter predicate
     */
    public static Specification<Client> isInStatus(final ClientSearchFilter searchFilter) {
        return new Specification<Client>() {
            @Override
            public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && ClientSearchFilter.StatusFilter.ALL != searchFilter.getStatus()) {
                    return cb.equal(root.get(Client_.active), searchFilter.getStatus().equals(ClientSearchFilter.StatusFilter.ACTIVE_ONLY));
                }
                return null;
            }
        };
    }

}
