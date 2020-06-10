package com.example.bluetoothtest.Models.bleCallBacks.sleep_band;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;



public class J1657_Service {



    public static List<List<byte[]>> groupListDataByteList = new ArrayList<>();
    static List<Integer> heartRateMeasures = new ArrayList<>();
    static List<Integer> breathMeasures = new ArrayList<>();
    public static LinkedHashMap<Long, List<Integer>> heartRateMapList = new LinkedHashMap();
    public static LinkedHashMap<Long, List<Integer>> breathMapList = new LinkedHashMap();
    public static ArrayList<byte[]> groupDataByteList = new ArrayList<>();


    public static List<List<byte[]>> groupListOutOfBedDataByteList = new ArrayList<>();
    public static ArrayList<byte[]> groupOutOfBedDataByteList = new ArrayList<>();
    public static ArrayList<OutOfBedModel> outOfBedList = new ArrayList<>();


    public static List<List<byte[]>> groupListRolloverStorageDataByteList = new ArrayList<>();
    public static ArrayList<byte[]> groupRolloverStorageDataByteList = new ArrayList<>();
    public static ArrayList<OutOfBedModel> rolloverStorageList = new ArrayList<>();

    public static void initializeList(){
        heartRateMeasures = new ArrayList<>();
        breathMeasures = new ArrayList<>();

        groupDataByteList = new ArrayList<>();
        groupListDataByteList = new ArrayList<>();
        heartRateMapList = new LinkedHashMap();
        breathMapList = new LinkedHashMap();

        groupListOutOfBedDataByteList = new ArrayList<>();
        groupOutOfBedDataByteList = new ArrayList<>();
        outOfBedList = new ArrayList<>();

        groupListRolloverStorageDataByteList = new ArrayList<>();
        groupRolloverStorageDataByteList = new ArrayList<>();
        rolloverStorageList = new ArrayList<>();
    }

    public static void processData(){

        for (int i=0 ; i < groupListDataByteList.size(); i++){

            List<byte[]> allGroups30min = groupListDataByteList.get(i);

            for (int counter100 = 0; counter100 < 100 ; ){

                long groupStartTime = 0;

                for (int group = 0 ; group < 4 ; group++ , counter100++){

                    byte [] item = allGroups30min.get(counter100);

                        switch (group){

                            case 0:

                                heartRateMeasures = new ArrayList<>();
                                breathMeasures = new ArrayList<>();

                                byte  z1 = item[2];
                                byte  z2 = item[3];
                                byte  z3 = item[4];
                                byte  z4 = item[5];
                                groupStartTime = getTimeMillis(z1, z2, z3, z4);

                                for (int x = 6 ; x < 19 ; x += 2){
                                    heartRateMeasures.add(item[x] & 0xFF);
                                }

                                for (int x = 7 ; x < 19 ; x += 2){
                                    breathMeasures.add(item[x] & 0xFF);
                                }

                                break;

                            case 1:

                                for (int x = 3 ; x < 19 ; x += 2){
                                    heartRateMeasures.add(item[x] & 0xFF);
                                }

                                for (int x = 2 ; x < 19 ; x += 2){
                                    breathMeasures.add(item[x] & 0xFF);
                                }


                                break;

                            case 2:

                                for (int x = 2 ; x < 19 ; x += 2){
                                    heartRateMeasures.add(item[x] & 0xFF);
                                }

                                for (int x = 3 ; x < 19 ; x += 2){
                                    breathMeasures.add(item[x] & 0xFF);

                                }

                                break;

                            case 3:

                                for (int x = 3 ; x < 15 ; x += 2){
                                    heartRateMeasures.add(item[x] & 0xFF);
                                }

                                for (int x = 2 ; x < 15 ; x += 2){
                                    breathMeasures.add(item[x] & 0xFF);
                                }

                                break;
                        }

                }
                heartRateMapList.put(groupStartTime, heartRateMeasures);
                breathMapList.put(groupStartTime, breathMeasures);
            }
        }
        Log.i("zzz" , breathMapList.toString());
        Log.i("zzz" , heartRateMapList.toString());

        saveDataText();
        SleepBandBleCallBack.mListener.onHeartRateBreathingStorageDataFinished();
    }

