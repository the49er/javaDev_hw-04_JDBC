package com.goit.javadev.tables.entity.customer;

import com.goit.javadev.exception.DaoException;
import com.goit.javadev.tables.entity.crudEntityDAO;
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
public class CustomerDaoService implements crudEntityDAO<Customer> {

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

    @Override
    public int deleteEntitiesFromListById(long[] ids) {
        try {
            int result = 0;
            for (long id : ids) {
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
        }catch (DaoException | SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            deleteById.setLong(1, id);
            deleteById.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public long insertNewEntity(Customer customer) {
        try {
            insertSt.setString(1, customer.getName());
            insertSt.setString(2, customer.getBusinessSphere());
            insertSt.executeUpdate();

            long id;
            try (ResultSet rs = getMaxIdSt.executeQuery()) {
                rs.next();
                id = rs.getLong("maxId");
            }
            return id;
        } catch (DaoException | SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean clearTable() {
        try {
            clearTableSt.executeUpdate();
            log.info(TABLE_NAME + " has been cleared");
            return true;
        }catch (DaoException | SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public int insertEntitiesFromJsonFile(String jsonFilePath) {
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

        insertNewEntities(customerList);
        if (customerList.size() > 1) {
            log.info("Created " + customerList.size() + " new records from JSON");
        } else if (customerList.size() == 1) {
            log.info("Created " + customerList.size() + "new record from JSON");
        }
        return customerList.size();
    }

    @Override
    public int insertNewEntities(List<Customer> customerList) {
        try {
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
        }catch (DaoException | SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateEntityFieldsById(Customer customer, long id) {
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

    @Override
    public Customer getEntityById(long id) {
        try {
            getEntityByIdSt.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
        }catch (DaoException | SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public List<Customer> getAllEntities() {
        return getCustomers(getAllEntitiesSt);
    }

    private List<Customer> getCustomers(PreparedStatement st){
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
        } catch (DaoException | SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

