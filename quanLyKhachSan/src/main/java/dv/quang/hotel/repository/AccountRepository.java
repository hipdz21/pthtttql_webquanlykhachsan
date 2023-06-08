/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dv.quang.hotel.repository;

import dv.quang.hotel.entity.AccountEntity;
import dv.quang.hotel.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dovan
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    public AccountEntity findByUser(UserEntity user);

    public AccountEntity findByUsername(String username);

    @Query(value = "SELECT COUNT(*) FROM tbl_account ta WHERE ta.account_type = 'client' AND MONTH(ta.creation_date) = ?1", nativeQuery = true)
    public int getNumberClientInMonth(int month);
}