    public static void saveDataText (){

        try {

            File heartRateDataFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "/heartRateData.csv");

            //File heartRateDateFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/heartRateDateFile.txt");
            //File heartRateDataFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/heartRateDataFile.txt");
            //File breathDateFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/breathDateFile.txt");
            //File breathDatafile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/breathDatafile.txt");


            heartRateDataFile.createNewFile();

            //heartRateDateFile.createNewFile();
            //heartRateDataFile.createNewFile();

            FileWriter heartRateDataFileWriter = new FileWriter(heartRateDataFile);




            //FileWriter writer1 = new FileWriter(heartRateDateFile);
            //FileWriter writer2 = new FileWriter(heartRateDataFile);

            for (LinkedHashMap.Entry entry : heartRateMapList.entrySet()){
                long firstDate = (long) entry.getKey() + (29 * 60 * 1000);
                firstDate -= 60 * 60 * 1000;

                //long firstDate = (long) entry.getKey() ;
                Date measureDateD = new Date();

                List<Integer> pp = (List<Integer>) entry.getValue();

                for (int i= pp.size() - 1 ; i >=0 ; i--){

                    measureDateD.setTime(firstDate);

                    String dateMeasureFormated = new SimpleDateFormat
                            ("yyyy-MM-dd HH:mm:ss ").format(measureDateD);

                    heartRateDataFileWriter.append(dateMeasureFormated + " ;");
                    heartRateDataFileWriter.append(pp.get(i) + " \n");


                    //writer1.append(dateMeasureFormated + " \n");
                    //writer2.append(pp.get(i) + " \n");

                    firstDate -= 60 * 1000;
                }

            }

            heartRateDataFileWriter.flush();
            heartRateDataFileWriter.close();
            //writerInternal2.flush();
            //writerInternal2.close();


            //writer1.flush();
            //writer1.close();
            //writer2.flush();
            //writer2.close();


            //breathDateFile.createNewFile();
            //breathDatafile.createNewFile();

            //FileWriter writer3 = new FileWriter(breathDateFile);
            //FileWriter writer4 = new FileWriter(breathDatafile);

            File breathDataFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/breathDateData.csv");
            breathDataFile.createNewFile();
            FileWriter breathDataFileWriter = new FileWriter(breathDataFile);


            for (LinkedHashMap.Entry entry : breathMapList.entrySet()){
                long firstDate = (long) entry.getKey() + (29 * 60 * 1000);
                firstDate -= 60 * 60 * 1000;

                //long firstDate = (long) entry.getKey() ;
                Date measureDateD = new Date();

                List<Integer> pp = (List<Integer>) entry.getValue();

                for (int i= pp.size() - 1 ; i >=0 ; i--){

                    measureDateD.setTime(firstDate);

                    String dateMeasureFormated = new SimpleDateFormat
                            ("yyyy-MM-dd HH:mm:ss ").format(measureDateD);

                    breathDataFileWriter.append(dateMeasureFormated + " ;");
                    breathDataFileWriter.append(pp.get(i) + " \n");

                    //writer3.append(dateMeasureFormated + " \n");
                    //writer4.append(pp.get(i) + " \n");

                    firstDate -= 60 * 1000;
                }

            }

            breathDataFileWriter.flush();
            breathDataFileWriter.close();
            //writerInternal4.flush();
            //writerInternal4.close();

            //writer3.flush();
            //writer3.close();
            //writer4.flush();
            //writer4.close();

            //Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void processDataOutOfBed(){

        for (int i=0 ; i < groupListOutOfBedDataByteList.size(); i++){

            List<byte[]> allGroups30min = groupListOutOfBedDataByteList.get(i);

            for (int counter100 = 0; counter100 < 4 ; ){

                for (int group = 0 ; group < 4 ; group++ , counter100++){

                    byte [] item = allGroups30min.get(counter100);

                    byte  z1 = item[2];
                    byte  z2 = item[3];
                    byte  z3 = item[4];
                    byte  z4 = item[5];
                    long groupStartTime  = getTimeMillis(z1, z2, z3, z4);

                    byte  t1 = item[6];
                    byte  t2 = item[7];
                    byte  t3 = item[8];
                    byte  t4 = item[9];
                    double duration = forbyteToDouble(t1, t2, t3, t4);

                    byte  z5 = item[11];
                    byte  z6 = item[12];
                    byte  z7 = item[13];
                    byte  z8 = item[14];
                    long groupStartTime2  = getTimeMillis(z5, z6, z7, z8);

                    byte  t5 = item[15];
                    byte  t6 = item[16];
                    byte  t7 = item[17];
                    byte  t8 = item[18];
                    double duration2 = forbyteToDouble(t5, t6, t7, t8);

                    constructOutOfBedObject(groupStartTime, duration,
                            groupStartTime2 , duration2, item[10] == 1, OperationList.OUT_OF_BED );

                }

            }
        }

        //saveOutOfBedText();
        saveOutOfBedTextInternal();
        SleepBandBleCallBack.mListener.onOutOfBedStorageDataFinished();
    }

