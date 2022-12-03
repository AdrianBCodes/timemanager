package com.adrianbcodes.timemanager.client;

public class ClientExample {
    public static Client getClient1(){
        return ClientBuilder
                .builder()
                .withId(1L)
                .withName("Client1")
                .withNote("Note1")
                .buildWithId();
    }
    public static Client getClient2(){
        return  ClientBuilder
                .builder()
                .withId(2L)
                .withName("Client2")
                .withNote("Note2")
                .buildWithId();
    }

    public static Client getClient3WithEmptyName(){
        return  ClientBuilder
                .builder()
                .withId(3L)
                .withName("")
                .withNote("Note3")
                .buildWithId();
    }


}
