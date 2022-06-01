package com.goit.javadev.tables.manytomany.companycustomer;

import com.goit.javadev.exception.DaoException;
import com.goit.javadev.tables.manytomany.ManyToMany;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CompanyCustomerDaoService implements ManyToMany<CompanyCustomerKey> {
    PreparedStatement insertSt;
    PreparedStatement deleteSt;
    PreparedStatement getMaxIdSt;
    PreparedStatement getAllSt;
    private static final String TABLE_NAME = "`homework_4`.company_customer";

    public CompanyCustomerDaoService(Connection connection) throws SQLException {
        insertSt = connection.prepareStatement(
                "INSERT INTO " + TABLE_NAME + " (company_id, customer_id) VALUES (?, ?)"
        );

        deleteSt = connection.prepareStatement(
                "DELETE FROM " + TABLE_NAME + " WHERE company_id = ? and customer_id = ?"
        );

        getMaxIdSt = connection.prepareStatement(
                "SELECT count(company_id) AS maxId FROM " + TABLE_NAME
        );

        getAllSt = connection.prepareStatement(
                "SELECT company_id, customer_id FROM " + TABLE_NAME
        );
    }
    @Override
    public long insertKey(CompanyCustomerKey key) throws SQLException {
        insertSt.setLong(1, key.getCompanyId());
        insertSt.setLong(2, key.getCustomerId());
        insertSt.executeUpdate();

        long id;
        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getLong("maxId");
        }
        return id;
    }
    @Override
    public boolean deleteKey(int companyId, int customerId) throws SQLException {
        try {
            deleteSt.setInt(1, companyId);
            deleteSt.setInt(1, customerId);
            deleteSt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<List<CompanyCustomerKey>> getAll() {
        List<CompanyCustomerKey> list = new ArrayList<>();

        try (ResultSet rs = getAllSt.executeQuery()) {
            while (rs.next()){
                CompanyCustomerKey companyCustomerKey = new CompanyCustomerKey();
                companyCustomerKey.setCompanyId(rs.getInt("company_id"));
                companyCustomerKey.setCustomerId(rs.getInt("customer_id"));
                list.add(companyCustomerKey);

            }
        } catch (DaoException | SQLException ex) {
            ex.printStackTrace();
        }
        log.info("Received list of: " + list.size() + " company_customer keys");
        return Optional.of(list);
    }
}
