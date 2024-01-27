package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.Account;
import org.example.domain.Sex;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountRepository {

    @Qualifier("masterJdbcTemplate")
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Qualifier("slaveJdbcTemplate")
    private final NamedParameterJdbcTemplate slaveJdbcTemplate;

    public Account insert(Account account) {
        if (account.getId() != null) {
            throw new IllegalStateException("Can't save user. Id must be null");
        }
        account.setId(UUID.randomUUID());
        Map<String, Object> p = Map.of(
                "id", account.getId() ,
                "first_name", account.getFirstName(),
                "last_name", account.getLastName(),
                "birth_date", account.getBirthDate(),
                "sex", account.getSex().toString(),
                "city", account.getCity(),
                "interests", account.getInterests(),
                "rank", account.getRank()
        );
        jdbcTemplate.update("insert into account values (:id, :first_name, :last_name, :birth_date, :sex, :city, :interests)", p);
        return account;
    }

    public void delete(UUID id) {
        jdbcTemplate.update("delete from account where id=:id", Map.of("id", id));
    }

    public Account findById(UUID id) {
        return slaveJdbcTemplate.query("select * from account where id = :id", Map.of("id", id), rs -> rs.next() ? account(rs) : null);
    }

    public List<Account> findLike(String firstName, String lastName) {
        String first = StringUtils.trimToEmpty(firstName).toUpperCase() + "%";
        String last = StringUtils.trimToEmpty(lastName).toUpperCase() + "%";
        return slaveJdbcTemplate.query(
                "select * from account where upper(first_name) like :first and upper(last_name) like :last order by id",
                Map.of("first", first, "last", last),
                rs -> (List<Account>) accountList(rs)
        );
    }

    public List<UUID> findAllIds(int pageNumber, int pageSize) {
        int offset = pageNumber * pageSize;
        return slaveJdbcTemplate.queryForList(
                "select id from account limit :limit offset :offset",
                Map.of("limit", pageSize, "offset", offset),
                UUID.class
        );
    }

    private List<Account> accountList(ResultSet rs) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        while (rs.next()) {
            accounts.add(account(rs));
        }
        return accounts;
    }

    private Account account(ResultSet rs) {
        try {
            Account u = new Account();
            u.setId(UUID.fromString(rs.getString("id")));
            u.setFirstName(rs.getString("first_name"));
            u.setLastName(rs.getString("last_name"));
            u.setBirthDate(Optional.ofNullable(rs.getDate("birth_date")).map(Date::toLocalDate).orElse(null));
            u.setSex(Sex.from(rs.getString("sex")));
            u.setCity(rs.getString("city"));
            u.setInterests(rs.getString("interests"));
            u.setRank(rs.getInt("rank"));
            return u;
        } catch (SQLException e) {
            log.error("Exception when data was extracting from ResultSet", e);
            return null;
        }
    }
}
