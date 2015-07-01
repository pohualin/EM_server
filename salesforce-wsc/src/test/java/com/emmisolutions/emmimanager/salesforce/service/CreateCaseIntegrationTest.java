package com.emmisolutions.emmimanager.salesforce.service;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.model.salesforce.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.salesforce.BaseIntegrationTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.*;

/**
 * Test stub for create case.. this is really just
 * useful for development and shouldn't be running in an automated way
 */
public class CreateCaseIntegrationTest extends BaseIntegrationTest {

    @Resource
    CaseManager caseManager;

    @Resource
    SalesForceLookup salesForceLookup;

    @Test
    public void create() {
        for (CaseType caseType : caseManager.caseTypes()) {
            CaseForm aCase = caseManager.newCase(caseType);

            for (Section section : aCase.getSections()) {
                List<CaseField> dependentFieldsToAddToSection = new ArrayList<>();
                for (CaseField caseField : section.getCaseFields()) {
                    if (StringUtils.equalsIgnoreCase("AccountId", caseField.getName())) {
                        SalesForceSearchResponse salesForceSearchResponse = salesForceLookup.findAccounts("magee", 1);
                        SalesForce salesForce = salesForceSearchResponse.getAccounts().get(0);
                        ((ReferenceCaseField) caseField).setReferenceId(salesForce.getAccountNumber());
                    }

                    if (StringUtils.equalsIgnoreCase("Subject", caseField.getName()) &&
                            caseField instanceof StringCaseField) {
                        StringCaseField stringCaseField = (StringCaseField) caseField;
                        stringCaseField.setValue(
                                RandomStringUtils.randomAlphanumeric(stringCaseField.getMaxLength()));
                    }

                    if (StringUtils.equalsIgnoreCase("Description", caseField.getName()) &&
                            caseField instanceof StringCaseField) {
                        StringCaseField stringCaseField = (StringCaseField) caseField;
                        // we need this for closed cases
                        stringCaseField.setValue(
                                RandomStringUtils.randomAlphanumeric(255));
                    }

                    if (caseField.getType().equals(PICK_LIST) &&
                            caseField instanceof PickListCaseField) {
                        final PickListCaseField pickListCaseField = (PickListCaseField) caseField;
                        if (StringUtils.equalsIgnoreCase("Status", pickListCaseField.getLabel())) {
                            final DependentPickListPossibleValue closedStatus =
                                    pickListCaseField.getOptions().get(pickListCaseField.getOptions().size() - 1);

                            // pick a closed/un-resolved status
                            pickListCaseField.setValues(new ArrayList<String>() {{
                                add(closedStatus.getValue());
                            }});
                            if (!CollectionUtils.isEmpty(closedStatus.getRequiredWhenChosen())) {
                                for (PickListCaseField listCaseField : closedStatus.getRequiredWhenChosen()) {
                                    dependentFieldsToAddToSection.add(listCaseField);
                                    final String firstSelection = listCaseField.getOptions().get(0).getValue();
                                    listCaseField.setValues(new ArrayList<String>() {{
                                        add(firstSelection);
                                    }});
                                }
                            }
                        }
                        if (CollectionUtils.isEmpty(pickListCaseField.getValues())) {
                            final DependentPickListPossibleValue dependentPickListPossibleValue = pickListCaseField.getOptions().get(0);
                            pickListCaseField.setValues(new ArrayList<String>() {{
                                add(dependentPickListPossibleValue.getValue());
                            }});
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
                // add dependent case fields to the section
                for (CaseField caseField : dependentFieldsToAddToSection) {
                    section.addField(caseField);
                }
            }
            caseManager.saveCase(aCase, new UserAdmin("mfleming@emmisolutions.com", "****"));
        }
    }
}
