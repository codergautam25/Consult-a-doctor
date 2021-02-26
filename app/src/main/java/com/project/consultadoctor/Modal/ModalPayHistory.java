package com.project.consultadoctor.Modal;

public class ModalPayHistory {
    String patientName, doctorName,senderUid,receiverUid,senderImage, doctorImageUrl,amount, phone,patientImageUrl;

    public ModalPayHistory(String patientName, String doctorName, String senderUid, String receiverUid, String patientImageUrl, String phone, String senderImage, String doctorImageUrl, String amount) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.senderImage = senderImage;
        this.doctorImageUrl = doctorImageUrl;
        this.amount = amount;
        this.phone = phone;
        this.patientImageUrl=patientImageUrl;
    }

    public String getPatientImageUrl() {
        return patientImageUrl;
    }

    public void setPatientImageUrl(String patientImageUrl) {
        this.patientImageUrl = patientImageUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ModalPayHistory() {
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getDoctorImageUrl() {
        return doctorImageUrl;
    }

    public void setDoctorImageUrl(String doctorImageUrl) {
        this.doctorImageUrl = doctorImageUrl;
    }
}
