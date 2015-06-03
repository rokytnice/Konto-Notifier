package org.rochlitz.hbci.tests.web;

import java.util.Properties;

import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.callback.HBCICallbackThreaded;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecThreadedStatus;
import org.kapott.hbci.structures.Konto;

/**
 * @author A Roc CREATE PASSPORT
 */
public class TestUmsatzAbrufenThreaded {

	public static String fileanme = "0000000000001_000000000001.dat";

	public static void main(String[] args) {
		Properties props = new Properties();

		// zu verwendendes passport konfigurieren
		props.setProperty("client.passport.default", "PinTan");

		props.setProperty("client.passport.PinTan.filename", fileanme);
		props.setProperty("client.passport.PinTan.init", "1");

		// loglevel und -filter auf maximales logging setzen
		props.setProperty("log.loglevel.default", "5");
		props.setProperty("log.filter", "0");

		MyCallback mCallback = new MyCallback();
		mCallback.setPassphrase("1");

		HBCICallbackThreaded cbh = new HBCICallbackThreaded(mCallback);
		HBCIUtils.init(props, cbh);
		// HBCIUtils.init(props, new HBCICallbackAndroid());

		// hbcihandler instanziieren
		HBCIPassport passport = AbstractHBCIPassport.getInstance();
		HBCIHandler handler = new HBCIHandler("300", passport);

		// eigenes konto
		Konto myAccount = passport.getAccounts()[0];
		Konto myAccount1 = passport.getAccounts()[1];
		Konto myAccount2 = passport.getAccounts()[2];
		myAccount.number = "490058500";
		// gegenkonto
		// sepa-ueberweisung erzeugen
		HBCIJob job = handler.newJob("SaldoReqAll");

		// daten fuer eigenes konto setzen
		job.setParam("my.blz", "20041133");
		job.setParam("my.number", "490058500");
		job.setParam("my.subnumber", "00");

		// job hinzufuegen
		job.addToQueue();

		// execute dialog
		HBCIExecThreadedStatus dialogStatus = handler.executeThreaded();
		System.out.println("status:");
		System.out.println(dialogStatus);

		// print information about complete dialog
		if (!dialogStatus.isFinished()) {
			System.out.println("getExecStatus :");
			System.out.println(dialogStatus.getExecStatus());
		}

		// check each business task
		if (job.getJobResult().isOK()) {
			System.out.println("saldo information for account " + myAccount2);
			System.out.println(job.getJobResult().toString());
		} else {
			System.out.println("an error occured in task SaldoRequest");
			System.out.println(job.getJobResult().getJobStatus()
					.getErrorString());
		}

		// aufraeumen
		handler.close();
	}
}
