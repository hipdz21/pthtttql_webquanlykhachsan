/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

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
public interface RoomTypeRepositore extends JpaRepository<RoomTypeEntity, Integer> {

    @Query(value = "SELECT * FROM tbl_room_type trt WHERE trt.name_room_type LIKE ?1", nativeQuery = true)
    public List<RoomTypeEntity> findAllByNameRoomType(String nameRoomType);

    @Query(value = "SELECT\n"
            + "  tbl_room_type.name_room_type,\n"
            + "  COUNT(*) AS so_luong\n"
            + "FROM tbl_room\n"
            + "  INNER JOIN tbl_room_type\n"
            + "    ON tbl_room.room_type_id = tbl_room_type.room_type_id\n"
            + "GROUP BY tbl_room_type.name_room_type", nativeQuery = true)
    public List<Object[]> getNumberRoomByRoomType();
}
