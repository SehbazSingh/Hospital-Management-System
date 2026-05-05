package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import util.DBConnection;

public class Main {
    private static JTextArea outputArea;

    static void addDoctor(Scanner sc) {
        sc.nextLine();
        System.out.print("Enter Doctor Name: ");
        String doctorName = sc.nextLine();
        System.out.print("Enter Doctor Specialization: ");
        String specialization = sc.nextLine();
        System.out.println(addDoctor(doctorName, specialization));
    }

    static void addPatient(Scanner sc) {
        sc.nextLine();
        System.out.print("Enter Patient Name: ");
        String patientName = sc.nextLine();
        System.out.print("Enter Patient Age: ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Patient Disease: ");
        String disease = sc.nextLine();
        System.out.println(addPatient(patientName, age, disease));
    }

    static void addAppointment(Scanner sc) {
        sc.nextLine();
        System.out.print("Enter Doctor ID: ");
        int doctorId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Patient ID: ");
        int patientId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Appointment Date (YYYY-MM-DD HH:MM:SS): ");
        String date = sc.nextLine();
        System.out.println(addAppointment(doctorId, patientId, date));
    }

    static void displayDoctors() {
        System.out.println(getDoctorsReport());
    }

    static void displayPatients() {
        System.out.println(getPatientsReport());
    }

    static void displayAppointments() {
        System.out.println(getAppointmentsReport());
    }

