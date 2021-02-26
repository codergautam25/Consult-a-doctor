package com.project.consultadoctor.Modal;

public class ProfileModal {
    public String name,cover,image,phone,specialization,experience,degree,gender,uid,email;

    public ProfileModal() {
    }

    public ProfileModal(String name,String uid, String cover,String email, String image, String phone, String specialization, String experience, String degree, String gender) {
        this.name = name;
        this.uid=uid;
        this.cover = cover;
        this.image = image;
        this.phone = phone;
        this.specialization = specialization;
        this.experience = experience;
        this.degree = degree;
        this.gender = gender;
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emial) {
        this.email = emial;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
