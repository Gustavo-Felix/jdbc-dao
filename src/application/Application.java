package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Application {

    public static void main(String[] args) {

        System.out.println("=== TEST 01 - seller findById ===");
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(1);

        System.out.println(seller);

        System.out.println();

        System.out.println("=== TEST 02 - department findById ===");
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Department department = departmentDao.findById(1);

        System.out.println(department);

    }

}
