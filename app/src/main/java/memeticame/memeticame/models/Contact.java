package memeticame.memeticame.models;

import java.util.HashMap;

/**
 * Created by efmino on 18-08-17.
 */

public class Contact {

    private String name;
    private String phone;
    private String id;
    private HashMap<String, String> contacts;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
