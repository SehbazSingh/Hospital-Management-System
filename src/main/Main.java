package main;


import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DBConnection;

public class Main {
    
    static void addDoctor(Scanner sc) {
    // Consume the leftover newline from the main menu's nextInt()
    sc.nextLine(); 

    System.out.print("Enter Doctor Name: ");
    String doctorName = sc.nextLine();

    System.out.print("Enter Doctor Specialization: ");
    String specialization = sc.nextLine();

    String query = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";


    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setString(1, doctorName);
        ps.setString(2, specialization);
        ps.executeUpdate();

        System.out.println("Doctor added successfully!");
        System.out.println("---------------------");

    } catch (SQLException e) {
        System.err.println("Error adding doctor: " + e.getMessage());
    }
}

    static void addPatient(Scanner sc) {
        sc.nextLine(); // Consume newline
        System.out.print("Enter Patient Name: ");
        String patientName = sc.nextLine();

        System.out.print("Enter Patient Age: ");
        int age = sc.nextInt();
        sc.nextLine(); // Consume newline

        System.out.print("Enter Patient Disease: ");
        String disease = sc.nextLine();

        try {
            // Step 1: Get connection
            Connection conn = DBConnection.getConnection();

            // Step 2: SQL query
            String query = "INSERT INTO patients (name, age, disease) VALUES (?, ?, ?)";

            // Step 3: Prepare statement
            PreparedStatement ps = conn.prepareStatement(query);

            // Step 4: Set values
            ps.setString(1, patientName);
            ps.setInt(2, age);
            ps.setString(3, disease);

            // Step 5: Execute
            ps.executeUpdate();
            
            System.out.println("Patient added successfully to database!");
            System.out.println("---------------------");

            // Step 6: Close resources
            ps.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            }
    }

    static void addAppointment(Scanner sc) {
        sc.nextLine(); // Consume newline
        try {
            Connection conn = DBConnection.getConnection();

            // Step 1: Take input
            System.out.print("Enter Doctor ID: ");
            int doctorId = sc.nextInt();
            sc.nextLine(); // consume newline

            System.out.print("Enter Patient ID: ");
            int patientId = sc.nextInt();
            sc.nextLine(); // consume newline

            System.out.print("Enter Appointment Date (YYYY-MM-DD HH:MM:SS): ");
            String date = sc.nextLine();

            // Step 2: Check if doctor exists
            String checkDoctor = "SELECT id FROM doctors WHERE id = ?";
            PreparedStatement psDoctor = conn.prepareStatement(checkDoctor);
            psDoctor.setInt(1, doctorId);
            ResultSet rsDoctor = psDoctor.executeQuery();

            if (!rsDoctor.next()) {
                System.out.println("Doctor not found!");
                conn.close();
                return;
            }

            // Step 3: Check if patient exists
            String checkPatient = "SELECT id FROM patients WHERE id = ?";
            PreparedStatement psPatient = conn.prepareStatement(checkPatient);
            psPatient.setInt(1, patientId);
            ResultSet rsPatient = psPatient.executeQuery();

            if (!rsPatient.next()) {
                System.out.println("Patient not found!");
                conn.close();
                return;
            }

            // Step 4: Insert appointment
            String insertQuery = "INSERT INTO appointments (doctor_id, patient_id, appointment_date) VALUES (?, ?, ?)";
            PreparedStatement psInsert = conn.prepareStatement(insertQuery);

            psInsert.setInt(1, doctorId);
            psInsert.setInt(2, patientId);
            psInsert.setString(3, date);

            psInsert.executeUpdate();

            System.out.println("Appointment added successfully!");

            // Step 5: Close resources
            psDoctor.close();
            psPatient.close();
            psInsert.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    static void displayDoctors() {
        try {
            // Step 1: Get connection
            Connection conn = DBConnection.getConnection();

            // Step 2: SQL query
            String query = "SELECT * FROM doctors";

            // Step 3: Prepare statement
            PreparedStatement ps = conn.prepareStatement(query);

            // Step 4: Execute query
            ResultSet rs = ps.executeQuery();

            // Step 5: Check if empty
            boolean isEmpty = true;

            // Step 6: Loop through results
            while (rs.next()) {
                isEmpty = false;

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String specialization = rs.getString("specialization");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Specialization: " + specialization);
                System.out.println("--------------------");
            }

            if (isEmpty) {
                System.out.println("No doctors found.");
            }

            // Step 7: Close resources
            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void displayPatients() {
        try {
            // Step 1: Get connection
            Connection conn = DBConnection.getConnection();

            // Step 2: SQL query
            String query = "SELECT * FROM patients";

            // Step 3: Prepare statement
            PreparedStatement ps = conn.prepareStatement(query);

            // Step 4: Execute query
            ResultSet rs = ps.executeQuery();

            // Step 5: Check if empty
            boolean isEmpty = true;

            // Step 6: Loop through results
            while (rs.next()) {
                isEmpty = false;

                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String disease = rs.getString("disease");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Age: " + age);
                System.out.println("Disease: " + disease);
                System.out.println("--------------------");                }

            if (isEmpty) {
                System.out.println("No patients found.");
            }

                // Step 7: Close resources
            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
            
    static void displayAppointments() {
        try {
            Connection conn = DBConnection.getConnection();

            String query = "SELECT a.id, d.name AS doctor_name, p.name AS patient_name, a.appointment_date " +
                           "FROM appointments a " +
                           "JOIN doctors d ON a.doctor_id = d.id " +
                           "JOIN patients p ON a.patient_id = p.id";

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            boolean isEmpty = true;

            while (rs.next()) {
                isEmpty = false;

                int id = rs.getInt("id");
                String doctorName = rs.getString("doctor_name");
                String patientName = rs.getString("patient_name");
                String appointmentDate = rs.getString("appointment_date");

                System.out.println("ID: " + id);
                System.out.println("Doctor: " + doctorName);
                System.out.println("Patient: " + patientName);
                System.out.println("Date: " + appointmentDate);
                System.out.println("--------------------");
            }

            if (isEmpty) {
                System.out.println("No appointments found.");
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        //Scanner for user input
        Scanner sc = new Scanner(System.in);


        // testing sql connection
        if (!util.DBConnection.testConnection()) {
            System.out.println("Failed to connect to the database.");
        }
        
        // menu system
        while (true) {
            
            System.out.println("Hospital Management System");
            System.out.println("1. Add Doctor");
            System.out.println("2. Add Patient");
            System.out.println("3. Display Doctors");
            System.out.println("4. Display Patients");
            System.out.println("5. Add Appointment");
            System.out.println("6. Display Appointments");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    addDoctor(sc);
                    break;


                case 2:
                    addPatient(sc);
                    break;


                case 3:
                    displayDoctors();
                    break;


                case 4:
                    displayPatients();
                    break;


                case 5:
                    addAppointment(sc);
                    break;


                case 6:
                    displayAppointments();
                    break;


                case 10:
                    System.out.println("Exiting...");
                    sc.close();
                    return;


                default:
                    System.out.println("Invalid choice. Please try again.");
            }
    }
    }
}