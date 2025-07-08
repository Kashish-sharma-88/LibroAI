package com.example.libroai;

public class StdLibraryRequestModel {
    
    private String requestId;
    private String studentName;
    private String studentEmail;
    private String libraryName;
    private String libraryId;
    private String requestDate;
    private String status; // "pending", "approved", "rejected"
    private String requestMessage;
    private String studentId;

    // Required empty constructor for Firebase
    public StdLibraryRequestModel() {
    }

    // Parameterized constructor
    public StdLibraryRequestModel(String requestId, String studentName, String studentEmail, 
                                 String libraryName, String libraryId, String requestDate, 
                                 String status, String requestMessage, String studentId) {
        this.requestId = requestId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.libraryName = libraryName;
        this.libraryId = libraryId;
        this.requestDate = requestDate;
        this.status = status;
        this.requestMessage = requestMessage;
        this.studentId = studentId;
    }

    // Getter methods
    public String getRequestId() {
        return requestId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public String getStudentId() {
        return studentId;
    }

    // Setter methods
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
} 