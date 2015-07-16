package com.emmisolutions.emmimanager.salesforce.wsc;

import com.emmisolutions.emmimanager.model.salesforce.*;
import com.emmisolutions.emmimanager.model.salesforce.CaseType;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.fault.InvalidFieldFault;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.PICK_LIST;
import static com.emmisolutions.emmimanager.model.salesforce.FieldType.fromSalesForceTypeString;
import static com.emmisolutions.emmimanager.salesforce.wsc.ConnectionFactory.escape;

/**
 * Responsible for creating a case in SF.
 */
@Repository
public class CaseManagerImpl implements CaseManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaseManagerImpl.class);

    private static final String REFERENCE_SEARCH = "FIND {%s} RETURNING %s(Id, Name ORDER BY Name LIMIT %s)";

    @Resource
    ConnectionFactory salesForceConnection;

    Map<CaseType, CaseForm> forms;

    @Override
    public List<CaseType> caseTypes() {
        init();
        List<CaseType> ret = forms != null ? new ArrayList<>(forms.keySet()) : new ArrayList<CaseType>();
        Collections.sort(ret, new Comparator<CaseType>() {
            @Override
            public int compare(CaseType o1, CaseType o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return ret;
    }

    @Override
    public CaseForm newCase(CaseType caseType) {
        init();
        if (caseType == null || forms == null) {
            return null;
        }
        return forms.get(caseType);
    }

    @Override
    public CaseSaveResult saveCase(CaseForm caseForm, UserAdmin user) {
        return saveCase(caseForm, user, true);
    }

    /**
     * Saves the form to salesforce
     *
     * @param caseForm       to save
     * @param user           requesting the save
     * @param reUpConnection to try to reconnect on any failure
     * @return the saved result
     */
    private CaseSaveResult saveCase(CaseForm caseForm, UserAdmin user, boolean reUpConnection) {
        CaseSaveResult ret = new CaseSaveResult();
        if (salesForceConnection.get() == null) {
            LOGGER.error("No Connection to SalesForce present, unable to create case");
            return ret.addErrorMessage("No Connection to SalesForce present, unable to create case");
        }
        if (caseForm != null && caseForm.getType() != null) {
            try {
                SObject newCase = new SObject("Case");
                newCase.setSObjectField("RecordTypeId", caseForm.getType().getId());

                // set the owner id of the case to the passed user's email address/login
                if (user != null && StringUtils.isNotBlank(user.getLogin())) {
                    SearchResult searchResult = salesForceConnection.get()
                            .search(String.format(REFERENCE_SEARCH, escape(user.getLogin()), "User", 1));
                    if (ArrayUtils.isNotEmpty(searchResult.getSearchRecords())) {
                        newCase.setSObjectField("OwnerId",
                                searchResult.getSearchRecords()[0].getRecord().getSObjectField("Id"));
                    }
                }

                // populate the new case with the passed caseForm
                populateNewCaseFromForm(newCase, caseForm);

                SaveResult[] saveResults =
                        salesForceConnection.get().create(new SObject[]{newCase});
                LOGGER.debug("Attempting to save {}", caseForm);
                for (SaveResult saveResult : saveResults) {
                    if (saveResult.isSuccess()) {
                        LOGGER.debug("{} ({}): saved successfully", caseForm.getType().getName(), saveResult.getId());
                        ret.setSuccess(true);
                        ret.setId(saveResult.getId());
                    } else {
                        for (Error error : saveResult.getErrors()) {
                            LOGGER.debug("error ({}): {}", error.getStatusCode(), error.getMessage());
                            ret.addErrorMessage(error.getMessage());
                        }
                    }
                }
            } catch (InvalidFieldFault apiFault) {
                LOGGER.debug("API Call Error", apiFault);
                ret.addErrorMessage(apiFault.getExceptionMessage());
            } catch (ConnectionException e) {
                if (reUpConnection) {
                    init();
                    saveCase(caseForm, user, false);
                } else {
                    LOGGER.error("Unable to save case due to SalesForce Error", e);
                    ret.addErrorMessage("Unable to save case due to unknown SalesForce error. Please try again later");
                }
            }
        }
        return ret;
    }

    @PostConstruct
    private synchronized void init() {
        if (forms == null) {
            reUp(true);
        }
    }

    /**
     * Do not call this method directly.. rather use the init() method.
     *
     * @param retry to try to re-establish SalesForce connection
     */
    private void reUp(boolean retry) {
        try {
            forms = createForms();
        } catch (ConnectionException e) {
            if (retry) {
                salesForceConnection.reUp();
                reUp(false);
            } else {
                LOGGER.error("Could not fetch case forms due to SalesForce connection error...");
            }
        }
    }

    /**
     * This method populates the newCase object from the passed caseForm by
     * looping over the sections and then fields within the sections
     *
     * @param newCase  to populate
     * @param caseForm from here
     */
    private void populateNewCaseFromForm(SObject newCase, CaseForm caseForm) {
        for (Section section : caseForm.getSections()) {
            for (CaseField caseField : section.getCaseFields()) {
                sObjectFromCaseField(newCase, caseField);
            }
        }
    }

    /**
     * Actually writes the SalesForce property for the selected value in the caseField.
     *
     * @param newCase   to be written to
     * @param caseField to find the options
     */
    private void sObjectFromCaseField(SObject newCase, CaseField caseField) {
        String sObjectValue = null;
        if (caseField.getType() != null) {
            switch (caseField.getType()) {
                case PICK_LIST:
                    PickListCaseField pickListCaseField = (PickListCaseField) caseField;
                    sObjectValue = pickListCaseField.getValue().getValue();
                    sObjectFromDependentPickListCaseFields(newCase, pickListCaseField);
                    break;
                case MULTI_PICK_LIST:
                    MultiPickListCaseField multiPickListCaseField = (MultiPickListCaseField) caseField;
                    StringBuilder sb = new StringBuilder();
                    if (!CollectionUtils.isEmpty(multiPickListCaseField.getValues())) {
                        for (PickListValue s : multiPickListCaseField.getValues()) {
                            sb.append(s.getValue()).append(";");
                        }
                        if (sb.length() > 0) {
                            sb.deleteCharAt(sb.length() - 1);
                        }
                        sObjectValue = sb.toString();
                    }
                    sObjectFromDependentPickListCaseFields(newCase, multiPickListCaseField);
                    break;
                case BOOLEAN:
                    BooleanCaseField booleanCaseField = (BooleanCaseField) caseField;
                    boolean choice = Boolean.TRUE.equals(booleanCaseField.getValue());
                    newCase.setSObjectField(caseField.getName(), choice);
                    if (choice) {
                        for (PickListCaseField trueDependentPickList : booleanCaseField.getRequiredPicklistsWhenTrue()) {
                            sObjectFromCaseField(newCase, trueDependentPickList);
                        }
                    } else {
                        for (PickListCaseField falseDependentPickList : booleanCaseField.getRequiredPicklistsWhenFalse()) {
                            sObjectFromCaseField(newCase, falseDependentPickList);
                        }
                    }
                    break;
                case DOUBLE:
                    DoubleCaseField doubleCaseField = (DoubleCaseField) caseField;
                    if (doubleCaseField.getValue() != null) {
                        sObjectValue = doubleCaseField.getValue().toString();
                    }
                    break;
                case DATETIME:
                    DateTimeCaseField dateTimeCaseField = (DateTimeCaseField) caseField;
                    if (dateTimeCaseField.getValue() != null) {
                        newCase.setSObjectField(caseField.getName(),
                                dateTimeCaseField.getValue().toDate());
                    }
                    break;
                case DATE:
                    DateCaseField dateCaseField = (DateCaseField) caseField;
                    if (dateCaseField.getValue() != null) {
                        newCase.setSObjectField(caseField.getName(),
                                dateCaseField.getValue().toDate());
                    }
                    break;
                case REFERENCE:
                    ReferenceCaseField referenceField = (ReferenceCaseField) caseField;
                    if (StringUtils.isNotBlank(referenceField.getReferenceId())) {
                        sObjectValue = referenceField.getReferenceId();
                    }
                    break;
                default:
                    StringCaseField stringCaseField = (StringCaseField) caseField;
                    if (StringUtils.isNotBlank(stringCaseField.getValue())) {
                        sObjectValue = stringCaseField.getValue();
                    }
                    break;

            }
        }
        if (sObjectValue != null) {
            newCase.setSObjectField(caseField.getName(), sObjectValue);
        }
    }

    /**
     * Populates the newCase from dependent pick list fields
     *
     * @param newCase           to populate
     * @param pickListCaseField to find the chosen option
     */
    private void sObjectFromDependentPickListCaseFields(SObject newCase, PickList pickListCaseField) {
        List<PickListValueDependentPickList> selectedOptions = pickListCaseField.getSelectedOptions();
        if (!CollectionUtils.isEmpty(selectedOptions)) {
            for (PickListValueDependentPickList selectedOption : selectedOptions) {
                if (!CollectionUtils.isEmpty(selectedOption.getRequiredWhenChosen())) {
                    for (PickListCaseField requiredCaseField : selectedOption.getRequiredWhenChosen()) {
                        sObjectFromCaseField(newCase, requiredCaseField);
                    }
                }
            }
        }
    }

    /**
     * Responsible for creating all Case forms
     *
     * @return a Map of CaseForm objects by CaseType
     * @throws ConnectionException when SF gives a problem
     */
    private Map<CaseType, CaseForm> createForms() throws ConnectionException {
        if (salesForceConnection.get() == null) {
            throw new ConnectionException("No connection");
        }
        String objectName = "Case";
        DescribeSObjectResult dsr = salesForceConnection.get().describeSObject(objectName);

        // save base level field information for Case objects in general
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : dsr.getFields()) {
            fieldMap.put(field.getName(), field);
        }

        // figure out dependent pick lists based upon the values chosen for other fields
        Map<Field, List<Field>> baseFieldDependentFieldsMap = new HashMap<>();
        for (Field field : dsr.getFields()) {
            if (field.isDependentPicklist()) {
                Field controller = fieldMap.get(field.getControllerName());
                List<Field> dependentFields = baseFieldDependentFieldsMap.get(controller);
                if (dependentFields == null) {
                    dependentFields = new ArrayList<>();
                    baseFieldDependentFieldsMap.put(controller, dependentFields);
                }
                dependentFields.add(field);
            }
        }

        // create a form for each record type
        Map<CaseType, CaseForm> caseTypeCaseFormMap = new HashMap<>();
        for (RecordTypeInfo recordTypeInfo : dsr.getRecordTypeInfos()) {
            if (!StringUtils.equalsIgnoreCase("master", recordTypeInfo.getName())) {

                // create a form for every non-master type
                CaseType caseType = new CaseType();
                caseType.setId(recordTypeInfo.getRecordTypeId());
                caseType.setEmmiCaseType(EmmiCaseType.fromSalesForceId(recordTypeInfo.getRecordTypeId()));
                caseType.setName(recordTypeInfo.getName());
                CaseForm caseForm = new CaseForm();
                caseForm.setType(caseType);
                caseTypeCaseFormMap.put(caseType, caseForm);

                // populate the form from the record type specific layout
                populateCaseForm(caseForm, salesForceConnection.get().describeLayout(objectName, null,
                        new String[]{recordTypeInfo.getRecordTypeId()}), fieldMap, baseFieldDependentFieldsMap);
            }
        }
        return caseTypeCaseFormMap;
    }

    /**
     * Populates a case form from its description
     *
     * @param caseForm                    to be populated
     * @param dlr                         the layout of the Case screens
     * @param fieldMap                    the base fields present on a Case object
     * @param baseFieldDependentFieldsMap the dependent fields keyed by the base field which controls them
     * @throws ConnectionException when SF gives a problem
     */
    private void populateCaseForm(CaseForm caseForm, DescribeLayoutResult dlr, Map<String, Field> fieldMap,
                                  Map<Field, List<Field>> baseFieldDependentFieldsMap)
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
                for (DescribeLayoutRow lr : els.getLayoutRows()) {
                    for (DescribeLayoutItem li : lr.getLayoutItems()) {
                        if (li.isEditableForNew()) {
                            if (ArrayUtils.isNotEmpty(li.getLayoutComponents())) {
                                Field baseField = fieldMap.get(li.getLayoutComponents()[0].getValue());
                                CaseField emmiCaseField =
                                        createCaseField(baseField, pickListOverrides, li);
                                section.addField(emmiCaseField);

                                // create any dependent fields from the options of the base field
                                List<Field> dependentFields = baseFieldDependentFieldsMap.get(baseField);
                                if (!CollectionUtils.isEmpty(dependentFields)) {
                                    for (Field dependentField : dependentFields) {
                                        addDependentFieldsToEmmiField(baseField, emmiCaseField, fieldMap, dependentField);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This is responsible for transforming the SF concept of a dependent field into a new picklist
     * attached to an option on a different emmi field. Thi sis
     *
     * @param controller the field determining when a dependent picklist becomes active and required
     * @param emmiField  the existing emmi CaseField to be modified
     * @param fieldMap   containing the base Case field definitinos
     * @param field      the dependent field itself
     */
    private void addDependentFieldsToEmmiField(Field controller, CaseField emmiField, Map<String, Field> fieldMap, Field field) {

        Field baseField = fieldMap.get(field.getName());
        switch (controller.getType()) {
            case picklist:
                // the controlling field is a picklist, build the dependent picklist based upon the possible options
                Map<String, SimplePickList> pickListForOneSelectedOptionMap = new HashMap<>();
                for (PicklistEntry picklistEntry : field.getPicklistValues()) {
                    BitSet validFor = new BitSet(picklistEntry.getValidFor());
                    for (int k = 0; k < validFor.size(); k++) {
                        if (validFor.testBit(k)) {
                            // if bit k is set, this entry is valid for the for the controlling entry at index k
                            SimplePickList pickListForThisOption =
                                    pickListForOneSelectedOptionMap.get(controller.getPicklistValues()[k].getValue());
                            if (pickListForThisOption == null) {
                                pickListForThisOption = new SimplePickList();
                                pickListForThisOption.name = field.getName();
                                pickListForThisOption.label = field.getLabel();
                                pickListForOneSelectedOptionMap.put(
                                        controller.getPicklistValues()[k].getValue(), pickListForThisOption);
                            }
                            pickListForThisOption.values.add(picklistEntry.getValue());
                        }
                    }
                }
                // loop over the existing emmi field values
                PickListCaseField emmiPickListCaseField = (PickListCaseField) emmiField;
                for (PickListValueDependentPickList dependentPickListPossibleValue : emmiPickListCaseField.getOptions()) {
                    PickListCaseField pickListCaseField = createDependentCaseField(baseField,
                            pickListForOneSelectedOptionMap.get(dependentPickListPossibleValue.getValue()));
                    if (pickListCaseField != null) {
                        // add this picklist field to the dependent list for this option
                        dependentPickListPossibleValue.addRequiredWhenChosen(pickListCaseField);
                    }
                }
                break;
            case _boolean:
                // the controlling field is a boolean, build the dependent picklist based upon true and false values
                SimplePickList truePickList = new SimplePickList();
                truePickList.name = field.getName();
                truePickList.label = field.getLabel();
                SimplePickList falsePickList = new SimplePickList();
                falsePickList.name = field.getName();
                falsePickList.label = field.getLabel();
                for (PicklistEntry picklistEntry : field.getPicklistValues()) {
                    BitSet validFor = new BitSet(picklistEntry.getValidFor());
                    // the controller is a checkbox
                    // if bit 1 is set this entry is valid if the controller is checked
                    if (validFor.testBit(1)) {
                        truePickList.values.add(picklistEntry.getValue());
                    }
                    // if bit 0 is set this entry is valid if the controller is not checked
                    if (validFor.testBit(0)) {
                        falsePickList.values.add(picklistEntry.getValue());
                    }
                }

                // add the required picklists to the boolean case field
                BooleanCaseField booleanCaseField = (BooleanCaseField) emmiField;
                PickListCaseField truePickListCaseField = createDependentCaseField(baseField, truePickList);
                if (truePickListCaseField != null) {
                    booleanCaseField.getRequiredPicklistsWhenTrue().add(truePickListCaseField);
                }
                PickListCaseField falsePickListCaseField = createDependentCaseField(baseField, falsePickList);
                if (falsePickListCaseField != null) {
                    booleanCaseField.getRequiredPicklistsWhenFalse().add(falsePickListCaseField);
                }
                break;
        }
    }

    /**
     * Creates a PickListCaseField from a SimplePickList
     *
     * @param baseField      the Case level field
     * @param simplePickList the picklist model
     * @return PickListCaseField
     */
    private PickListCaseField createDependentCaseField(Field baseField, SimplePickList simplePickList) {
        if (simplePickList != null) {
            // this means we have a dependent picklist for this selected option
            Map<String, PicklistForRecordType> picklistForRecordTypeMap = new HashMap<>();
            picklistForRecordTypeMap.put(baseField.getName(), simplePickList.asPicklistForRecordType());

            // create a new picklist field
            PickListCaseField dependentCaseField = createPickListField(new PickListCaseField(),
                    baseField, picklistForRecordTypeMap);
            dependentCaseField.setName(simplePickList.name);
            dependentCaseField.setLabel(simplePickList.label);
            dependentCaseField.setRequired(true);
            dependentCaseField.setType(PICK_LIST);

            return dependentCaseField;
        }
        return null;
    }

    /**
     * Creates a case field from the base field.
     *
     * @param baseField         the starting point
     * @param pickListOverrides a Map keyed by baseField name that has overrides by record type
     * @param layoutItem        screen layout in salesforce
     * @return CaseField with options
     */
    private CaseField createCaseField(Field baseField, Map<String, PicklistForRecordType> pickListOverrides,
                                      DescribeLayoutItem layoutItem) {
        CaseField emmiCaseField;
        switch (baseField.getType()) {
            case picklist:
                emmiCaseField = createPickListField(new PickListCaseField(), baseField, pickListOverrides);
                break;
            case multipicklist:
                emmiCaseField = createPickListField(new MultiPickListCaseField(), baseField, pickListOverrides);
                break;
            case string:
                StringCaseField stringField = new StringCaseField();
                stringField.setMaxLength(baseField.getLength());
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
            case datetime:
                emmiCaseField = new DateTimeCaseField();
                break;
            case reference:
                ReferenceCaseField referenceField = new ReferenceCaseField();
                referenceField.addReferenceTypes(baseField.getReferenceTo());
                emmiCaseField = referenceField;
                break;
            default:
                emmiCaseField = new StringCaseField();
                break;
        }
        emmiCaseField.setType(fromSalesForceTypeString(baseField.getType().toString()));
        emmiCaseField.setLabel(layoutItem.getLabel());
        emmiCaseField.setName(baseField.getName());
        emmiCaseField.setRequired(layoutItem.isRequired());
        return emmiCaseField;
    }

    /**
     * Creates a PickListCaseField from a base field
     *
     * @param globalField       the base
     * @param pickListOverrides a Map keyed by baseField name that has overrides by record type
     * @return PickListCaseField with the overrides or base picklist values
     */
    private <T extends PickList> T createPickListField(T pickListField,
                                                       Field globalField,
                                                       Map<String, PicklistForRecordType> pickListOverrides) {
        pickListField.setLabel(globalField.getLabel());
        List<PickListValueDependentPickList> options = new ArrayList<>();
        List<PickListValue> values = new ArrayList<>();
        PicklistForRecordType override = pickListOverrides.get(globalField.getName());
        PicklistEntry[] entries;
        if (override != null) {
            entries = override.getPicklistValues();
        } else {
            entries = globalField.getPicklistValues();
        }
        for (PicklistEntry entry : entries) {
            if (entry.isActive()) {
                PickListValueDependentPickList possibleValue = new PickListValueDependentPickList();
                possibleValue.setValue(entry.getValue());
                options.add(possibleValue);
                if (entry.isDefaultValue()) {
                    values.add(new PickListValue(entry.getValue()));
                }
            }
        }
        pickListField.setOptions(options);
        pickListField.setValues(values);
        return pickListField;
    }

    /**
     * Utility class to allow for PicklistForRecordType creation
     */
    class SimplePickList {
        private String name;
        private String label;
        private List<String> values = new ArrayList<>();

        PicklistForRecordType asPicklistForRecordType() {
            PicklistForRecordType ret = new PicklistForRecordType();
            ret.setPicklistName(name);
            if (!CollectionUtils.isEmpty(values)) {
                IPicklistEntry[] iPicklistEntries = new IPicklistEntry[values.size()];
                for (int i = 0; i < values.size(); i++) {
                    String stringEntry = values.get(i);
                    PicklistEntry picklistEntry = new PicklistEntry();
                    picklistEntry.setActive(true);
                    picklistEntry.setLabel(stringEntry);
                    picklistEntry.setValue(stringEntry);
                    iPicklistEntries[i] = picklistEntry;
                }
                ret.setPicklistValues(iPicklistEntries);
            }
            return ret;
        }
    }

    /**
     * Utility class to process the dependent picklist.validFor() attribute.
     */
    class BitSet {
        byte[] data;

        public BitSet(byte[] data) {
            this.data = data == null ? new byte[0] : data;
        }

        public boolean testBit(int n) {
            return (data[n >> 3] & (0x80 >> n % 8)) != 0;
        }

        public int size() {
            return data.length * 8;
        }
    }

}
