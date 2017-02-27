package CTPAction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

//import org.apache.log4j.Logger;

public class CTPServiceAction {
	
	public String processRequest(String policyNumber) throws IOException{
		
		StringBuilder result=new StringBuilder();
		String output=null;
		String prmium = null;
		String soastatusCode = null;
		String soaMsg = null;
		Map responsejson =null;
		try 
		{
			URL url = new URL("https://gatewayuat.maxlifeinsurance.com/apimgm/dev/soa/ptp/v2");
			XTrustProvider xTrustProvider =new XTrustProvider();
			xTrustProvider.install();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setConnectTimeout(40000);
			conn.setReadTimeout(40000);
			final String input ="{	\"request\": {		\"header\": {			\"soaMsgVersion\": \"1.0\",			\"soaAppId\": \"Techprocess\",			\"soaCorrelationId\": \""+UUID.randomUUID()+"\",			\"soaUserId\": \"techuat\",			\"soaPassword\": \"dGVjaCQxMjM=\"		},		\"requestPayload\": {			\"policyNumber\": \""+policyNumber+"\",			\"mobileFlag\": \"CTP\",			\"payType\": \"PTP\"		}	}}";
			
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if(conn.getResponseCode() == 200){
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				while ((output = br.readLine()) != null) {
					result.append(output);
				}
				responsejson= Commons.getGsonData(result.toString());
				soastatusCode = (String)((Map)((Map)responsejson.get("response")).get("responsePayload")).get("soaStatusCode");
				soaMsg = (String)((Map)((Map)responsejson.get("response")).get("responsePayload")).get("soaMessage");
				prmium = (String)((Map)((Map)responsejson.get("response")).get("responsePayload")).get("netAmountDue");
			}
			else if(conn.getResponseCode() == 500){
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				while ((output = br.readLine()) != null) {
					result.append(output);
				}
			}
			conn.disconnect();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return prmium;
	

}
}
