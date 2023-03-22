package carsharing;

import carsharing.DAO.CarDaoImpl;
import carsharing.DAO.CompanyDaoImpl;
import carsharing.DAO.CustomerDaoImpl;
import carsharing.Models.Company;

import java.util.Scanner;

public class Manager {

    public void manageAction() {
        boolean check = true;
        Scanner scanner = new Scanner(System.in);
        CompanyDaoImpl companyDao = new CompanyDaoImpl();
        do {
            System.out.println("\n1. Company list\n2. Create a company\n0. Back");
            int choose = scanner.nextInt();
            switch (choose) {
                case 1 -> companyDao.companyList();
                case 2 -> companyDao.createCompany();
                case 0 -> check = false;
            }
        } while (check);
    }

    public static void cars(Company company) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("'%s' company\n%n", company.getName());
        boolean check = true;
        CarDaoImpl carDao = new CarDaoImpl();
        do {
            System.out.println("\n1. Car list\n" + "2. Create a car\n" + "0. Back");
            int carsList = scanner.nextInt();
            switch (carsList) {
                case 1 -> carDao.carsList(company);
                case 2 -> CarDaoImpl.createList(company);
                case 0 -> check = false;
            }

        } while (check);

    }

    public static void customers() {
        Scanner scanner = new Scanner(System.in);
        CustomerDaoImpl customer = new CustomerDaoImpl();
        boolean check = true;
        do {
            System.out.println("\n1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" + "0. Back");

            int carList = scanner.nextInt();
            switch (carList) {
                case 1 -> customer.rentACar();
                case 2 -> customer.deleteCar();
                case 3-> customer.currentRentalCar();
                case 0 -> check = false;
            }
        }
        while (check);
    }


}