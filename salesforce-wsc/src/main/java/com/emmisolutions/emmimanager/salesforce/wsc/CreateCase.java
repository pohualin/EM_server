package com.emmisolutions.emmimanager.salesforce.wsc;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.salesforce.*;
import com.emmisolutions.emmimanager.salesforce.service.SalesForceCreateCase;
import com.sforce.soap.enterprise.*;
import com.sforce.soap.enterprise.sobject.Case;
import com.sforce.ws.ConnectionException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.fromString;

/**
 * Responsible for creating a case in SF.
 */
@Repository
public class CreateCase implements SalesForceCreateCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCase.class);

    private static final String REFERENCE_SEARCH = "FIND {%s} RETURNING %s(Id, Name ORDER BY Name LIMIT %s)";

    private static final int DEFAULT_PAGE_SIZE = 20;

    @Resource
    ConnectionFactory salesForceConnection;

    @Override
    public void openCase(SalesForce salesForceAccount) {
        if (salesForceConnection.get() == null) {
            LOGGER.error("No Connection to SalesForce present, unable to create case");
            return;
        }

        Case newCase = new Case();

        newCase.setAccountId(salesForceAccount.getAccountNumber());
        newCase.setProgram__c("ff");
        newCase.setContactId("cont");
        newCase.setRecordTypeId("recordTypeId");
        newCase.setOwnerId("");
        newCase.setCreatedById("");
        newCase.setLastModifiedById("");
        newCase.setCSS_Specialist__c("");

        try {
            Map<CaseType, CaseForm> forms = createForms();

//                System.out.println("");
//                System.out.println("Search Program Records...");
//                QueryResult queryResult = salesForceConnection.get()
//                        .query("SELECT Id, Name from Program__c WHERE name like '%ANT%' LIMIT 25");
//                for (SObject searchRecord : queryResult.getRecords()) {
//                    System.out.println("Program Id: " + searchRecord.getId());
//                    System.out.println("Program Name: " + ((Program__c) searchRecord).getName());
//                }

            System.out.println("");
            System.out.println("ParsedForms: [");
            Iterator it = forms.values().iterator();
            while (it.hasNext()) {
                System.out.println(it.next());
                if (it.hasNext()) {
                    System.out.println(",");
                }
            }
            System.out.println("]");
        } catch (ConnectionException e) {
            salesForceConnection.reUp();
            openCase(salesForceAccount);
        }
    }

    private Map<CaseType, CaseForm> createForms() throws ConnectionException {
        String objectName = "Case";
        DescribeSObjectResult dsr = salesForceConnection.get().describeSObject(objectName);

        // save base level field information for Case objects in general
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : dsr.getFields()) {
            fieldMap.put(field.getName(), field);
        }

        // create a form for each record type
        Map<CaseType, CaseForm> caseTypeCaseFormMap = new HashMap<>();
        for (RecordTypeInfo recordTypeInfo : dsr.getRecordTypeInfos()) {
            if (!StringUtils.equalsIgnoreCase("master", recordTypeInfo.getName())) {

                // create a form for every non-master type
                CaseForm caseForm = new CaseForm();
                CaseType caseType = new CaseType();
                caseType.setId(recordTypeInfo.getRecordTypeId());
                caseType.setName(recordTypeInfo.getName());
                caseForm.setType(caseType);
                caseTypeCaseFormMap.put(caseType, caseForm);

                // populate the form from the record type specific layout
                populateCaseForm(caseForm, salesForceConnection.get().describeLayout(objectName, null,
                        new String[]{recordTypeInfo.getRecordTypeId()}), fieldMap);
            }
        }
        return caseTypeCaseFormMap;
    }

    private void populateCaseForm(CaseForm caseForm, DescribeLayoutResult dlr, Map<String, Field> fieldMap)
            throws ConnectionException {

        // some pick list values are different per record type
        Map<String, PicklistForRecordType> pickListOverrides = new HashMap<>();
        for (RecordTypeMapping recordTypeMapping : dlr.getRecordTypeMappings()) {
            for (PicklistForRecordType picklistForRecordType : recordTypeMapping.getPicklistsForRecordType()) {
                pickListOverrides.put(picklistForRecordType.getPicklistName(), picklistForRecordType);
            }
        }

        // Get all the layouts for the sObject
        for (DescribeLayout layout : dlr.getLayouts()) {
            // For each edit layout section, get its details.
            for (DescribeLayoutSection els : layout.getEditLayoutSections()) {
                Section section = new Section();
                section.setName(els.getHeading());
                caseForm.addSection(section);
                DescribeLayoutRow[] dlrList = els.getLayoutRows();
                for (DescribeLayoutRow lr : dlrList) {
                    for (DescribeLayoutItem li : lr.getLayoutItems()) {
                        if (li.getEditableForNew()) {
                            if (!ArrayUtils.isNotEmpty(li.getLayoutComponents())) {
                                Field globalField = fieldMap.get(li.getLayoutComponents()[0].getValue());
                                CaseField emmiCaseField;
                                switch (globalField.getType()) {
                                    case picklist:
                                        emmiCaseField = createPickListField(globalField, pickListOverrides);
                                        break;
                                    case multipicklist:
                                        PickListCaseField multiPickListField = createPickListField(globalField, pickListOverrides);
                                        multiPickListField.setMultiSelect(true);
                                        emmiCaseField = multiPickListField;
                                        break;
                                    case string:
                                        StringCaseField stringField = new StringCaseField();
                                        stringField.setMaxLength(globalField.getLength());
                                        emmiCaseField = stringField;
                                        break;
                                    case _boolean:
                                        emmiCaseField = new BooleanCaseField();
                                        break;
                                    case _double:
                                        emmiCaseField = new DoubleCaseField();
                                        break;
                                    case date:
                                        emmiCaseField = new DateCaseField();
                                        break;
                                    case reference:
                                        ReferenceCaseField referenceField = new ReferenceCaseField();
                                        referenceField.setReferenceType(globalField.getReferenceTo()[0]);
                                        emmiCaseField = referenceField;
                                        break;
                                    default:
                                        emmiCaseField = new StringCaseField();
                                        break;
                                }
                                emmiCaseField.setLabel(globalField.getLabel());
                                emmiCaseField.setName(globalField.getName());
                                emmiCaseField.setRequired(li.getRequired());
                                emmiCaseField.setType(fromString(globalField.getType().toString()));
                                section.addField(emmiCaseField);
                            }
                        }
                    }
                }
            }
        }
    }

    private PickListCaseField createPickListField(Field globalField, Map<String, PicklistForRecordType> pickListOverrides) {
        PickListCaseField pickListField = new PickListCaseField();
        pickListField.setLabel(globalField.getLabel());
        List<String> options = new ArrayList<>();
        List<String> values = new ArrayList<>();
        PicklistForRecordType override = pickListOverrides.get(globalField.getName());
        PicklistEntry[] entries;
        if (override != null) {
            entries = override.getPicklistValues();
        } else {
            entries = globalField.getPicklistValues();
        }
        for (PicklistEntry entry : entries) {
            if (entry.isActive()) {
                options.add(entry.getValue());
                if (entry.isDefaultValue()) {
                    values.add(entry.getValue());
                }
            }
        }
        pickListField.setOptions(options.toArray(new String[options.size()]));
        pickListField.setValues(values.toArray(new String[values.size()]));
        return pickListField;
    }

}
