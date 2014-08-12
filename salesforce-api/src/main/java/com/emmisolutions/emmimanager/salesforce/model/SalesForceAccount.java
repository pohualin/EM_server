package com.emmisolutions.emmimanager.salesforce.model;

import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.RecordType;
import com.sforce.soap.enterprise.sobject.User;

/**
 * This class is a wrapper DTO for an underlying Account 
 * object.  This underlying account object is generated from
 * the WSDL file of SalesForce's web service.
 */
public class SalesForceAccount {

	private Account account;

	public SalesForceAccount(Account acct){
		account = acct;
	}
	
	public String getId(){
		return account.getId();
	}
	
	public String getName(){
		return account.getName();
	}
	
	public String getAddress1(){
		return account.getBillingStreet();
	}
	
	public String getCity(){
		return account.getBillingCity();
	}
	
	public String getState(){
		return account.getBillingState();
	}
	
	public String getCountry(){
		return account.getBillingCountry();
	}
	
	public String getZip(){
		return account.getBillingPostalCode();
	}
	
	
	public String getPhone(){
		return account.getPhone();
	}
	
	public String getFax(){
		return account.getFax();
	}
	
	public String getRecordType(){
		RecordType rt = account.getRecordType();
		if (rt != null)
			return rt.getName();
		
		return null;
	}
	
	public String getType(){
		return account.getType();
	}
	
	public String getStatus(){
		return account.getAccount_Status__c();
	}
	
	public String getOwner(){
		return account.getOwnerId();
	}
	
	public String getOwnerAlias(){
		User owner = account.getOwner();
		if (owner != null)
			return owner.getAlias();
		
		return null;
	}
}
