package model;

public class Appointment {

    private int id;
    private int doctorId;
    private int patientId;
    private String date;

    // Constructor
    public Appointment(int id, int doctorId, int patientId, String date) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
    }
    // Getter
    public int getId() {
        return id;
    }
    public int getDoctorId() {
        return doctorId;
    }
    public int getPatientId() {
        return patientId;
    }
    public String getDate() {
        return date;
    }
    // Display appointment information
    public void displayAppointment() {
        System.out.println("Appointment ID: " + id);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Date: " + date);
    }

}