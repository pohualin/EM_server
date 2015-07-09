package com.emmisolutions.emmimanager.model.salesforce;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A salesforce case
 */
@XmlRootElement(name = "case-form")
public class CaseForm {

    private CaseType type;

    @XmlElement(name = "sections")
    @XmlElementWrapper(name = "sections")
    private List<Section> sections = new ArrayList<>();

    public CaseType getType() {
        return type;
    }

    public void setType(CaseType type) {
        this.type = type;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\":" + type +
                ", \"sections\":" + sections +
                '}';
    }
}
