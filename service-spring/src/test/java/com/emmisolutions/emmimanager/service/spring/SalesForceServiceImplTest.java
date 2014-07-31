package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.SalesForceService;
import org.junit.Test;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
public class SalesForceServiceImplTest extends BaseIntegrationTest {

    @Resource
    SalesForceService salesForceService;

    @Test(expected = ConstraintViolationException.class)
    public void createWithoutAccountNumber(){
        salesForceService.create(new SalesForce());
    }

    @Test
    public void create(){
        SalesForce sf = salesForceService.create(new SalesForce("account number"));
        assertThat("An ID was created", sf.getId(), is(notNullValue()));
        assertThat("a version was created", sf.getVersion(), is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNonPersistent() {
        salesForceService.update(new SalesForce("account number") );
    }

    @Test
    public void update(){
        SalesForce sf = salesForceService.create(new SalesForce("account number update"));
        sf.setCountry("usa");
        salesForceService.update(sf);
        sf = salesForceService.reload(sf);
        assertThat("USA is the country", sf.getCountry(), is("usa"));
    }
}