package rs.ac.bg.fon.CinemaServer.repository.db.impl;

import rs.ac.bg.fon.CinemaServer.repository.db.DbConnectionFactory;
import rs.ac.bg.fon.CinemaServer.repository.db.DbRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;

public class RepositoryDBGeneric implements DbRepository<GenericEntity> {

	@Override
	public List<GenericEntity> getAll(GenericEntity param) throws Exception {
		try {
			String sql = "select * from " + param.getTableName();
			System.out.println(sql);
			Connection connection = DbConnectionFactory.getInstance().getConnection();

			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);

			return param.readList(rs);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Error retriving objects from the database!");
		}
	}

	@Override
	public List<GenericEntity> getAll(GenericEntity param, String where) throws Exception {
		try {
			String sql = "select * from " + param.getTableName() + " where " + where;
			System.out.println(sql);
			Connection connection = DbConnectionFactory.getInstance().getConnection();

			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);

			return param.readList(rs);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Error retriving objects from the database!");
		}
	}

	@Override
	public List<GenericEntity> getAllLeftJoin(GenericEntity first, GenericEntity second, String where)
			throws Exception {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM ").append(first.getTableName()).append(" ").append(first.getAlias())
					.append(" JOIN ").append(second.getTableName()).append(" ").append(second.getAlias()).append(" ON ")
					.append(first.getAlias()).append(".").append(first.getForeignKey()).append(" = ")
					.append(second.getAlias()).append(".").append(second.getPrimaryKey()).append(" WHERE ")
					.append(where);
			String query = sb.toString();
			System.out.println(query);
			Connection connection = DbConnectionFactory.getInstance().getConnection();

			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			return first.readList(rs);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Error retriving objects from the database!");
		}
	}

	@Override
	public int addReturnKey(GenericEntity param) throws Exception {
		try {
			Connection connection = DbConnectionFactory.getInstance().getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO ").append(param.getTableName()).append(" (").append(param.getColumnNamesForInsert())
					.append(")").append(" VALUES (").append(param.getInsertValues()).append(")");
			String query = sb.toString();
			System.out.println(query);
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.executeUpdate();

			ResultSet rsKey = statement.getGeneratedKeys();

			if (rsKey.next()) {
				int id = rsKey.getInt(1);
				rsKey.close();
				statement.close();

				return id;
			}

			throw new Exception();

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new Exception("Error adding object to the database!");
		}
	}

	@Override
	public void add(GenericEntity param) throws Exception {
		try {
			Connection connection = DbConnectionFactory.getInstance().getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO ").append(param.getTableName()).append(" (").append(param.getColumnNamesForInsert())
					.append(")").append(" VALUES (").append(param.getInsertValues()).append(")");
			String query = sb.toString();
			System.out.println(query);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			statement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new Exception("Error adding object to the database!");
		}
	}

	@Override
	public void edit(GenericEntity entity) throws Exception {
		try {
			Connection connection = DbConnectionFactory.getInstance().getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE ").append(entity.getTableName()).append(" SET ").append(entity.getUpdateValues(entity))
					.append(" WHERE ").append(entity.getUpdateString());
			String query = sb.toString();
			System.out.println(query);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			statement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new Exception("Error editing object in the database!");
		}

	}

	@Override
	public void delete(GenericEntity entity) throws Exception {
		try {
			Connection connection = DbConnectionFactory.getInstance().getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM ").append(entity.getTableName()).append(" WHERE ").append(entity.getDeleteString());
			String query = sb.toString();
			System.out.println(query);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new Exception("Error deleting object from the database!");
		}

	}

	@Override
	public int getTwoJoinsGroupBy(GenericEntity first, GenericEntity second, GenericEntity third, String select,
			String where) throws Exception {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ").append(select).append(" FROM ").append(first.getTableName()).append(" ")
					.append(first.getAlias()).append(" JOIN ").append(second.getTableName()).append(" ")
					.append(second.getAlias()).append(" ON ").append(first.getAlias()).append(".")
					.append(first.getPrimaryKey()).append(" = ").append(second.getAlias()).append(".")
					.append(second.getPrimaryKey()).append(" JOIN ").append(third.getTableName()).append(" ")
					.append(third.getAlias()).append(" ON ").append(first.getAlias()).append(".")
					.append(first.getSecondForeignKey()).append(" = ").append(third.getAlias()).append(".")
					.append(third.getPrimaryKey()).append(" WHERE ").append(where);
			String query = sb.toString();
			System.out.println(query);
			Connection connection = DbConnectionFactory.getInstance().getConnection();

			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			if (rs.next()) {
				if (rs.getString("result") != null) {
					return rs.getInt("result");
				}
			}
			return -1;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Error retriving objects from the database!");
		}
	}

}
