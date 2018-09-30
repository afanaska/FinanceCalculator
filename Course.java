package Valute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Course {

    private static Logger log = Logger.getLogger("calculating");

    private double koef;

    public double getKoef() {
        return koef;
    }

    private Valute valuteFrom;

    public Valute getValuteFrom() {
        return valuteFrom;
    }

    private Valute valuteTo;

    public Valute getValuteTo() {
        return valuteTo;
    }

    public Course (Valute valuteFrom, Valute valuteTo, double koef)
    {
        this.koef = koef;
        this.valuteFrom = valuteFrom;
        this.valuteTo = valuteTo;
    }

    public static ArrayList<Course> readCoursesFromFile(String fileName, ArrayList<Valute> valutes) throws Exception {
        ArrayList<Course> courseList = new ArrayList<Course>();

        File file = new File(fileName);
        if (!file.exists())
            throw new Exception("Файла не существует " + file.getAbsolutePath());

        try(BufferedReader br = new BufferedReader(new FileReader(file.getPath())))
        {
            //чтение построчно
            String s=br.readLine();
            while((s=br.readLine()) != null) {

                if (s.equals("Курс")) {

                    while ((s = br.readLine()) != null) {
                        String[] courseArray = s.split(" ");

                        Valute fromV = valutes.stream().filter((i) -> i.getName().equals(courseArray[0])).findFirst().orElseGet(() -> null);
                        if (fromV == null)
                            throw new Exception("Валюта "+ courseArray[0] + " не была задана в файле" );

                        Valute toV = valutes.stream().filter((i) -> i.getName().equals(courseArray[1])).findFirst().orElseGet(() -> null);
                        if (toV == null)
                            throw new Exception("Валюта "+ courseArray[1] + " не была задана в файле" );

                        Course c = new Course(fromV, toV, Double.parseDouble(courseArray[2]));
                        courseList.add(c);
                    }
                }
            }
        }
        catch(IOException ex){

            System.err.println(ex.getMessage());
            log.warning(ex.getMessage());
        }

        return courseList;
    }
}
