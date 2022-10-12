package com.adrianbcodes.timemanager.client;
public class ClientBuilder {
    private Long id;
    private String name;
    private String note;

    public static ClientBuilder builder(){ return new ClientBuilder();}

    public ClientBuilder withId(Long id){
        this.id = id;
        return this;
    }
    public ClientBuilder withName(String name){
        this.name = name;
        return this;
    }
    public ClientBuilder withNote(String note){
        this.note = note;
        return this;
    }

    public Client build(){
        return new Client(name, note);
    }

    public Client buildWithId(){
        return new Client(id, name, note);
    }
}
