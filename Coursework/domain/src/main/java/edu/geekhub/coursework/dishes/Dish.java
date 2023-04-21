package edu.geekhub.coursework.dishes;

import java.util.Arrays;

public class Dish {
    private int id;
    private String name;
    private byte[] image;

    public Dish(int id, String name, byte[] image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Dish() {
        this(-1, null, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Dish{"
               + "id=" + id
               + ", name='" + name + '\''
               + ", image=" + Arrays.toString(image)
               + '}';
    }
}
