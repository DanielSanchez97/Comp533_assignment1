package Assignment4;

import examples.gipc.counter.customization.ACustomDuplexReceivedCallInvokerFactory;
import examples.gipc.counter.customization.ACustomSerializerFactory;
import examples.gipc.counter.customization.FactorySetter;
import inputport.datacomm.duplex.object.DuplexObjectInputPortSelector;
import inputport.rpc.duplex.ADuplexSentCallCompleterFactory;
import inputport.rpc.duplex.DuplexReceivedCallInvokerSelector;
import inputport.rpc.duplex.DuplexSentCallCompleterSelector;
import serialization.SerializerSelector;

public class MyFactorySetter2 implements FactorySetter {
	@Override
	public void setFactories() {
		/*
		 * Determines the ports  for sending and
		 * receiving objects. Needed for sync receive, as you will
		 * define a new port that overrides the receive stub inherited
		 * from the regular GIPC port. Look at the set factory, 
		 * ACustomDuplexObjectInputPortFactory
		 */
		DuplexObjectInputPortSelector.setDuplexInputPortFactory(
				new MyCustomDuplexObjectInputPortFactory());
		
		
		/*
		 * Two alternatives for received call invoker factory, with one
		 * commented out. This factory determines the object that 
		 * actually calls a method of a remote object in response to
		 * a received message. You will need the commented out asynchronous 
		 * version when you implement blocking procedure calls. Look at
		 * each of the set factories, ACustomDuplexReceivedCallInvokerFactory and
		 * AnAsynchronousCustomDuplexReceivedCallInvokerFactory
		 */
		/*
		 * Make the selector thread invoke remote calls directtly - the synchronous
		 * threading approach to handling incoming procedure calls.
		 */
		DuplexReceivedCallInvokerSelector.setReceivedCallInvokerFactory(
				new ACustomDuplexReceivedCallInvokerFactory());	
		/*
		 * Make the selector thread invoke remote calls through a separate
		 * thread, the asynchronous threading approach to handling incoming procedure calls.
		 */
//		DuplexReceivedCallInvokerSelector.setReceivedCallInvokerFactory(
//				new AnAsynchronousCustomDuplexReceivedCallInvokerFactory());
		
		/*
		 * Determines the object that processes the return value, if any, of
		 * a remote call
		 */
		DuplexSentCallCompleterSelector.setDuplexSentCallCompleterFactory(
				new ADuplexSentCallCompleterFactory());
		
		
		/*
		 * This is for the serializer assignment, determines the serializer
		 */
		SerializerSelector.setSerializerFactory(new ACustomSerializerFactory());	
	}
}
