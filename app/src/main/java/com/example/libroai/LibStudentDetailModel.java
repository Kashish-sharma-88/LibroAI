package com.example.libroai;

import java.util.List;

public class LibStudentDetailModel {
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String studentAddress;
    private String studentDepartment;
    private String studentRollNumber;
    private String studentSemester;
    private String studentYear;
    private String studentGender;
    private String studentDateOfBirth;
    private String studentBloodGroup;
    private String studentEmergencyContact;
    private String studentEmergencyPhone;
    private List<LibStudentRecordModel> issuedBooks;

    public LibStudentDetailModel() {
        // Default constructor
    }

    public LibStudentDetailModel(String studentId, String studentName, String studentEmail, 
                                String studentPhone, String studentAddress, String studentDepartment,
                                String studentRollNumber, String studentSemester, String studentYear,
                                String studentGender, String studentDateOfBirth, String studentBloodGroup,
                                String studentEmergencyContact, String studentEmergencyPhone) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentPhone = studentPhone;
        this.studentAddress = studentAddress;
        this.studentDepartment = studentDepartment;
        this.studentRollNumber = studentRollNumber;
        this.studentSemester = studentSemester;
        this.studentYear = studentYear;
        this.studentGender = studentGender;
        this.studentDateOfBirth = studentDateOfBirth;
        this.studentBloodGroup = studentBloodGroup;
        this.studentEmergencyContact = studentEmergencyContact;
        this.studentEmergencyPhone = studentEmergencyPhone;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getStudentEmail() { return studentEmail; }
    public String getStudentPhone() { return studentPhone; }
    public String getStudentAddress() { return studentAddress; }
    public String getStudentDepartment() { return studentDepartment; }
    public String getStudentRollNumber() { return studentRollNumber; }
    public String getStudentSemester() { return studentSemester; }
    public String getStudentYear() { return studentYear; }
    public String getStudentGender() { return studentGender; }
    public String getStudentDateOfBirth() { return studentDateOfBirth; }
    public String getStudentBloodGroup() { return studentBloodGroup; }
    public String getStudentEmergencyContact() { return studentEmergencyContact; }
    public String getStudentEmergencyPhone() { return studentEmergencyPhone; }
    public List<LibStudentRecordModel> getIssuedBooks() { return issuedBooks; }

    // Setters
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public void setStudentPhone(String studentPhone) { this.studentPhone = studentPhone; }
    public void setStudentAddress(String studentAddress) { this.studentAddress = studentAddress; }
    public void setStudentDepartment(String studentDepartment) { this.studentDepartment = studentDepartment; }
    public void setStudentRollNumber(String studentRollNumber) { this.studentRollNumber = studentRollNumber; }
    public void setStudentSemester(String studentSemester) { this.studentSemester = studentSemester; }
    public void setStudentYear(String studentYear) { this.studentYear = studentYear; }
    public void setStudentGender(String studentGender) { this.studentGender = studentGender; }
    public void setStudentDateOfBirth(String studentDateOfBirth) { this.studentDateOfBirth = studentDateOfBirth; }
    public void setStudentBloodGroup(String studentBloodGroup) { this.studentBloodGroup = studentBloodGroup; }
    public void setStudentEmergencyContact(String studentEmergencyContact) { this.studentEmergencyContact = studentEmergencyContact; }
    public void setStudentEmergencyPhone(String studentEmergencyPhone) { this.studentEmergencyPhone = studentEmergencyPhone; }
    public void setIssuedBooks(List<LibStudentRecordModel> issuedBooks) { this.issuedBooks = issuedBooks; }
} 