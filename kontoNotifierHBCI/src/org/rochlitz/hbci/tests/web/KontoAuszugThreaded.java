package org.rochlitz.hbci.tests.web;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.ejb.Stateless;

import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;

@Stateless
public class KontoAuszugThreaded extends GVBase
{
    public KontoAuszugThreaded(MyCallback mc){
		super(mc);
	}

    public KontoAuszugThreaded( ){
    	super();
	}
    
    public GVRKUms getAuszug(){
        // eigenes konto
        Konto myAccountGiro = passport.getAccounts()[2];
        HBCIJob job=handler.newJob("KUmsAll");
        // daten fuer eigenes konto setzen
        job.setParam("my.blz", mCallback.getBlz());
        job.setParam("my.number", mCallback.getKontoNr() );
        job.setParam("my.subnumber", mCallback.getKontoSubNr());
        Calendar calStart = new GregorianCalendar();
        Calendar calEnd = new GregorianCalendar();
        calStart.add(Calendar.DAY_OF_MONTH, DAY_OFFSET);
//        calEnd.add(Calendar.DAY_OF_MONTH, DAY_OFFSET);
        
        job.setParam("startdate", calStart.getTime() );
        job.setParam("enddate",  calEnd.getTime());
        job.addToQueue(passport.getCustomerId());
       
     // execute dialog
        HBCIExecStatus dialogStatus=handler.execute();
        System.out.println("status:");
        System.out.println(dialogStatus);
        GVRKUms umsResults=(GVRKUms)job.getJobResult();
        for(int i=0; i< umsResults.getFlatData().size();i++ ){
        	UmsLine btag = (UmsLine) umsResults.getFlatData().get(i);
        	System.out.println("VALUE: "+ btag.value +" | DATE: "+btag.bdate + " |  NEUER SLADO: " +btag.saldo    + " |  TEXT: " +btag.toString()   );
        	System.out.println("toString "+ btag.usage.toString() );
        	System.out.println("");
        }
        Properties results = job.getJobResult().getResultData();
//        writeToFile(results);
        // print information about complete dialog
        if (!dialogStatus.isOK()) {
            System.out.println("some error has occured during execution of the HBCI dialog:");
            System.out.println(dialogStatus.getErrorString());
        }
        // check each business task
        if (job.getJobResult().isOK()) {
            System.out.println("saldo information for account "+myAccountGiro);
//            System.out.println(job.getJobResult().toString());
        } else {
            System.out.println("an error occured in task SaldoRequest");
            System.out.println(job.getJobResult().getJobStatus().getErrorString());
        }
        // aufraeumen
        handler.close();
        System.out.println("*********************** cal start "+calStart.getTime());
        System.out.println("*********************** cal end "+calEnd.getTime());
        
        return umsResults;
    }

	public static void main(String[] args)
    {
		MyCallback mc = new MyCallback();
		 new KontoAuszugThreaded(mc).getAuszug();
    }
   
}
