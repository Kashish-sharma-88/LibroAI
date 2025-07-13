package com.example.libroai;

public class LibStudentRecordModel {
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookCategory;
    private String issueDate;
    private String returnDate;
    private String status; // ISSUED, RETURNED, OVERDUE
    private int daysRemaining;
    private double fineAmount;
    private String studentPhone;

    public LibStudentRecordModel() {
        // Default constructor
    }

    public LibStudentRecordModel(String studentId, String studentName, String studentEmail, 
                                String bookId, String bookTitle, String bookAuthor, String bookCategory,
                                String issueDate, String returnDate, String status, 
                                int daysRemaining, double fineAmount, String studentPhone) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookCategory = bookCategory;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.daysRemaining = daysRemaining;
        this.fineAmount = fineAmount;
        this.studentPhone = studentPhone;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getStudentEmail() { return studentEmail; }
    public String getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public String getBookAuthor() { return bookAuthor; }
    public String getBookCategory() { return bookCategory; }
    public String getIssueDate() { return issueDate; }
    public String getReturnDate() { return returnDate; }
    public String getStatus() { return status; }
    public int getDaysRemaining() { return daysRemaining; }
    public double getFineAmount() { return fineAmount; }
    public String getStudentPhone() { return studentPhone; }

    // Setters
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }
    public void setBookCategory(String bookCategory) { this.bookCategory = bookCategory; }
    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public void setStatus(String status) { this.status = status; }
    public void setDaysRemaining(int daysRemaining) { this.daysRemaining = daysRemaining; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }
    public void setStudentPhone(String studentPhone) { this.studentPhone = studentPhone; }
} 