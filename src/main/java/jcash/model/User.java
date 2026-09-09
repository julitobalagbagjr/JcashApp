package jcash.model;

public class User {

    private int id;
    private String name;
    private String email;
    private String number;
    private String pinHash;

    public User(String name, String email, String number, String pinHash) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.pinHash = pinHash;
    }

    public User(int id, String name, String email, String number, String pinHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.number = number;
        this.pinHash = pinHash;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }
}
