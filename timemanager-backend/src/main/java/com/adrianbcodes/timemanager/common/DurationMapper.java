package com.adrianbcodes.timemanager.common;

public class DurationMapper {
    public static String durationToString(Long duration){
        long hours = duration / 60;
        long minutes = duration - hours * 60;
        if(hours < 10){
            return '0' + String.valueOf(hours) + ':' + minutes;
        }
        return String.valueOf(hours) + ':' + minutes;
    }
}
