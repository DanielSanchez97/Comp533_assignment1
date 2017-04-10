package port.trace.consensus;

import consensus.ProposalVetoKind;
import inputport.rpc.RemoteCall;
import util.annotations.ComponentWidth;
import util.annotations.DisplayToString;
import util.trace.TraceableInfo;
@DisplayToString(true)
@ComponentWidth(1000)
public class ProposalAcceptedNotificationReceived extends TraceableInfo {
	

	public ProposalAcceptedNotificationReceived(String aMessage, Object aSource, String anObjectName, float aProposalNumber, Object aProposal, ProposalVetoKind anAgreement) {
		super(aMessage, aSource );
	}
	
	
	public static ProposalAcceptedNotificationReceived newCase(Object aSource, String anObjectName, float aProposalNumber, Object aProposal, ProposalVetoKind anAgreement) {
    	String aMessage =  anObjectName + "," + aProposalNumber + "=" + aProposal + "-->" + anAgreement;
    	ProposalAcceptedNotificationReceived retVal = new ProposalAcceptedNotificationReceived(aMessage, aSource, anObjectName, aProposalNumber, aProposal, anAgreement);
   	    retVal.announce();
    	return retVal;

	}
	
	static {
//		Tracer.setKeywordDisplayStatus(CallReceived.class, true);
	}

}
