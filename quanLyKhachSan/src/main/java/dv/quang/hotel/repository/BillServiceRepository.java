/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

import dv.quang.hotel.entity.BillServiceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dovan
 */
@Repository
public interface BillServiceRepository extends JpaRepository<BillServiceEntity, Integer> {

    @Query(value = "SELECT SUM(tbs.total_amount) FROM tbl_bill_service tbs WHERE MONTH(tbs.bill_date) = ?1", nativeQuery = true)
    public double getTotalRevenueForMonth(int month);

    @Query(value = "SELECT MONTH(tbs.bill_date) AS month, SUM(tbs.total_amount) AS doanh_thu \n"
            + "FROM tbl_bill_service tbs\n"
            + "WHERE YEAR(tbs.bill_date) = ?1\n"
            + "GROUP BY month", nativeQuery = true)
    public List<Object[]> getTotalRevenueUseServiceByMonthInYear(int year);

    @Query(value = "SELECT\n"
            + "  tbl_service.service_name,\n"
            + "  MONTH(tbl_bill_service.bill_date) AS month,\n"
            + "  COUNT(*) AS so_luong\n"
            + "FROM tbl_bill_service\n"
            + "  INNER JOIN tbl_use_service\n"
            + "    ON tbl_bill_service.use_service_id = tbl_use_service.use_service_id\n"
            + "  INNER JOIN tbl_service\n"
            + "    ON tbl_use_service.service_id = tbl_service.service_id\n"
            + "WHERE YEAR(tbl_bill_service.bill_date) = ?1\n"
            + "GROUP BY tbl_service.service_name,\n"
            + "         MONTH(tbl_bill_service.bill_date)", nativeQuery = true)
    public List<Object[]> getNumberServiceUsedInYear(int year);

    @Query(value = "SELECT\n"
            + "  tbl_service.service_name,\n"
            + "  MONTH(tbl_bill_service.bill_date) AS month,\n"
            + "  SUM(tbl_bill_service.total_amount) AS doanh_thu\n"
            + "FROM tbl_bill_service\n"
            + "  INNER JOIN tbl_use_service\n"
            + "    ON tbl_bill_service.use_service_id = tbl_use_service.use_service_id\n"
            + "  INNER JOIN tbl_service\n"
            + "    ON tbl_use_service.service_id = tbl_service.service_id\n"
            + "WHERE YEAR(tbl_bill_service.bill_date) = ?1\n"
            + "GROUP BY tbl_service.service_name,\n"
            + "         MONTH(tbl_bill_service.bill_date)", nativeQuery = true)
    public List<Object[]> getTotalRevenueServiceInYear(int year);

    @Query(value = "SELECT\n"
            + "  tbl_service.service_name,\n"
            + "  COUNT(*) AS expr1\n"
            + "FROM tbl_bill_service\n"
            + "  INNER JOIN tbl_use_service\n"
            + "    ON tbl_bill_service.use_service_id = tbl_use_service.use_service_id\n"
            + "  INNER JOIN tbl_service\n"
            + "    ON tbl_use_service.service_id = tbl_service.service_id\n"
            + "WHERE MONTH(tbl_bill_service.bill_date) = ?1\n"
            + "AND YEAR(tbl_bill_service.bill_date) = 2023\n"
            + "GROUP BY tbl_service.service_name", nativeQuery = true)
    public List<Object[]> getNumberServiceInMonth(int month);

    @Query(value = "SELECT\n"
            + "  tbl_service.service_name,\n"
            + "  SUM(tbl_bill_service.total_amount) AS doanh_thu\n"
            + "FROM tbl_bill_service\n"
            + "  INNER JOIN tbl_use_service\n"
            + "    ON tbl_bill_service.use_service_id = tbl_use_service.use_service_id\n"
            + "  INNER JOIN tbl_service\n"
            + "    ON tbl_use_service.service_id = tbl_service.service_id\n"
            + "WHERE MONTH(tbl_bill_service.bill_date) = ?1\n"
            + "AND YEAR(tbl_bill_service.bill_date) = 2023\n"
            + "GROUP BY tbl_service.service_name", nativeQuery = true)
    public List<Object[]> getTotalRevenueServiceInMonth(int month);
}
