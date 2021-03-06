package org.rochlitz.hbci.tests.web;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.kapott.hbci.callback.HBCICallbackThreaded;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;

public class GVBase {
	
	protected  MyCallback mCallback;
	protected  Properties props;
	protected  String fileanme;
	public static final int DAY_OFFSET = -1; //TODO m,uss -1 sein
	protected HBCICallbackThreaded cbh;
	protected HBCIPassport passport;
	protected HBCIHandler handler;
	
	public GVBase(){
//		 mCallback = new MyCallback();
//		 props=new Properties();
//		 fileanme = "~/"+mCallback.getUserID()+"_"+mCallback.getKontoId()+"_passport.dat";
//		 iniProps();
//		 cbh = new HBCICallbackThreaded(mCallback);
//		 HBCIUtils.init(props, cbh);
//			// hbcihandler instanziieren
//		 passport = AbstractHBCIPassport.getInstance();
//		 handler = new HBCIHandler("300", passport);
	}
	
	public GVBase(MyCallback mc){
		 mCallback = mc;
		 props=new Properties();
		 //TODO filename in app properties
		 fileanme = "/tmp/"+mCallback.getUserID()+"_"+mCallback.getKontoId()+"_passport.dat";
		 System.out.println("**************  nutze  passport " + fileanme);
		 iniProps();
		 cbh = new HBCICallbackThreaded(mCallback);
		 HBCIUtils.init(props, cbh);
			// hbcihandler instanziieren
		 passport = AbstractHBCIPassport.getInstance();
		 handler = new HBCIHandler("300", passport);
	}
	
	protected void iniProps() {
		// zu verwendendes passport konfigurieren
        props.setProperty("client.passport.default", "PinTan");
        props.setProperty("client.passport.PinTan.filename", fileanme);
        props.setProperty("client.passport.PinTan.init", "1");
        // loglevel und -filter auf maximales logging setzen
        props.setProperty("log.loglevel.default", "5");
        props.setProperty("log.filter", "0");
	}
    
    protected void writeToFile(Properties results){
    	
    	String filePath = "KontoAuszug"+System.currentTimeMillis()+".txt";
    	try {
			results.store( new FileOutputStream(filePath), String.valueOf(System.currentTimeMillis()) );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
