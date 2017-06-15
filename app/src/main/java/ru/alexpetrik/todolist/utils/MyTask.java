package ru.alexpetrik.todolist.utils;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MyTask extends RealmObject {

    @PrimaryKey
    private String date;
    private String caption;
    private String description;
    private String refToFile;
    private String typeOfNote;
    private String deadline;
    private boolean remind;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefToFile() {
        return refToFile;
    }

    public void setRefToFile(String refToFile) {
        this.refToFile = refToFile;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTypeOfNote() {
        return typeOfNote;
    }

    public void setTypeOfNote(String typeOfNote) {
        this.typeOfNote = typeOfNote;
    }

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

}
