package jcash.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jcash.database.DatabaseConnection;
import jcash.model.Account;

public class AccountRepository {

    public boolean createAccount(int userId) {

        String sql = """
                INSERT INTO accounts (user_id, balance)
                VALUES (?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);
            statement.setBigDecimal(2, BigDecimal.ZERO);

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Account findByUserId(int userId) {

        String sql = """
                SELECT id, user_id, balance, created_at
                FROM accounts
                WHERE user_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                return new Account(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getBigDecimal("balance"),
                        resultSet.getTimestamp("created_at")
                                .toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean decreaseBalance(
            Connection connection,
            int accountId,
            BigDecimal amount
    ) {

        String sql = """
                UPDATE accounts
                SET balance = balance - ?
                WHERE id = ?
                AND balance >= ?
                """;

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setBigDecimal(1, amount);
            statement.setInt(2, accountId);
            statement.setBigDecimal(3, amount);

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean increaseBalance(
            Connection connection,
            int accountId,
            BigDecimal amount
    ) {

        String sql = """
                UPDATE accounts
                SET balance = balance + ?
                WHERE id = ?
                """;

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setBigDecimal(1, amount);
            statement.setInt(2, accountId);

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}