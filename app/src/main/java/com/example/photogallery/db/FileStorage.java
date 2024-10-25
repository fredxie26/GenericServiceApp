package com.example.photogallery.db;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;

public class FileStorage {
    private Context context;
    public FileStorage(Context context) {
        this.context = context;
    }
    public ArrayList<String> findPhotos(Date startTimestamp, Date endTimestamp, String keywords) {
        File folder = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photogallery/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = folder.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime()
                        && f.lastModified() <= endTimestamp.getTime())
                ) && (keywords == "" || f.getPath().contains(keywords)))
                    photos.add(f.getPath());
            }
        }
        return photos;
    }
    public String updatePhotoA(String path, String caption) {
        String[] attr = path.split("_");
        String newPath = attr[0] + "_" + caption + "_" + attr[2] + "_" + attr[3] + "_" + attr[4];
        File to = new File(newPath);
        File from = new File(path);
        from.renameTo(to);
        return newPath;
    }
    public File createPhoto() throws IOException {
        File photoFile = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date());
            String imageFileName = "_caption_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            photoFile = File.createTempFile(imageFileName, ".jpg",storageDir);
        } catch (IOException ex) { }
        return photoFile;
    }
}

