package it.stasbranger.rotarylive.service.utility;

import java.util.Date;

public interface UtilityService {
	
	public String encodeID(String id);

	public String dencodeID(String code);
	
	public long getDifferenceDays(Date d1, Date d2);
}
