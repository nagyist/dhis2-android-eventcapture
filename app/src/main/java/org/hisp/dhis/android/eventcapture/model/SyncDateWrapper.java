package org.hisp.dhis.android.eventcapture.model;

import android.support.annotation.Nullable;

import org.hisp.dhis.client.sdk.ui.AppPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SyncDateWrapper {
    public static final int DAYS_OLD = 1;

    private AppPreferences appPreferences;

    public SyncDateWrapper(AppPreferences appPreferences) {
        this.appPreferences = appPreferences;
    }

    public void setLastSyncedNow() {
        long lastSynced = Calendar.getInstance().getTime().getTime();
        appPreferences.setLastSynced(lastSynced);
    }

    public void clearLastSynced() {
        appPreferences.setLastSynced(0L);
    }

    @Nullable
    public Date getLastSyncedDate() {
        long lastSynced = appPreferences.getLastSynced();
        if (lastSynced == 0L) {
            return null;
        }
        Date date = new Date();
        date.setTime(lastSynced);
        return date;
    }

    public long getLastSyncedLong() {
        return appPreferences.getLastSynced();
    }

    public String getLastSyncedString() {
        long lastSynced = getLastSyncedLong();
        if (lastSynced == 0f) {
            return "Never";
        } else {
            Long diff = Calendar.getInstance().getTime().getTime() - lastSynced;
            if (diff >= TimeUnit.DAYS.toMillis(DAYS_OLD)) {

                Date d = getLastSyncedDate();
                SimpleDateFormat dt = new SimpleDateFormat("dd/mm/yy hh:mm");
                return dt.format(d);
            } else {
                Long hours = TimeUnit.MILLISECONDS.toHours(diff);
                Long minutes = TimeUnit.MILLISECONDS.toMinutes(
                        diff - TimeUnit.HOURS.toMillis(hours));
                String result = "";
                if (hours > 0) {
                    result += hours + "h ";
                }
                result += minutes + "m ago";
                return result;
            }
        }
    }
}
