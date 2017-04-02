package examples.gipc.counter.sessionport.twopartyconsensus;

import port.trace.consensus.ConsensusTraceUtility;

public class TwoPartyConsensusMember1 extends ATwoPartyConsensusSessionMember{
	static final int MY_PORT_NUMBER = 7001;
	static final String MY_NAME = "1";
	public static final String GREETING_1 = "Hello";
	public static final String GREETING_2 = "Howdy";
	public static int MEANING = 29;


	
	public static void doOperations() {
		int aGreetingProposal1 = greetingMechanism.propose(GREETING_1);
		greetingMechanism.waitForStateChange(aGreetingProposal1);
		int aGreetingProposal2 = greetingMechanism.propose(GREETING_2);
		int aMeaningOfLifeProposal = meaningOfLifeMechanism.propose(MEANING);
		greetingMechanism.waitForStateChange(aGreetingProposal2);
		meaningOfLifeMechanism.waitForStateChange(aMeaningOfLifeProposal);		
		
	}
	public static void beSessionMember() {
		ConsensusTraceUtility.setTracing();
		init(MY_NAME, MY_PORT_NUMBER);
		doOperations();
	}

	public static void main (String[] args) {
		
		beSessionMember();
//		doOperations();
	}
}
