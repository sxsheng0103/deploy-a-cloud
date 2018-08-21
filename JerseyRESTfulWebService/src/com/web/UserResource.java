package com.web;


import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Component;

@Component
@Path("/user")
public class UserResource {
	//@Produces(MediaType.TEXT_PLAIN)|@Produces(MediaType.TEXT_XML) | @Produces(MediaType.APPLICATION_JSON)
	@GET
	@Path("/users/{username}")
	@Produces("text/plain;charset=UTF-8")
	public String getUser(@PathParam("username") String username) {
		return username;
	}
	 
	@POST
	@Path("/users/save")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) //请求数据类型
	public void saveUser(@FormParam("id") String id,@FormParam("username") String username,
			@Context HttpServletRequest request,@Context HttpServletResponse response) {
		System.out.println(id+username);
	}
	public void errorException() {
		new WebApplicationException(Response.status(404).entity("message").type(MediaType.TEXT_PLAIN).build());
	}
}