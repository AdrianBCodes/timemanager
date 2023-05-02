package com.adrianbcodes.timemanager.user;

public class UserExample {
    public static User getUser1(){
        return UserBuilder
                .builder()
                .withId(1L)
                .withName("Name1")
                .withSurname("Surname1")
                .withEmail("email1@email.com")
                .withUsername("Username1")
                .withPassword("Password1")
                .buildWithId();
    }

    public static User getUser2(){
        return UserBuilder
                .builder()
                .withId(2L)
                .withName("Name2")
                .withSurname("Surname2")
                .withEmail("email2@email.com")
                .withUsername("Username2")
                .withPassword("Password2")
                .buildWithId();
    }

    public static User getUser3WithEmptyUsername(){
        return UserBuilder
                .builder()
                .withId(3L)
                .withName("Name3")
                .withSurname("Surname3")
                .withEmail("email3@email.com")
                .withUsername("")
                .withPassword("Password3")
                .buildWithId();
    }
}
