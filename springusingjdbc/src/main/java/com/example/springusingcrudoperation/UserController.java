package com.example.springusingcrudoperation;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    // Create (Insert)
    @PostMapping
    public String addUser(@RequestBody User user) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setInt(3, user.getAge());
            statement.executeUpdate();
            return "User added successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding user.";
        }
    }

    // Read (Get All)
    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getInt("age")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Update
    @PutMapping("/{id}")
    public String updateUser(@PathVariable int id, @RequestBody User user) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "UPDATE users SET name = ?, email = ?, age = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setInt(3, user.getAge());
            statement.setInt(4, id);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0 ? "User updated successfully!" : "User not found.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating user.";
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0 ? "User deleted successfully!" : "User not found.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting user.";
        }
    }
}
