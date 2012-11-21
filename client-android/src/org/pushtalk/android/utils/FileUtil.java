package org.pushtalk.android.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.content.Context;

public class FileUtil {
    private static final String TAG = "FileUtil";
	
    
	public static ArrayList<String> readLines(InputStream is) {
		ArrayList<String> data = new ArrayList<String>();
		
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr, 2048);
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if ("".equals(line)) continue;	// remove blank line
				data.add(line);
			}
			
			isr.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}

	public static void zipFiles(String target, File... entries) {
		String[] names = new String[entries.length];
		int i = 0;
		for (File entry : entries) {
			names[i] = entry.getAbsolutePath();
			i++;
		}
		zipFiles(target, names);
	}
	
	public static void zipFiles(String target, String... entries) {
	    // Create a buffer for reading the files
	    byte[] buf = new byte[1024];
	    ZipOutputStream out = null;
	    try {
	        out = new ZipOutputStream(new FileOutputStream(target));
	        for (String entry : entries) {
	            FileInputStream in = new FileInputStream(entry);
	            entry = entry.substring(entry.lastIndexOf("/") + 1);
	            out.putNextEntry(new ZipEntry(entry));
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }
	            out.closeEntry();
	            in.close();
	        }
	        out.close();
	    } catch (IOException e) {
	    	Logger.e("FileUtil", "zip file io error", e);
	    } finally {
	    	if (null != out) {
	    		try {
					out.close();
				} catch (IOException e) {
					Logger.e("FileUtil", "close zip out stream error", e);
				}
	    	}
	    }
	}
	
	public static void unzipFromAssets(Context context, String zipFilename, 
			String targetDir) throws IOException {
        InputStream zipStream = context.getAssets().open(zipFilename);
        if (null == zipStream) {
            Logger.e(TAG, "Hanzi audio zip file does not exist - " + zipFilename);
            return;
        }		
	}
	
    public static void unzipStream(InputStream zipStream, String targetDir) {
    	 if (!targetDir.endsWith("/")) {
    		 targetDir = targetDir + "/";
         }
         FileOutputStream fileOut = null;
         ZipInputStream zipIn = null;
         ZipEntry zipEntry = null;
         File file = null;
         int readedBytes = 0;
         byte buf[] = new byte[4096];
         try {
             zipIn = new ZipInputStream(new BufferedInputStream(zipStream));
             while ((zipEntry = zipIn.getNextEntry()) != null) {
                 file = new File(targetDir + zipEntry.getName());
                 if (zipEntry.isDirectory()) {
                     file.mkdirs();
                 } else {
                     // 如果指定文件的目录不存在,则创建之.
                     File parent = file.getParentFile();
                     if (!parent.exists()) {
                         parent.mkdirs();
                     }
                     fileOut = new FileOutputStream(file);
                     while ((readedBytes = zipIn.read(buf)) > 0) {
                         fileOut.write(buf, 0, readedBytes);
                     }
                     fileOut.close();
                 }
                 zipIn.closeEntry();
             }
         } catch (IOException ioe) {
             ioe.printStackTrace();
         }
    }
    
    
    public static String readTextFile(File file) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            reader.close();
        } catch (Exception e) {
        }
        String s = builder.toString();
        builder.setLength(0);
        builder = null;
        return s;
    }

    public static void copyFile(InputStream srcFileStream, File destFile)
            throws IOException {
        InputStream fis = srcFileStream;
        FileOutputStream fos = null;
        boolean success = false;
        try {
            File parentFile = destFile.getAbsoluteFile().getParentFile();
            if ((parentFile != null) && (!parentFile.exists())) {
                makeDirs(parentFile);
            }

            fos = new FileOutputStream(destFile);
            int readCount;
            byte[] buffer = new byte[1024];
            while ((readCount = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, readCount);
            }
            closeStream(fis);
            closeStream(fos);
            success = true;
        } catch (IOException e) {
            throw e;
        } finally {
            closeStream(fis);
            closeStream(fos);
            if (!success) {
                destFile.delete();
            }
        }
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    public static void makeDirs(File dir) throws IOException {
        if (dir.exists()) {
            return;
        }
        boolean success = dir.mkdirs();
        if (!success) {
            throw new IOException("cannot create folder "
                    + dir.getAbsolutePath());
        }
    }
    
    public static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return null;
        }
        return filename.substring(lastDotIndex + 1);
    }

    public static String getFilePureName(String filename) {
    	if (StringUtils.isEmpty(filename)) return null;
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return null;
        }
        return filename.substring(0, lastDotIndex);
    }

    public static String getFilePureNameWithFixedExt(String filename, String ext) {
    	if (StringUtils.isEmpty(filename)) return null;
    	if (StringUtils.isEmpty(ext)) return getFilePureName(filename);
    	
        int lastDotIndex = filename.lastIndexOf("." + ext);
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return null;
        }
        return filename.substring(0, lastDotIndex);
    }

    public static void delete(String path) {
        delete(new File(path));
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delete(f);
            }
        }
        if (file.exists()) {
            boolean deleteFlag = file.delete();
            if (!deleteFlag) {
                Logger.e(TAG, "delete " + file.getAbsolutePath() + " : " + deleteFlag);
            }
        }
    }
    
    public static void deleteFilesInDir(String path) {
        deleteFilesInDir(new File(path));
    }

    public static void deleteFilesInDir(File file) {
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            delete(f);
        }
    }
    
    public static void createFile(String filename) throws IOException {
        File file = new File(filename);
        File dir = file.getParentFile();
        if (dir != null) {
            dir.mkdirs();
        }
        file.createNewFile();
    }


}
