package carsharing.DAO;

import carsharing.H2DataBase;
import carsharing.Models.Cars;
import carsharing.Models.Company;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarDaoImpl implements AbstractDao<Cars> {
    static Connection conn = H2DataBase.getConnection();

    static Company carCompany;

    public static void createList(Company company) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the car name:");
        String nameOfCar = scanner.nextLine();
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO CAR" +
                " (NAME,COMPANY_ID) VALUES" +
                " (?,?);")) {
            st.setString(1, nameOfCar);
            st.setString(2, String.valueOf(company.getId()));
            st.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        System.out.println("The car was added!");
    }

    public void carsList(Company company) {
        carCompany = company;
        List<Cars> carsList = getList();
        if (carsList.isEmpty()) {
            System.out.println("The car list is empty!\n");
            return;
        }

        System.out.println("Car list: ");
        for (Cars cars : carsList) {
            System.out.println(cars.toString());
        }
    }

    public static Cars getAbstractModel(List<Cars> carsList) {
        Scanner scanner = new Scanner(System.in);
        Cars cars = new Cars();
        System.out.println("Choose a car:");
        for (Cars carForList : carsList) {
            System.out.println(carForList.toString());
        }
        System.out.println("0. Back");
        int chooseCar = scanner.nextInt();
        if (chooseCar == 0) {
            return cars;
        }
        cars = carsList
                .stream()
                .filter(chCom -> chCom.getId() == chooseCar)
                .findAny()
                .orElse(null);
        return cars;
    }

    @Override
    public List<Cars> getList() {
        String sql = String.format("SELECT * FROM CAR " +
                        "WHERE COMPANY_ID=%s"
                , carCompany.getId());
        return getCars(sql);
    }

    static List<Cars> getCars(String sql) {
        List<Cars> carsList = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int i = 1;
            while (rs.next()) {
                Cars car = new Cars();
                car.setId(i);
                car.setName(rs.getString("NAME"));
                car.setComId(rs.getInt("ID"));
                carsList.add(car);
                i++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return carsList;
    }


}
