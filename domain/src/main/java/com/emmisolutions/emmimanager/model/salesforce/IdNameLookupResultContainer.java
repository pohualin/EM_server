package com.emmisolutions.emmimanager.model.salesforce;

import org.springframework.util.CollectionUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * A generic id and name search result container
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IdNameLookupResultContainer {

    boolean complete;

    @XmlElement(name = "content")
    @XmlElementWrapper(name = "content")
    List<IdNameLookupResult> content;

    public IdNameLookupResultContainer(boolean complete, List<IdNameLookupResult> content) {
        this.complete = complete;
        this.content = content;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean hasResults() {
        return !CollectionUtils.isEmpty(content);
    }

    public List<IdNameLookupResult> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "{" +
                "\"complete\":\"" + complete + "\"" +
                ", \"content\":" + content +
                '}';
    }
}
