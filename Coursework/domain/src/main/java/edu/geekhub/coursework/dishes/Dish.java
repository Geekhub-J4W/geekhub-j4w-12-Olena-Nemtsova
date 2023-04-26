package edu.geekhub.coursework.dishes;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dish dish = (Dish) o;

        return id == dish.id
               && Objects.equals(name, dish.name)
               && Arrays.equals(image, dish.image);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
}
