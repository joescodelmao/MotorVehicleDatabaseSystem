package lab442;

import java.sql.*;
import java.util.Scanner;

public class example {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/test";

    // Database credentials
    static final String USER = "root";
    static final String PASS = "messi100";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // STEP 2: Open a connection to database
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            System.out.println("Creating database...");
            stmt = conn.createStatement();

            // STEP 3: Use SQL to Create Database
            String sql = "CREATE DATABASE IF NOT EXISTS VehicleOffice";
            try {
                stmt.executeUpdate(sql);
                System.out.println("Database created successfully...");
            } catch (SQLException e) {
                System.err.println("Error message: " + e.getMessage());
                System.err.println("Error number: " + e.getErrorCode());
            }

            // STEP 4: Use SQL to select the database
            sql = "USE VehicleOffice";
            try {
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                System.err.println("Error message: " + e.getMessage());
                System.err.println("Error number: " + e.getErrorCode());
            }

            // STEP 5: Use SQL to create tables
            try {
                // Branch table creation
                sql = "CREATE TABLE IF NOT EXISTS branch( " +
                        "branch_id INTEGER NOT NULL PRIMARY KEY, " +
                        "branch_name VARCHAR(20), " +
                        "branch_addr VARCHAR(50), " +
                        "branch_city VARCHAR(20), " +
                        "branch_phone INTEGER)";
                stmt.executeUpdate(sql);

                // Driver table creation
                sql = "CREATE TABLE IF NOT EXISTS driver( " +
                        "driver_ssn INTEGER NOT NULL PRIMARY KEY, " +
                        "driver_name VARCHAR(20), " +
                        "driver_addr VARCHAR(50), " +
                        "driver_city VARCHAR(20), " +
                        "driver_birthdate DATE, " +
                        "driver_phone INTEGER)";
                stmt.executeUpdate(sql);

                // License table creation
                sql = "CREATE TABLE IF NOT EXISTS license( " +
                        "license_no INTEGER NOT NULL PRIMARY KEY, " +
                        "driver_ssn INTEGER, " +
                        "license_type CHAR(1), " +
                        "license_class INTEGER, " +
                        "license_expiry DATE, " +
                        "issue_date DATE, " +
                        "branch_id INTEGER, " +
                        "FOREIGN KEY (driver_ssn) REFERENCES driver(driver_ssn), " +
                        "FOREIGN KEY (branch_id) REFERENCES branch(branch_id))";
                stmt.executeUpdate(sql);

                // Exam table creation
                sql = "CREATE TABLE IF NOT EXISTS exam( " +
                        "driver_ssn INTEGER, " +
                        "branch_id INTEGER, " +
                        "exam_date DATE, " +
                        "exam_type CHAR(1), " +
                        "exam_score INTEGER, " +
                        "PRIMARY KEY (driver_ssn, branch_id, exam_date), " +
                        "FOREIGN KEY (driver_ssn) REFERENCES driver(driver_ssn), " +
                        "FOREIGN KEY (branch_id) REFERENCES branch(branch_id))";
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                System.err.println("Error message: " + e.getMessage());
                System.err.println("Error number: " + e.getErrorCode());
            }

