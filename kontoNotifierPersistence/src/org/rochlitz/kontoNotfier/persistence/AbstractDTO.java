package org.rochlitz.kontoNotfier.persistence;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;

public class AbstractDTO implements IDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -243001313153222612L;

	public AbstractDTO(String fieldRest) {
		super();
		fieldRest = fieldRest.replaceAll("\"", "");
		String[] fields = fieldRest.split("&");
		
		for(String property : fields){
			String[] keyValue = property.split("=");
			try {
				if(keyValue.length<=1){//no value for key
					continue;
				}
				String key = keyValue[0];
				String value = URLDecoder.decode( keyValue[1], "UTF-8" );
				
				Field field = this.getClass().getDeclaredField(key);
				field.setAccessible(true);
				Class<?> typeOF = field.getType();
				 
				if(typeOF.equals(Integer.class)){
					 field.set(this,Integer.parseInt( value ));
				 }if(typeOF.isPrimitive()){//long sind die einzigsten primitiven die bisher verwendet werden
					 field.set(this,Integer.parseInt( value ));
				 }else{
					 field.set(this,keyValue[1]);
				 }
			} catch (ArrayIndexOutOfBoundsException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public AbstractDTO() {
		super();
	}
}
