import java.sql.*;
import java.util.Scanner;

public class VehicleShowroom {

    // ====== CHANGE THESE TO YOUR OWN VALUES ======
    static final String URL  = "jdbc:mysql://localhost:3306/showroom_db";
    static final String USER = "root";
    static final String PASS = "MySQL@12345";
    // =============================================

    static Scanner sc = new Scanner(System.in);

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // ---------- helper input methods (avoid Scanner newline problems) ----------
    static String readString(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    static int readInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    static double readDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // ---------- CREATE ----------
    static void addVehicle() {
        String brand = readString("Brand: ");
        String model = readString("Model: ");
        String type  = readString("Type (Car/Bike/Scooter): ");
        String fuel  = readString("Fuel (Petrol/Diesel/Electric): ");
        int year     = readInt("Year: ");
        double price = readDouble("Price: ");

        String sql = "INSERT INTO vehicles (brand, model, type, fuel, year, price) VALUES (?,?,?,?,?,?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, brand);
            ps.setString(2, model);
            ps.setString(3, type);
            ps.setString(4, fuel);
            ps.setInt(5, year);
            ps.setDouble(6, price);
            ps.executeUpdate();
            System.out.println("Vehicle added to stock!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------- READ (all or only available) ----------
    static void viewVehicles(boolean onlyAvailable) {
        String sql = onlyAvailable
                ? "SELECT * FROM vehicles WHERE status='Available'"
                : "SELECT * FROM vehicles";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.printf("%n%-4s %-12s %-12s %-9s %-9s %-6s %-10s %-10s%n",
                    "ID", "Brand", "Model", "Type", "Fuel", "Year", "Price", "Status");
            System.out.println("-----------------------------------------------------------------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-4d %-12s %-12s %-9s %-9s %-6d %-10.0f %-10s%n",
                        rs.getInt("id"), rs.getString("brand"), rs.getString("model"),
                        rs.getString("type"), rs.getString("fuel"), rs.getInt("year"),
                        rs.getDouble("price"), rs.getString("status"));
            }
            if (!found) System.out.println("No vehicles found.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------- READ (by id) ----------
    static void searchById() {
        int id = readInt("Enter vehicle ID: ");
        String sql = "SELECT * FROM vehicles WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nID:        " + rs.getInt("id"));
                    System.out.println("Vehicle:   " + rs.getString("brand") + " " + rs.getString("model"));
                    System.out.println("Type/Fuel: " + rs.getString("type") + " / " + rs.getString("fuel"));
                    System.out.println("Year:      " + rs.getInt("year"));
                    System.out.println("Price:     Rs. " + rs.getDouble("price"));
                    System.out.println("Status:    " + rs.getString("status"));
                } else {
                    System.out.println("No vehicle found with that ID.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------- UPDATE (details and price) ----------
    static void updateVehicle() {
        int id = readInt("ID of vehicle to update: ");
        String brand = readString("New brand: ");
        String model = readString("New model: ");
        double price = readDouble("New price: ");

        String sql = "UPDATE vehicles SET brand=?, model=?, price=? WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, brand);
            ps.setString(2, model);
            ps.setDouble(3, price);
            ps.setInt(4, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Vehicle updated!" : "ID not found.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------- UPDATE (mark as sold) ----------
    static void markAsSold() {
        int id = readInt("ID of sold vehicle: ");
        String sql = "UPDATE vehicles SET status='Sold' WHERE id=? AND status='Available'";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Marked as SOLD!" : "Not found or already sold.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------- DELETE ----------
    static void deleteVehicle() {
        int id = readInt("ID of vehicle to delete: ");
        String sql = "DELETE FROM vehicles WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Vehicle removed!" : "ID not found.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------- MAIN MENU ----------
    public static void main(String[] args) {
        // Load the MySQL driver explicitly and give a clear message if the jar is missing
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL driver jar not found on the classpath.");
            System.out.println("Run with: java -cp \".;mysql-connector-j-x.x.x.jar\" VehicleShowroom");
            return;
        }

        while (true) {
            System.out.println("\n===== VEHICLE SHOWROOM =====");
            System.out.println("1. Add Vehicle");
            System.out.println("2. View All Vehicles");
            System.out.println("3. View Available Vehicles");
            System.out.println("4. Search by ID");
            System.out.println("5. Update Vehicle");
            System.out.println("6. Mark as Sold");
            System.out.println("7. Delete Vehicle");
            System.out.println("8. Exit");

            int choice = readInt("Choice: ");

            switch (choice) {
                case 1: addVehicle(); break;
                case 2: viewVehicles(false); break;
                case 3: viewVehicles(true); break;
                case 4: searchById(); break;
                case 5: updateVehicle(); break;
                case 6: markAsSold(); break;
                case 7: deleteVehicle(); break;
                case 8:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Enter 1-8.");
            }
        }
    }
}