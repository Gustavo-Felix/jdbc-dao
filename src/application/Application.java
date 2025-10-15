package application;

import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;

public class Application {

    public static void main(String[] args) {

        Department objDepartment = new Department(1, "Books");
        System.out.println("Department - " + objDepartment);

        Seller objSeller = new Seller(1, "Gustavo", "gustavo@gmail.com", LocalDate.now(), 3000.00, objDepartment);
        System.out.println("Seller - " + objSeller);

    }

}
