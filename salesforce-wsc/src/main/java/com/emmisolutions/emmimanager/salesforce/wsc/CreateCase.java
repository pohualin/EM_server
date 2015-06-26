package com.emmisolutions.emmimanager.salesforce.wsc;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.salesforce.*;
import com.emmisolutions.emmimanager.salesforce.service.SalesForceCreateCase;
import com.sforce.soap.enterprise.*;
import com.sforce.soap.enterprise.Field;
import com.sforce.soap.enterprise.FieldType;
import com.sforce.soap.enterprise.sobject.*;
import com.sforce.ws.ConnectionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.STRING;
import static com.emmisolutions.emmimanager.salesforce.wsc.ConnectionFactory.escape;

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
            describeGlobalSample();
            try {
                System.out.println("");
                System.out.println("Search User Records...");
                SearchResult searchResult = salesForceConnection.get()
                        .search(String.format(REFERENCE_SEARCH, escape("david"),
                                "User", DEFAULT_PAGE_SIZE + 1));
                for (SearchRecord searchRecord : searchResult.getSearchRecords()) {
                    System.out.println("Id: " + searchRecord.getRecord().getId());
                    System.out.println("Name: " + ((User) searchRecord.getRecord()).getName());
                }

                System.out.println("");
                System.out.println("Search Program Records...");
                QueryResult queryResult = salesForceConnection.get()
                        .query("SELECT Id, Name from Program__c WHERE name like '%ANT%' LIMIT 25");
                for (SObject searchRecord : queryResult.getRecords()) {
                    System.out.println("Program Id: " + searchRecord.getId());
                    System.out.println("Program Name: " + ((Program__c) searchRecord).getName());
                }

                System.out.println("");
                System.out.println("Search Contact Records...");
                searchResult = salesForceConnection.get()
                        .search(String.format(REFERENCE_SEARCH, escape("david"),
                                "Contact", DEFAULT_PAGE_SIZE + 1));
                for (SearchRecord searchRecord : searchResult.getSearchRecords()) {
                    System.out.println("Id: " + searchRecord.getRecord().getId());
                    System.out.println("Name: " + ((Contact) searchRecord.getRecord()).getName());
                }
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        } catch (ConnectionException e) {
            salesForceConnection.reUp();
            openCase(salesForceAccount);
        }
    }

    private Map<CaseType, CaseForm> describeGlobalSample() throws ConnectionException {
        DescribeSObjectResult dsr = salesForceConnection.get().describeSObject("Case");

        Map<String, Field> fieldMap = describeObject(dsr);

        // refine the field map by type
        Map<CaseType, CaseForm> caseTypeCaseFormMap = new HashMap<>();
        for (RecordTypeInfo recordTypeInfo : dsr.getRecordTypeInfos()) {
            if (!StringUtils.equalsIgnoreCase("master", recordTypeInfo.getName())) {
                System.out.println("RecordType:");
                System.out.println("\tId: " + recordTypeInfo.getRecordTypeId());
                System.out.println("\tName: " + recordTypeInfo.getName());
                System.out.println("\tAvailable: " + recordTypeInfo.getAvailable());
                System.out.println("\tDefaultRecordTypeMapping: " + recordTypeInfo.getDefaultRecordTypeMapping());
                CaseForm caseForm = new CaseForm();
                CaseType caseType = new CaseType();
                caseType.setId(recordTypeInfo.getRecordTypeId());
                caseType.setName(recordTypeInfo.getName());
                caseForm.setType(caseType);
                caseTypeCaseFormMap.put(caseType, caseForm);
                describeLayout(caseForm, salesForceConnection.get().describeLayout("Case", null,
                        new String[]{recordTypeInfo.getRecordTypeId()}), fieldMap);

            }
        }
        return caseTypeCaseFormMap;
    }

    private void describeLayout(CaseForm caseForm, DescribeLayoutResult dlr, Map<String, Field> fieldMap)
            throws ConnectionException {

        // some pick list values are different per record type
        Map<String, PicklistForRecordType> pickListOverrides = new HashMap<>();
        for (RecordTypeMapping recordTypeMapping : dlr.getRecordTypeMappings()) {
            System.out.println("Record type mapping:");
            System.out.println("\tName: " + recordTypeMapping.getName());
            System.out.println("\tRecord Type Id: " + recordTypeMapping.getRecordTypeId());
            System.out.println("\tAvailable: " + recordTypeMapping.getAvailable());
            for (PicklistForRecordType picklistForRecordType : recordTypeMapping.getPicklistsForRecordType()) {
                pickListOverrides.put(picklistForRecordType.getPicklistName(), picklistForRecordType);
                System.out.println("\tPickList: " + picklistForRecordType.getPicklistName());
                for (PicklistEntry picklistEntry : picklistForRecordType.getPicklistValues()) {
                    if (picklistEntry.isActive()) {
                        System.out.println("\t\t" + picklistEntry.getValue());
                    }
                }
            }
        }

        // Get all the layouts for the sObject
        for (int i = 0; i < dlr.getLayouts().length; i++) {
            DescribeLayout layout = dlr.getLayouts()[i];
            DescribeLayoutSection[] editLayoutSectionList =
                    layout.getEditLayoutSections();

            // For each edit layout section, get its details.
            for (DescribeLayoutSection els : editLayoutSectionList) {
                Section section = new Section();
                section.setName(els.getHeading());
                System.out.println("Edit layout section heading: " + els.getHeading());
                System.out.println("\tDisplay Heading: " + els.getUseHeading());
                DescribeLayoutRow[] dlrList = els.getLayoutRows();
                System.out.println("This edit layout section has " + dlrList.length + " layout rows.");
                for (DescribeLayoutRow lr : dlrList) {
                    System.out.println(" This row has " + lr.getNumItems() + " layout items.");
                    for (DescribeLayoutItem li : lr.getLayoutItems()) {
                        if (li.getEditableForNew()) {
                            if ((li.getLayoutComponents() != null) && (li.getLayoutComponents().length > 0)) {
                                String componentName = li.getLayoutComponents()[0].getValue();
                                System.out.println("\tlayout component: " + componentName);
                                System.out.println("\tRequired: " + li.getRequired());
                                Field globalField = fieldMap.get(componentName);
                                com.emmisolutions.emmimanager.model.salesforce.Field emmiField = null;
                                switch (globalField.getType()) {

                                    case picklist:
                                        PickListField pickListField = new PickListField();
                                        pickListField.setLabel(globalField.getLabel());
                                        List<String> options = new ArrayList<>();
                                        List<String> values = new ArrayList<>();
                                        PicklistForRecordType override = pickListOverrides.get(componentName);
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
                                        emmiField = pickListField;
                                        break;
                                    case multipicklist:
                                        PickListField multiPickListField = new PickListField();
                                        multiPickListField.setMultiSelect(true);
                                        emmiField = multiPickListField;
                                        break;
                                    case string:
                                        StringField stringField = new StringField();
                                        stringField.setType(STRING);
                                        stringField.setMaxLength(globalField.getLength());
                                        emmiField = stringField;
                                        break;
                                }
                                if (emmiField != null) {
                                    emmiField.setLabel(globalField.getLabel());
                                    emmiField.setRequired(li.getRequired());
                                    section.addField(emmiField);
                                }
                            } else {
                                System.out.println("\tLayout item, no layout component");
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<String, Field> describeObject(DescribeSObjectResult dsr) throws ConnectionException {
        Map<String, Field> fieldMap = new HashMap<>();

        // Now, retrieve metadata for each field
        for (Field field : dsr.getFields()) {// Get the field
            fieldMap.put(field.getName(), field);

            // Write some field properties
            System.out.println("Field name: " + field.getName());
            System.out.println("\tField Label: " + field.getLabel());

            // This next property indicates that this
            // field is searched when using
            // the name search group in SOSL
            if (field.getNameField())
                System.out.println("\tThis is a name field.");
            if (field.getRestrictedPicklist())
                System.out.println("This is a RESTRICTED picklist field.");
            System.out.println("\tType is: " + field.getType());
            if (field.getLength() > 0)
                System.out.println("\tLength: " + field.getLength());
            if (field.getScale() > 0)
                System.out.println("\tScale: " + field.getScale());
            if (field.getPrecision() > 0)
                System.out.println("\tPrecision: " + field.getPrecision());
            if (field.getDigits() > 0)
                System.out.println("\tDigits: " + field.getDigits());
            if (field.getCustom())
                System.out.println("\tThis is a custom field.");

            // Write the permissions of this field
            if (field.isNillable())
                System.out.println("\tCan be nulled.");
            if (field.getCreateable())
                System.out.println("\tCreateable");
            if (field.getFilterable())
                System.out.println("\tFilterable");
            if (field.getUpdateable())
                System.out.println("\tUpdateable");

            // If this is a picklist field, show the picklist values
            if (field.getType().equals(FieldType.picklist)) {
                System.out.println("\t\tPicklist values: ");
                for (PicklistEntry picklistEntry : field.getPicklistValues()) {
                    System.out.println("\t\tValue: "
                            + picklistEntry.getValue());
                }
            }

            // If this is a foreign key field (reference),
            // show the values
            if (field.getType().equals(FieldType.reference)) {
                System.out.println("\tCan reference these objects:");
                for (String referenceTo : field.getReferenceTo()) {
                    System.out.println("\t\t" + referenceTo);
                }
            }

            System.out.println("");

        }
        return fieldMap;
    }

}
