package com.serviceorder.web.rest;

import com.serviceorder.web.depedency.SampleRestServiceClient;
import com.codahale.metrics.annotation.Timed;
import com.serviceorder.domain.ServiceOrder;
import com.serviceorder.repository.ServiceOrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;

/**
 * REST controller for managing ServiceOrder.
 */
@RestController
@RequestMapping("/api")
public class ServiceOrderResource {

    private final Logger log = LoggerFactory.getLogger(ServiceOrderResource.class);

    @Inject
    private ServiceOrderRepository serviceOrderRepository;

    @Inject
    private SampleRestServiceClient sampleRestServiceClient;

    /**
     * POST  /serviceOrders -> Create a new serviceOrder.
     */
    @RequestMapping(value = "/serviceOrders",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ServiceOrder serviceOrder) throws URISyntaxException {
        log.debug("REST request to save ServiceOrder : {}", serviceOrder);
        if (serviceOrder.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new serviceOrder cannot already have an ID").build();
        }
        //call some sample service
        sampleRestServiceClient.sampleHystrixCommand(new HashMap<String, Object>());
        //save
        serviceOrderRepository.save(serviceOrder);
        return ResponseEntity.created(new URI("/api/serviceOrders/" + serviceOrder.getId())).build();
    }

    /**
     * PUT  /serviceOrders -> Updates an existing serviceOrder.
     */
    @RequestMapping(value = "/serviceOrders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ServiceOrder serviceOrder) throws URISyntaxException {
        log.debug("REST request to update ServiceOrder : {}", serviceOrder);
        if (serviceOrder.getId() == null) {
            return create(serviceOrder);
        }
        serviceOrderRepository.save(serviceOrder);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /serviceOrders -> get all the serviceOrders.
     */
    @RequestMapping(value = "/serviceOrders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ServiceOrder> getAll() {
        log.debug("REST request to get all ServiceOrders");
        return serviceOrderRepository.findAll();
    }

    /**
     * GET  /serviceOrders/:id -> get the "id" serviceOrder.
     */
    @RequestMapping(value = "/serviceOrders/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceOrder> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get ServiceOrder : {}", id);
        ServiceOrder serviceOrder = serviceOrderRepository.findOne(id);
        if (serviceOrder == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(serviceOrder, HttpStatus.OK);
    }

    /**
     * DELETE  /serviceOrders/:id -> delete the "id" serviceOrder.
     */
    @RequestMapping(value = "/serviceOrders/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ServiceOrder : {}", id);
        serviceOrderRepository.delete(id);
    }
}
