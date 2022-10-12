package com.adrianbcodes.timemanager.user;
public class UserBuilder {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;

    public static UserBuilder builder(){ return new UserBuilder();}

    public UserBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public UserBuilder withName(String name){
        this.name = name;
        return this;
    }

    public UserBuilder withSurname(String surname){
        this.surname = surname;
        return this;
    }

    public UserBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public UserBuilder withUsername(String username){
        this.username = username;
        return this;
    }

    public UserBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    public User build(){
        return new User(name, surname, email, username, password);
    }

    public User buildWithId(){
        return new User(id, name, surname, email, username, password);
    }
}

