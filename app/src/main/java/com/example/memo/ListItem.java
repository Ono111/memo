package com.example.memo;

public class ListItem {
    private long id = 0;
    private String title = null;
    private String body = null;
    private String uuid = null;
    private String date = null;
    private String date2 = null;
    private String date3 = null;

    long getId() {return id;}
    String getTitle() {return title;}
    String getBody() {return  body;}
    String getUuid() {return uuid;}
    String getDate() {return date;}
    String getDate2() {return date2;}
    String getDate3() {return date3;}

    void setId(long id) {this.id = id; }
    void setTitle(String title) {this.title = title;}
    void setBody(String body) {this.body = body;}
    void setUuid(String uuid) {this.uuid = uuid;}
    void setDate(String date) {this.date = date;}
    void setDate2(String date2) {this.date2 = date2;}
    void setDate3(String date3) {this.date3 = date3;}
}
