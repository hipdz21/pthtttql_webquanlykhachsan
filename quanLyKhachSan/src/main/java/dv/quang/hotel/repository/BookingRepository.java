/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

import dv.quang.hotel.entity.BookingEntiity;
import dv.quang.hotel.entity.RoomEntity;
import dv.quang.hotel.entity.UserEntity;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dovan
 */
@Repository
public interface BookingRepository extends JpaRepository<BookingEntiity, Integer> {

    @Query(value = "SELECT *\n"
            + "FROM tbl_booking tb \n"
            + "WHERE (tb.booking_status = 'Đã đặt' OR tb.booking_status = 'Đã nhận')\n"
            + "AND tb.booking_date BETWEEN ?1 AND ?2", nativeQuery = true)
    public List<BookingEntiity> findAllByDate(Date checkInDate, Date checkOutDate);

    public List<BookingEntiity> findAllByUser(UserEntity user);

    public List<BookingEntiity> findAllByUser(UserEntity user, Sort sort);

    public List<BookingEntiity> findAllByRoom(RoomEntity room, Sort sort);

    @Query(value = "SELECT COUNT(*) FROM tbl_booking tb WHERE MONTH(tb.booking_date) = ?1", nativeQuery = true)
    public int getNumberBookingInMonth(int month);
}
