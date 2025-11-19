package org.example;

public class ParseResult {
    public Double number;
    public Float floatNum;
    public String error;

    public ParseResult(Double number, String error){
        this.number = number;
        this.error = error;
    }

    public ParseResult(Float floatNum, String error){
        this.floatNum = floatNum;
        this.error = error;
    }
}
