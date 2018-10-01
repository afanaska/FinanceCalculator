package MatchParser;

import Valute.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchParser {

    private ArrayList<Valute> valutes;
    private ArrayList<Course> courses;

    private void setValutesAndCourses() throws Exception {

        String fileName = "valute_course.txt";
        this.valutes = Valute.readValutesFromFile(fileName);
        this.courses = Course.readCoursesFromFile(fileName, valutes);
    }

    public MatchParser() throws Exception {
        setValutesAndCourses();
    }

    public Money Parse(String s) throws Exception
    {
        Result result = AddSub(s);
        if (!result.rest.isEmpty()) {
            throw new Exception("Строку не получилось разобрать полностью остаток строки: " + result.rest);
        }
        return result.current;
    }

    private Result AddSub(String s) throws Exception
    {
        Result currentResult = Bracket(s);
        Money currentMoney = currentResult.current;

        while (currentResult.rest.length() > 0) {
            if (!(currentResult.rest.charAt(0) == '+' || currentResult.rest.charAt(0) == '-')) break;

            char sign = currentResult.rest.charAt(0);
            String next = currentResult.rest.substring(1);

            currentResult = Bracket(next);

            if (sign == '+') {
                currentMoney = currentMoney.add(currentResult.current);
            } else {
                currentMoney = currentMoney.sub(currentResult.current);
            }
        }
        return new Result(currentMoney, currentResult.rest);
    }

    private Result Bracket(String s) throws Exception
    {
        if (s.charAt(0) == '(') {
            Result r = AddSub(s.substring(1));
            if (!r.rest.isEmpty() && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                throw new Exception("Незакрытая скобка");
            }
            return r;
        }
        return Function(s);
    }

    private Result Function(String s) throws Exception
    {
        String f = "";
        int i = 0;
        // ищем название функции
        // имя обязательно должна начинаться с буквы
        while (i < s.length() && (Character.isLetter(s.charAt(i)) || ( Character.isDigit(s.charAt(i)) && i > 0 ) )) {
            f += s.charAt(i);
            i++;
        }
        if (!f.isEmpty()) { // если что-нибудь нашли
            if ( s.length() > i && s.charAt( i ) == '(') { // и следующий символ скобка значит - это функция
                Result r = Bracket(s.substring(f.length()));
                return processFunction(f, r);
            }
        }
        return Num(s);
    }

    private Result Num(String s) throws Exception
    {
        boolean negative = false;
        double value;

        String valuteName = null;

        // число также может начинаться с минуса
        if( s.charAt(0) == '-' ){
            negative = true;
            s = s.substring( 1 );
        }

        //поиск действительного числа по регулярному выражению
        Pattern pattern = Pattern.compile("\\d+([.](\\d){1,2})?");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find())
        {
            String number = matcher.group();
            value = Double.parseDouble(number);
        }
        else
            throw new Exception( "Не найдено допустимое число в строке '" + s + "'" );

        if( negative ) value = -value;

        //определение валюты по формату - знакам до или после числа
        String before = s.substring(0, matcher.start());
        String after = "";

        for (Valute v : valutes)
        {
            String [] formatArray = v.getFormat().split("%.2f");

            if (formatArray.length > 0 && !formatArray[0].equals("")) {
                if (formatArray[0].equals(before)) {
                    valuteName = v.getName();
                    break;
                }
            }

            if (formatArray.length > 1) {

                after = s.substring(matcher.end(), matcher.end() + formatArray[1].length());

                if (formatArray[1].equals(after)) {
                    valuteName = v.getName();
                    break;
                }
            }
        }

        if (valuteName == null)
        {
            throw new Exception( "Валюта не определена" );
        }

        String finalValuteName = valuteName;

        Valute val = valutes.stream().filter((x)->x.getName().equals(finalValuteName)).findFirst().orElseGet(()->null);
        if (val == null)
            throw new Exception("Валюта "+ valuteName +" не была задана в файле" );


        s = s.substring(matcher.end() + after.length());

        String restPart = s;

        Money m = new Money(val, value);

        return new Result(m, restPart);
    }

    // определение функций
    private Result processFunction(String func, Result r) throws Exception {

        for (Valute v : valutes)
        {
            if (func.equals("to" + v.getName()))
            {
                return new Result(r.current.convert(v, courses), r.rest);
            }
        }

        throw new Exception("Функция '" + func + "' не определена");
    }

}
