package carsharing.DAO;

import carsharing.H2DataBase;
import carsharing.Manager;
import carsharing.Models.Cars;
import carsharing.Models.Company;
import carsharing.Models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static carsharing.DAO.CarDaoImpl.getCars;

public class CustomerDaoImpl implements AbstractDao<Customer> {
    static Connection conn = H2DataBase.getConnection();

    public static Customer getCustomer() {
        return customer;
    }
    private static Customer customer;
    static Company carForCompany;

    public void createCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the customer name:");
        String nameOfCustomer = scanner.nextLine();
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO CUSTOMER" +
                " (NAME) VALUES " +
                " (?);")) {
            st.setString(1, nameOfCustomer);
            st.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        System.out.println("The Customer was added!");
    }

    public void companyList() {
        List<Customer> customerList = getList();
        if (customerList.isEmpty()) {
            System.out.println("The customer list is empty!\n");
            return;
        }

        customer = getAbstractModel(customerList);
        if (customer.getName() == null) {
            return;
        }
        Manager.customers();
    }


    public static Customer getAbstractModel(List<Customer> companyList) {
        Scanner scanner = new Scanner(System.in);
        Customer customer = new Customer();
        System.out.println("Customer list: ");
        for (Customer customerForList : companyList) {
            System.out.println(customerForList.toString());
        }
        System.out.println("0. Back");
        int chooseCustomer = scanner.nextInt();
        if (chooseCustomer == 0) {
            return customer;
        }
        customer = companyList
                .stream()
                .filter(chCom -> chCom.getId() == chooseCustomer)
                .findAny()
                .orElse(null);

        return customer;
    }


    public void currentRentalCar() {
        Cars cars = new Cars();
        Company company = new Company();
        try {
            Statement stmt = conn.createStatement();
            String sqlCAR = String.format("SELECT * " +
                            "FROM CAR " +
                            "INNER JOIN CUSTOMER " +
                            "ON CAR.ID=CUSTOMER.RENTED_CAR_ID " +
                            "WHERE CUSTOMER.ID=%s",
                    customer.getId());
            ResultSet rs = stmt.executeQuery(sqlCAR);
            while (rs.next()) {
                cars.setId((rs.getInt("CAR.ID")));
                cars.setName(rs.getString("CAR.NAME"));
                cars.setComId(rs.getInt("CAR.COMPANY_ID"));
            }

            String sqlCOMPANY = String.format("SELECT * FROM COMPANY " +
                    "INNER JOIN CAR " +
                    "ON COMPANY.ID = CAR.COMPANY_ID " +
                    "WHERE CAR.ID = %d", cars.getId());
            ResultSet pin = stmt.executeQuery(sqlCOMPANY);
            while (pin.next()) {
                int id = Integer.parseInt(pin.getString("id"));
                String name = pin.getString("name");
                company.setId(id);
                company.setName(name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (cars.getName() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }

        System.out.println("Your rented car:\n" + cars.getName() + "\nCompany:\n" + company.getName());
    }


    public void deleteCar() {
        Customer customer2 = new Customer();
        try (Statement st = conn.createStatement()) {
            try (ResultSet pin = st.executeQuery(String.format("SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = %d", customer.getId()))) {
                while (pin.next()) {
                    String id = pin.getString("RENTED_CAR_ID");
                    customer2.setRentedID(id);
                }
            }
            if (customer2.getRentedID() == null) {
                System.out.println("You didn't rent a car!");
                return;
            }
            try (PreparedStatement st2 = conn.prepareStatement("UPDATE CUSTOMER SET " +
                    "RENTED_CAR_ID =? WHERE NAME =?")) {
                st2.setString(1, null);
                st2.setString(2, customer.getName());
                st2.executeUpdate();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            System.out.println("You've returned a rented car!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Customer> getList() {
        List<Customer> customerList = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            try (ResultSet pin = st.executeQuery("SELECT id, name FROM CUSTOMER")) {
                while (pin.next()) {
                    Customer customer = new Customer();
                    int id = Integer.parseInt(pin.getString("id"));
                    String name = pin.getString("name");
                    customer.setId(id);
                    customer.setName(name);
                    customerList.add(customer);
                }
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();

        }
        return customerList;
    }


    public void rentACar() {

        Customer customer1 = checkCustomer();
        if (customer1.getRentedID() != null) {
            System.out.println("You've already rented a car!");
            return;
        }
        CompanyDaoImpl companyDao = new CompanyDaoImpl();
        List<Company> companyList = companyDao.getList();

        if (companyList.isEmpty()) {
            System.out.println("You didn't rent a car!\n");
            return;
        }
        carForCompany = CompanyDaoImpl.getAbstractModel(companyList);
        if (carForCompany.getName() == null) {
            return;
        }
        List<Cars> carsList = getList2();
        if (carsList.isEmpty()) {
            System.out.println("You've already rented a car!");
            return;
        }

        Cars car = CarDaoImpl.getAbstractModel(carsList);
        if (car.getName() == null) {
            return;
        }
        addCar(Objects.requireNonNull(car));

    }


    public Customer checkCustomer() {
        Customer customer1 = new Customer();
        try {

            Statement stmt = conn.createStatement();
            try (ResultSet pin = stmt.executeQuery(String.format("SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = %d", CustomerDaoImpl.getCustomer().getId()))) {
                while (pin.next()) {
                    String id = pin.getString("RENTED_CAR_ID");
                    customer1.setRentedID(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer1;
    }

    public List<Cars> getList2() {
        String sql = String.format("SELECT car.id, car.name " +
                "FROM car LEFT JOIN customer " +
                "ON car.id = customer.rented_car_id " +
                "WHERE customer.name IS NULL AND CAR.COMPANY_ID = %d;", carForCompany.getId());

        return getCars(sql);
    }

    public void addCar(Cars car) {
        try (PreparedStatement st = conn.prepareStatement("UPDATE CUSTOMER SET " +
                "RENTED_CAR_ID =? WHERE NAME =?")) {
            st.setString(1, String.valueOf(car.getComId()));
            st.setString(2, CustomerDaoImpl.getCustomer().getName());
            st.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        System.out.println("You rented '" + car.getName() + "'");
    }


}
