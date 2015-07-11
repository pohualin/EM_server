package com.emmisolutions.emmimanager.salesforce.wsc;

import com.emmisolutions.emmimanager.model.salesforce.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.salesforce.BaseIntegrationTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test stub for create case.. this is really just
 * useful for development and shouldn't be running in an automated way
 */
public class CreateCaseIntegrationTest extends BaseIntegrationTest {

    @Resource
    CaseManager caseManager;

    @Resource
    SalesForceLookup salesForceLookup;

    @Resource
    ConnectionFactory salesForceConnection;

    @Test
    public void create() {

        CaseForm aCase = caseManager.newCase(caseManager.caseTypes().get(0));

        for (Section section : aCase.getSections()) {
            for (CaseField caseField : section.getCaseFields()) {
                if (StringUtils.equalsIgnoreCase("AccountId", caseField.getName())) {
                    ReferenceCaseField referenceCaseField = (ReferenceCaseField) caseField;
                    IdNameLookupResultContainer lookupResultContainer = salesForceLookup.find("magee", 1,
                            referenceCaseField.getReferenceTypes()
                                    .toArray(new String[referenceCaseField.getReferenceTypes().size()]));
                    IdNameLookupResult lookupResult = lookupResultContainer.getContent().get(0);
                    referenceCaseField.setReferenceId(lookupResult.getId());
                }

                if (StringUtils.equalsIgnoreCase("Program__c", caseField.getName())) {
                    ReferenceCaseField referenceCaseField = (ReferenceCaseField) caseField;
                    IdNameLookupResultContainer lookupResultContainer = salesForceLookup.find("anti", 1,
                            referenceCaseField.getReferenceTypes()
                                    .toArray(new String[referenceCaseField.getReferenceTypes().size()]));
                    IdNameLookupResult lookupResult = lookupResultContainer.getContent().get(0);
                    referenceCaseField.setReferenceId(lookupResult.getId());
                }

                if (StringUtils.equalsIgnoreCase("Subject", caseField.getName()) &&
                        caseField instanceof StringCaseField) {
                    StringCaseField stringCaseField = (StringCaseField) caseField;
                    stringCaseField.setValue(
                            RandomStringUtils.randomAlphanumeric(stringCaseField.getMaxLength()));
                }

                if (caseField.getType().equals(PICK_LIST) &&
                        caseField instanceof PickListCaseField) {

                    final PickListCaseField pickListCaseField = (PickListCaseField) caseField;

                    // status field only, choose the last item in list
                    if (StringUtils.equalsIgnoreCase("Status", pickListCaseField.getLabel())) {
                        final PickListValueDependentPickList closedStatus =
                                pickListCaseField.getOptions().get(pickListCaseField.getOptions().size() - 1);

                        // pick a closed/un-resolved status
                        pickListCaseField.setValues(new ArrayList<PickListValue>() {{
                            add(new PickListValue(closedStatus.getValue()));
                        }});
                        if (!CollectionUtils.isEmpty(closedStatus.getRequiredWhenChosen())) {
                            for (PickListCaseField listCaseField : closedStatus.getRequiredWhenChosen()) {
                                final String firstSelection = listCaseField.getOptions().get(0).getValue();
                                listCaseField.setValues(new ArrayList<PickListValue>() {{
                                    add(new PickListValue(firstSelection));
                                }});
                            }
                        }
                    } else {
                        // all other lists choose the first option if one isn't already chosen
                        if (pickListCaseField.getValue() != null) {
                            final PickListValueDependentPickList dependentPickListPossibleValue = pickListCaseField.getOptions().get(0);
                            pickListCaseField.setValues(new ArrayList<PickListValue>() {{
                                add(new PickListValue(dependentPickListPossibleValue.getValue()));
                            }});
                        }
                    }
                }
                if (caseField.getType().equals(EMAIL) &&
                        caseField instanceof StringCaseField) {
                    StringCaseField emailCaseField = (StringCaseField) caseField;
                    emailCaseField.setValue("matt@mattfleming.com");
                }
                if (caseField.getType().equals(DATE) &&
                        caseField instanceof DateCaseField) {
                    DateCaseField dateCaseField = (DateCaseField) caseField;
                    dateCaseField.setValue(new LocalDate(1976, 2, 12));
                }
                if (caseField.getType().equals(PHONE) &&
                        caseField instanceof StringCaseField) {
                    StringCaseField phoneCaseField = (StringCaseField) caseField;
                    phoneCaseField.setValue("847-555-1212");
                }
                if (caseField.getType().equals(BOOLEAN) &&
                        caseField instanceof BooleanCaseField) {
                    BooleanCaseField booleanCaseField = (BooleanCaseField) caseField;
                    booleanCaseField.setValue(true);
                }
            }
        }

        // fail for missing field exception
        CaseSaveResult caseSaveResult =
                caseManager.saveCase(aCase, new UserAdmin("mfleming@emmisolutions.com", "****"));
        assertThat("Save should fail (due to description not being present)", caseSaveResult.isSuccess(), is(false));
        assertThat("should have error message",
                caseSaveResult.getErrorMessages(), hasItem("Unresolved Reason Details are required."));


        /*
            This is a bit hokey but we need to ensure that the values
            not yet present in the true survey are still parsed properly
            and sent over to SalesForce. Since these fields don't really
            exist, invalid api exceptions will happen on the save call
         */
        Section fakeSection = new Section();
        aCase.addSection(fakeSection);

        // add a multi-select pick list
        MultiPickListCaseField fakeMultiPickList = new MultiPickListCaseField();
        fakeMultiPickList.setName("fakeMultiPickList__c");
        fakeMultiPickList.setValues(Arrays.asList(new PickListValue("one"), new PickListValue("two")));
        fakeSection.addField(fakeMultiPickList);

        // date time field
        DateTimeCaseField fakeDateTime = new DateTimeCaseField();
        fakeDateTime.setName("fakeDateTime__c");
        fakeDateTime.setValue(LocalDateTime.now());
        fakeSection.addField(fakeDateTime);

        DoubleCaseField fakeDoubleCaseField = new DoubleCaseField();
        fakeDoubleCaseField.setName("fakeDouble__c");
        fakeDoubleCaseField.setValue(0.0);
        fakeSection.addField(fakeDoubleCaseField);

        caseSaveResult = caseManager.saveCase(aCase, new UserAdmin("mfleming@emmisolutions.com", "****"));

        assertThat("Save should fail (invalid fields)", caseSaveResult.isSuccess(), is(false));
        assertThat("should have error message",
                caseSaveResult.getErrorMessages(),
                hasItem("No such column 'fakeMultiPickList__c' on entity 'Case'. " +
                        "If you are attempting to use a custom field, be sure to append the '__c' " +
                        "after the custom field name. Please reference your WSDL or the describe call " +
                        "for the appropriate names."));

    }

    @Test
    public void nullCase() {
        assertThat("null makes null", caseManager.newCase(null), is(nullValue()));
    }

    /**
     * Loops over the case types and ensures there is no conversion
     * issue.. i.e. we should be able to read all SF fields.
     */
    @Test
    public void dataTypesCanBeReadFromSF() {
        for (CaseType caseType : caseManager.caseTypes()) {
            assertThat("form is good", caseManager.newCase(caseType).getType(), is(caseType));
        }
    }
}
