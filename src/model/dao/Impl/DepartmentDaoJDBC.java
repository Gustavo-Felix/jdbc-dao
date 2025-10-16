package model.dao.Impl;

import db.entities.DB;
import db.exceptions.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("Id"));
        department.setName(resultSet.getString("Name"));
        return department;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection.setAutoCommit(false);

            String sql = "INSERT INTO department (Name)" +
                         "VALUES " +
                         "(?)";

            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, department.getName());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();

                System.out.println(resultSet.getInt(1) + " - " + department.getName() + " Incluído com sucesso!");
            } else {
                System.out.println("No rows affected");
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException ex) {
                throw new DbException("Error trying to rollback! Caused by: " + ex.getMessage());
            }
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResulSet(resultSet);
        }

    }

    @Override
    public void update(Department department) {

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;

        try {

            connection.setAutoCommit(false);

            Department dep = findById(id);
            if (dep == null) {
                System.out.println("Department not found!");
                return;
            }

            String sql = "DELETE FROM department " +
                         "WHERE Id = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deleting department: " + dep.getName());
            } else {
                System.out.println("No rows affected");
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new DbException(e.getMessage());
            } catch (SQLException ex) {
                throw new DbException(ex.getMessage());
            }
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM department " +
                         "WHERE Id = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return instantiateDepartment(resultSet);
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResulSet(resultSet);
        }
    }

    @Override
    public List<Department> findAll() {
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            String sql = "SELECT * FROM department";

            statement = connection.createStatement();

            resultSet = statement.executeQuery(sql);

            List<Department> departmentList = new ArrayList<>();

            while (resultSet.next()) {
                Department dep = instantiateDepartment(resultSet);
                departmentList.add(dep);
            }

            Comparator<Department> comp = Comparator.comparing(Department::getId);

            return departmentList
                    .stream()
                    .sorted(comp)
                    .toList();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResulSet(resultSet);
        }
    }
}
