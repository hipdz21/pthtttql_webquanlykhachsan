/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

import dv.quang.hotel.entity.BillBookingEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dovan
 */
@Repository
public interface BillBookingRepository extends JpaRepository<BillBookingEntity, Integer> {

    @Query(value = "SELECT SUM(tbb.total_amount) FROM tbl_bill_booking tbb \n"
            + "WHERE MONTH(tbb.bill_date) = ?1 \n"
            + "AND YEAR(tbb.bill_date) = 2023", nativeQuery = true)
    public double getTotalRevenueForMonth(int month);

    @Query(value = "SELECT DAY(tbb.bill_date) AS day,SUM(tbb.total_amount) AS total\n"
            + "FROM tbl_bill_booking tbb, tbl_bill_service tbs\n"
            + "WHERE MONTH(tbb.bill_date) = ?1\n"
            + "AND YEAR(tbb.bill_date) = 2023\n"
            + "GROUP BY day", nativeQuery = true)
    public List<Object[]> getTotalRevenueByDayInMonth(int month);

    @Query(value = "SELECT MONTH(tbb.bill_date) AS month,SUM(tbb.total_amount) AS total\n"
            + "FROM tbl_bill_booking tbb, tbl_bill_service tbs\n"
            + "WHERE YEAR(tbb.bill_date) = ?1\n"
            + "GROUP BY month", nativeQuery = true)
    public List<Object[]> getTotalRevenueByMonthInYear(int year);

    @Query(value = "SELECT YEAR(tbb.bill_date) AS year,SUM(tbb.total_amount) AS total\n"
            + "FROM tbl_bill_booking tbb, tbl_bill_service tbs\n"
            + "GROUP BY year", nativeQuery = true)
    public List<Object[]> getTotalRevenueByYear();

    @Query(value = "SELECT SUM(tbb.total_amount) AS doanh_thu_phong, SUM(tbs.total_amount) AS doanh_thu_dv\n"
            + "FROM tbl_bill_booking tbb, tbl_bill_service tbs\n"
            + "WHERE MONTH(tbb.bill_date) = ?1 AND MONTH(tbs.bill_date) = ?1\n"
            + "AND YEAR(tbs.bill_date) = 2023", nativeQuery = true)
    public List<Object[]> getTotalRevenueBookingAndUseServiceInMonth(int month);

    @Query(value = "SELECT MONTH(tbb.bill_date) AS month, SUM(tbb.total_amount) AS doanh_thu \n"
            + "FROM tbl_bill_booking tbb\n"
            + "WHERE YEAR(tbb.bill_date) = ?1\n"
            + "GROUP BY month", nativeQuery = true)
    public List<Object[]> getTotalRevenueBookingByMonthInYear(int year);

    @Query(value = "SELECT\n"
            + "  tbl_room_type.name_room_type,\n"
            + "  MONTH(tbl_bill_booking.bill_date) AS month,\n"
            + "  COUNT(*) AS so_luong\n"
            + "FROM tbl_bill_booking\n"
            + "  INNER JOIN tbl_booking\n"
            + "    ON tbl_bill_booking.booking_id = tbl_booking.booking_id\n"
            + "  INNER JOIN tbl_room\n"
            + "    ON tbl_booking.room_id = tbl_room.room_id\n"
            + "  INNER JOIN tbl_room_type\n"
            + "    ON tbl_room.room_type_id = tbl_room_type.room_type_id\n"
            + "WHERE YEAR(tbl_bill_booking.bill_date) = ?1\n"
            + "GROUP BY tbl_room_type.name_room_type,\n"
            + "         MONTH(tbl_bill_booking.bill_date)", nativeQuery = true)
    public List<Object[]> getNumberRoomTypeBookingInYear(int year);

