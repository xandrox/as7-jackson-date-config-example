package com.example.jsontest;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("test")
public class TestEJB {
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Contact load(Contact c) {
		return new Contact();
	}

}
