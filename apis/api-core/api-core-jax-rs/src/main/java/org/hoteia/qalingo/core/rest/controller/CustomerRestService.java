package org.hoteia.qalingo.core.rest.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hoteia.qalingo.core.pojo.customer.CustomerPojo;
import org.hoteia.qalingo.core.service.pojo.CustomerPojoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/customer/")
@Component("customerRestService")
public class CustomerRestService {

    @Autowired
    private CustomerPojoService customerService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerPojo> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerPojo getCustomerById(@PathParam("id") final String id) {
        return customerService.getCustomerById(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveOrUpdate(final CustomerPojo customerJsonPojo) throws Exception {
        customerService.saveOrUpdate(customerJsonPojo);
    }

}
