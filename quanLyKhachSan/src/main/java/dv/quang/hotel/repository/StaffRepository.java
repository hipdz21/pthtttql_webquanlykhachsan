/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

import dv.quang.hotel.entity.StaffEntity;
import dv.quang.hotel.entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dovan
 */
@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Integer>{

    @Query(value = "SELECT * FROM tbl_staff ts WHERE ts.staff_code LIKE ?1", nativeQuery = true)
    public List<StaffEntity> findAllByStaffCode(String staffCode);

    public StaffEntity findByUser(UserEntity user);
    
}
