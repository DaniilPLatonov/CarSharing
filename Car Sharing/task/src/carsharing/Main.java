package carsharing;

import carsharing.DAO.CustomerDaoImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        H2DataBase.createTable();
        conMenu();
    }

    public static void conMenu() {
        Scanner scanner = new Scanner(System.in);
        Manager managerAct = new Manager();
        CustomerDaoImpl customerDao = new CustomerDaoImpl();
        while (true) {
            System.out.println("1. Log in as a manager\n" +
                    "2. Log in as a customer\n"
                    + "3. Create a customer\n"
                    + "0. Exit");
            int choose = scanner.nextInt();
            switch (choose) {
                case 1 -> managerAct.manageAction();
                case 2 -> customerDao.companyList();
                case 3 -> customerDao.createCustomer();
                case 0 -> {
                    H2DataBase.closeConnection();
                    System.exit(0);
                }
            }
        }
    }


}