package com.goit.javadev.tables.entity.customer;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CustomerDaoService {

    private static final String TABLE_NAME = "`homework_4`.customers";
    private PreparedStatement insertSt;
    private PreparedStatement updateContractEntityFieldsSt;
    private PreparedStatement updateContractNameFieldSt;
    private PreparedStatement getEntityByIdSt;
    private PreparedStatement getBySpecificFieldLikeSt;
    private PreparedStatement getAllEntitiesSt;
    private PreparedStatement deleteById;
    private PreparedStatement clearTableSt;
    private PreparedStatement getMaxIdSt;


    public CustomerDaoService(Connection connection) throws SQLException {

        getMaxIdSt = connection.prepareStatement(
                "SELECT max(id) AS maxId FROM " + TABLE_NAME
        );

        insertSt = connection.prepareStatement(
                "INSERT INTO " + TABLE_NAME + " (name, business_sphere) VALUES (?, ?)"
        );

        updateContractEntityFieldsSt = connection.prepareStatement(
                "UPDATE " + TABLE_NAME + " SET name = ?, business_sphere = ? WHERE id = ?"
        );

        updateContractNameFieldSt = connection.prepareStatement(
                "UPDATE " + TABLE_NAME + " SET name = ? WHERE id = ?"
        );

        getEntityByIdSt = connection.prepareStatement(
                "SELECT id, name, business_sphere FROM " +
                        TABLE_NAME + " WHERE id = ?"
        );

        getBySpecificFieldLikeSt = connection.prepareStatement(
                "SELECT id, name, business_sphere FROM " +
                        TABLE_NAME + " WHERE name LIKE ?"
        );

        getAllEntitiesSt = connection.prepareStatement(
                "SELECT id, name, business_sphere FROM " + TABLE_NAME
        );

        deleteById = connection.prepareStatement(
                "DELETE FROM " + TABLE_NAME + " WHERE ID = ?"
        );

        clearTableSt = connection.prepareStatement(
                "DELETE FROM " + TABLE_NAME
        );
    }

    public int deleteEntitiesFromListById (long[] ids) throws SQLException {
        int result = 0;
        for (long id: ids) {
            deleteById.setLong(1, id);
            deleteById.addBatch();
            result++;
        }
        deleteById.executeBatch();
        if (ids.length > 1) {
            log.info("Attention! " + ids.length + " records were deleted");
        } else if (ids.length == 1) {
            log.info("Attention! " + ids.length + " record was deleted");
        }
        return result;
    }

    public boolean deleteById (long id) throws SQLException {
        try {
            deleteById.setLong(1, id);
            deleteById.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public long insertNewEntity(Customer customer) throws SQLException {
        insertSt.setString(1, customer.getName());
        insertSt.setString(2, customer.getBusinessSphere());
        insertSt.executeUpdate();

        long id;
        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getLong("maxId");
        }
        return id;
    }

    public void clearTable() throws SQLException {
        clearTableSt.executeUpdate();
        log.info(TABLE_NAME + " has been cleared");
    }

    public int insertNewEntitiesFromJsonFile(String jsonFilePath) throws SQLException {
        Gson gson = new Gson();
        String inString = null;
        try {
            inString = String.join(
                    "\n",
                    Files.readAllLines(Paths.get(jsonFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Customer[] customers = gson.fromJson(inString, Customer[].class);
        List<Customer> customerList =
                Arrays.stream(customers)
                        .collect(Collectors.toList());

        createEntityFromList(customerList);
        if (customerList.size() > 1) {
            log.info("Created " + customerList.size() + " new records from JSON");
        } else if (customerList.size() == 1) {
            log.info("Created " + customerList.size() + "new record from JSON");
        }
        return customerList.size();
    }

    public int createEntityFromList(List<Customer> customerList) throws SQLException {
        for (int i = 0; i < customerList.size(); i++) {

            String name = customerList.get(i).getName();
            String business_sphere = customerList.get(i).getBusinessSphere();

            insertSt.setString(1, name);
            insertSt.setString(2, business_sphere);
            insertSt.addBatch();
        }
        if (customerList.size() > 1) {
            log.info("Insert " + customerList.size() + " new records");
        } else if (customerList.size() == 1) {
            log.info("Insert " + customerList.size() + "new record");
        }
        insertSt.executeBatch();

        return customerList.size();
    }

    public boolean updateContractEntityFieldsStOfCustomerById(Customer customer, long id) {
        try {
            updateContractEntityFieldsSt.setString(1, customer.getName());
            updateContractEntityFieldsSt.setString(2, customer.getBusinessSphere());
            updateContractEntityFieldsSt.setLong(3, id);
            return updateContractEntityFieldsSt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Customer getEntityById(long id) throws SQLException {
        getEntityByIdSt.setLong(1, id);

        try (ResultSet rs = getEntityByIdSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            Customer customer = new Customer();
            customer.setId(id);
            customer.setName(rs.getNString("name"));
            customer.setBusinessSphere(rs.getString("business_sphere"));
            log.info("get Customer by id: " + id);
            return customer;
        }
    }

    public List<Customer> getCustomerBySpecificFieldLike(String query) throws SQLException {
        getBySpecificFieldLikeSt.setString(1, "%" + query + "%");
        log.info("get list of Customers by:  query: " + "\"" + query + "\"");
        return getCustomers(getBySpecificFieldLikeSt);
    }

    public List<Customer> getAllEntities() throws SQLException {
        return getCustomers(getAllEntitiesSt);
    }

    private List<Customer> getCustomers(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getNString("name"));
                customer.setBusinessSphere(rs.getString("business_sphere"));
                customers.add(customer);

            }
            log.info("Received list of: " + customers.size() + " Customers");
            return customers;
        }
    }
}

