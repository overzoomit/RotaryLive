package it.stasbranger.rotarylive.service.utility;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service("UtilityService")
public class UtilityServiceImpl implements UtilityService {

	private static String secretKey = "6^XeM$mtP^rErYpdeEjQpBC?*dwB48RE";

	public String encodeID(String id) {
		String code = Base64.encodeBase64URLSafeString(id.getBytes());
		String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(code+secretKey);
		return code+"_"+md5.toUpperCase();
	}

	public String dencodeID(String code) {
		try{
			String[] codes = code.split("_");
			String token = codes[0];
			String checksum = codes[1].toLowerCase();

			String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(token+secretKey);
			if(md5.equals(checksum)){
				byte[] decodedBytes = Base64.decodeBase64(token.getBytes());
				String result =  new String(decodedBytes);		
				return result;
			}else{
				return null;
			}
		}catch(Exception e){	
			return null;
		}
	}
	
	public long getDifferenceDays(Date d1, Date d2) {
	    long diff = d2.getTime() - d1.getTime();
	    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
}
