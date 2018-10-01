package Valute;

import java.util.ArrayList;

public class Money {

    private double value;

    private Valute valute;

    public Money(Valute valute, double value)
    {
        this.valute = valute;
        this.value = value;
    }

    public String money(){

        return String.format(valute.getFormat(), this.value);
    }

    public Money add(Money m) throws Exception
    {
        if (!this.valute.equals(m.valute))
            throw new Exception("Нельзя складывать разные валюты! Проверьте правильность введенного Вами выражения.");

        Money sumMoney = new Money(this.valute, this.value + m.value);
        return sumMoney;
    }

    public Money sub(Money m) throws Exception
    {
        if (!this.valute.equals(m.valute))
            throw new Exception("Нельзя вычитать разные валюты! Проверьте правильность введенного Вами выражения.");

        Money difMoney = new Money(this.valute, this.value - m.value);
        return difMoney;
    }

    public Money convert(Valute vTo, ArrayList<Course> courseList) throws Exception
    {
        if (this.valute.equals(vTo))
            throw new Exception("Нельзя переводить валюту саму в себя! Проверьте правильность введенного Вами выражения.");

        Course neededCourse = null;

        for(Course course : courseList){

            if (course.getValuteFrom().equals(this.valute) && course.getValuteTo().equals(vTo))
            {
                neededCourse = course;
                break;
            }
        }

        if (neededCourse == null)
            throw new Exception("Курс для перевода " + this.valute.getName() + " в " + vTo.getName() + " не был задан в файле");

        Money convertMoney = new Money(vTo, this.value * neededCourse.getKoef());
        return convertMoney;
    }
}
