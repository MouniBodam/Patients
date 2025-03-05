package com.hospital_management.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import ch.qos.logback.core.model.Model;
import com.hospital_management.model.Patient;

import org.springframework.ui.Model;  // Correct import for Model


public class PatientController {
    
    @Value("${spring.datasource.url}")
    private String dbUrl;
    
    @Value("${spring.datasource.username}")
    private String dbUsername;
    
    @Value("${spring.datasource.password}")
    private String dbPassword;

    /*
     * This method establishes a connection to the database and returns the connection object.
     */
    private Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /*
     * This method returns the home page with a list of all patients.
     */
    @GetMapping("/")
    public String home(Model model) {
        List<Patient> patients = getAllPatients();
        model.addAttribute("patients", patients);
        return "index";
    }

    /*
     * This method returns the form to add a new patient.
     */
    @GetMapping("/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "add-patient";
    }

    /*
     * This method adds a new patient to the database.
     */
    @PostMapping("/add")
    public String addPatient(@ModelAttribute Patient patient, Model model) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO patients (id, Patient_Name, Mobile, issue) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, patient.getId());
            statement.setString(2, patient.getName());
            statement.setLong(3, patient.getMobile());
            statement.setString(4, patient.getissue());
            statement.executeUpdate();

            model.addAttribute("success", "Successfully added patient " + patient.getName());
            return "add-patient";
        } catch (SQLException e) {
            model.addAttribute("error", "Error adding patient " + e.getMessage());
            e.printStackTrace();
            return "add-patient";
        }
    }

    /*
     * This method returns the list of patients whose name contains the search string.
     */
    @GetMapping("/search")
    public String searchPatients(@RequestParam String name, Model model) {
        List<Patient> patients = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM patients WHERE Patient_Name LIKE ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getLong("id"));
                patient.setName(resultSet.getString("Patient_Name"));
                patient.setMobile(resultSet.getLong("Mobile"));
                patient.setissue(resultSet.getString("issue"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        model.addAttribute("patients", patients);
        return "index";
    }

    /*
     * This method returns the form to edit a patient with the given ID.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Patient patient = getPatientById(id);
            model.addAttribute("patient", patient);
            return "edit-patient";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "index";
        }
    }

    /*
     * This method updates a patient with the given ID in the database.
     */
    @PostMapping("/edit/{id}")
    public String editPatient(@PathVariable long id, @ModelAttribute Patient patient, Model model) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE patients SET Patient_Name = ?, Mobile = ?, issue = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, patient.getName());
            statement.setLong(2, patient.getMobile());
            statement.setString(3, patient.getissue());
            statement.setLong(4, id);
            statement.executeUpdate();

            model.addAttribute("success", "Successfully updated patient " + patient.getName());
            return "edit-patient";
        } catch (SQLException e) {
            model.addAttribute("error", "Error updating patient " + e.getMessage());
            e.printStackTrace();
            return "edit-patient";
        }
    }

    /*
     * This method returns the form to delete a patient with the given ID.
     */
    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        try {
            Patient patient = getPatientById(id);
            model.addAttribute("patient", patient);
            return "delete-patient";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "index";
        }
    }

    /*
     * This method deletes a patient with the given ID from the database.
     */
    @PostMapping("/delete/{id}")
    public String deletePatient(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE from patients WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
            redirectAttributes.addFlashAttribute("success", "Patient with ID " + id + " is successfully deleted!");

            return "redirect:/";
        } catch (SQLException e) {
            model.addAttribute("error", "Error deleting patient " + e.getMessage());
            e.printStackTrace();
            return "delete-patient";
        }
    }

    /*
     * This method returns a list of all patients from the database.
     */
    private List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM patients";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getLong("id"));
                patient.setName(resultSet.getString("Patient_Name"));
                patient.setMobile(resultSet.getLong("Mobile"));
                patient.setissue(resultSet.getString("issue"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    /*
     * This method returns a patient with the given ID from the database.
     */
    private Patient getPatientById(Long id) throws Exception {
        Patient patient = new Patient();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM patients WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                patient.setId(resultSet.getLong("id"));
                patient.setName(resultSet.getString("Patient_Name"));
                patient.setMobile(resultSet.getLong("Mobile"));
                patient.setissue(resultSet.getString("issue"));
            } else {
                throw new Exception("Patient not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }
}
