package memeticame.memeticame.models;

import java.util.HashMap;

/**
 * Created by efmino on 18-08-17.
 */

public class Contact {

    private String email;
    private String phone;
    private String id;
    private HashMap<String, String> contacts = null;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public HashMap<String, String> getContacts() {
        return contacts;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
