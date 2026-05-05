# Hospital Management System

A Java Swing-based Hospital Management System using MySQL and JDBC.

This project allows you to:
- Add doctors, patients, and appointments from a desktop UI
- View doctors, patients, and appointments in the output panel
- Delete doctors, patients, and appointments by ID
- Automatically remove linked appointments when deleting a doctor or patient

## Features

- Swing desktop interface with tab-based forms
- MySQL database integration through JDBC
- Validation for required fields and numeric IDs
- Doctor/patient existence checks before booking appointments
- Safe delete flow for foreign-key related records

## Tech Stack

- Java
- Swing
- JDBC
- MySQL

## Project Structure

```text
Hospital Management System/
|-- src/
|   |-- App.java
|   |-- main/
|   |   `-- Main.java
|   |-- model/
|   |   |-- Doctor.java
|   |   |-- Patient.java
|   |   `-- Appointment.java
|   `-- util/
|       `-- DBConnection.java
|-- lib/
|   `-- mysql-connector-j-9.6.0.jar
|-- bin/
`-- README.md
```

## Database Setup

Run the following SQL in MySQL to create the required database and tables:

```sql
CREATE DATABASE IF NOT EXISTS hospital_db;
USE hospital_db;

CREATE TABLE IF NOT EXISTS doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    disease VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    appointment_date DATETIME NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id),
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);
```

## Configuration

Set database credentials via environment variables:

- HMS_DB_URL (default: jdbc:mysql://localhost:3306/hospital_db)
- HMS_DB_USER (default: root)
- HMS_DB_PASSWORD (default: empty)

PowerShell example:

```powershell
$env:HMS_DB_URL="jdbc:mysql://localhost:3306/hospital_db"
$env:HMS_DB_USER="root"
$env:HMS_DB_PASSWORD="your_password"
```

If not set, defaults from DBConnection are used.

## How to Build and Run

From project root:

```powershell
javac -cp "lib\mysql-connector-j-9.6.0.jar" -d bin src\App.java src\main\Main.java src\util\DBConnection.java src\model\Appointment.java src\model\Doctor.java src\model\Patient.java
```

Run:

```powershell
$env:HMS_DB_URL="jdbc:mysql://localhost:3306/hospital_db"; $env:HMS_DB_USER="root"; $env:HMS_DB_PASSWORD="your_password"; java -cp "bin;lib\mysql-connector-j-9.6.0.jar" App
```

## UI Tabs and Actions

- Add Doctor
- Add Patient
- Add Appointment
- Delete Records

Bottom actions:

- Display Doctors
- Display Patients
- Display Appointments
- Clear Output
- Exit

## Notes

- Appointment date input format: YYYY-MM-DD HH:MM:SS
- Appointment creation requires existing doctor and patient IDs
- Deleting a doctor or patient also deletes linked appointments first

## Future Improvements

- Date picker and stronger date validation
- Edit/update operations for all entities
- Search and filter support in UI
- Improved logging and exception reporting

## Authors

- Sehbaz Singh
- Sashank Rana
- Sahil Bhandari
