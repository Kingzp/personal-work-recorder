package chokoapp.imanani;


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    private static final long UP_DOWN_STEP = 15 * 60 * 1000;

    public static String getDateString(long time) {
        return (new SimpleDateFormat("yyyy/MM/dd")).format(new Date(time));
    }
    public static String getTimeString(long time) {
        return (new SimpleDateFormat("HH:mm:ss")).format(new Date(time));
    }
    public static String getTimeStringFrom(long time, long from) {
        Date start = (new SimpleDateFormat("yyyy/MM/dd"))
            .parse(getDateString(from), new ParsePosition(0));
        long beginning_of_date = start.getTime();
        long hour =   (time - beginning_of_date) / (60 * 60 * 1000);
        long min  = ( (time - beginning_of_date) / (60 * 1000) ) % 60;
        long sec  = ( (time - beginning_of_date) / 1000 ) % 60;
        return String.format("%02d:%02d:%02d", hour, min, sec);
    }

    public static long up(long time) {
        long remainder = time % UP_DOWN_STEP;
        return remainder > 0 ?
            time + UP_DOWN_STEP - remainder :
            time + UP_DOWN_STEP;
    }
    public static long down(long time) {
        if ( time <= 0 ) return time;

        long remainder = time % UP_DOWN_STEP;
        return remainder > 0 ?
            time - remainder :
            time - UP_DOWN_STEP;
    }

    public static long adjustTime(long time) {
        long remainder = time % UP_DOWN_STEP;
        if ( remainder == 0 ) {
            return time;
        } else if ( remainder < UP_DOWN_STEP / 2 ) {
            return time - remainder;
        } else {
            return time + (UP_DOWN_STEP - remainder);
        }
    }
}