package notifier.parser;

import notifier.SRM;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public abstract class CalendarParser {
    protected static final SimpleDateFormat format;
    static {
        format = new SimpleDateFormat("yyyy年MM月dd日（E） HH時mm分", Locale.JAPAN);
        format.setTimeZone(TimeZone.getTimeZone("GMT+09:00"));
    }

    abstract public List<SRM> getSRMs();

    public static SimpleDateFormat getDataFormat() {
        return format;
    }
}
