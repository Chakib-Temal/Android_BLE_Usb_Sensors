package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils;

import com.example.bluetoothtest.Models.bleCallBacks.sleep_band.Convert;

import java.util.Date;

public class PedometerCommand {

    public static final int CMD_GET_BATTERY_LEVEL = 0x13;
    public static final int CMD_GET_DATA = 0x43;
    public static final int CMD_GET_ACTIVITY = 0x26;
    public static final int CMD_GET_ALARM = 0x24;
    public static final int CMD_GET_SLEEP_RANGE = 0x5C;
    public static final int CMD_GET_SLEEP_MODE_BY_PRESS = 0x5E;
    public static final int CMD_GET_TIME = 0x41;
    public static final int CMD_GET_TOTAL_ACTIVITY = 0x07;
    public static final int CMD_VIBRATE = 0x36;
    public static final int CMD_SET_ACTIVITY = 0x25;
    public static final int CMD_SET_ALARM = 0x23;
    public static final int CMD_SET_FOOTSTEP_GOAL = 0x0B;
    public static final int CMD_SET_PERSONAL_DATA = 0x2;
    public static final int CMD_SET_SLEEP_RANGE = 0x5B;
    public static final int CMD_SET_SLEEP_MODE_BY_PRESS = 0x5D;
    public static final int CMD_SET_TIME = 0x1;
    public static final int CMD_SET_TIME_FORMAT = 0x37;
    public static final int CMD_SET_UNIT_DISTANCE = 0x0F;
    public static final int CMD_START_REAL_TIME = 0x09;
    public static final int CMD_STOP_REAL_TIME = 0x0A;

    public static final int UNIT_KM = 0;
    public static final int UNIT_ML = 1;

    private static final int FORMAT_12H = 0;
    private static final int FORMAT_24H = 1;

    private static final int GENDER_FEMALE = 0;
    private static final int GENDER_MALE = 1;

    /**
     * Get detailed activity data of a specific day.<br />
     * The device stores 30 days of activity data in its memory.
     * @param day the day
     *            (0 stands for current's day data,
     *            1 stands for data of 1 day ago,
     *            and so on,
     *            29 stands for data of 29 day ago)
     * @return the command
     */
	public static byte[] getData(int day) {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_GET_DATA;
        command[1] = (byte) day;

        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
	}

    public static byte[] setUnitDistance(int unit) {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_SET_UNIT_DISTANCE;
        command[1] = (byte) unit;

        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] getTotalActivity(int day) {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_GET_TOTAL_ACTIVITY;
        command[1] = (byte) day;

        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] startRealTime() {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_START_REAL_TIME;

        command[1] = (byte) 0;
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] stopRealTime() {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_STOP_REAL_TIME;

        command[1] = (byte) 0;
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] setTimeFormat() {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_SET_TIME_FORMAT;
        command[1] = (byte) FORMAT_24H;

        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command , 15);

