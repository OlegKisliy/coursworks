package model;

import java.io.Serializable;
import java.util.Objects;

public class UserModel implements Serializable {

    private String name;
    private String surname;
    private String telephoneNumber;
    private String password;

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UserModel(String telephoneNumber, String password) {
        this.telephoneNumber = telephoneNumber;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(telephoneNumber, userModel.telephoneNumber) &&
                Objects.equals(password, userModel.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(telephoneNumber, password);
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserModel(String name, String surname, String telephoneNumber, String password) {

        this.name = name;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
        this.password = password;
    }

    public UserModel(String name, String surname, String telephoneNumber) {
        this.name = name;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
