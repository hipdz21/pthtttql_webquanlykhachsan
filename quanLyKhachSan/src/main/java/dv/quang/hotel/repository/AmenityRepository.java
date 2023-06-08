/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

import dv.quang.hotel.entity.AmenityEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dovan
 */
@Repository
public interface AmenityRepository extends JpaRepository<AmenityEntity, Integer>{

    @Query(value = "SELECT * FROM tbl_amenity ta WHERE ta.amenity_name LIKE ?1", nativeQuery = true)
    public List<AmenityEntity> findAllByAmenityName(String amenityName);
    
}
