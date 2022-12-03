package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.client.ClientExample;
import com.adrianbcodes.timemanager.user.UserExample;

public class ProjectExample {
    public static Project getProject1(){
        return ProjectBuilder
                .builder()
                .withId(1L)
                .withName("Project1")
                .withClient(ClientExample.getClient1())
                .withOwner(UserExample.getUser1())
                .buildWithId();
    }

    public static Project getProject2(){
        return ProjectBuilder
                .builder()
                .withId(2L)
                .withName("Project2")
                .withClient(ClientExample.getClient1())
                .withOwner(UserExample.getUser1())
                .buildWithId();
    }

    public static Project getProject3WithEmptyName(){
        return ProjectBuilder
                .builder()
                .withId(3L)
                .withName("")
                .withClient(ClientExample.getClient1())
                .withOwner(UserExample.getUser1())
                .buildWithId();
    }
}
