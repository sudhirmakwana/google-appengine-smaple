package com.globant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import java.util.logging.Level;

@RestController
@RequestMapping(value = "/memcache/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemcacheController {

	@RequestMapping(value="/addstate", method = RequestMethod.GET)
	public ResponseEntity<String> addValueToCache(@RequestParam String key,@RequestParam String value) {
		
		
		 MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		    syncCache.put(key, value);
		
		return new ResponseEntity<String>("Value Added in Cache Key:" +key+" Value:"+value,HttpStatus.OK);
		
		
		
	}
	
	
	@RequestMapping(value="/getvalue", method = RequestMethod.GET)
	public ResponseEntity<String> addValueToCache(@RequestParam String key) {
		
		
		 MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		    String value = (String) syncCache.get(key);
		    
		
		return new ResponseEntity<String>("Got Cached data Key:" +key+" Value:"+value,HttpStatus.OK);
		
		
		
	}
}