    @Query(value = "SELECT\n"
            + "  tbl_room_type.name_room_type,\n"
            + "  MONTH(tbl_bill_booking.bill_date) AS month,\n"
            + "  SUM(tbl_bill_booking.total_amount) AS doanh_thu\n"
            + "FROM tbl_bill_booking\n"
            + "  INNER JOIN tbl_booking\n"
            + "    ON tbl_bill_booking.booking_id = tbl_booking.booking_id\n"
            + "  INNER JOIN tbl_room\n"
            + "    ON tbl_booking.room_id = tbl_room.room_id\n"
            + "  INNER JOIN tbl_room_type\n"
            + "    ON tbl_room.room_type_id = tbl_room_type.room_type_id\n"
            + "WHERE YEAR(tbl_bill_booking.bill_date) = ?1\n"
            + "GROUP BY tbl_room_type.name_room_type,\n"
            + "MONTH(tbl_bill_booking.bill_date)", nativeQuery = true)
    public List<Object[]> getTotalRevenueRoomTypeBookingInYear(int year);

    @Query(value = "SELECT\n"
            + "  tbl_room_type.name_room_type,\n"
            + "  SUM(tbl_bill_booking.total_amount) AS doanh_thu\n"
            + "FROM tbl_bill_booking\n"
            + "  INNER JOIN tbl_booking\n"
            + "    ON tbl_bill_booking.booking_id = tbl_booking.booking_id\n"
            + "  INNER JOIN tbl_room\n"
            + "    ON tbl_booking.room_id = tbl_room.room_id\n"
            + "  INNER JOIN tbl_room_type\n"
            + "    ON tbl_room.room_type_id = tbl_room_type.room_type_id\n"
            + "WHERE MONTH(tbl_bill_booking.bill_date) = ?1\n"
            + "AND YEAR(tbl_bill_booking.bill_date) = 2023\n"
            + "GROUP BY tbl_room_type.name_room_type,\n"
            + "MONTH(tbl_bill_booking.bill_date)", nativeQuery = true)
    public List<Object[]> getTotalRevenueRoomTypeBookingInMonth(int month);

    @Query(value = "SELECT\n"
            + "  tbl_room_type.name_room_type,\n"
            + "  COUNT(*) AS so_luong\n"
            + "FROM tbl_bill_booking\n"
            + "  INNER JOIN tbl_booking\n"
            + "    ON tbl_bill_booking.booking_id = tbl_booking.booking_id\n"
            + "  INNER JOIN tbl_room\n"
            + "    ON tbl_booking.room_id = tbl_room.room_id\n"
            + "  INNER JOIN tbl_room_type\n"
            + "    ON tbl_room.room_type_id = tbl_room_type.room_type_id\n"
            + "WHERE MONTH(tbl_bill_booking.bill_date) = ?1\n"
            + "AND YEAR(tbl_bill_booking.bill_date) = 2023\n"
            + "GROUP BY tbl_room_type.name_room_type,\n"
            + "         MONTH(tbl_bill_booking.bill_date)", nativeQuery = true)
    public List<Object[]> getNumberRoomTypeBookingInMonth(int month);

    @Query(value = "SELECT\n"
            + "  tbl_user.name,\n"
            + "  tbl_user.cccd,\n"
            + "  SUM(tbl_bill_booking.total_amount+tbl_bill_service.total_amount) AS doanh_thu\n"
            + "FROM tbl_bill_booking\n"
            + "  INNER JOIN tbl_booking\n"
            + "    ON tbl_bill_booking.booking_id = tbl_booking.booking_id\n"
            + "  INNER JOIN tbl_user\n"
            + "    ON tbl_booking.user_id = tbl_user.user_id\n"
            + "  CROSS JOIN tbl_bill_service\n"
            + "  INNER JOIN tbl_use_service\n"
            + "    ON tbl_bill_service.use_service_id = tbl_use_service.use_service_id\n"
            + "    AND tbl_use_service.booking_id = tbl_booking.booking_id\n"
            + "WHERE MONTH(tbl_bill_booking.bill_date) = ?1\n"
            + "AND YEAR(tbl_bill_booking.bill_date) = 2023\n"
            + "GROUP BY tbl_user.name,\n"
            + "         tbl_user.cccd\n"
            + "ORDER BY doanh_thu DESC\n"
            + "LIMIT 10", nativeQuery = true)
    public List<Object[]> getTop10ClientRevenueInMonth(int month);
}
