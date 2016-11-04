package com.globant.controller;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globant.dataaccess.PersistentManager;
import com.globant.model.Customer;

@RestController
@RequestMapping(value = "/jdo/", produces = MediaType.APPLICATION_JSON_VALUE)
public class JDOController {

	
	@RequestMapping(value="/add", method = RequestMethod.GET)
	public ResponseEntity<Boolean> add(@RequestParam String name,@RequestParam String email) {

	    Date date = new Date();
	    Customer c = new Customer();
		c.setName(name);
		c.setEmail(email);
		c.setDate(new Date());

		PersistenceManager pm = PersistentManager.get().getPersistenceManager();
		try {
			pm.makePersistent(c);
		} finally {
			pm.close();
		}
                
           return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@RequestMapping(value="/search", method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> findCustomer(@RequestParam String name) {
			
		PersistenceManager pm = PersistentManager.get().getPersistenceManager();
		List<Customer> results=null;
		Query q = pm.newQuery(Customer.class);
		q.setFilter("name == nameParameter");
		q.setOrdering("date desc");
		q.declareParameters("String nameParameter");

		try {
			results = (List<Customer>) q.execute(name);
			return new ResponseEntity<List<Customer>>(results,HttpStatus.OK);
		} finally {
			q.closeAll();
			pm.close();
		}
		
	}
}
