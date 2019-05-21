package hw.caching;

import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.Optional;

public class DBCacheStorage implements CacheStorage {

    private final String path;
    private final String tableName;

    public DBCacheStorage(String cacheFile, String tableName) throws IOException {
        this.path = "jdbc:sqlite:" + cacheFile + ".db";
        this.tableName = "\"" + tableName + "\"";
    }

    @Override
    public void put(Object[] args, Object value) throws IOException {
        if (!(value instanceof Serializable)) {
            throw new IOException();
        }

        Serializable val = (Serializable) value;
        String key = generateKey(args);

        try (Connection connection = connectToDB();
             PreparedStatement preparedStatement = connection.prepareStatement(
                             "INSERT INTO " + tableName + "(argKey,cacheValue) VALUES(?,?)")) {
            preparedStatement.setString(1, key);
            preparedStatement.setString(2, StringSerializer.encode(val));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public Optional<Object> get(Object[] args) throws IOException {
        String key = generateKey(args);
        try (Connection connection = connectToDB()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT cacheValue FROM " + tableName
                            + " WHERE argKey = " + "\"" + key + "\"");
            if (rs.next()) {
                return Optional.of(StringSerializer.decode(rs.getString("cacheValue")));
            } else {
                return Optional.empty();
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    private Connection connectToDB() throws IOException {
        try {
            Connection connection = DriverManager.getConnection(path);
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + " ("
                    + " id integer PRIMARY KEY,"
                    + "	argKey text NOT NULL,"
                    + "	cacheValue text NOT NULL);");
            return connection;

        } catch (SQLException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void closeDBConnection(Connection connection) throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException(e.getMessage());
        }
    }

    private String generateKey(Object[] args) throws IOException {
        return StringSerializer.encode(args);
    }
}
