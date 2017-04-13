package consensus;

import java.util.Set;

import inputport.ConnectionListener;


public interface ConsensusMechanism<StateType> extends LocalConsensusMechanism<StateType>, 
		 ConsensusCustomization {
//	float propose(StateType aProposal);	
//	ProposalState waitForStateChange(float aProposalNumber);
//	ProposalState getProposalState(float aProposalNumber);
//	Float getMyLastProposalNumber();
//	Float getLastProposalNumber();
//	void addConsensusListener(ConsensusListener<StateType> aConsensusListener);
//	void removeConsensusListener(ConsensusListener<StateType> aConsensusListener);
//	void addConsensusRejectioner(ConsensusRejectioner<StateType> aConsensusRejectioner);
//	void removeConsensusRejectioner(ConsensusRejectioner<StateType> aConsensusRejectioner);
//	boolean myLastProposalIsPending();
//	boolean someProposalIsPending();
//	boolean isPending(float aProposalNumber);
//	Set<Float> getPendingProposals();
//	public Float getLastConsensusProposal() ;
//	public void setLastConsensusProposal(Float lastConsensusProposal) ;

}
