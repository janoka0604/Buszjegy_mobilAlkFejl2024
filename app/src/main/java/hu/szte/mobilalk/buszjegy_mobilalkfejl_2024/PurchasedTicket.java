package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import java.util.Calendar;
import java.util.Date;

public class PurchasedTicket {
    private String documentID;
    private String userEmail;
    private String city;
    private String type;
    private String validDate;

    public PurchasedTicket(String userEmail, String city, String type, String validDate, String documentID) {
        this.userEmail = userEmail;;
        this.city = city;
        this.type = type;
        this.validDate = validDate;
        this.documentID = documentID;
    }

    public String getUserEmail() {
        return userEmail;
    }


    public String getCity() {
        return city;
    }


    public String getType() {
        return type;
    }


    public String getValidDate() {
        return validDate;
    }

    public String _getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}

