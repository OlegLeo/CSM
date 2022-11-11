package com.example.csm;

public class Equipments {

    String name, brand, description, color, photo, roomId, assign, category;

public Equipments() {
        }

public Equipments( String name, String brand, String description, String color, String photo, String roomId, String assign, String category) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.color = color;
        this.photo = photo;
        this.roomId = roomId;
        this.assign = assign;
        this.category = category;

        }
public Equipments( String name, String brand, String description, String color, String photo) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.color = color;
        this.photo = photo;
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

public String getRoomId() {
        return roomId;
        }

public void setRoomId(String roomId) {
        this.roomId = roomId;
        }

public String getAssign() {
        return assign;
}

public void setAssign(String assign) {
        this.assign = assign;
}

public String getCategory() {
        return category;
}

public void setCategory(String category) {
        this.category = category;
}

}
