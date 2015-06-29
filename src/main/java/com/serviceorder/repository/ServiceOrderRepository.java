package com.serviceorder.repository;

import com.serviceorder.domain.ServiceOrder;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the ServiceOrder entity.
 */
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder,Long> {

}
