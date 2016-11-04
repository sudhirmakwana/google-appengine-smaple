package com.globant.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cloudsql/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClouldSQLController {

	@RequestMapping(value="/add", method = RequestMethod.GET)
	public ResponseEntity<String> cloudSQLops(){
		
		// store only the first two octets of a users ip address
	    String userIp = "10.10";
		
	   final String createTableSql = "CREATE TABLE IF NOT EXISTS visits ( visit_id INT NOT NULL "
	        + "AUTO_INCREMENT, user_ip VARCHAR(46) NOT NULL, timestamp DATETIME NOT NULL, "
	        + "PRIMARY KEY (visit_id) )";
	    final String createVisitSql = "INSERT INTO visits (user_ip, timestamp) VALUES (?, ?)";
	    final String selectSql = "SELECT user_ip, timestamp FROM visits ORDER BY timestamp DESC "
	        + "LIMIT 10";
	    
	    
	    String url;
	    if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
	           url = System.getProperty("ae-cloudsql.cloudsql-database-url");
	      try {
	        // Load the class that provides the new "jdbc:google:mysql://" prefix.
	        Class.forName("com.mysql.jdbc.GoogleDriver");
	      } catch (ClassNotFoundException e) {
	    	  e.printStackTrace();
	        
	      }
	    } else {
	      // Set the url with the local MySQL database connection url when running locally
	      url = System.getProperty("ae-cloudsql.local-database-url");
	    }
	    try{
	    		
	    	Connection conn = DriverManager.getConnection(url);
	    		
	        PreparedStatement statementCreateVisit = conn.prepareStatement(createVisitSql);
	        
	      conn.createStatement().executeUpdate(createTableSql);
	      statementCreateVisit.setString(1, userIp);
	      statementCreateVisit.setTimestamp(2, new Timestamp(new Date().getTime()));
	      statementCreateVisit.executeUpdate();

	      ResultSet rs = conn.prepareStatement(selectSql).executeQuery();
	       
	        while (rs.next()) {
	          String savedIp = rs.getString("user_ip");
	          String timeStamp = rs.getString("timestamp");
	        }
	      }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
		return new ResponseEntity<String>("Complete",HttpStatus.OK);
	}
}
