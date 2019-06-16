/**
 * 
 */
package com.telecom.cep;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;

public class AlertEventProcessing {

	private static final String groupId = "com.telecom";
	private static final String artifactId = "Open-Ticket-App";
	private static final String version = "1.0.0-SNAPSHOT";

	private static AlertEventProcessing cepService = null;

	// Drools Fusion Runtime Configuration
	private KieServices kServices;
	private KieContainer kContainer;
	private KieSession kSession;

	// memory sizing and reporting activities
	static long totalFactCount = 0;

	public static LinkListStack issues = new LinkListStack();

	public static AlertEventProcessing getInstance() {

		if (cepService == null) {
			cepService = new AlertEventProcessing();
			cepService.init();
		}
		return cepService;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("Complex Event Processing is Testing Standalone...");

		System.setProperty("drools.dialect.mvel.strict", "false");
		System.setProperty("org.kie.demo", "false");

		System.out.println("Complex Event Processing - Test Standalone Events");
		System.out.println("==========================================================");

		AlertEventProcessing droolsCEPServiceL = AlertEventProcessing.getInstance();
		AlertEvent alertEvent = new AlertEvent();

		int k = 0;

		while (true) {
			int v = 100, s = 0;
			if (k % 2 == 0) {
				v = 255;
			}

			// 100 / 10s
			for (int i = 0; i <= 100; i++) {
				if (s > 25)
					s = 0;
				int a = (int) (Math.random() * 10);
				if (k % 2 == 0)
					a = 1;
				alertEvent = new AlertEvent();
				// alertEvent.set...
				droolsCEPServiceL.execute(alertEvent);
			}
			k++;

			// 1m - #poc_demo_scope_only
			Thread.sleep(10000);
		}
	}

	public void init() {

		try {

			System.out.println("initializing kie runtime for drools fusion...");
			
			kServices = KieServices.Factory.get();
		    kContainer = kServices.newKieContainer(kServices.newReleaseId( groupId, artifactId, version ));
		    kSession = kContainer.newKieSession( "default-stateful-kie-session" );
	        kSession.addEventListener( new DebugAgendaEventListener() );
	        kSession.addEventListener( new DebugRuleRuntimeEventListener() );
			kServices.getLoggers().newConsoleLogger(kSession);
	        kServices.getLoggers().newFileLogger( kSession, "./target/drools" );
			
			//kConfiguration = KieServices.Factory.get().newKieBaseConfiguration();
			//kConfiguration.setProperty("drools.dialect.mvel.strict", "false");
			//kConfiguration.setProperty("org.kie.demo", "false");
			//kConfiguration.setOption(EventProcessingOption.STREAM);			
			//kBase = kContainer.newKieBase(kConfiguration);
			//kBase.addKnowledgePackages(kBuilder.getKnowledgePackages());

			// clock type for the session
			//KieSessionConfiguration kSessionConfiguration = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
			//kSessionConfiguration.setOption(ClockTypeOption.get("realtime"));

			kSession.setGlobal("issueMap", new HashMap<Long, AlertEventCorrelation>());
			kSession.setGlobal("startTime", new Date().getTime());
			kSession.setGlobal("startMemory", Runtime.getRuntime().freeMemory());
			kSession.setGlobal("totalFactCount", totalFactCount);
			System.out.println("initialized the kie runtime for drools fusion...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static long prevTime = 0, currTime = 0;

	public void execute(AlertEvent event) {

		// try {
		System.out.println("received an Alert event in the complex event processing service...");

		// anything to with event object
		kSession.setGlobal("totalFactCount", totalFactCount++);
		System.out.println("running through the complex event processing rules..." + totalFactCount);
		kSession.insert(event);
		kSession.fireAllRules();

		HashMap issueM = (HashMap) kSession.getGlobal("issueMap");

		LinkedList list = new LinkedList();
		list.addAll(issueM.values());
		issues.pushAll(list);
		System.out.println("finished running through the complex event processing rules...");

		if (prevTime == 0)
			prevTime = Long.parseLong(kSession.getGlobal("startTime").toString());
		currTime = new Date().getTime();
	}
}
