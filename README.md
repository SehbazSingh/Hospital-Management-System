# Hospital Management System

A simple Java console-based Hospital Management System using MySQL and JDBC.

This project allows you to:
- Add and view doctors
- Add and view patients
- Create appointments by linking existing doctor and patient records
- View all appointments with doctor and patient names

## Features

- Menu-driven CLI application
- MySQL database integration through JDBC
- Basic validation for doctor and patient existence before booking appointments
- Separate model and utility classes for cleaner structure

## Tech Stack

- Java
- JDBC
- MySQL
- VS Code Java Project structure

## Project Structure

```text
Hospital Management System/
|-- src/
|   |-- main/
|   |   `-- Main.java
|   |-- model/
|   |   |-- Doctor.java
|   |   |-- Patient.java
|   |   `-- Appointment.java
|   `-- util/
|       `-- DBConnection.java
|-- lib/
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

- `HMS_DB_URL` (default: `jdbc:mysql://localhost:3306/hospital_db`)
- `HMS_DB_USER` (default: `root`)
- `HMS_DB_PASSWORD` (default: empty)

PowerShell example:

```powershell
$env:HMS_DB_URL="jdbc:mysql://localhost:3306/hospital_db"
$env:HMS_DB_USER="root"
$env:HMS_DB_PASSWORD="your_password"
```

If you do not set these variables, the application uses the defaults shown above.

## How to Run

1. Make sure Java and MySQL are installed.
2. Add the MySQL JDBC driver to your classpath (or VS Code Java project dependencies).
3. Set `HMS_DB_URL`, `HMS_DB_USER`, and `HMS_DB_PASSWORD` in your terminal/session.
4. Compile and run:

```powershell
# From project root
javac -d bin src\util\DBConnection.java src\model\Doctor.java src\model\Patient.java src\model\Appointment.java src\main\Main.java
java -cp "bin;lib\*" main.Main
```

If you run from VS Code, you can run `src/main/Main.java` directly using the Java extension after configuring dependencies.

## Menu Options

When running, the program provides these options:

- `1` Add Doctor
- `2` Add Patient
- `3` Display Doctors
- `4` Display Patients
- `5` Add Appointment
- `6` Display Appointments
- `10` Exit

## Notes

- Appointment input format in CLI: `YYYY-MM-DD HH:MM:SS`
- IDs used while creating appointments must already exist in `doctors` and `patients` tables.

## Future Improvements

- Input validation for date and numeric fields
- Update/delete operations for doctors, patients, and appointments
- Better exception handling and logging

## Authors

- Sehbaz Singh
- Sashank Rana
- Sahil Bhandari
