package gradingTools.comp533s19.assignment2;

import grader.basics.execution.BasicExecutionSpecificationSelector;
import gradingTools.comp533s19.assignment2.testcases.RMINIOClientMetaStateBroadcast;
import gradingTools.comp533s19.assignment2.testcases.RMINIOClientMetaStateNoBroadcast;
import gradingTools.comp533s19.assignment2.testcases.RMINIOServerMetaStateBroadcast;
import gradingTools.comp533s19.assignment2.testcases.RMINIOServerMetaStateNoBroadcast;
import gradingTools.comp533s19.assignment2.testcases.RMINIOTwoClientConnection;
import gradingTools.comp533s19.assignment2.testcases.RMINIOTwoClientReadWriteAtomic;
import gradingTools.comp533s19.assignment2.testcases.RMINIOTwoClientReadWriteNonAtomic;
import gradingTools.comp533s19.flexible.testcases.FlexibleStaticArgumentsTestCase;

import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import util.tags.DistributedTags;


@RunWith(Suite.class)
@Suite.SuiteClasses({
//	RMINIOStaticArguments.class,
	RMINIOTwoClientConnection.class,
	RMINIOTwoClientReadWriteNonAtomic.class,
	RMINIOTwoClientReadWriteAtomic.class,
	RMINIOClientMetaStateNoBroadcast.class,
	RMINIOClientMetaStateBroadcast.class,
	RMINIOServerMetaStateNoBroadcast.class,
	RMINIOServerMetaStateBroadcast.class
	
	
})
public class Assignment2TwoClientSuite {

public static void twoClientSetupProcesses() {
	List<String> aClientTags = Arrays.asList(DistributedTags.CLIENT, DistributedTags.RMI, DistributedTags.NIO);
	List<String> aServerTags = Arrays.asList(DistributedTags.SERVER, DistributedTags.RMI, DistributedTags.NIO);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setProcessTeams(Arrays.asList("RegistryBasedDistributedProgram"));
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setTerminatingProcesses("RegistryBasedDistributedProgram", Arrays.asList("Client_0", "Client_1"));
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setProcesses("RegistryBasedDistributedProgram", Arrays.asList("Registry", "Server", "Client_0", "Client_1"));
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryTags("Registry", Arrays.asList("Registry"));
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryTags("Server", aServerTags);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryTags("Client_0", aClientTags);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryTags("Client_1", aClientTags);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setArgs("Registry", FlexibleStaticArgumentsTestCase.TEST_REGISTRY_ARGS);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setArgs("Server", FlexibleStaticArgumentsTestCase.TEST_SERVER_ARGS);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setArgs("Client_0", FlexibleStaticArgumentsTestCase.TEST_CLIENT_0_ARGS);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setArgs("Client_1", FlexibleStaticArgumentsTestCase.TEST_CLIENT_1_ARGS);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setSleepTime("Registry", 500);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setSleepTime("Server", 2000);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setSleepTime("Client_0", 5000);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setSleepTime("Client_1", 5000);
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getProcessTeams().forEach(team -> System.out.println("### " + team));
}

}