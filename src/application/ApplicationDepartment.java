package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;

public class ApplicationDepartment {

    public static void main(String[] args) {

        System.out.println("\n=== TEST 01 - department findById ===");
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Department department = departmentDao.findById(1);

        System.out.println(department);

        System.out.println("\n=== TEST 02 - department findAll ===");
        List<Department> departmentAll = departmentDao.findAll();

        departmentAll.forEach(System.out::println);

        System.out.println("\n=== TEST 03 - department INSERT ===");
        Department dep = new Department(null, "Taskflow");
        departmentDao.insert(dep);

        System.out.println("\n=== TEST 04 - department DELETE ===");
        departmentDao.deleteById(11);
    }

}
