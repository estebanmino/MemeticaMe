package memeticame.memeticame.models;

import java.util.HashMap;

/**
 * Created by efmino on 18-08-17.
 */

public class Contact {

    private String NAME = "";
    private String PHONE = "";
    private String ID = "0";
    private HashMap<String, Boolean> contacts = null;

    public String getID() {
        return ID;
    }

    public String getName() {
        return NAME;
    }

    public String getPhone() {
        return PHONE;
    }

    public HashMap<String, Boolean> getContacts() {
        return contacts;
    }

    public void setId(String CONTACT_ID) {
        this.ID = CONTACT_ID;
    }

    public void setName(String CONTACT_NAME) {
        this.NAME = CONTACT_NAME;
    }

    public void setPhone(String CONTACT_PHONE) {
        this.PHONE = CONTACT_PHONE;
    }

}
