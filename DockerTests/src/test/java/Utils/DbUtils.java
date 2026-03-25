package Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtils {

    /**
     * Выполняет SQL команды из файла
     * @param fileName имя файла в папке src/test/resources/sql/
     */
    public static void executeSqlFile(String fileName) {
        String sql = readSqlFromFile(fileName);

        try (Connection conn = DriverManager.getConnection(
                ConfigProvider.getDbUrl(),
                ConfigProvider.getDbUser(),
                ConfigProvider.getDbPassword());
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("SQL Script executed: " + fileName);

        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL: " + e.getMessage(), e);
        }
    }

    private static String readSqlFromFile(String fileName) {
        try {
            // Ищем файл в ресурсах или по прямому пути
            Path path = Paths.get("src/test/resources/sql/" + fileName);
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not read SQL file: " + fileName, e);
        }
    }
}
