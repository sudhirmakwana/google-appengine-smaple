package com.globant.controller;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

@RestController
@RequestMapping(value = "/dataservices/", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataStoreExample {

	@RequestMapping(value="/add", method = RequestMethod.GET)
	public ResponseEntity<Boolean> add(@RequestParam String name,@RequestParam String email) {

	    Date date = new Date();
                Entity customer = new Entity("Customer", name);
                customer.setProperty("name", name);
                customer.setProperty("email", email);
                customer.setProperty("date", date);

                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                datastore.put(customer);
                
           return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	
	//get all customers
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ResponseEntity<List<Entity>> listCustomer() {

			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query query =
	                      new Query("Customer").addSort("date", Query.SortDirection.DESCENDING);
		        List<Entity> customers =
	                      datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));

		       

			return new ResponseEntity<List<Entity>>(customers,HttpStatus.OK);

		}
		
		@RequestMapping(value="/transaction/test/{name}", method = RequestMethod.GET)
		public ResponseEntity<String> TestTransactions(@PathVariable String name) {
			
			
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try {
			               
		  Key customerKey = KeyFactory.createKey("Customer", name);
		  Entity customer = datastore.get(customerKey);
		  customer.setProperty("ssn", 10);

		  datastore.put(txn, customer);

		  txn.commit();
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<String>("Could Not Find Customer Try Another one",HttpStatus.OK);
		} finally {
		  if (txn.isActive()) {
		    txn.rollback();
		  }
		}
		return new ResponseEntity<String>("Transaction Completed Cheers",HttpStatus.OK);
		}
	
		//Create Entity Group in transactions boundry
		
		@RequestMapping(value="/transaction/entity/{name}", method = RequestMethod.GET)
		public ResponseEntity<String> testentityGroup(@PathVariable String name) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Entity customer = new Entity("Customer", name);
			datastore.put(customer);

			// Transactions on root entities
			Transaction txn = datastore.beginTransaction();

			Entity customer1;
			try {
				customer1 = datastore.get(customer.getKey());
				customer1.setProperty("age", 40);
				datastore.put(txn, customer1);
				txn.commit();

				// Transactions on child entities
				txn = datastore.beginTransaction();
				customer1 = datastore.get(customer.getKey());
				
			} catch (EntityNotFoundException e1) {
				return new ResponseEntity<String>("Customer Not Found"+name,HttpStatus.OK);
			}
			
			
			Entity photo = new Entity("Photo", customer.getKey());

			// Create a Photo that is a child of the Person entity named "tom"
			photo.setProperty("photoUrl", "http://domain.com/path/to/photo.jpg");
			datastore.put(txn, photo);
			txn.commit();
		
			return new ResponseEntity<String>("Transaction Completed Cheers",HttpStatus.OK);
		}
}
