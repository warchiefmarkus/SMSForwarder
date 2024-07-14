package com.SMSForwarder;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Objects;


class AsyncTGRequest extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... arg) {
        try {
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

            String arg_3;
            if (Objects.equals(arg[3], "8939079000052251227")) {
                arg_3 = "\uD83D\uDCF2sim information\uD83D\uDCF1";
            }else {
                arg_3 = arg[3];
            }

            String tgText = "NEW SMS:\n" +
                            "\nFrom: " + arg[2] +
                            "\nMessage: " + arg[5] +
                            "\nSim Iccid (Income On): " + arg_3 +
                            "\nSim Slot  (Income On): " + arg[4] ;


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

