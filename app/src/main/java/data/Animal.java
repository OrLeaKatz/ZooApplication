package data;

public class Animal {
    // public fields & empty constructor for firebase
    public int id;
    public String name;
    public String description;

    public Animal() {

    }

    public Animal(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getResourceName() {
        return "animal" + id;
    }

    public String getWikiAPIUrl() {
        return "https://he.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=" + name;
    }
}
