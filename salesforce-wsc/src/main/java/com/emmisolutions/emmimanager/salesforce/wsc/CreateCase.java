package com.emmisolutions.emmimanager.salesforce.wsc;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.salesforce.service.SalesForceCreateCase;
import com.sforce.soap.enterprise.*;
import com.sforce.soap.enterprise.sobject.Case;
import com.sforce.ws.ConnectionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Responsible for creating a case in SF.
 */
@Repository
public class CreateCase implements SalesForceCreateCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCase.class);

    @Resource
    SalesforceConnection salesForceConnection;

    @Override
    public void openCase(SalesForce salesForceAccount) {
        if (salesForceConnection.get() == null) {
            LOGGER.error("No Connection to SalesForce present, unable to create case");
            return;
        }

        Case newCase = new Case();
        newCase.setAccountId(salesForceAccount.getAccountNumber());
        newCase.setIsClosed(true);

        try {
            describeGlobalSample();

        } catch (ConnectionException e) {
            salesForceConnection.reUp();
            openCase(salesForceAccount);
        }
    }

    private void describeGlobalSample() throws ConnectionException {

        // describeGlobal() returns an array of object results that
        // includes the object names that are available to the logged-in user.

        DescribeGlobalResult dgr = salesForceConnection.get().describeGlobal();
        // Loop through the array echoing the object names to the console
        for (DescribeGlobalSObjectResult sObjectResult : dgr.getSobjects()) {

            if (StringUtils.equalsIgnoreCase("Case", sObjectResult.getName())) {
                DescribeSObjectResult dsr = salesForceConnection.get()
                        .describeSObject(sObjectResult.getName());

                describeObject(dsr);

                for (RecordTypeInfo recordTypeInfo : dsr.getRecordTypeInfos()) {
                    if (!StringUtils.equalsIgnoreCase("master", recordTypeInfo.getName())) {
                        System.out.println("RecordType:");
                        System.out.println("\tId: " + recordTypeInfo.getRecordTypeId());
                        System.out.println("\tName: " + recordTypeInfo.getName());
                        System.out.println("\tAvailable: " + recordTypeInfo.getAvailable());
                        System.out.println("\tDefaultRecordTypeMapping: " + recordTypeInfo.getDefaultRecordTypeMapping());
                        describeLayout(salesForceConnection.get().describeLayout(
                                sObjectResult.getName(), null, new String[]{recordTypeInfo.getRecordTypeId()}));
                    }
                }

            }
        }

    }

    private void describeLayout(DescribeLayoutResult dlr) throws ConnectionException {
// Get all the layouts for the sObject
        for (int i = 0; i < dlr.getLayouts().length; i++) {
            DescribeLayout layout = dlr.getLayouts()[i];
            DescribeLayoutSection[] editLayoutSectionList =
                    layout.getEditLayoutSections();
            System.out.println(" There are " +
                    editLayoutSectionList.length +
                    " edit layout sections");
            // Write the headings of the edit layout sections
            for (int x = 0; x < editLayoutSectionList.length; x++) {
                System.out.println(x +
                        " This edit layout section has a heading of " +
                        editLayoutSectionList[x].getHeading());
            }

            for (RecordTypeMapping recordTypeMapping : dlr.getRecordTypeMappings()) {
                System.out.println("Record type mapping:");
                System.out.println("\tName: " + recordTypeMapping.getName());
                System.out.println("\tRecord Type Id: " + recordTypeMapping.getRecordTypeId());
                System.out.println("\tAvailable: " + recordTypeMapping.getAvailable());
                for (PicklistForRecordType picklistForRecordType : recordTypeMapping.getPicklistsForRecordType()) {
                    System.out.println("\tPickList: " + picklistForRecordType.getPicklistName());
                    for (PicklistEntry picklistEntry : picklistForRecordType.getPicklistValues()) {
                        if (picklistEntry.isActive()) {
                            System.out.println("\t\t" + picklistEntry.getValue());
                        }
                    }
                }
            }
            // For each edit layout section, get its details.
            for (DescribeLayoutSection els : editLayoutSectionList) {
                System.out.println("Edit layout section heading: " +
                        els.getHeading());
                System.out.println("\tDisplay Heading: " + els.getUseHeading());
                DescribeLayoutRow[] dlrList = els.getLayoutRows();
                System.out.println("This edit layout section has " +
                        dlrList.length + " layout rows.");
                for (DescribeLayoutRow lr : dlrList) {
                    System.out.println(" This row has " +
                            lr.getNumItems() + " layout items.");
                    DescribeLayoutItem[] dliList = lr.getLayoutItems();
                    for (int n = 0; n < dliList.length; n++) {
                        DescribeLayoutItem li = dliList[n];
                        if (li.getEditableForNew()) {

                            if ((li.getLayoutComponents() != null) &&
                                    (li.getLayoutComponents().length > 0)) {
                                System.out.println("\tLayout item " + n +
                                        ", layout component: " +
                                        li.getLayoutComponents()[0].getValue());
                                System.out.println("\tRequired: " + li.getRequired());

                            } else {
                                System.out.println("\tLayout item " + n + ", no layout component");
                            }
                        }
                    }
                }
            }
        }

    }

    private void describeObject(DescribeSObjectResult dsr) throws ConnectionException {
        // First, get some object properties
        System.out.println("\n\nObject Name: " + dsr.getName());
        if (dsr.getCustom())
            System.out.println("Custom Object");
        if (dsr.getLabel() != null)
            System.out.println("Label: " + dsr.getLabel());
        // Get the permissions on the object
        if (dsr.getCreateable())
            System.out.println("Createable");
        if (dsr.getDeletable())
            System.out.println("Deleteable");
        if (dsr.getQueryable())
            System.out.println("Queryable");
        if (dsr.getReplicateable())
            System.out.println("Replicateable");
        if (dsr.getRetrieveable())
            System.out.println("Retrieveable");
        if (dsr.getSearchable())
            System.out.println("Searchable");
        if (dsr.getUndeletable())
            System.out.println("Undeleteable");
        if (dsr.getUpdateable())
            System.out.println("Updateable");
        System.out.println("Number of fields: " + dsr.getFields().length);

        // Now, retrieve metadata for each field
        for (Field field : dsr.getFields()) {// Get the field
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
    }

}
