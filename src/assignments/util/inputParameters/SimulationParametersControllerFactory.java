package assignments.util.inputParameters;

import util.trace.bean.BeanTraceUtility;

public class SimulationParametersControllerFactory {
	static SimulationParametersController singleton = new ASimulationParametersController();

	public static SimulationParametersController getSingleton() {
		return singleton;
	}

	public static void setSingleton(SimulationParametersController singleton) {
		SimulationParametersControllerFactory.singleton = singleton;
	}
	

}
