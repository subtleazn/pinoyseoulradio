package com.pinoyseoul.radio.radiosHelper;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.pinoyseoul.radio.settings.Configs;
import com.pinoyseoul.radio.utils.Utils;

public class Recorder extends AsyncTask {

    private Context context;
    private String urlPath;
    private String recordedFileName;
    private boolean isRecording = false;

    public Recorder() {
    }

    public Recorder(Context context, String url, String recordedFilePath) {
        this.context = context;
        this.urlPath = url;
        this.recordedFileName = recordedFilePath;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        isRecording = true;

        try {
            URL url = new URL(urlPath);
            InputStream inputStream = url.openStream();

            File file = new File(Utils.getRootDirPath(context.getApplicationContext()) + Configs.FOLDER_REC);

            file.mkdirs();

            final File files = new File(file, recordedFileName);

            OutputStream outputStream = new FileOutputStream(files);

            byte[] buffer = new byte[4 * 1024];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                if (isCancelled())
                    break;
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void record() {
        File file = new File(Utils.getRootDirPath(context.getApplicationContext()) + Configs.FOLDER_REC + recordedFileName);

        if (file.exists()) {
            file.delete();
        }

        //  player.play();

        this.execute();

    }

    public void stopRecording() {
        isRecording = false;
        this.cancel(true);

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getRecordedFileName() {
        return recordedFileName;
    }

    public void setRecordedFileName(String recordedFileName) {
        this.recordedFileName = recordedFileName;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        this.isRecording = recording;
    }

}
