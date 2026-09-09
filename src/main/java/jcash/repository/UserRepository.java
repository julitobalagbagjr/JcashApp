package jcash.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jcash.database.DatabaseConnection;
import jcash.model.User;

public class UserRepository {

    public boolean saveUser(User user) {

        String sql = """
                INSERT INTO users (name, email, number, pin_hash)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getNumber());
            statement.setString(4, user.getPinHash());

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected == 0) {
                return false;
            }

            // Get the generated user ID
            ResultSet generatedKeys =
                    statement.getGeneratedKeys();

            if (generatedKeys.next()) {

                int generatedId =
                        generatedKeys.getInt(1);

                user.setId(generatedId);

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public User findByNumber(String number) {

        String sql = """
                SELECT id, name, email, number, pin_hash
                FROM users
                WHERE number = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, number);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("number"),
                        resultSet.getString("pin_hash")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updatePIN(
            int userId,
            String newPinHash
    ) {

        String sql = """
                UPDATE users
                SET pin_hash = ?
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, newPinHash);
            statement.setInt(2, userId);

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
