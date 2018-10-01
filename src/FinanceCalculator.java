import MatchParser.*;

import java.util.*;
import java.util.logging.*;
import java.io.*;

public class FinanceCalculator {

    private static Logger log = Logger.getLogger("calculating");
    private static FileHandler handler = null;

    private static String math_expression = null;

    public static void main(String[] args) {

        try {
            handler = new FileHandler("calculating.log", true);
            handler.setFormatter(new SimpleFormatter());
            log.addHandler(handler);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        try {

            Scanner in = new Scanner(System.in);
            System.out.println("ФИНАНСОВЫЙ КАЛЬКУЛЯТОР");

            do
            {
                System.out.println("Введите выражение (или exit для выхода)");
                math_expression = in.nextLine();

                if (!math_expression.equals("exit")) {
                    MatchParser p = new MatchParser();

                    String result = p.Parse(math_expression).money();
                    System.out.println("Результат " + result);

                    log.info(math_expression + "=" + result);
                }

            } while (math_expression != null && !math_expression.equals("exit"));

        }

        catch(Exception ex){

            //for (StackTraceElement s : ex.getStackTrace())
            //{
            //    System.err.println(s.toString());
            //}

            System.err.println(ex.getMessage());
            log.warning(math_expression +"\n"+ ex.getMessage());

        }

    }
}