            // STEP 6: Insert tuples into tables
            try {
                // Insert data into Branch table
            	sql = "INSERT IGNORE INTO branch (branch_id, branch_name, branch_addr, branch_city, branch_phone) VALUES " +
                        "(10, 'Main Hoboken', '1234 Main St.', 'Hoboken', 5551234), " +
                        "(20, 'NYC 33rd street', '2320 33rd street', 'NYC', 5552331), " +
                        "(30, 'West Creek', '251 Creek Rd.', 'Newark', 5552511), " +
                        "(40, 'Blenheim', '1342 W.22 Ave.', 'Princeton', 5551342), " +
                        "(50, 'NYC 98 street', '340 98th street', 'NYC', 5214202), " +
                        "(60, 'NYC 4th street', '21 4th street', 'NYC', 5214809)";
            	stmt.executeUpdate(sql);
                // Insert data into Driver table
                sql = "INSERT IGNORE INTO driver (driver_ssn, driver_name, driver_addr, driver_city, driver_birthdate, driver_phone) VALUES " +
                        "(11111111, 'Bob Smith', '111 E.11 Street', 'Hoboken', '1975-01-01', 5551111), " +
                        "(22222222, 'John Walters', '222 E.22 St.', 'Princeton', '1976-02-02', 5552222), " +
                        "(33333333, 'Troy Rops', '333 W 33 Ave', 'NYC', '1970-03-03', 5553333), " +
                        "(44444444, 'Kevin Mark', '444 E.4 Ave.', 'Hoboken', '1974-04-04', 5554444), " +
                        "(55555555, 'Amelie Kim', '63 Main street', 'Hoboken', '2000-09-10', 5551456), " +
                        "(66666666, 'Mary Gup', '47 W 13th street', 'NYC', '1998-12-31', 5552315), " +
                        "(77777777, 'Clark Johnson', '36 east 8th street', 'NYC', '1999-10-01', 5559047)";
                stmt.executeUpdate(sql);

                // Insert data into License table
                sql = "INSERT IGNORE INTO license (license_no, driver_ssn, license_type, license_class, license_expiry, issue_date, branch_id) VALUES " +
                        "(1, 11111111, 'D', 5, '2022-05-24', '2017-05-25', 20), " +
                        "(2, 22222222, 'D', 5, '2024-09-29', '2016-08-29', 40), " +
                        "(3, 33333333, 'L', 5, '2022-12-27', '2017-06-27', 20), " +
                        "(4, 44444444, 'D', 5, '2022-08-30', '2017-08-30', 40), " +
                        "(5, 77777777, 'D', 3, '2025-08-17', '2020-08-17', 50), " +
                        "(6, 66666666, 'D', 1, '2024-01-12', '2020-01-11', 50), " +
                        "(7, 44444444, 'L', 5, '2023-01-31', '2020-12-31', 30)";
                stmt.executeUpdate(sql);

                // Insert data into Exam table
                sql = "INSERT IGNORE INTO exam (driver_ssn, branch_id, exam_date, exam_type, exam_score) VALUES " +
                        "(11111111, 20, '2017-05-25', 'D', 79), " +
                        "(22222222, 30, '2016-05-06', 'L', 25), " +
                        "(22222222, 40, '2016-06-10', 'L', 51), " +
                        "(33333333, 10, '2017-07-07', 'L', 45), " +
                        "(33333333, 20, '2017-07-27', 'L', 61), " +
                        "(44444444, 10, '2017-07-27', 'L', 71), " +
                        "(44444444, 20, '2017-08-30', 'L', 65), " +
                        "(44444444, 40, '2017-09-01', 'L', 82), " +
                        "(11111111, 20, '2017-12-02', 'L', 67), " +
                        "(22222222, 40, '2016-08-29', 'D', 81), " +
                        "(33333333, 20, '2017-06-27', 'L', 49), " +
                        "(44444444, 10, '2019-04-10', 'D', 80), " +
                        "(77777777, 30, '2020-12-31', 'L', 90), " +
                        "(77777777, 30, '2020-10-30', 'L', 40), " +
                        "(66666666, 40, '2020-02-03', 'D', 90)";
                stmt.executeUpdate(sql);

            } catch (SQLException e) {
                System.err.println("Error message: " + e.getMessage());
                System.err.println("Error number: " + e.getErrorCode());
            }

