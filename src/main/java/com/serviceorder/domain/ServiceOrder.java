package com.serviceorder.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ServiceOrder.
 */
@Entity
@Table(name = "SERVICEORDER")
public class ServiceOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceOrder serviceOrder = (ServiceOrder) o;

        if ( ! Objects.equals(id, serviceOrder.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceOrder{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + "'" +
                ", description='" + description + "'" +
                '}';
    }
}
