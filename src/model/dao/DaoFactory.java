package model.dao;

import model.dao.Impl.DepartmentDaoJDBC;
import model.dao.Impl.SellerDaoJDBC;

public class DaoFactory {

    // Para não precisar instanciar diretamente os DaoJDBCs

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC();
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC();
    }

}
