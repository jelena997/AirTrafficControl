package backup;

import exceptionHandler.LoggerClass;
import java.io.*;
import java.nio.file.*;
 
import java.util.*;
 
import java.util.logging.Level;
import java.util.zip.*;
 
 
public class BackupSystem extends Thread
{
    private FileFilter filter;
 
    public BackupSystem() {
    	super();
    	start();
    }
 
    public String getBackupFolderPath()
    {
        Package pack=this.getClass().getPackage();
        String packageName=pack.getName();
        String[] parsePackageName=packageName.split("\\.");
        packageName="";
 
        for(String s:parsePackageName)
            packageName+=s + File.separator;
        return System.getProperty("user.dir") + File.separator + "src" + File.separator +  packageName  + "backupFolder";
    }
 
    @Override
    public void run()
   {
      LoggerClass loggerClass= new LoggerClass(BackupSystem.class.getName());
      int counter=0;
 
       while(true)
       {
 
           if(counter==5) break;
           counter++;
           
 
           Calendar calendar= GregorianCalendar.getInstance();
           String nazivZipFajla="backup_" +calendar.get(Calendar.YEAR) + "_" + (calendar.get(Calendar.MONTH)+1) + "_"
                   + calendar.get(Calendar.DATE) + "_" +  calendar.get(Calendar.HOUR_OF_DAY) + "_" + calendar.get(Calendar.MINUTE) + ".zip";
 
           File zipFile = new File(getBackupFolderPath()  + File.separator + nazivZipFajla);
 
           try
           {
               Path startDir = Paths.get(System.getProperty("user.dir") + File.separator + "events");
               String  regExpr = "*.txt";
 
              filter = new FileFilter(regExpr);
               Files.walkFileTree(startDir, filter);
               zipFile.createNewFile();
               ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
 
 
 
               for (File f : filter.getFiles())
               {
                   ZipEntry zipTxt = new ZipEntry(f.getName());
                   zipStream.putNextEntry(zipTxt);
                   String filePath=filter.getFilesPaths().get(f);
                   byte[] bytes = Files.readAllBytes(Paths.get(filePath));
 
                   zipStream.write(bytes, 0, bytes.length);
                   zipStream.closeEntry();
 
 
 
               }
               zipStream.close();
 
 
               sleep(60000);
 
           }
           catch (FileNotFoundException e)
           {
            
               loggerClass.log(Level.WARNING,e);
           }
           catch (IOException e)
           {
        	  
               loggerClass.log(Level.WARNING,e);
           }
          
           catch (InterruptedException exc)
           {
        	 
               loggerClass.log(Level.WARNING,exc);
           }
 
       }
 
   }
}