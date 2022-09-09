package com.lessing.equipment.sdk;


import lombok.Data;

import java.util.Calendar;

@Data
public class AlarmEventInfo {
    public static long index = 0;
    public long id;
    public int chn;
    public int type;
    public Calendar date;
    public AlarmStatus status;

    public AlarmEventInfo(int chn, int type, AlarmStatus status) {
        this.chn = chn;
        this.type = type;
        this.status = status;
        this.date = Calendar.getInstance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlarmEventInfo showInfo = (AlarmEventInfo) o;
        return chn == showInfo.chn && type == showInfo.type;
    }
}
