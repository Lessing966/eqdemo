package com.lessing.equipment.sdk;

import java.awt.*;

public class AlarmListenEvent extends AWTEvent {
    private static final long serialVersionUID = 1L;
    public static final int EVENT_ID = AWTEvent.RESERVED_ID_MAX + 1;

    private AlarmEventInfo alarmEventInfo;

    public AlarmListenEvent(Object target,
                            AlarmEventInfo alarmEventInfo) {
        super(target,EVENT_ID);

        this.alarmEventInfo = alarmEventInfo;
        ++AlarmEventInfo.index;
        this.alarmEventInfo.id = AlarmEventInfo.index;
    }

    public AlarmEventInfo getAlarmEventInfo() {
        return alarmEventInfo;
    }
}
