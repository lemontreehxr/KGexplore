package cn.edu.ruc.ultity;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLComponent {
	private String enc;
	public URLComponent(String enc){
		this.enc = enc;
	}
	
	public String decode(String oldString){	
		try {
			return URLDecoder.decode(oldString.replaceAll("%(?![0-9a-fA-F]{2})", "%25"), enc).replace(" ", "+");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String encode(String oldString){	
		try {
			return URLEncoder.encode(oldString, enc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
