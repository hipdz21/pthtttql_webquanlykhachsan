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
import javax.persistence.OneToOne;
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
@Table(name = "tbl_booking")
public class BookingEntiity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date bookingDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date checkInDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date checkOutDate;
    private float roomPrice;
    private float bookingDeposit;
    private String bookingStatus;
    private String bookingNote;
    private int roomTypeId;
    private int numberPeople;
    
    @ManyToOne
    @JoinColumn(name = "roomId")
    private RoomEntity room;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;
    
    @OneToOne
    @JoinColumn(name = "clientId")
    private ClientEntity client;
}
