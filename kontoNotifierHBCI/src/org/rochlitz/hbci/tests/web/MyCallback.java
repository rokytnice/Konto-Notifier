package org.rochlitz.hbci.tests.web;

import org.kapott.hbci.callback.HBCICallbackConsole;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.passport.INILetter;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;

//TODO AR change passwort
//TODO transactions
//TODO ssl
//TODO explain filter on GUI

public class MyCallback extends HBCICallbackConsole {
	// User data
	private String pin ; // 
	private String customerId ; 
	private String passphrase = "1"; // passport passwort   //TODO account passwd

	// comdirect data
	private String blz;
	private String hbciHost = "fints.comdirect.de/fints";
	private String countryOfBank = "DE";
	private String TANVerfahren = "901"; // mobile TAN = 901
	private String kontoNr; // = "490058500";  
	private String kontoNrFill = "00";
	private String kontoSubNr = "00";
	
	//bankNotfier Stammdaten 
	private String userID ;
	private String kontoID ;
	
	public MyCallback(KontoDTO not, UserDTO user)  {
		super();
		
		this.pin = not.getPassword();
		this.customerId = not.getAccount();
		this.kontoNr = String.valueOf(not.getKtonr())+kontoNrFill;
		this.kontoID = String.valueOf(not.getId());
		this.userID = String.valueOf(user.getId());
		this.blz = String.valueOf(not.getBlz());

	}
	
	

	public MyCallback() {
		// TODO Auto-generated constructor stub
	}



	public void callback(HBCIPassport passport, int reason, String msg,
			int datatype, StringBuffer retData) {

		switch (reason) {
		case NEED_PT_SECMECH:
			retData.setLength(0);
			retData.append(this.TANVerfahren);
			break;
		case NEED_PT_PIN:
			retData.setLength(0);
			retData.append(this.pin);
			break;
		case NEED_BLZ:
			retData.setLength(0);
			retData.append(this.blz);
			break;

		case NEED_COUNTRY:
			retData.setLength(0);
			retData.append(this.countryOfBank);
			break;

		case NEED_HOST:
			retData.setLength(0);
			retData.append(this.hbciHost);
			break;

		case NEED_PORT:
			retData.setLength(0);
			retData.append("3000");
			break;

		case NEED_PASSPHRASE_LOAD:
			retData.setLength(0);
			retData.append(passphrase);
			break;
		case NEED_PASSPHRASE_SAVE:
			retData.setLength(0);
			retData.append(passphrase);
			break;
		case NEED_USERID:
		case NEED_CUSTOMERID:
			retData.setLength(0);
			retData.append(this.customerId);
			break;

		case NEED_NEW_INST_KEYS_ACK:
			INILetter iniletter = new INILetter(passport,
					INILetter.TYPE_INST);
			// data=(Properties)passport.getClientData("init");//TODO AR
			// data.setProperty("hash",HBCIUtils.data2hex(iniletter.getKeyHashDisplay()));
			break;

		case NEED_CONNECTION:
		case HAVE_INST_MSG:
		case CLOSE_CONNECTION:
			break;
		}
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	public String getBlz() {
		return blz;
	}

	public void setBlz(String blz) {
		this.blz = blz;
	}

	public String getHbciHost() {
		return hbciHost;
	}

	public void setHbciHost(String hbciHost) {
		this.hbciHost = hbciHost;
	}

	public String getCountryOfBank() {
		return countryOfBank;
	}

	public void setCountryOfBank(String countryOfBank) {
		this.countryOfBank = countryOfBank;
	}

	public String getTANVerfahren() {
		return TANVerfahren;
	}

	public void setTANVerfahren(String tANVerfahren) {
		TANVerfahren = tANVerfahren;
	}

	public String getKontoNr() {
		return kontoNr;
	}

	public void setKontoNr(String kontoNr) {
		this.kontoNr = kontoNr;
	}

	public String getKontoSubNr() {
		return kontoSubNr;
	}

	public void setKontoSubNr(String kontoSubNr) {
		this.kontoSubNr = kontoSubNr;
	}

	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getKontoId() {
		return kontoID;
	}

	public void setKontoId(String konto) {
		this.kontoID = konto;
	}


}