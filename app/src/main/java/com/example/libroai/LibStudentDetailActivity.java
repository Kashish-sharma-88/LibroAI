package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LibStudentDetailActivity extends AppCompatActivity {

    private TextView studentNameHeader, studentNameText, studentRollNumberText, studentDepartmentText;
    private TextView studentEmailText, studentPhoneText, studentAddressText;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_student_detail);

        initViews();
        setupBackButton();
        loadStudentData();
    }

    private void initViews() {
        studentNameHeader = findViewById(R.id.studentNameHeader);
        studentNameText = findViewById(R.id.studentNameText);
        studentRollNumberText = findViewById(R.id.studentRollNumberText);
        studentDepartmentText = findViewById(R.id.studentDepartmentText);
        studentEmailText = findViewById(R.id.studentEmailText);
        studentPhoneText = findViewById(R.id.studentPhoneText);
        studentAddressText = findViewById(R.id.studentAddressText);
        backButton = findViewById(R.id.backButton);
    }

    private void setupBackButton() {
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void loadStudentData() {
        // Get student ID from intent (you can pass this from the previous activity)
        String studentId = getIntent().getStringExtra("studentId");
        if (studentId == null) {
            studentId = "STU001"; // Default for demo
        }

        // Load student details based on ID
        LibStudentDetailModel studentDetail = getStudentDetailById(studentId);
        if (studentDetail != null) {
            displayStudentData(studentDetail);
        }
    }

    private LibStudentDetailModel getStudentDetailById(String studentId) {
        // Sample data - in real app, this would come from database
        switch (studentId) {
            case "STU001":
                return new LibStudentDetailModel(
                    "STU001", "Rahul Sharma", "rahul.sharma@example.com",
                    "+91 9876543210", "123, Main Street, New Delhi, Delhi - 110001",
                    "Computer Science", "CS2024001", "6th Semester", "3rd Year",
                    "Male", "15 March 2002", "B+", "Mr. Rajesh Sharma", "+91 9876543200"
                );
            case "STU002":
                return new LibStudentDetailModel(
                    "STU002", "Priya Patel", "priya.patel@example.com",
                    "+91 9876543211", "456, Park Avenue, Mumbai, Maharashtra - 400001",
                    "Information Technology", "IT2024002", "4th Semester", "2nd Year",
                    "Female", "22 July 2003", "O+", "Mrs. Sunita Patel", "+91 9876543201"
                );
            case "STU003":
                return new LibStudentDetailModel(
                    "STU003", "Amit Kumar", "amit.kumar@example.com",
                    "+91 9876543212", "789, Lake Road, Bangalore, Karnataka - 560001",
                    "Electronics", "EC2024003", "8th Semester", "4th Year",
                    "Male", "10 November 2001", "A+", "Mr. Ramesh Kumar", "+91 9876543202"
                );
            default:
                return new LibStudentDetailModel(
                    "STU001", "Rahul Sharma", "rahul.sharma@example.com",
                    "+91 9876543210", "123, Main Street, New Delhi, Delhi - 110001",
                    "Computer Science", "CS2024001", "6th Semester", "3rd Year",
                    "Male", "15 March 2002", "B+", "Mr. Rajesh Sharma", "+91 9876543200"
                );
        }
    }

    private void displayStudentData(LibStudentDetailModel student) {
        // Set header and basic info
        studentNameHeader.setText(student.getStudentName());
        studentNameText.setText(student.getStudentName());
        studentRollNumberText.setText("Roll No: " + student.getStudentRollNumber());
        studentDepartmentText.setText(student.getStudentDepartment());

        // Set contact information
        studentEmailText.setText(student.getStudentEmail());
        studentPhoneText.setText(student.getStudentPhone());
        studentAddressText.setText(student.getStudentAddress());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
} 