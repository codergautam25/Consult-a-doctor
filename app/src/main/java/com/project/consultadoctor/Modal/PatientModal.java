package com.project.consultadoctor.Modal;

public class PatientModal {

    String image,name,phone,gender,uid;

    public PatientModal() {
    }

    public PatientModal(String image, String name, String phone, String gender,String uid) {
        this.image = image;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
