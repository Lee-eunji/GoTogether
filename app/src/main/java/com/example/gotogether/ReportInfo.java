package com.example.gotogether;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportInfo implements Serializable {
    private String title;
    private ArrayList<String> contents;
    private String publisher;
    private Date createdAt;
    private String id;

    public ReportInfo(String title, ArrayList<String> contents, String publisher, Date createdAt, String id){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.id = id;
    }

    public ReportInfo(String title, ArrayList<String> contents, String publisher, Date createdAt){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }

    public Map<String, Object> getReportInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("title",title);
        docData.put("contents",contents);
        docData.put("publisher",publisher);
        docData.put("createdAt",createdAt);
        return  docData;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public ArrayList<String> getContents(){
        return this.contents;
    }
    public void setContents(ArrayList<String> contents){
        this.contents = contents;
    }
    public String getPublisher(){
        return this.publisher;
    }
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    public Date getCreatedAt(){
        return this.createdAt;
    }
    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }
    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
}

