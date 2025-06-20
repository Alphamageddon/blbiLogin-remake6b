package com.blbilink.blbilogin.modules;


import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.load.LoadFunction;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sqlite {

    private Connection connection;
    private final BlbiLogin plugin;

    public static Sqlite getSqlite() {
        return LoadFunction.sqlite;
    }

    public Sqlite(BlbiLogin plugin) {
        this.plugin = plugin;
        try {
            File dbFile = new File(plugin.getDataFolder(), "players.db");
            dbFile.getParentFile().mkdirs();
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getPath());
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS players (uuid TEXT PRIMARY KEY, username TEXT, password TEXT)")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to initialize sqlite database");
            e.printStackTrace();
        }
    }

    public boolean playerExists(String uuid) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void registerPlayer(String uuid, String username, String password) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO players (uuid, username, password) VALUES (?, ?, ?)")) {
            statement.setString(1, uuid);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPassword(String uuid, String password) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT password FROM players WHERE uuid = ?")) {
            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getString("password").equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resetPassword(String uuid, String newPassword) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE players SET password = ? WHERE uuid = ?")) {
            statement.setString(1, newPassword);
            statement.setString(2, uuid);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}