package edu.uncc.assignment12.models;

public class ToDoListItem {
    String documentId;
    String name;
    String priority;

    public ToDoListItem() {
    }

    public ToDoListItem(String docId, String name, String priority) {
        this.documentId = docId;
        this.name = name;
        this.priority = priority;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setDocumentId(String documentId) { this.documentId = documentId; }
}
