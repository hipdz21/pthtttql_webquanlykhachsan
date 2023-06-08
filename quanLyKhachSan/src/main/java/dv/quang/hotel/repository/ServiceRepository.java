/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

import dv.quang.hotel.entity.ServiceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dovan
 */
@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Integer>{

    @Query(value = "SELECT * FROM tbl_service ts WHERE ts.service_name LIKE ?1", nativeQuery = true)
    public List<ServiceEntity> findAllByServiceName(String serviceName);
    
}
