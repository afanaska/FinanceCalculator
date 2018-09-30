package Valute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Valute {

    private static Logger log = Logger.getLogger("calculating");

    private String name;

    public String getName() {
        return name;
    }

    private String format;

    public String getFormat() {
        return format;
    }

    public Valute (String name, String format)
    {
        this.name = name;
        this.format = format;
    }

    public static ArrayList<Valute> readValutesFromFile(String fileName) throws Exception {
        ArrayList<Valute> valuteList = new ArrayList<Valute>();

        File file = new File(fileName);
        if (!file.exists())
            throw new Exception("Файла не существует " + file.getAbsolutePath());

        try(BufferedReader br = new BufferedReader(new FileReader(file.getPath())))
        {
            //чтение построчно
            String s =br.readLine();

            if (s.equals("Валюты"))
            {
                while(!(s=br.readLine()).equals("Курс"))
                {
                    String[] valuteArray = s.split(" ");
                    Valute v = new Valute(valuteArray[1], valuteArray[2]);
                    valuteList.add(v);
                }
            }
        }
        catch(IOException ex){

            System.err.println(ex.getMessage());
            log.warning(ex.getMessage());
        }

        return valuteList;
    }
}