            // Console-based GUI
            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            while (running) {
                System.out.println("\nMotor Vehicle Office System");
                System.out.println("1. Query License Information by Driver");
                System.out.println("2. Query Exam Information by Driver");
                System.out.println("3. Search Drivers by Branch");
                System.out.println("4. Search Branches by City");
                System.out.println("5. Report Drivers with Expired Licenses");
                System.out.println("6. Report Data Errors in Exam Table");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (option) {
                    case 1:
                        queryLicenseInformation(conn, scanner);
                        break;
                    case 2:
                        queryExamInformation(conn, scanner);
                        break;
                    case 3:
                        searchDriversByBranch(conn, scanner);
                        break;
                    case 4:
                        searchBranchesByCity(conn, scanner);
                        break;
                    case 5:
                        reportExpiredLicenses(conn);
                        break;
                    case 6:
                        reportExamDataErrors(conn);
                        break;
                    case 7:
                        running = false;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
            scanner.close();
        } catch (SQLException se) {
            System.err.println("SQL Exception: " + se.getMessage());
            System.err.println("SQL Error Code: " + se.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }

    private static void queryLicenseInformation(Connection conn, Scanner scanner) {
        System.out.print("Enter driver's name: ");
        String driverName = scanner.nextLine();

        String sql = "SELECT l.license_type, l.issue_date, l.license_expiry, b.branch_name " +
                     "FROM driver d JOIN license l ON d.driver_ssn = l.driver_ssn " +
                     "JOIN branch b ON l.branch_id = b.branch_id " +
                     "WHERE d.driver_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, driverName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("License Type: " + rs.getString("license_type"));
                System.out.println("Issue Date: " + rs.getDate("issue_date"));
                System.out.println("License Expiry: " + rs.getDate("license_expiry"));
                System.out.println("Branch: " + rs.getString("branch_name"));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void queryExamInformation(Connection conn, Scanner scanner) {
        System.out.print("Enter driver's name: ");
        String driverName = scanner.nextLine();

        String sql = "SELECT b.branch_name, e.exam_date, e.exam_score " +
                     "FROM driver d JOIN exam e ON d.driver_ssn = e.driver_ssn " +
                     "JOIN branch b ON e.branch_id = b.branch_id " +
                     "WHERE d.driver_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, driverName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Branch: " + rs.getString("branch_name"));
                System.out.println("Exam Date: " + rs.getDate("exam_date"));
                System.out.println("Exam Score: " + rs.getInt("exam_score"));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void searchDriversByBranch(Connection conn, Scanner scanner) {
        System.out.print("Enter branch name: ");
        String branchName = scanner.nextLine();

        String sql = "SELECT d.driver_name, d.driver_addr, d.driver_city, d.driver_phone, l.license_type " +
                     "FROM driver d JOIN license l ON d.driver_ssn = l.driver_ssn " +
                     "JOIN branch b ON l.branch_id = b.branch_id " +
                     "WHERE b.branch_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, branchName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Driver: " + rs.getString("driver_name"));
                System.out.println("Address: " + rs.getString("driver_addr"));
                System.out.println("City: " + rs.getString("driver_city"));
                System.out.println("Phone: " + rs.getInt("driver_phone"));
                System.out.println("License Type: " + rs.getString("license_type"));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void searchBranchesByCity(Connection conn, Scanner scanner) {
        System.out.print("Enter city name: ");
        String cityName = scanner.nextLine();

        String sql = "SELECT b.branch_name, b.branch_addr, b.branch_phone, " +
                     "COALESCE(COUNT(l.license_no), 0) AS total_licenses " +
                     "FROM branch b LEFT JOIN license l ON b.branch_id = l.branch_id " +
                     "WHERE b.branch_city = ? GROUP BY b.branch_id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cityName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Branch Name: " + rs.getString("branch_name"));
                System.out.println("Address: " + rs.getString("branch_addr"));
                System.out.println("Phone: " + rs.getInt("branch_phone"));
                System.out.println("Total Licenses: " + rs.getInt("total_licenses"));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void reportExpiredLicenses(Connection conn) {
        String sql = "SELECT d.driver_name, d.driver_phone " +
                     "FROM driver d JOIN license l ON d.driver_ssn = l.driver_ssn " +
                     "WHERE l.license_expiry < DATE_SUB('2024-11-02', INTERVAL 3 MONTH)";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("Driver: " + rs.getString("driver_name"));
                System.out.println("Phone: " + rs.getInt("driver_phone"));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void reportExamDataErrors(Connection conn) {
        // Type I error – Unmatching branch IDs
        String typeIErrorSql = "SELECT d.driver_name " +
                               "FROM driver d JOIN license l ON d.driver_ssn = l.driver_ssn " +
                               "JOIN exam e ON d.driver_ssn = e.driver_ssn " +
                               "WHERE l.branch_id != e.branch_id";
        
        // Type II error – Unmatching issue date
        String typeIIErrorSql = "SELECT d.driver_name " +
                                "FROM driver d JOIN license l ON d.driver_ssn = l.driver_ssn " +
                                "JOIN exam e ON d.driver_ssn = e.driver_ssn " +
                                "WHERE l.issue_date < (SELECT MAX(e.exam_date) FROM exam e WHERE e.driver_ssn = d.driver_ssn)";

        // Type III error – Unmatching license type
        String typeIIIErrorSql = "SELECT d.driver_name " +
                                 "FROM driver d JOIN license l ON d.driver_ssn = l.driver_ssn " +
                                 "JOIN exam e ON d.driver_ssn = e.driver_ssn " +
                                 "WHERE l.license_type != (SELECT e.exam_type FROM exam e WHERE e.driver_ssn = d.driver_ssn ORDER BY e.exam_date DESC LIMIT 1)";

        // Type IV error – Invalid exam score
        String typeIVErrorSql = "SELECT d.driver_name " +
                                "FROM driver d JOIN license l ON d.driver_ssn = l.driver_ssn " +
                                "JOIN exam e ON d.driver_ssn = e.driver_ssn " +
                                "WHERE e.Exam_score <= 70 AND e.Exam_score = " +
                                "(SELECT MAX(e2.Exam_score) FROM exam e2 WHERE e2.driver_ssn = d.driver_ssn)";

        // Type V error – No exam taken
        String typeVErrorSql = "SELECT d.driver_name " +
        						"FROM driver d " +
                				"JOIN license l ON d.driver_ssn = l.driver_ssn " +
                				"LEFT JOIN exam e ON d.driver_ssn = e.driver_ssn " +
                				"WHERE e.exam_type IS NULL AND l.license_no IS NOT NULL";
        try (Statement stmt = conn.createStatement()) {
            // Type I error – Unmatching branch IDs
            ResultSet rs = stmt.executeQuery(typeIErrorSql);
            System.out.println("Type I Errors - Unmatching branch IDs:");
            while (rs.next()) {
                System.out.println("Driver: " + rs.getString("driver_name"));
            }

            // Type II error – Unmatching issue date
            rs = stmt.executeQuery(typeIIErrorSql);
            System.out.println("Type II Errors - Unmatching issue date:");
            while (rs.next()) {
                System.out.println("Driver: " + rs.getString("driver_name"));
            }

            // Type III error – Unmatching license type
            rs = stmt.executeQuery(typeIIIErrorSql);
            System.out.println("Type III Errors - Unmatching license type:");
            while (rs.next()) {
                System.out.println("Driver: " + rs.getString("driver_name"));
            }

            // Type IV error – Invalid exam score
            rs = stmt.executeQuery(typeIVErrorSql);
            System.out.println("Type IV Errors - Invalid exam score:");
            while (rs.next()) {
                System.out.println("Driver: " + rs.getString("driver_name"));
            }

            // Type V error – No exam taken
            rs = stmt.executeQuery(typeVErrorSql);
            System.out.println("Type V Errors - No exam taken:");
            while (rs.next()) {
                System.out.println("Driver: " + rs.getString("driver_name"));
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}