package model;

/**
 * Created by ASUS on 10-04-2016.
 */
public class TimeStamp {

    public static String getTimeStamp()
    {
        Long tsLong=System.currentTimeMillis()/1000;
        String ts=tsLong.toString();
        return ts;
    }
}
