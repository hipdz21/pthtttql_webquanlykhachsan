/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author dovan
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "tbl_useService")
public class UseServiceEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int useServiceId;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateUse;
    private float servicePrice;
    private int serviceQuantity;
    private String useServiceNote;
    private String useServiceStatus;
    
    @ManyToOne
    @JoinColumn(name = "serviceId")
    private ServiceEntity service;
    
    @ManyToOne
    @JoinColumn(name = "bookingId")
    private BookingEntiity booking;
}