    private static String addDoctor(String doctorName, String specialization) {
        String query = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, doctorName);
            ps.setString(2, specialization);
            ps.executeUpdate();
            return "Doctor added successfully!";
        } catch (SQLException e) {
            return "Error adding doctor: " + e.getMessage();
        }
    }

    private static String addPatient(String patientName, int age, String disease) {
        String query = "INSERT INTO patients (name, age, disease) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, patientName);
            ps.setInt(2, age);
            ps.setString(3, disease);
            ps.executeUpdate();
            return "Patient added successfully!";
        } catch (SQLException e) {
            return "Error adding patient: " + e.getMessage();
        }
    }

    private static String addAppointment(int doctorId, int patientId, String date) {
        String checkDoctor = "SELECT id FROM doctors WHERE id = ?";
        String checkPatient = "SELECT id FROM patients WHERE id = ?";
        String insertQuery = "INSERT INTO appointments (doctor_id, patient_id, appointment_date) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psDoctor = conn.prepareStatement(checkDoctor);
             PreparedStatement psPatient = conn.prepareStatement(checkPatient)) {

            psDoctor.setInt(1, doctorId);
            try (ResultSet rsDoctor = psDoctor.executeQuery()) {
                if (!rsDoctor.next()) {
                    return "Doctor not found!";
                }
            }

            psPatient.setInt(1, patientId);
            try (ResultSet rsPatient = psPatient.executeQuery()) {
                if (!rsPatient.next()) {
                    return "Patient not found!";
                }
            }

            try (PreparedStatement psInsert = conn.prepareStatement(insertQuery)) {
                psInsert.setInt(1, doctorId);
                psInsert.setInt(2, patientId);
                psInsert.setString(3, date);
                psInsert.executeUpdate();
            }

            return "Appointment added successfully!";
        } catch (SQLException e) {
            return "Error adding appointment: " + e.getMessage();
        }
    }

    private static String deleteAppointment(int appointmentId) {
        String query = "DELETE FROM appointments WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, appointmentId);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return "Appointment not found.";
            }

            return "Appointment deleted successfully!";
        } catch (SQLException e) {
            return "Error deleting appointment: " + e.getMessage();
        }
    }

    private static String deleteDoctor(int doctorId) {
        String deleteAppointments = "DELETE FROM appointments WHERE doctor_id = ?";
        String deleteDoctor = "DELETE FROM doctors WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psAppointments = conn.prepareStatement(deleteAppointments)) {
                psAppointments.setInt(1, doctorId);
                psAppointments.executeUpdate();
            }

            int affectedRows;
            try (PreparedStatement psDoctor = conn.prepareStatement(deleteDoctor)) {
                psDoctor.setInt(1, doctorId);
                affectedRows = psDoctor.executeUpdate();
            }

            if (affectedRows == 0) {
                conn.rollback();
                return "Doctor not found.";
            }

            conn.commit();
            return "Doctor and related appointments deleted successfully!";
        } catch (SQLException e) {
            return "Error deleting doctor: " + e.getMessage();
        }
    }

    private static String deletePatient(int patientId) {
        String deleteAppointments = "DELETE FROM appointments WHERE patient_id = ?";
        String deletePatient = "DELETE FROM patients WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psAppointments = conn.prepareStatement(deleteAppointments)) {
                psAppointments.setInt(1, patientId);
                psAppointments.executeUpdate();
            }

            int affectedRows;
            try (PreparedStatement psPatient = conn.prepareStatement(deletePatient)) {
                psPatient.setInt(1, patientId);
                affectedRows = psPatient.executeUpdate();
            }

            if (affectedRows == 0) {
                conn.rollback();
                return "Patient not found.";
            }

            conn.commit();
            return "Patient and related appointments deleted successfully!";
        } catch (SQLException e) {
            return "Error deleting patient: " + e.getMessage();
        }
    }

    private static String getDoctorsReport() {
        StringBuilder report = new StringBuilder();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM doctors");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                report.append("ID: ").append(rs.getInt("id")).append('\n');
                report.append("Name: ").append(rs.getString("name")).append('\n');
                report.append("Specialization: ").append(rs.getString("specialization")).append('\n');
                report.append("--------------------\n");
            }

            if (report.length() == 0) {
                return "No doctors found.";
            }

            return report.toString();
        } catch (SQLException e) {
            return "Error loading doctors: " + e.getMessage();
        }
    }

    private static String getPatientsReport() {
        StringBuilder report = new StringBuilder();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                report.append("ID: ").append(rs.getInt("id")).append('\n');
                report.append("Name: ").append(rs.getString("name")).append('\n');
                report.append("Age: ").append(rs.getInt("age")).append('\n');
                report.append("Disease: ").append(rs.getString("disease")).append('\n');
                report.append("--------------------\n");
            }

            if (report.length() == 0) {
                return "No patients found.";
            }

            return report.toString();
        } catch (SQLException e) {
            return "Error loading patients: " + e.getMessage();
        }
    }

    private static String getAppointmentsReport() {
        StringBuilder report = new StringBuilder();
        String query = "SELECT a.id, d.name AS doctor_name, p.name AS patient_name, a.appointment_date " +
                "FROM appointments a " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "JOIN patients p ON a.patient_id = p.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                report.append("ID: ").append(rs.getInt("id")).append('\n');
                report.append("Doctor: ").append(rs.getString("doctor_name")).append('\n');
                report.append("Patient: ").append(rs.getString("patient_name")).append('\n');
                report.append("Date: ").append(rs.getString("appointment_date")).append('\n');
                report.append("--------------------\n");
            }

            if (report.length() == 0) {
                return "No appointments found.";
            }

            return report.toString();
        } catch (SQLException e) {
            return "Error loading appointments: " + e.getMessage();
        }
    }

    private static void createAndShowUi() {
        if (!DBConnection.testConnection()) {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        JFrame frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(980, 680);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.setBackground(new Color(246, 248, 252));

        JLabel title = new JLabel("Hospital Management System", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(0, 0, 8, 0));
        root.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Add Doctor", createDoctorPanel());
        tabs.addTab("Add Patient", createPatientPanel());
        tabs.addTab("Add Appointment", createAppointmentPanel());
        tabs.addTab("Delete Records", createDeletePanel());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setText("Welcome. Use the forms to add records or the buttons below to view database data.\n");

        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Output"));
        outputScroll.setPreferredSize(new Dimension(420, 0));

        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, tabs, outputScroll);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(null);
        splitPane.setDividerLocation(500);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);

        JButton showDoctorsButton = new JButton("Display Doctors");
        showDoctorsButton.addActionListener(event -> outputArea.setText(getDoctorsReport()));

        JButton showPatientsButton = new JButton("Display Patients");
        showPatientsButton.addActionListener(event -> outputArea.setText(getPatientsReport()));

        JButton showAppointmentsButton = new JButton("Display Appointments");
        showAppointmentsButton.addActionListener(event -> outputArea.setText(getAppointmentsReport()));

        JButton clearButton = new JButton("Clear Output");
        clearButton.addActionListener(event -> outputArea.setText(""));

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(event -> frame.dispose());

        actions.add(showDoctorsButton);
        actions.add(showPatientsButton);
        actions.add(showAppointmentsButton);
        actions.add(clearButton);
        actions.add(exitButton);

        root.add(splitPane, BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private static JPanel createDoctorPanel() {
        JTextField nameField = new JTextField(20);
        JTextField specializationField = new JTextField(20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        panel.add(new JLabel("Doctor Name"), constraints);
        constraints.gridx = 1;
        panel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Specialization"), constraints);
        constraints.gridx = 1;
        panel.add(specializationField, constraints);

        JButton addButton = new JButton("Add Doctor");
        addButton.addActionListener(event -> {
            String name = nameField.getText().trim();
            String specialization = specializationField.getText().trim();

            if (name.isEmpty() || specialization.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter both doctor name and specialization.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            outputArea.setText(addDoctor(name, specialization));
            nameField.setText("");
            specializationField.setText("");
        });

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(addButton, constraints);

        return panel;
    }

    private static JPanel createPatientPanel() {
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField diseaseField = new JTextField(20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        panel.add(new JLabel("Patient Name"), constraints);
        constraints.gridx = 1;
        panel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Age"), constraints);
        constraints.gridx = 1;
        panel.add(ageField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Disease"), constraints);
        constraints.gridx = 1;
        panel.add(diseaseField, constraints);

        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener(event -> {
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String disease = diseaseField.getText().trim();

            if (name.isEmpty() || ageText.isEmpty() || disease.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all patient fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int age = Integer.parseInt(ageText);
                outputArea.setText(addPatient(name, age, disease));
                nameField.setText("");
                ageField.setText("");
                diseaseField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Age must be a valid number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        panel.add(addButton, constraints);

        return panel;
    }

    private static JPanel createAppointmentPanel() {
        JTextField doctorIdField = new JTextField(20);
        JTextField patientIdField = new JTextField(20);
        JTextField dateField = new JTextField(20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        panel.add(new JLabel("Doctor ID"), constraints);
        constraints.gridx = 1;
        panel.add(doctorIdField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Patient ID"), constraints);
        constraints.gridx = 1;
        panel.add(patientIdField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Appointment Date"), constraints);
        constraints.gridx = 1;
        panel.add(dateField, constraints);

        JLabel hint = new JLabel("Format: YYYY-MM-DD HH:MM:SS");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(Color.DARK_GRAY);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        panel.add(hint, constraints);

        JButton addButton = new JButton("Add Appointment");
        addButton.addActionListener(event -> {
            String doctorText = doctorIdField.getText().trim();
            String patientText = patientIdField.getText().trim();
            String date = dateField.getText().trim();

            if (doctorText.isEmpty() || patientText.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all appointment fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int doctorId = Integer.parseInt(doctorText);
                int patientId = Integer.parseInt(patientText);
                outputArea.setText(addAppointment(doctorId, patientId, date));
                doctorIdField.setText("");
                patientIdField.setText("");
                dateField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Doctor ID and Patient ID must be valid numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(addButton, constraints);

        return panel;
    }

    private static JPanel createDeletePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        JLabel title = new JLabel("Delete records by ID");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        constraints.gridwidth = 2;
        panel.add(title, constraints);

        JTextField doctorIdField = new JTextField(20);
        JTextField patientIdField = new JTextField(20);
        JTextField appointmentIdField = new JTextField(20);

        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(new JLabel("Doctor ID"), constraints);
        constraints.gridx = 1;
        panel.add(doctorIdField, constraints);

        JButton deleteDoctorButton = new JButton("Delete Doctor");
        deleteDoctorButton.addActionListener(event -> {
            String value = doctorIdField.getText().trim();
            if (value.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter a doctor ID.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int doctorId = Integer.parseInt(value);
                outputArea.setText(deleteDoctor(doctorId));
                doctorIdField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Doctor ID must be a valid number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(deleteDoctorButton, constraints);

        constraints.gridy = 3;
        constraints.gridwidth = 1;
        panel.add(new JLabel("Patient ID"), constraints);
        constraints.gridx = 1;
        panel.add(patientIdField, constraints);

        JButton deletePatientButton = new JButton("Delete Patient");
        deletePatientButton.addActionListener(event -> {
            String value = patientIdField.getText().trim();
            if (value.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter a patient ID.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int patientId = Integer.parseInt(value);
                outputArea.setText(deletePatient(patientId));
                patientIdField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Patient ID must be a valid number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(deletePatientButton, constraints);

        constraints.gridy = 5;
        constraints.gridwidth = 1;
        panel.add(new JLabel("Appointment ID"), constraints);
        constraints.gridx = 1;
        panel.add(appointmentIdField, constraints);

        JButton deleteAppointmentButton = new JButton("Delete Appointment");
        deleteAppointmentButton.addActionListener(event -> {
            String value = appointmentIdField.getText().trim();
            if (value.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter an appointment ID.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int appointmentId = Integer.parseInt(value);
                outputArea.setText(deleteAppointment(appointmentId));
                appointmentIdField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Appointment ID must be a valid number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        panel.add(deleteAppointmentButton, constraints);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowUi);
    }
}