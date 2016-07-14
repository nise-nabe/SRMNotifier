package notifier.parser;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import notifier.SRM;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class GoogleCalendarParser extends CalendarParser {
    private static final Logger log = Logger.getLogger(GoogleCalendarParser.class
            .getName());

    public GoogleCalendarParser() {
        Properties prop = new Properties();
        try {
            prop.load(GoogleCalendarParser.class.getClassLoader().getResourceAsStream("google-calendar.properties"));
            calendarId = prop.getProperty("calendar.calendarId");
            serviceAccountId = prop.getProperty("calendar.serviceAccountId");
            p12file = prop.getProperty("calendar.p12file");
        } catch (Exception e) {
            log.severe("Google カレンダー初期化失敗 : " + e.getMessage());
        }
    }

    private String calendarId;
    private String serviceAccountId;
    private String p12file;


    @Override
    public List<SRM> getSRMs() {
        List<SRM> result = new ArrayList<>();
        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            Credential credential = authorize(httpTransport, jsonFactory);
            Calendar client = new Calendar.Builder(httpTransport, jsonFactory, credential).setApplicationName("srmnotifier").build();
            Events events = client.events().list(calendarId)
                    .setTimeMin(new DateTime(new Date()))
                    .setTimeZone(CalendarParser.getDataFormat().getTimeZone().toString())
                    .execute();
            for (Event event : events.getItems()) {
                String name = event.getSummary();
                log.info("カレンダーデータ : " + event);
                if (!name.matches(".*(SRM|Algorithm).*")) {
                    continue;
                }
                SRM srm = new SRM();
                srm.setKey(event.getId());
                srm.setName(name);
                srm.setCompetitionTime(new Date(event.getStart().getDateTime().getValue()));
                srm.setRegisterTime(new Date(srm.getCompetitionTime().getTime() - 3L * 60 * 60 * 1000));
                srm.setCount(0);
                result.add(srm);
                log.info("SRMデータ解析 : " + srm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Credential authorize(HttpTransport httpTransport, JsonFactory jsonFactory) throws Exception {
        GoogleCredential.Builder builder  = new GoogleCredential.Builder();
        builder.setTransport(httpTransport);
        builder.setJsonFactory(jsonFactory);
        builder.setServiceAccountId(serviceAccountId);
        builder.setServiceAccountScopes(Arrays.asList(CalendarScopes.CALENDAR_READONLY));
        builder.setServiceAccountPrivateKeyFromP12File(new File(GoogleCalendarParser.class.getClassLoader().getResource(p12file).toURI()));
        return builder.build();
    }

}
