package com.adrianbcodes.timemanager.tag;

public class TagExample {
    public static Tag getTag1(){
        return TagBuilder.builder()
                .withId(1L)
                .withName("Tag1")
                .buildWithId();
    }
    public static Tag getTag2(){
        return TagBuilder.builder()
                .withId(2L)
                .withName("Tag2")
                .buildWithId();
    }

    public static Tag getTag3WithEmptyName(){
        return TagBuilder.builder()
                .withId(3L)
                .withName("")
                .buildWithId();
    }
}
