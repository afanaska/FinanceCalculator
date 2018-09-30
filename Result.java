package MatchParser;
import Valute.*;

public class Result {

    //текущее состояние процесса вычисления
    public Money current;

    //остаток строки
    public String rest;

    public Result(Money current, String rest)
    {
        this.current = current;
        this.rest = rest;
    }
}
