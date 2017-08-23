package memeticame.memeticame.models;

import android.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by efmino on 18-08-17.
 */

public class Contact {

    private String contact_name = "";
    private String contact_phone = "";
    private String contact_id = "0";
    private HashMap<String, Boolean> contacts = null;

    public String getContact_id() {
        return contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public HashMap<String, Boolean> getContacts() {
        return contacts;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }



}
