package model;


public class Doctor {
    private int id;
    private String name;
    private String specialization;

    // Constructor
    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }
    // Getter

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSpecialization() {
        return specialization;
    }
    // Display doctor information
    public void displayDoctor() {
        System.out.println("Doctor ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Specialization: " + specialization);
    }

}
