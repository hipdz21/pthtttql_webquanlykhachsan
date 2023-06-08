/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

import dv.quang.hotel.entity.BookingEntiity;
import dv.quang.hotel.entity.UseServiceEntity;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dovan
 */
@Repository
public interface UseServiceRepository extends JpaRepository<UseServiceEntity, Integer>{

    public List<UseServiceEntity> findAllByBooking(BookingEntiity booking, Sort sort);
    
}
