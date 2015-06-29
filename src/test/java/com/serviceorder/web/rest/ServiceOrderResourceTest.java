package com.serviceorder.web.rest;

import com.serviceorder.Application;
import com.serviceorder.domain.ServiceOrder;
import com.serviceorder.repository.ServiceOrderRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ServiceOrderResource REST controller.
 *
 * @see ServiceOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ServiceOrderResourceTest {

    private static final String DEFAULT_ORDER_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_ORDER_NUMBER = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private ServiceOrderRepository serviceOrderRepository;

    private MockMvc restServiceOrderMockMvc;

    private ServiceOrder serviceOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceOrderResource serviceOrderResource = new ServiceOrderResource();
        ReflectionTestUtils.setField(serviceOrderResource, "serviceOrderRepository", serviceOrderRepository);
        this.restServiceOrderMockMvc = MockMvcBuilders.standaloneSetup(serviceOrderResource).build();
    }

    @Before
    public void initTest() {
        serviceOrder = new ServiceOrder();
        serviceOrder.setOrderNumber(DEFAULT_ORDER_NUMBER);
        serviceOrder.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createServiceOrder() throws Exception {
        int databaseSizeBeforeCreate = serviceOrderRepository.findAll().size();

        // Create the ServiceOrder
        restServiceOrderMockMvc.perform(post("/api/serviceOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceOrder)))
                .andExpect(status().isCreated());

        // Validate the ServiceOrder in the database
        List<ServiceOrder> serviceOrders = serviceOrderRepository.findAll();
        assertThat(serviceOrders).hasSize(databaseSizeBeforeCreate + 1);
        ServiceOrder testServiceOrder = serviceOrders.get(serviceOrders.size() - 1);
        assertThat(testServiceOrder.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testServiceOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllServiceOrders() throws Exception {
        // Initialize the database
        serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get all the serviceOrders
        restServiceOrderMockMvc.perform(get("/api/serviceOrders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getServiceOrder() throws Exception {
        // Initialize the database
        serviceOrderRepository.saveAndFlush(serviceOrder);

        // Get the serviceOrder
        restServiceOrderMockMvc.perform(get("/api/serviceOrders/{id}", serviceOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serviceOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceOrder() throws Exception {
        // Get the serviceOrder
        restServiceOrderMockMvc.perform(get("/api/serviceOrders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceOrder() throws Exception {
        // Initialize the database
        serviceOrderRepository.saveAndFlush(serviceOrder);

		int databaseSizeBeforeUpdate = serviceOrderRepository.findAll().size();

        // Update the serviceOrder
        serviceOrder.setOrderNumber(UPDATED_ORDER_NUMBER);
        serviceOrder.setDescription(UPDATED_DESCRIPTION);
        restServiceOrderMockMvc.perform(put("/api/serviceOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceOrder)))
                .andExpect(status().isOk());

        // Validate the ServiceOrder in the database
        List<ServiceOrder> serviceOrders = serviceOrderRepository.findAll();
        assertThat(serviceOrders).hasSize(databaseSizeBeforeUpdate);
        ServiceOrder testServiceOrder = serviceOrders.get(serviceOrders.size() - 1);
        assertThat(testServiceOrder.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testServiceOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteServiceOrder() throws Exception {
        // Initialize the database
        serviceOrderRepository.saveAndFlush(serviceOrder);

		int databaseSizeBeforeDelete = serviceOrderRepository.findAll().size();

        // Get the serviceOrder
        restServiceOrderMockMvc.perform(delete("/api/serviceOrders/{id}", serviceOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceOrder> serviceOrders = serviceOrderRepository.findAll();
        assertThat(serviceOrders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
