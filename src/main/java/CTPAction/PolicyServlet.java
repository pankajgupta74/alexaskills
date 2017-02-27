package CTPAction;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

public class PolicyServlet extends SpeechletServlet  {
	
	  public PolicyServlet() {
		    this.setSpeechlet(new SessionSpeechlet());
		  }
	

}
