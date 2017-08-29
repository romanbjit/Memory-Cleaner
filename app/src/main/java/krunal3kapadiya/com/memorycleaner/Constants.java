package krunal3kapadiya.com.memorycleaner;

import android.webkit.MimeTypeMap;

import java.io.File;

import static krunal3kapadiya.com.memorycleaner.Constants.FileType.DIRECTORY;
import static krunal3kapadiya.com.memorycleaner.Constants.FileType.MISC_FILE;
import static krunal3kapadiya.com.memorycleaner.Constants.FileType.TXT;
import static krunal3kapadiya.com.memorycleaner.Constants.FileType.ZIP;

/**
 * Created by sanjay on 8/29/2017.
 */

public class Constants {

    private static String getExtension(String filename) {

        //returns the file extension or an empty string iff there is no extension

        return filename.contains(".") ? filename.substring(filename.lastIndexOf(".") + 1) : "";
    }

    public static String getMimeType(File file) {

        //returns the mime type for the given file or null iff there is none

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension(file.getName()));
    }

    public enum FileType {

        DIRECTORY, MISC_FILE, AUDIO, IMAGE, VIDEO, DOC, PPT, XLS, PDF, TXT, ZIP;

        public static FileType getFileType(File file) {

            if (file.isDirectory())
                return DIRECTORY;

            String mime = Constants.getMimeType(file);

            if (mime == null)
                return MISC_FILE;

            if (mime.startsWith("audio"))
                return FileType.AUDIO;

            if (mime.startsWith("image"))
                return FileType.IMAGE;

            if (mime.startsWith("video"))
                return FileType.VIDEO;

            if (mime.startsWith("application/ogg"))
                return FileType.AUDIO;

            if (mime.startsWith("application/msword"))
                return FileType.DOC;

            if (mime.startsWith("application/vnd.ms-word"))
                return FileType.DOC;

            if (mime.startsWith("application/vnd.ms-powerpoint"))
                return FileType.PPT;

            if (mime.startsWith("application/vnd.ms-excel"))
                return FileType.XLS;

            if (mime.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml"))
                return FileType.DOC;

            if (mime.startsWith("application/vnd.openxmlformats-officedocument.presentationml"))
                return FileType.PPT;

            if (mime.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml"))
                return FileType.XLS;

            if (mime.startsWith("application/pdf"))
                return FileType.PDF;

            if (mime.startsWith("text"))
                return TXT;

            if (mime.startsWith("application/zip"))
                return ZIP;

            return MISC_FILE;
        }
    }

    public static int getImageResource(File file) {

        switch (FileType.getFileType(file)) {

            case DIRECTORY:
                return R.drawable.ic_directory;

            case MISC_FILE:
                return R.drawable.ic_misc_file;

            case AUDIO:
                return R.drawable.ic_audio;

            case IMAGE:
                return R.drawable.ic_image;

            case VIDEO:
                return R.drawable.ic_video;

            case DOC:
                return R.drawable.ic_doc;

            case PPT:
                return R.drawable.ic_ppt;

            case XLS:
                return R.drawable.ic_xls;

            case PDF:
                return R.drawable.ic_pdf;

            case TXT:
                return R.drawable.ic_txt;

            case ZIP:
                return R.drawable.ic_zip;

            default:
                return 0;
        }
    }
    public static int getColorResource(File file) {

        switch (FileType.getFileType(file)) {

            case DIRECTORY:
                return R.color.directory;

            case MISC_FILE:
                return R.color.misc_file;

            case AUDIO:
                return R.color.audio;

            case IMAGE:
                return R.color.image;

            case VIDEO:
                return R.color.video;

            case DOC:
                return R.color.doc;

            case PPT:
                return R.color.ppt;

            case XLS:
                return R.color.xls;

            case PDF:
                return R.color.pdf;

            case TXT:
                return R.color.txt;

            case ZIP:
                return R.color.zip;

            default:
                return 0;
        }
    }


}
