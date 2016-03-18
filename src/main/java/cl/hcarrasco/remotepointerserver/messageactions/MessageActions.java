package cl.hcarrasco.remotepointerserver.messageactions;
import org.apache.log4j.Logger;

public class MessageActions {
	
	final static Logger logger = Logger.getLogger(MessageActions.class);
	
	public void hubMessages(String messageFromDevice){
		
		String action = getActionFromMessage(messageFromDevice);
		if(!"".equals(action)){
			
		}
		
	}
	
	private String getActionFromMessage(String messageFromDevice){
		
		return messageFromDevice;
	}

}
