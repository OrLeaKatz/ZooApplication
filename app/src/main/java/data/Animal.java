package data;

public class Animal {
    // public fields & empty constructor for firebase
    public long id;
    public String name;
    public String description;

    public Animal() {

    }

    public Animal(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
