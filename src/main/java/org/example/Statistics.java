package org.example;

public class Statistics {
    public TypeOfStats typeOfStats;

    private int count = 0;
    private double sum = 0;
    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = Integer.MIN_VALUE;

    public Statistics(TypeOfStats typeOfStats){
        this.typeOfStats = typeOfStats;
    }

    public void update(TypeOfStats typeOfStats, String value){
        switch (typeOfStats){
            case STRING:
                count++;
                int length = value.length();
                minLength = Math.min(minLength, length);
                maxLength = Math.max(maxLength, length);
                break;

            case DOUBLE, FLOAT:
                ParseResult result = (typeOfStats == TypeOfStats.DOUBLE)
                        ? parseDouble(value)
                        : parseFloat(value);
                if (result.number != null) {
                    double val = result.number;
                    count++;
                    sum += val;
                    min = Math.min(min, val);
                    max = Math.max(max, val);
                } else if(result.floatNum != null) {
                    float val = result.floatNum;
                    count++;
                    sum += val;
                    min = Math.min(min, val);
                    max = Math.max(max, val);
                }else{
                    System.out.println("Error while updated statistics.");
                }
                break;
        }
    }

    public String getStat(TypeOfStats typeOfStats, boolean shortStats){
        String result = "";
        switch (typeOfStats){
            case STRING:
                if (shortStats) {
                    result = "Количество: " + count;
                } else {
                    result = "Количество: " + count + ", Мин. длина строки: " + minLength + ", Макс. длина строки: " + maxLength;
                }
                break;

            case DOUBLE, FLOAT:
                if (shortStats) {
                    result = "Количество: " + count;
                } else {
                    double avg = count == 0 ? 0 : sum / count;
                    result = "Количество: " + count + ", Минимум: " + min + ", Максимум: " + max + ", Сумма: " + sum + ", Среднее: " + avg;
                }
                break;
        }
        return result;
    }

    public ParseResult parseDouble(String value){
        try{
            return new ParseResult(Double.parseDouble(value), value);
        }catch (NumberFormatException e){
            return new ParseResult((Double) null, e.getMessage());
        }
    }

    public ParseResult parseFloat(String value){
        try{
            return new ParseResult(Float.parseFloat(value), value);
        }catch (NumberFormatException e){
            return new ParseResult((Float) null, e.getMessage());
        }
    }

}