        return command;
    }

    /**
     * Get a "set personal data" command.
     * @param age age
     * @param height height in centimeters
     * @param weight weight in kilograms
     * @param footstepWidth footstep width in centimeters
     * @return the command
     */
    public static byte[] setPersonalData(int age, int height, int weight, int footstepWidth) {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_SET_PERSONAL_DATA;
        command[1] = (byte) GENDER_MALE;
        command[2] = (byte) age;
        command[3] = (byte) height;
        command[4] = (byte) weight;
        command[5] = (byte) footstepWidth;

        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] setFootstepGoal(int goal) {

        int goal_byte1_high = goal/(256*256);
        int goal_byte2_med = (goal/256)%256;
        int goal_byte3_low = goal % 256;

        byte[] command = new byte[16];

        command[0] = (byte) CMD_SET_FOOTSTEP_GOAL;
        command[1]= (byte) goal_byte1_high;
        command[2]= (byte) goal_byte2_med;
        command[3]= (byte) goal_byte3_low;

        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] getBatteryLevel() {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_GET_BATTERY_LEVEL;
        command[1] = (byte) 0;
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] getAlarm() {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_GET_ALARM;
        command[1] = (byte) 2; // nbr

        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] setAlarm() {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_SET_ALARM;
        command[1] = (byte) 2;    // nbr
        command[2] = (byte) 0;    // enable
        command[3] = (byte) 0x10; // hour
        command[4] = (byte) 0x45; // minute
        command[5] = (byte) 1;    // enabled on sunday
        command[6] = (byte) 1;    // enabled on monday
        command[7] = (byte) 1;    // enabled on tuesday
        command[8] = (byte) 1;    // enabled on wednesday
        command[9] = (byte) 1;    // enabled on thursay
        command[10] = (byte) 1;   // enabled on friday
        command[11] = (byte) 1;   // enabled on saturday

        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] getActivityPeriod() {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_GET_ACTIVITY;

        command[1] = (byte) 0;
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] setActivityPeriod() {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_SET_ACTIVITY;
        command[1] = (byte) 0x0; // begin hour
        command[2] = (byte) 0x1; // begin minute
        command[3] = (byte) 0x0; // end hour
        command[4] = (byte) 0x0; // end minute
        command[5] = (byte) 255; // enable week
        command[6] = (byte) 1;   // remind period

        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    public static byte[] vibrate() {

        byte[] commande= new byte[16];
        commande[0] = (byte) CMD_VIBRATE;
        commande[1] = (byte) 7; // vibration

        commande[2] = (byte) 0;
        commande[3] = (byte) 0;
        commande[4] = (byte) 0;
        commande[5] = (byte) 0;
        commande[6] = (byte) 0;
        commande[7] = (byte) 0;
        commande[8] = (byte) 0;
        commande[9] = (byte) 0;
        commande[10] = (byte) 0;
        commande[11] = (byte) 0;
        commande[12] = (byte) 0;
        commande[13] = (byte) 0;
        commande[14] = (byte) 0;

        commande[15] = Convert.get_checksum(commande, 15);

        return commande;
    }

    public static byte[] getTime() {

		 byte[] command = new byte[16];

		 command[0]=(byte) CMD_GET_TIME;

         command[1] = (byte) 0;
         command[2] = (byte) 0;
         command[3] = (byte) 0;
         command[4] = (byte) 0;
         command[5] = (byte) 0;
         command[6] = (byte) 0;
         command[7] = (byte) 0;
         command[8] = (byte) 0;
         command[9] = (byte) 0;
         command[10] = (byte) 0;
         command[11] = (byte) 0;
         command[12] = (byte) 0;
         command[13] = (byte) 0;
         command[14] = (byte) 0;

         command[15] = Convert.get_checksum(command, 15);

		 return command;
	}

	public static byte[] setTime() {

		Date date = new Date(System.currentTimeMillis());

		 byte[] commande= new byte[16];

		 commande[0] = (byte) CMD_SET_TIME;
		 commande[1] = Convert.intInByte(date.getYear()-100);
         commande[2] = Convert.intInByte(date.getMonth()+1);
         commande[3] = Convert.intInByte(date.getDate());
         commande[4] = Convert.intInByte(date.getHours());
         commande[5] = Convert.intInByte(date.getMinutes());
         commande[6] = Convert.intInByte(date.getSeconds());

         commande[7] = (byte)0;
         commande[8] = (byte) 0;
         commande[9] = (byte) 0;
         commande[10] = (byte) 0;
         commande[11] = (byte) 0;
         commande[12] = (byte) 0;
         commande[13] = (byte) 0;
         commande[14] = (byte) 0;
         commande[15] = (byte) 0;
         commande[15] = Convert.get_checksum(commande, 15);

		 return commande;
	}

    /**
     * Get a "set the sleep period" command.<br />
     * This command is specific to the E-care Fit pedometer.
     * @param startHour the start hour
     * @param startMinute the start minute
     * @param endHour the end hour
     * @param endMinute the end minute
     * @return the command
     */
    public static byte[] setSleepPeriod(int startHour, int startMinute, int endHour, int endMinute) {

        byte[] commande = new byte[16];

        commande[0] = (byte) CMD_SET_SLEEP_RANGE;

        // No matter the value passed (0 or 1),
        // the pedometer changes the mode only if the current time is between start and end.
        commande[1] = (byte) 1;

        commande[2] = Convert.intInByte(startHour);
        commande[3] = Convert.intInByte(startMinute);
        commande[4] = Convert.intInByte(endHour);
        commande[5] = Convert.intInByte(endMinute);

        commande[6] = (byte) 0;
        commande[7] = (byte) 0;
        commande[8] = (byte) 0;
        commande[9] = (byte) 0;
        commande[10] = (byte) 0;
        commande[11] = (byte) 0;
        commande[12] = (byte) 0;
        commande[13] = (byte) 0;
        commande[14] = (byte) 0;

        commande[15] = Convert.get_checksum(commande, 15);

        return commande;
    }

    /**
     * Get a "get the sleep period" command.<br />
     * This command is specific to the E-care Fit pedometer.
     * @return the command
     */
    public static byte[] getSleepPeriod() {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_GET_SLEEP_RANGE;

        command[1] = (byte) 0;
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }

    /**
     * Get a "set the sleep mode by press" command.<br />
     * This command is specific to the E-care Fit pedometer.
     * @param enable the user can manually set the sleep mode?
     * @return the command
     */
    public static byte[] setSleepModeByPress(boolean enable) {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_SET_SLEEP_MODE_BY_PRESS;
        command[1] = (byte) (enable ? 1 : 0);

        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }
    /**
     * Get a "get the sleep mode by press" command.<br />
     * This command is specific to the E-care Fit pedometer.
     * @return the command
     */
    public static byte[] getSleepModeByPress() {

        byte[] command = new byte[16];

        command[0] = (byte) CMD_GET_SLEEP_MODE_BY_PRESS;

        command[1] =(byte) 0;
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);

        return command;
    }
}
