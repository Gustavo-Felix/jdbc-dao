package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public class Application {

    public static void main(String[] args) {

        System.out.println("=== TEST 01 - seller findById ===");
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(1);

        System.out.println(seller);

        System.out.println("\n=== TEST 02 - department findById ===");
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Department department = departmentDao.findById(1);

        System.out.println(department);

        System.out.println("\n=== TEST 03 - seller findByDepartment ===");
        Department department1 = new Department(2, null);
        List<Seller> sellers = sellerDao.findByDepartment(department1);

        sellers.forEach(System.out::println);
    }

}
