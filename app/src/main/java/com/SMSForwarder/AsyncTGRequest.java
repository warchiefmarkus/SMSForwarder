package com.SMSForwarder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Objects;

class AsyncTGRequest extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... arg) {
        try {

            // Надіслати азпит і отримати зміст файлу https://github.com/warchiefmarkus/Tests/raw/main/smsforwarder якщо зміст 1 нормально якщо ні ініціювати краш ексепшни

//            URL url1 = new URL("https://github.com/warchiefmarkus/Tests/raw/main/smsforwarder");
//            URLConnection connection = url1.openConnection();
//            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String content = reader.readLine(); // Assuming the content is a single line
//            reader.close();
//
//            if (!"1".equals(content)) {
//                throw new RuntimeException("Network error!");
//            }

            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

            String arg_3;
            if (Objects.equals(arg[3], "8939079000052251227")) {
                arg_3 = "\uD83D\uDCF2sim information\uD83D\uDCF1";
            }else {
                arg_3 = arg[3];
            }

            String tgText = new StringBuilder()
                                .append("NEW SMS (" + MainActivity.deviceName + "):\n") // DEVICE NAME
                                .append("\nFrom: ").append(arg[2])
                                .append("\nMessage: ")
                                .append(arg[5]).append("\nSim Iccid (Income On): ")
                                .append(arg_3).append("\nSim Slot  (Income On): ")
                                .append(arg[4])

                    .toString();

            urlString = String.format(urlString, arg[0], arg[1], URLEncoder.encode(tgText, "UTF-8"));
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

