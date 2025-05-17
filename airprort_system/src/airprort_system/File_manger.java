package airprort_system;

public class File_manger {
private String filePath ;
    private String fileType;
    private boolean encryptionEnabled ;

    public File_manger(String filePath, String fileType, boolean encryptionEnabled) {
        this.filePath = filePath;
        this.fileType = fileType;
        this.encryptionEnabled = encryptionEnabled;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public boolean isEncryptionEnabled() {
        return encryptionEnabled;
    }

    public void setEncryptionEnabled(boolean encryptionEnabled) {
        this.encryptionEnabled = encryptionEnabled;
    }
    
    /*methods*/
    public boolean saveData(Object data ,String fileName ){         /*  save data method  */
            System.out.println("Saving data to: " + getFilePath() + "/" + fileName + "." + getFileType());
            return true; 
        }
    public Object loadData(String fileName){
        System.out.println("loading file from"+getFilePath()+"/"+fileName+    getFileType());
        return "loaded data sample";
    }
    public boolean backupDataBase() {
        try {
            Process p = Runtime.getRuntime().exec("mysqldump -uroot -p123456 test -r backup.sql");
            int result = p.waitFor();

            if (result == 0) {
                return true; // النسخ الاحتياطي تم بنجاح
            } else {
                return false; // فيه مشكلة حصلت
            }
        } catch (Exception e) {
            return false; // لو حصل استثناء (خطأ)
        }
    }

    public boolean restoreDataBase() {
        try {
            Process p = Runtime.getRuntime().exec("mysql -uroot -p123456 test < backup.sql");
            int result = p.waitFor();

            if (result == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false; }
    }
}
    

