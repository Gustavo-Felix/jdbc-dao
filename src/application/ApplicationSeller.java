package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;
import java.util.List;

public class ApplicationSeller {

    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== TEST 01 - seller findById ===");
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(1);

        System.out.println(seller);

        System.out.println("\n=== TEST 02 - seller findByDepartment ===");
        Department department1 = new Department(2, null);
        List<Seller> sellersDepartment = sellerDao.findByDepartment(department1);

        sellersDepartment.forEach(System.out::println);

        System.out.println("\n=== TEST 03 - seller findAll ===");
        List<Seller> sellersAll = sellerDao.findAll();

        sellersAll.forEach(System.out::println);

        System.out.println("\n=== TEST 04 - seller INSERT ===");
        Seller seller1 = new Seller(null, "Matheus", "matheus@gmail.com", LocalDate.parse("2007-12-18"), 5000.00, departmentDao.findById(13));
        sellerDao.insert(seller1);

        System.out.println("\n=== TEST 05 - seller DELETE ===");
        sellerDao.deleteById(8);

    }

}
