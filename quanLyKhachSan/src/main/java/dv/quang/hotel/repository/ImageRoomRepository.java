/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

import dv.quang.hotel.entity.ImageRoomEntity;
import dv.quang.hotel.entity.RoomTypeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dovan
 */
@Repository
public interface ImageRoomRepository extends JpaRepository<ImageRoomEntity, Integer>{

    public List<ImageRoomEntity> findAllByRoomType(RoomTypeEntity roomType);
    
}
