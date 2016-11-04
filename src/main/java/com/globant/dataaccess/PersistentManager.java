package com.globant.dataaccess;

import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;



public class PersistentManager {
		
	
	private static PersistenceManagerFactory pmfInstance; 

		private PersistentManager(){
		}

		public static PersistenceManagerFactory get() {
					
			Properties properties = new Properties();
			properties.setProperty("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
			properties.setProperty("javax.jdo.option.ConnectionURL","appengine");
			properties.setProperty("javax.jdo.option.NontransactionalRead","true");
			properties.setProperty("javax.jdo.option.NontransactionalWrite","true");
			properties.setProperty("javax.jdo.option.RetainValues","true");
			properties.setProperty("datanucleus.appengine.autoCreateDatastoreTxns","true");
			
			PersistenceManagerFactory pmfInstance =JDOHelper
					.getPersistenceManagerFactory(properties);
			
			
			return pmfInstance;
		}
		
}
