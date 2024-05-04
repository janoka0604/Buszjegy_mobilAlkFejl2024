package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

public class User {
    private String foreName;
    private String surName;
    private String email;
    private String birthDate;
    private BillingAddress billingAddress;

    public User() {}

    public User(String foreName, String surName, String email, String birthDate) {
        this.foreName = foreName;
        this.surName = surName;
        this.email = email;
        this.birthDate = birthDate;
        this.billingAddress = new BillingAddress();
    }

    public String getForeName() {
        return foreName;
    }

    public String getSurName() {
        return surName;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }
}
