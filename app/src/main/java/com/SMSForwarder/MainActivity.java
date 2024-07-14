package com.SMSForwarder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static String Sim1IccId = "";
    public static String Sim2IccId = "";
    private static Context context;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    Timer timer = new Timer();

    public static Context getMActContext() {
        return MainActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_mesaj_listesi);
//
//        insertDummyContactWrapper();
//
//        // add button2 event listener
//        final int buttonRead = R.id.buttonRead;
//        final Button btn2 = findViewById(buttonRead);
//        btn2.setOnClickListener(v -> readSims());
//
//        // add send1 event listener
//        final int button1Send = R.id.button1Send;
//        final Button btn3 = findViewById(button1Send);
//        btn3.setOnClickListener(v -> {
//            EditText editTextCacheNumber = findViewById(R.id.editTextCacheNumber);
//            EditText editText1 = findViewById(R.id.editText1);
//            String message = "" + editText1.getText();
//            sendSMSWithSimSlot(String.valueOf(editTextCacheNumber.getText()), message, 0);
//        });
//
//        // add send2 event listener
//        final int button2Send = R.id.button2Send;
//        final Button btn4 = findViewById(button2Send);
//        btn4.setOnClickListener(v -> {
//            EditText editTextCacheNumber = findViewById(R.id.editTextCacheNumber);
//            EditText editText2 = findViewById(R.id.editText2);
//            String message = "" + editText2.getText();
//            sendSMSWithSimSlot(String.valueOf(editTextCacheNumber.getText()), message, 1);
//        });
//
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                readSims();
//            }
//        }, 0, 3000);
    }

    // V1
    // send sms function with args number (editTextCacheNumber) and message
//    private void sendSMS(String number, String message) {
//        // add sms send code here
//        try {
//            // SmsManager smsManager = SmsManager.getDefault();
//            // smsManager.sendTextMessage(number, null, message, null, null);
//
//            SubscriptionManager subscriptionManager = SubscriptionManager.from(this);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            int simCounts = subscriptionManager.getActiveSubscriptionInfoCount();
//            sendSMSWithSimSlot(number, message, 1);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void sendSMSWithSimSlot(String number, String message, int slotIndex) {
        try {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            SubscriptionInfo subscriptionInfo = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(slotIndex);
            if (subscriptionInfo != null) {
                int subscriptionId = subscriptionInfo.getSubscriptionId();
                SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(subscriptionId);
                smsManager.sendTextMessage(number, null, message, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readSims() {
        runOnUiThread(() -> {
            try {
                TelecomManager tm2 = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);

                List<PhoneAccountHandle> phoneAccounts = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("SIM", "NO PERMISSION");
                }
                phoneAccounts = tm2.getCallCapablePhoneAccounts();

                if (phoneAccounts == null) {
                    return;
                }

                if (phoneAccounts.size() == 0) {
                    return;
                }

                PhoneAccountHandle phoneAccountHandle1 = null;
                PhoneAccountHandle phoneAccountHandle2 = null;
                if (phoneAccounts.size() > 0) {
                    phoneAccountHandle1 = phoneAccounts.get(0);
                } else {
                    return;
                }
                if (phoneAccounts.size() > 1) {
                    phoneAccountHandle2 = phoneAccounts.get(1);
                }

                // set textEdit1 text to SIM1 ICCID and textEdit2 text to SIM2 ICCID
                final int textEdit1 = R.id.editText1;
                final int textEdit2 = R.id.editText2;
                final android.widget.EditText et1 = findViewById(textEdit1);
                final android.widget.EditText et2 = findViewById(textEdit2);
                if (phoneAccountHandle1.getId().length() < 19) {
                    Toast.makeText(MainActivity.this, "NO ICCID", Toast.LENGTH_SHORT).show();
                }else{
                    et1.setText(phoneAccountHandle1.getId().substring(0, 19));


                    Sim1IccId = phoneAccountHandle1.getId().substring(0, 19);
                    if (phoneAccountHandle2 != null) {
                        et2.setText(phoneAccountHandle2.getId().substring(0, 19));
                        Sim2IccId = phoneAccountHandle2.getId().substring(0, 19);
                    }

                    // set editText1_2 text to SIM1 phone number and editText2_2 text to SIM2 phone number
                    final int textEdit1_2 = R.id.editText1_2;
                    final int textEdit2_2 = R.id.editText2_2;
                    final android.widget.EditText et1_2 = findViewById(textEdit1_2);
                    final android.widget.EditText et2_2 = findViewById(textEdit2_2);
                    et1_2.setText(tm2.getLine1Number(phoneAccountHandle1));
                    if (phoneAccountHandle2 != null) {
                        et2_2.setText(tm2.getLine1Number(phoneAccountHandle2));
                    }
                }

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertDummyContactWrapper() {
        int READ_SMS = checkSelfPermission(Manifest.permission.READ_SMS);
        int RECEIVE_SMS = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
        int SEND_SMS = checkSelfPermission(Manifest.permission.SEND_SMS);
        int READ_PHONE_NUMBERS = checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS);
        int READ_PHONE_STATE = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        int READ_CALL_LOG = checkSelfPermission(Manifest.permission.READ_CALL_LOG);
        int READ_CONTACTS = checkSelfPermission(Manifest.permission.READ_CONTACTS);


        if (RECEIVE_SMS != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_PHONE_NUMBERS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_CONTACTS
                    },

                    REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted

            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Need grant permissions!", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