    public static void saveOutOfBedTextInternal(){

        try {
            File outOfBedDataFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/outOfBedData.csv");

            outOfBedDataFile.createNewFile();

            FileWriter outOfBedDataFileWriter = new FileWriter(outOfBedDataFile);

            for (OutOfBedModel object : outOfBedList){
                outOfBedDataFileWriter.append(object.getFirstDate() + " ;");
                outOfBedDataFileWriter.append(object.getDuration1() + " ;");
                outOfBedDataFileWriter.append(object.getEndFirstDate() + " ;");
                outOfBedDataFileWriter.append(object.getSecondDate() + " ;");
                outOfBedDataFileWriter.append(object.getDuration2() + " ;");
                outOfBedDataFileWriter.append(object.getEndSecondDate() + " \n");
            }


            outOfBedDataFileWriter.flush();
            outOfBedDataFileWriter.close();
        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    public static void saveOutOfBedText(){

        //saveOutOfBedTextInternal();

        try {
            File firstDateFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/firstDateFile.txt");
            File durationDataFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/durationDataFile.txt");
            File endfirstDateFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/endfirstDateFile.txt");
            File secondDateFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/secondDateFile.txt");
            File seconddurationDataFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/seconddurationDataFile.txt");
            File endSecondDateFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/endSecondDateFile.txt");

            firstDateFile.createNewFile();
            durationDataFile.createNewFile();
            endfirstDateFile.createNewFile();
            secondDateFile.createNewFile();
            seconddurationDataFile.createNewFile();
            endSecondDateFile.createNewFile();

            FileWriter writer1 = new FileWriter(firstDateFile);
            FileWriter writer2 = new FileWriter(durationDataFile);
            FileWriter writer3 = new FileWriter(endfirstDateFile);
            FileWriter writer4 = new FileWriter(secondDateFile);
            FileWriter writer5 = new FileWriter(seconddurationDataFile);
            FileWriter writer6 = new FileWriter(endSecondDateFile);

            for (OutOfBedModel object : outOfBedList){
                writer1.append(object.getFirstDate() + " \n");
                writer2.append(object.getDuration1() + " \n");
                writer3.append(object.getEndFirstDate() + " \n");
                writer4.append(object.getSecondDate() + " \n");
                writer5.append(object.getDuration2() + " \n");
                writer6.append(object.getEndSecondDate() + " \n");
            }


            writer1.flush();
            writer1.close();
            writer2.flush();
            writer2.close();
            writer3.flush();
            writer3.close();
            writer4.flush();
            writer4.close();
            writer5.flush();
            writer5.close();
            writer6.flush();
            writer6.close();
        }
        catch (Exception e){

            e.printStackTrace();
        }

    }


    public static void saveRolloverStorageTextInternal(){
        try {
            File firstDateFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/RolloverStorageData.csv");

            firstDateFile.createNewFile();


            FileWriter writer1 = new FileWriter(firstDateFile);


            for (OutOfBedModel object : rolloverStorageList){
                writer1.append(object.getFirstDate() + " ;");
                writer1.append(object.getDuration1() + " ;");
                writer1.append(object.getEndFirstDate() + " ;");
                writer1.append(object.getSecondDate() + " ;");
                writer1.append(object.getDuration2() + " ;");
                writer1.append(object.getEndSecondDate() + " \n");
            }


            writer1.flush();
            writer1.close();

        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    public static void saveRolloverStorageText(){
        saveRolloverStorageTextInternal();

        try {
            File firstDateFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/firstDateFileRolloverStorage.txt");
            File durationDataFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/durationDataFileRolloverStorage.txt");
            File endfirstDateFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/endfirstDateFileRolloverStorage.txt");
            File secondDateFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/secondDateFileRolloverStorage.txt");
            File seconddurationDataFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/seconddurationDataFileRolloverStorage.txt");
            File endSecondDateFile = new File("/data/data/fr.semantic.ecare.app.android/sample" + "/endSecondDateFileRolloverStorage.txt");

            firstDateFile.createNewFile();
            durationDataFile.createNewFile();
            endfirstDateFile.createNewFile();
            secondDateFile.createNewFile();
            seconddurationDataFile.createNewFile();
            endSecondDateFile.createNewFile();

            FileWriter writer1 = new FileWriter(firstDateFile);
            FileWriter writer2 = new FileWriter(durationDataFile);
            FileWriter writer3 = new FileWriter(endfirstDateFile);
            FileWriter writer4 = new FileWriter(secondDateFile);
            FileWriter writer5 = new FileWriter(seconddurationDataFile);
            FileWriter writer6 = new FileWriter(endSecondDateFile);

            for (OutOfBedModel object : rolloverStorageList){
                writer1.append(object.getFirstDate() + " \n");
                writer2.append(object.getDuration1() + " \n");
                writer3.append(object.getEndFirstDate() + " \n");
                writer4.append(object.getSecondDate() + " \n");
                writer5.append(object.getDuration2() + " \n");
                writer6.append(object.getEndSecondDate() + " \n");
            }


            writer1.flush();
            writer1.close();
            writer2.flush();
            writer2.close();
            writer3.flush();
            writer3.close();
            writer4.flush();
            writer4.close();
            writer5.flush();
            writer5.close();
            writer6.flush();
            writer6.close();
        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    public static void processRolloverStorage(){

        for (int i=0 ; i < groupListRolloverStorageDataByteList.size(); i++){

            List<byte[]> allGroups30min = groupListRolloverStorageDataByteList.get(i);

            for (int counter100 = 0; counter100 < 50 ; counter100++ ){

                    byte [] item = allGroups30min.get(counter100);

                    byte  z1 = item[2];
                    byte  z2 = item[3];
                    byte  z3 = item[4];
                    byte  z4 = item[5];
                    long groupStartTime  = getTimeMillis(z1, z2, z3, z4);

                    byte  t1 = item[6];
                    byte  t2 = item[7];
                    byte  t3 = item[8];
                    byte  t4 = item[9];
                    double duration = forbyteToDouble(t1, t2, t3, t4);

                    byte  z5 = item[11];
                    byte  z6 = item[12];
                    byte  z7 = item[13];
                    byte  z8 = item[14];
                    long groupStartTime2  = getTimeMillis(z5, z6, z7, z8);

                    byte  t5 = item[15];
                    byte  t6 = item[16];
                    byte  t7 = item[17];
                    byte  t8 = item[18];
                    double duration2 = forbyteToDouble(t5, t6, t7, t8);

                    constructOutOfBedObject(groupStartTime, duration,
                            groupStartTime2 , duration2, item[10] == 1, OperationList.ROLLOVER_STORAGE);



            }
        }

        //saveRolloverStorageText();
        saveRolloverStorageTextInternal();
        SleepBandBleCallBack.mListener.onRolloverStorageDataFinished();
    }

    public static void constructOutOfBedObject(long groupStartTime, double duration,
                                               long groupStartTime2, double duration2, boolean available, OperationList nameList ){

        OutOfBedModel object = new OutOfBedModel();

        Date measureDateD1 = new Date();
        groupStartTime -= 60 * 60 * 1000;

        measureDateD1.setTime(groupStartTime);
        String dateMeasure1Formated = new SimpleDateFormat
                ("yyyy-MM-dd HH:mm:ss").format(measureDateD1);

        String firstString = groupStartTime + "";
        firstString = firstString.substring(0, firstString.length() - 3);
        long newfirst = Long.parseLong(firstString);

        long measureDate = newfirst +  (long) (duration);
        String dd = (measureDate) + "000";

        long end1 = Long.parseLong(dd);


        Date measureEndDateD1 = new Date();
        measureEndDateD1.setTime(end1);
        String dateEndMeasure1Formated = new SimpleDateFormat
                ("yyyy-MM-dd HH:mm:ss").format(measureEndDateD1);

        object.setFirstDate(dateMeasure1Formated);
        object.setDuration1(Double.toString(duration));
        object.setEndFirstDate(dateEndMeasure1Formated);

        if (available) {
            Date measureDateD2 = new Date();
            groupStartTime2 -= 60 * 60 * 1000;

            measureDateD2.setTime(groupStartTime2);
            String dateMeasure2Formated = new SimpleDateFormat
                    ("yyyy-MM-dd HH:mm:ss").format(measureDateD2);


            String secondString = groupStartTime2 + "";
            secondString = secondString.substring(0, secondString.length() - 3);
            long newSecond= Long.parseLong(secondString);

            long measureDate2 = newSecond +  (long) (duration2);
            String dd1 = (measureDate2) + "000";

            long end2 = Long.parseLong(dd1);

            Date measureEndDateD2 = new Date();
            measureEndDateD2.setTime(end2);
            String dateEndMeasure2Formated = new SimpleDateFormat
                    ("yyyy-MM-dd HH:mm:ss").format(measureEndDateD2);


            object.setSecondDate(dateMeasure2Formated);
            object.setDuration2(Double.toString(duration2));
            object.setEndSecondDate(dateEndMeasure2Formated);
        }

        switch (nameList){
            case OUT_OF_BED:
                outOfBedList.add(object);
                break;

            case ROLLOVER_STORAGE:

                rolloverStorageList.add(object);
                break;
        }


    }

    private static long getTimeMillis(int i1, int i2, int i3, int i4){
        Calendar firstJan2000 = Calendar.getInstance();

        firstJan2000.set(Calendar.HOUR, 0);
        firstJan2000.set(Calendar.MINUTE, 0);
        firstJan2000.set(Calendar.AM_PM, Calendar.AM);
        firstJan2000.set(Calendar.SECOND, 0);
        firstJan2000.set(Calendar.MILLISECOND, 0);

        firstJan2000.set(Calendar.MONTH , Calendar.JANUARY);
        firstJan2000.set(Calendar.DAY_OF_MONTH, 1);
        firstJan2000.set(Calendar.YEAR, 2000);

        //long year2000 = 946681200;
        long year2000 = firstJan2000.getTimeInMillis();
        String year2000String = year2000 + "";
        year2000String = year2000String.substring(0, year2000String.length() - 3);
        long newYear2000 = Long.parseLong(year2000String);

        String z1 = (Integer.toHexString( i1 & 0xFF));
        String z2 = (Integer.toHexString( i2 & 0xFF));
        String z3 = (Integer.toHexString( i3 & 0xFF));
        String z4 = (Integer.toHexString( i4 & 0xFF));

        int c1 = Integer.parseInt(z1, 16);
        int c2 = Integer.parseInt(z2, 16);
        int c3 = Integer.parseInt(z3, 16);
        int c4 = Integer.parseInt(z4, 16);
        double xx = c1  + (c2 * 256) + (c3 * 65536) + (c4 * 16777216) ;

        long measureDate = newYear2000 +  (long) xx;

        String dd = (measureDate) + "000";

        return Long.parseLong(dd);
    }

    public static double forbyteToDouble(int i1, int i2, int i3, int i4){

        String z1 = (Integer.toHexString( i1 & 0xFF));
        String z2 = (Integer.toHexString( i2 & 0xFF));
        String z3 = (Integer.toHexString( i3 & 0xFF));
        String z4 = (Integer.toHexString( i4 & 0xFF));

        int c1 = Integer.parseInt(z1, 16);
        int c2 = Integer.parseInt(z2, 16);
        int c3 = Integer.parseInt(z3, 16);
        int c4 = Integer.parseInt(z4, 16);
        return c1  + (c2 * 256) + (c3 * 65536) + (c4 * 16777216) ;
    }

    public enum OperationList{
        OUT_OF_BED, ROLLOVER_STORAGE;
    }
}
