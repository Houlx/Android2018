package com.ssdut.houlx.addrbook;

import com.ssdut.houlx.addrbook.db.Contact;

import java.util.Comparator;

public class PinyinComparator implements Comparator<Contact> {
    @Override
    public int compare(Contact contact1, Contact contact2) {
        if ("@".equals(contact1.getNameLetters()) || "#".equals(contact2.getNameLetters())) {
            return -1;
        } else if ("#".equals(contact1.getNameLetters()) || "@".equals(contact2.getNameLetters())) {
            return -1;
        } else {
            return contact1.getNameLetters().compareTo(contact2.getNameLetters());
        }
    }
}
