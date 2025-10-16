package model.dao.Impl;

import db.entities.DB;
import db.exceptions.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));
        return department;
    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setBirthDate(resultSet.getDate("BirthDate").toLocalDate());
        seller.setDepartment(department);
        return seller;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection.setAutoCommit(false);

            String sql = "INSERT INTO seller " +
                         "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                         "VALUES " +
                         "(?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, Date.valueOf(seller.getBirthDate()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());

            int rowsAffected = preparedStatement.executeUpdate();

            connection.commit();

            if (rowsAffected > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();

                System.out.println(resultSet.getInt(1) + " - " + seller.getName() + " Incluído com sucesso!");
            } else {
                System.out.println("No rows affected");
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new DbException(e.getMessage());
            } catch (SQLException ex) {
                throw new DbException(ex.getMessage());
            }
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResulSet(resultSet);
        }
    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;

        try {

            connection.setAutoCommit(false);

            Seller seller = findById(id);
            if (seller == null) {
                System.out.println("Seller not found!");
                return;
            }

            String sql = "DELETE FROM seller " +
                         "WHERE Id = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deleting Seller: " + seller.getName());
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
    public Seller findById(Integer id) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT seller.*, department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);
                return instantiateSeller(resultSet, department);
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
    public List<Seller> findAll() {
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            String sql = "SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY Name";

            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> departments = new HashMap<>();

            while (resultSet.next()) {
                Department dep = departments.get(resultSet.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(resultSet);
                    departments.put(resultSet.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(resultSet, dep);
                sellerList.add(seller);
            }

            Comparator<Seller> comp = Comparator.comparing(Seller::getId);

            return sellerList
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

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? " +
                    "ORDER BY Name";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, department.getId());

            resultSet = preparedStatement.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()) {

                Department dep = map.get(resultSet.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(resultSet, dep);
                sellers.add(seller);
            }

            Comparator<Seller> comp = Comparator.comparing(Seller::getId);

            return sellers
                    .stream()
                    .sorted(comp)
                    .toList();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResulSet(resultSet);
        }
    }
}
