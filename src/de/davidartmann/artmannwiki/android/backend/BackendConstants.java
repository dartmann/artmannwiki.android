package de.davidartmann.artmannwiki.android.backend;

public class BackendConstants {
	
	public static final String HEADER_KEY = "artmannwiki_headerkey";
	//TODO: change this when productive
	public static final String HEADER_VALUE = "blafoo";
	
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_JSON = "application/json";
	
	public static final String ARTMANNWIKI_ROOT = "http://213.165.81.7:8080/ArtmannWiki";
	
	//---------------------------------------------------------------------
	// The REST URL constants:
	//---------------------------------------------------------------------
	//TODO: get & delete variable id fields must be removed before usage!!!
	public static final String GET_ACCOUNT_BY_ID = "/rest/account/get/{id}";
	public static final String GET_ACCOUNT_BY_IBAN = "/rest/account/get/{iban}";
	public static final String GET_ALL_ACCOUNTS = "/rest/account/get/all";
	public static final String GET_ACCOUNTS_SINCE = "/rest/account/get/since/";
	public static final String DELETE_ACCOUNT_BY_ID = "/rest/account/delete/{id}";
	public static final String DELETE_ACCOUNT_BY_IBAN = "/rest/account/delete/{iban}";
	public static final String SOFT_DELETE_ACCOUNT_BY_ID ="/rest/account/softdelete/";
	public static final String ADD_ACCOUNT = "/rest/account/post/add";
	public static final String UPDATE_ACCOUNT = "/rest/account/post/update/";
	
	public static final String GET_DEVICE_BY_ID = "/rest/device/get/{id}";
	public static final String GET_DEVICE_BY_NUMBER = "/rest/device/get/{number}";
	public static final String GET_ALL_DEVICES = "/rest/device/get/all";
	public static final String DELETE_DEVICE_BY_ID = "/rest/device/delete/{id}";
	public static final String DELETE_DEVICE_BY_NUMBER = "/rest/device/delete/{number}";
	public static final String SOFT_DELETE_DEVICE_BY_ID ="/rest/device/softdelete/";
	public static final String ADD_DEVICE = "/rest/device/post/add";
	public static final String UPDATE_DEVICE = "/rest/device/post/update/";
	
	public static final String GET_EMAIL_BY_ID = "/rest/email/get/{id}";
	public static final String GET_EMAIL_BY_EMAILADRESS = "/rest/email/get/{emailadress}";
	public static final String GET_ALL_EMAILS = "/rest/email/get/all";
	public static final String DELETE_EMAIL_BY_ID = "/rest/email/delete/{id}";
	public static final String DELETE_EMAIL_BY_EMAILADRESS = "/rest/email/delete/{emailadress}";
	public static final String SOFT_DELETE_EMAIL_BY_ID ="/rest/email/softdelete/";
	public static final String ADD_EMAIL = "/rest/email/post/add";
	public static final String UPDATE_EMAIL = "/rest/email/post/update/";
	
	public static final String GET_INSURANCE_BY_ID = "/rest/insurance/get/{id}";
	public static final String GET_INSURANCE_BY_MEMBERSHIPID = "/rest/insurance/get/{membershipId}";
	public static final String GET_ALL_INSURANCES = "/rest/insurance/get/all";
	public static final String DELETE_INSURANCE_BY_ID = "/rest/insurance/delete/{id}";
	public static final String DELETE_INSURANCE_BY_MEMBERSHIPID = "/rest/insurance/delete/{membershipId}";
	public static final String SOFT_DELETE_INSURANCE_BY_ID ="/rest/insurance/softdelete/";
	public static final String ADD_INSURANCE = "/rest/insurance/post/add";
	public static final String UPDATE_INSURANCE = "/rest/insurance/post/update/";
	
	public static final String GET_LOGIN_BY_ID = "/rest/login/get/{id}";
	public static final String GET_LOGIN_BY_USERNAME = "/rest/login/get/{username}";
	public static final String GET_ALL_LOGINS = "/rest/login/get/all";
	public static final String DELETE_LOGIN_BY_ID = "/rest/login/delete/{id}";
	public static final String DELETE_LOGIN_BY_USERNAME = "/rest/login/delete/{username}";
	public static final String SOFT_DELETE_LOGIN_BY_ID ="/rest/login/softdelete/";
	public static final String ADD_LOGIN = "/rest/login/post/add";
	public static final String UPDATE_LOGIN = "/rest/login/post/update/";
	
	public static final String GET_MISCELLANEOUS_BY_ID = "/rest/miscellaneous/get/{id}";
	public static final String GET_ALL_MISCELLANEOUS = "/rest/miscellaneous/get/all";
	public static final String DELETE_MISCELLANEOUS_BY_ID = "/rest/miscellaneous/delete/{id}";
	public static final String SOFT_DELETE_MISCELLANEOUS_BY_ID ="/rest/miscellaneous/softdelete/";
	public static final String ADD_MISCELLANEOUS = "/rest/miscellaneous/post/add";
	public static final String UPDATE_MISCELLANEOUS = "/rest/miscellaneous/post/update/";
	
	public static final String GET_AND_SET_LAST_UPDATE = "/rest/lastupdate";
}
