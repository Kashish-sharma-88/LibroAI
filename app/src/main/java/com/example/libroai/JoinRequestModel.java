package com.example.libroai;

import com.google.firebase.Timestamp;

public class JoinRequestModel {
    
    private String requestId;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String libraryId;
    private String libraryName;
    private String status; // "pending", "approved", "rejected"
    private String requestMessage;
    private Timestamp requestDate;
    private Timestamp responseDate;
    private String librarianId;
    private String librarianName;
    private String responseMessage;

    // Required empty constructor for Firestore
    public JoinRequestModel() {
    }

    // Parameterized constructor
    public JoinRequestModel(String requestId, String studentId, String studentName, String studentEmail,
                           String libraryId, String libraryName, String status, String requestMessage,
                           Timestamp requestDate) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.libraryId = libraryId;
        this.libraryName = libraryName;
        this.status = status;
        this.requestMessage = requestMessage;
        this.requestDate = requestDate;
    }

    // Getter methods
    public String getRequestId() {
        return requestId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public Timestamp getResponseDate() {
        return responseDate;
    }

    public String getLibrarianId() {
        return librarianId;
    }

    public String getLibrarianName() {
        return librarianName;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    // Setter methods
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public void setResponseDate(Timestamp responseDate) {
        this.responseDate = responseDate;
    }

    public void setLibrarianId(String librarianId) {
        this.librarianId = librarianId;
    }

    public void setLibrarianName(String librarianName) {
        this.librarianName = librarianName;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
} 