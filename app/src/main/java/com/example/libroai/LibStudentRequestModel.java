package com.example.libroai;

public class LibStudentRequestModel {
    
    private String requestId;
    private String studentName;
    private String studentEmail;
    private String bookTitle;
    private String requestDate;
    private String status; // "pending", "approved", "rejected"
    private String requestMessage;
    private String libraryId;
    private String studentId;

    // Required empty constructor for Firebase
    public LibStudentRequestModel() {
    }

    // Parameterized constructor
    public LibStudentRequestModel(String requestId, String studentName, String studentEmail, 
                                 String bookTitle, String requestDate, String status, 
                                 String requestMessage, String libraryId, String studentId) {
        this.requestId = requestId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.bookTitle = bookTitle;
        this.requestDate = requestDate;
        this.status = status;
        this.requestMessage = requestMessage;
        this.libraryId = libraryId;
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

    public String getBookTitle() {
        return bookTitle;
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

    public String getLibraryId() {
        return libraryId;
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

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
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

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
} 