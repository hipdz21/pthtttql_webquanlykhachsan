/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

import dv.quang.hotel.entity.RoomEntity;
import dv.quang.hotel.entity.RoomTypeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dovan
 */
@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer>{

    @Query(value = "SELECT * FROM tbl_room tr WHERE tr.room_name LIKE ?1", nativeQuery = true)
    public List<RoomEntity> findAllByRoomName(String roomName);

    public List<RoomEntity> findAllByRoomType(RoomTypeEntity roomType);
    
}
