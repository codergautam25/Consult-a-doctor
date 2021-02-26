package com.project.consultadoctor.Modal;

public class DoctorModal {
    String name, image, specialization, degree, address,uid, phone;

    public DoctorModal() {
    }

    public DoctorModal(String name, String imageUrl, String specialization, String degree, String distance, String uid,String phone) {
        this.name = name;
        this.image = imageUrl;
        this.specialization = specialization;
        this.degree = degree;
        this.address = distance;
        this.uid=uid;
        this.phone =phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
