package org.rochlitz.hbci.tests.web;

import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.status.HBCIExecThreadedStatus;
import org.kapott.hbci.structures.Konto;

/**
 * @author A Roc CREATE PASSPORT
 */
public class TestUmsatzAbrufenThreaded extends GVBase{
	
	public TestUmsatzAbrufenThreaded(MyCallback mc){
		super(mc);
	}

//	protected static String fileanme = mCallback.getUserID()+"_"+mCallback.getKontoId()+"_passport.dat";
	
	protected void getUmsaetze(){

		// eigenes konto
		Konto myAccount = passport.getAccounts()[0];
		Konto myAccount1 = passport.getAccounts()[1];
		Konto myAccount2 = passport.getAccounts()[2];
		myAccount.number = mCallback.getKontoNr();
		// gegenkonto
		// sepa-ueberweisung erzeugen
		HBCIJob job = handler.newJob("SaldoReqAll");

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

	
	public static void main(String[] args) {
//		MyCallback mc = new MyCallback();
//		TestUmsatzAbrufenThreaded t = new TestUmsatzAbrufenThreaded(mc);
//		t.getUmsaetze();
	}
}
