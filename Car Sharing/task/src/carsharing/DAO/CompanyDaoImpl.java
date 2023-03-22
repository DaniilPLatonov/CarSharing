package carsharing.DAO;

import carsharing.H2DataBase;
import carsharing.Manager;
import carsharing.Models.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class CompanyDaoImpl implements AbstractDao<Company> {
    static Connection conn = H2DataBase.getConnection();

    public void createCompany() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the company name:");
        String nameOfCompany = scanner.nextLine();
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO COMPANY" +
                " (NAME) VALUES" +
                " (?);")) {
            st.setString(1, nameOfCompany);
            st.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }


    public void companyList() {

        List<Company> companyList = getList();
        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!\n");
            return;
        }
        Company company = getAbstractModel(companyList);
        if (company.getName() == null) {
            return;
        }
        Manager.cars(Objects.requireNonNull(company));
    }

    public static Company getAbstractModel(List<Company> companyList) {
        Company company = new Company();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a company: ");
        for (Company companyForList : companyList) {
            System.out.println(companyForList.toString());
        }
        System.out.println("0. Back");
        int chooseCompany = scanner.nextInt();
        if (chooseCompany == 0) {
            return company;
        }
        company = companyList
                .stream()
                .filter(chCom1 -> chCom1.getId() == chooseCompany)
                .findAny()
                .orElse(null);
        return company;
    }


    @Override
    public List<Company> getList() {
        List<Company> companyList = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            try (ResultSet pin = st.executeQuery("SELECT id, name FROM COMPANY")) {
                while (pin.next()) {
                    Company company = new Company();
                    int id = Integer.parseInt(pin.getString("id"));
                    String name = pin.getString("name");
                    company.setId(id);
                    company.setName(name);
                    companyList.add(company);
                }
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();

        }
        return companyList;
    }
}