package edu.uncc.assignment12.models;

import java.io.Serializable;

public class ToDoList implements Serializable {
    private String documentId;  // Firestore doc ID
    private String name;

    public ToDoList() {
    }

    public ToDoList(String docId, String name) {
        this.documentId = docId;
        this.name = name;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String docId) {
        this.documentId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
