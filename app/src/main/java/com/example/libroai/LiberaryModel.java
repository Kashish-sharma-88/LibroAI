package com.example.libroai;

public class LiberaryModel {


        private String name;
        private String address;
        private String area;
        private String mobile;

        // 🔹 Required empty constructor for Firebase
        public LiberaryModel() {
        }

        // 🔹 Parameterized constructor
        public LiberaryModel(String name, String address, String area, String mobile) {
            this.name = name;
            this.address = address;
            this.area = area;
            this.mobile = mobile;
        }

        // 🔹 Getter methods
        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getArea() {
            return area;
        }

        public String getMobile() {
            return mobile;
        }

        // 🔹 Setter methods (optional for Firebase write)
        public void setName(String name) {
            this.name = name;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }



