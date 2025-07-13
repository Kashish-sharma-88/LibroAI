package com.example.libroai;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class OrderModel {
    private String title, author, price, deliveryType, address, status, returnDate, orderType;
    private String deliveryDate; // ✅ NEW field
    private String docId;
    private String studentUid;

    public OrderModel() {}

    public OrderModel(String title, String author, String price, String deliveryType,
                      String address, String status, String returnDate, String orderType, String deliveryDate) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.deliveryType = deliveryType;
        this.address = address;
        this.status = status;
        this.returnDate = returnDate;
        this.orderType = orderType;
        this.deliveryDate = deliveryDate; // ✅
    }

    public static OrderModel fromDoc(DocumentSnapshot doc, String orderType) {
        String title = doc.getString("title");
        String author = doc.getString("author");
        String price = doc.getString("price");
        String deliveryType = doc.getString("deliveryType");
        String address = doc.getString("address");
        String status = doc.getString("status");
        String returnDate = doc.contains("returnDate") ? doc.getString("returnDate") : "";
        String deliveryDate = doc.contains("deliveryDate") ? doc.getString("deliveryDate") : ""; // ✅
        String studentUid = doc.getString("studentId");
        String docId = doc.getId();

        if (studentUid == null || studentUid.isEmpty())
            studentUid = FirebaseAuth.getInstance().getCurrentUser() != null ?
                    FirebaseAuth.getInstance().getCurrentUser().getUid() : "unknown";

        OrderModel model = new OrderModel(title, author, price, deliveryType, address, status, returnDate, orderType, deliveryDate);
        model.setStudentUid(studentUid);
        model.setDocId(docId);

        return model;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPrice() { return price; }
    public String getDeliveryType() { return deliveryType; }
    public String getAddress() { return address; }
    public String getStatus() { return status; }
    public String getReturnDate() { return returnDate; }
    public String getOrderType() { return orderType; }
    public String getDeliveryDate() { return deliveryDate; } // ✅
    public String getDocId() { return docId; }
    public String getStudentUid() { return studentUid; }

    public void setDocId(String docId) { this.docId = docId; }
    public void setStudentUid(String studentUid) { this.studentUid = studentUid; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; } // ✅
}
