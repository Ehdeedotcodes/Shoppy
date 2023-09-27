package controller;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ItemDuration {
    private Instant then, now;

    public ItemDuration(){}

    //method to check duration between two times
    public boolean compareTime(Long timeAdded, int daysToSpent){
        Duration duration;
        long days = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = Instant.now();
            then = Instant.ofEpochMilli(timeAdded);

            long timeSpent = ChronoUnit.HOURS.between(then, now);

            duration = Duration.between(then, now);
            days = duration.toDays();

        }
        return days >= daysToSpent;
    }
}
