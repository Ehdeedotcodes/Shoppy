package controller;

import android.util.Log;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import model.DeletedItemModel;

public class HistoryItemDuration {
    private Instant then, now;

    public HistoryItemDuration(){}

    //method to check duration between two times
    public boolean compareTime(Long timeAdded){
        Duration duration;
        long days = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = Instant.now();
            then = Instant.ofEpochMilli(timeAdded);

            long timeSpent = ChronoUnit.HOURS.between(then, now);

            duration = Duration.between(then, now);
            days = duration.toDays();

        }
        return days >= 1;
    }
}
