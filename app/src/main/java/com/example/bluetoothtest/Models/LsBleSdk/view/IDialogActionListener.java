package com.example.bluetoothtest.Models.LsBleSdk.view;

import android.content.Intent;

import com.lifesense.ble.bean.kchiing.KReminder;
import com.lifesense.ble.bean.kchiing.KRepeatSetting;

import java.util.List;

public abstract class IDialogActionListener {

	public void onSingleChoiceItemValue(int index){};
	
	public void onTimeChoiceValue(int hour,int minute){};
	
	public void onTitleAndTimeChoiceValue(String title, int hour, int minute){};

	public void onMultiChoiceItemValue(List<Integer> items){};
	
	public void onIntentResults(Intent intent){};
	
	public void onSettingItems(List<SettingItem> items){};
	
	public void onBindingResults(){};
	
	public void onKchiingReminderCreate(SettingItem item, KReminder reminder){};
	
	public void onRepeatSettingCreate(SettingItem item, KRepeatSetting repeatSetting){}

}
