package demomavinfx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    Stage primaryStage;
    final String DB_URL = "jdbc:mysql://localhost:3306/hr_management";
    final String DB_USER = "root"; 
    final String DB_PASS = "";   
    
    // Store logged-in user's email
    private String loggedInUserEmail = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        showLoginPage();
    }

    // ---------------- LOGIN PAGE ----------------
    private void showLoginPage() {
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");

        // Save email and then show dashboard page
        loginButton.setOnAction(e -> {
            loggedInUserEmail = emailField.getText().trim();
            if (loggedInUserEmail.isEmpty()) {
                // You can add alert here for empty email if you want
                return;
            }
            showDashboardPage();
        });

        VBox layout = new VBox(10, emailLabel, emailField, passwordLabel, passwordField, loginButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 400, 300);

        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ---------------- DASHBOARD PAGE ----------------
    private void showDashboardPage() {
        // Format current date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String currentDate = LocalDate.now().format(formatter);

        // Welcome message and current date label
        Label welcomeLabel = new Label("Welcome, " + loggedInUserEmail);
        welcomeLabel.setFont(new Font("Arial", 16));
        Label dateLabel = new Label(currentDate);
        dateLabel.setFont(new Font("Arial", 14));

        HBox welcomeBox = new HBox(20, welcomeLabel, dateLabel);
        welcomeBox.setAlignment(Pos.CENTER);

        Button adminBtn = new Button("Admin");
        Button empBtn = new Button("Employee");
        Button logoutBtn = new Button("Logout");
        Button exitBtn = new Button("Exit");

        adminBtn.setOnAction(e -> showAdminPage());
        empBtn.setOnAction(e -> showEmployeePage());
        logoutBtn.setOnAction(e -> showLoginPage());
        exitBtn.setOnAction(e -> primaryStage.close());

        VBox layout = new VBox(15, welcomeBox, adminBtn, empBtn, logoutBtn, exitBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(scene);
    }

    // ---------------- ADMIN PAGE ----------------
    private void showAdminPage() {
        TableView<Person> table = new TableView<>();

        TableColumn<Person, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Person, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(nameCol, emailCol);

        ObservableList<Person> data = FXCollections.observableArrayList();

        // Fetch admin data from database
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, email FROM admins")) {

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                data.add(new Person(name, email));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        table.setItems(data);

        Button createBtn = new Button("Create");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button viewBtn = new Button("View");
        Button backBtn = new Button("Back");

        backBtn.setOnAction(e -> showDashboardPage());

        VBox layout = new VBox(10, table, createBtn, updateBtn, deleteBtn, viewBtn, backBtn);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setTitle("Admin Page");
        primaryStage.setScene(scene);
    }

    // ---------------- EMPLOYEE PAGE ----------------
    private void showEmployeePage() {
        TableView<Person> table = new TableView<>();

        TableColumn<Person, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Person, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(nameCol, emailCol);

        ObservableList<Person> data = FXCollections.observableArrayList();

        // Fetch employee data from database
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, email FROM employees")) {

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                data.add(new Person(name, email));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        table.setItems(data);

        Button createBtn = new Button("Create");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button viewBtn = new Button("View");
        Button backBtn = new Button("Back");

        backBtn.setOnAction(e -> showDashboardPage());

        VBox layout = new VBox(10, table, createBtn, updateBtn, deleteBtn, viewBtn, backBtn);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setTitle("Employee Page");
        primaryStage.setScene(scene);
    }

    // ---------------- PERSON CLASS ----------------
    public static class Person {
        private final String name;
        private final String email;

        public Person(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
