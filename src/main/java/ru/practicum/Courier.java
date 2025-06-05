package ru.practicum;

public class Courier {
    private String login;
    private String password;

    //Конструктор, принимающий логин и пароль
    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    //Конструктор по умолчанию
    public Courier() {
    }

    //Геттер для поля login
    public String getLogin() {
        return login;
    }

    //Сеттер для поля login
    public void setLogin(String login) {
        this.login = login;
    }

    //Геттер для поля password
    public String getPassword() {
        return password;
    }

    //Сеттер для поля password
    public void setPassword(String password) {
        this.password = password;
    }
}