package com.example.csm;

public class Equipments {

    String documentID, name, brand, description, color, photo, roomsId;

public Equipments() {
        }

public Equipments( String name, String brand, String description, String color, String photo) {
        this.documentID = documentID;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.color = color;
        this.photo = photo;
        this.roomsId = roomsId;
        }

public String getDocumentID() {
        return documentID;
        }

public void setDocumentID(String documentID) {
        this.documentID = documentID;
        }

public String getName() {
        return name;
        }

public void setName(String name) {
        this.name = name;
        }

public String getBrand() {
        return brand;
        }

public void setBrand(String brand) {
        this.brand = brand;
        }

public String getDescription() {
        return description;
        }

public void setDescription(String description) {
        this.description = description;
        }

public String getColor() {
        return color;
        }

public void setCor(String color) {
        this.color = color;
        }

public String getPhoto() {
        return photo;
        }

public void setPhoto(String photo) {
        this.photo = photo;
        }

public String getRoomsId() {
        return roomsId;
        }

public void setRoomsId(String roomsId) {
        this.roomsId = roomsId;
        }
        }
