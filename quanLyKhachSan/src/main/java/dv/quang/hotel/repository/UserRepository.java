/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

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
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query(value = "SELECT * FROM tbl_user tu WHERE tu.name LIKE ?1", nativeQuery = true)
    public List<UserEntity> findAllByName(String name);

    public List<UserEntity> findAllByUserType(String userType);

    public List<UserEntity> findAllByCccd(String cccd);

    public UserEntity findByEmail(String email);

    @Query(value = "SELECT \n"
            + "  CASE\n"
            + "    WHEN (2023 - year(u.dob)) < 20 THEN 'Under 20'\n"
            + "    WHEN (2023 - year(u.dob)) >= 20 AND (2023 - year(u.dob)) < 30 THEN '20-30'\n"
            + "    WHEN (2023 - year(u.dob)) >= 30 AND (2023 - year(u.dob)) < 40 THEN '30-40'\n"
            + "    WHEN (2023 - year(u.dob)) >= 40 AND (2023 - year(u.dob)) < 60 THEN '40-60'\n"
            + "    ELSE 'Over 60'\n"
            + "  END AS age_group,\n"
            + "  COUNT(*) AS so_luong\n"
            + "FROM tbl_user u\n"
            + "WHERE u.user_type = 'client'\n"
            + "GROUP BY age_group", nativeQuery = true)
    public List<Object[]> getNumberClientByGroupAge();

    @Query(value = "SELECT\n"
            + "  CASE WHEN 2023 - YEAR(u.dob) < 20 THEN 'Under 20' WHEN 2023 - YEAR(u.dob) >= 20 AND\n"
            + "      2023 - YEAR(u.dob) < 30 THEN '20-30' WHEN 2023 - YEAR(u.dob) >= 30 AND\n"
            + "      2023 - YEAR(u.dob) < 40 THEN '30-40' WHEN 2023 - YEAR(u.dob) >= 40 AND\n"
            + "      2023 - YEAR(u.dob) < 60 THEN '40-60' ELSE 'Over 60' END AS age_group,\n"
            + "  SUM(tbl_bill_booking.total_amount + tbl_bill_service.total_amount) AS doanh_thu\n"
            + "FROM tbl_booking\n"
            + "  INNER JOIN tbl_user u\n"
            + "    ON tbl_booking.user_id = u.user_id\n"
            + "  INNER JOIN tbl_bill_booking\n"
            + "    ON tbl_bill_booking.booking_id = tbl_booking.booking_id\n"
            + "  CROSS JOIN tbl_bill_service\n"
            + "  INNER JOIN tbl_use_service\n"
            + "    ON tbl_bill_service.use_service_id = tbl_use_service.use_service_id\n"
            + "    AND tbl_use_service.booking_id = tbl_booking.booking_id\n"
            + "WHERE u.user_type = 'client'\n"
            + "AND MONTH(tbl_bill_booking.bill_date) = ?1\n"
            + "AND YEAR(tbl_bill_booking.bill_date) = 2023\n"
            + "GROUP BY 1", nativeQuery = true)
    public List<Object[]> getEvenueClientByGroupAge(int month);
}
