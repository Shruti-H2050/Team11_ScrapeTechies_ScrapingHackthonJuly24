package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class Tarfileextract {
    
     public static void Archivetotar(String srcPath) {
         File SourceFilepath = new File(srcPath);
       
         String SourceFileName = SourceFilepath.getName();

         String BaseFileNamePath = SourceFilepath.getParent();
         
         String destPath = BaseFileNamePath + File.separator + SourceFileName + ".tar";
        
         try {
         TarArchiveOutputStream outputStream = new TarArchiveOutputStream(
                 new FileOutputStream(new File(destPath)));
         crunchfyArchive(SourceFilepath, outputStream);
         outputStream.flush();
     
         outputStream.close();
         }
         catch(Exception e) {
        	 e.getStackTrace() ;
         }
     }
     private static void crunchfyArchive(File SourceFilepath,
             TarArchiveOutputStream outputStream)  {
    	 try {
         if (SourceFilepath.isDirectory()) {
             
             archiveCrunchifyDirectory(SourceFilepath, outputStream);
         	//System.out.println("its  a directory");
         } else {
             
             archiveCrunchifyFile(SourceFilepath, outputStream);
            // System.out.println("its  a file directory");
         	
         }}
    	 catch(Exception e) {
        	 e.getStackTrace() ;
         }
     }

     private static void archiveCrunchifyDirectory(File SourceDirectory,
             TarArchiveOutputStream outputStream)  {
     	
         File[] crunchifyFiles = SourceDirectory.listFiles();
         try{ 
         if (crunchifyFiles != null){
         	if (crunchifyFiles.length < 1) 
         	{
               
                 TarArchiveEntry entry = new TarArchiveEntry(
                 		SourceDirectory.getName() );
               
                 outputStream.putArchiveEntry(entry);
                 outputStream.closeArchiveEntry();
             }        
             
            
             for (File crunchifyFile : crunchifyFiles) 
             {
                 crunchfyArchive(crunchifyFile, outputStream);
                 
             }
         }}
         catch(Exception e) {
        	 e.getStackTrace() ;
         }
         
     }
     
     
    private static void archiveCrunchifyFile(File crunchifyFile,
             TarArchiveOutputStream outputStream)  {
    	try {
         TarArchiveEntry crunchifyEntry = new TarArchiveEntry(
                 crunchifyFile.getName());
     
         crunchifyEntry.setSize(crunchifyFile.length());
         outputStream.putArchiveEntry(crunchifyEntry);
         BufferedInputStream inputStream = new BufferedInputStream(
                 new FileInputStream(crunchifyFile));
         int counter;
         
         byte byteData[] = new byte[512];
         while ((counter = inputStream.read(byteData, 0, 512)) != -1) {
             outputStream.write(byteData, 0, counter);
         }
         inputStream.close();
         outputStream.closeArchiveEntry();
    	}
    	catch(Exception e) {
       	 e.getStackTrace() ;
        }
    	
    	
     }
 	
 	}

