package cn.org.rapid_framework.generator.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.org.rapid_framework.generator.GeneratorProperties;
/**
 * 
 * @author badqiu
 * @email badqiu(a)gmail.com
 */
public class FileHelper {
	/**
	 * 得到相对路径
	 */
	public static String getRelativePath(File baseDir,File file) {
		if(baseDir.equals(file))
			return "";
		if(baseDir.getParentFile() == null)
			return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length());
		return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length()+1);
	}
	
	public static void listFiles(File file,List collector) throws IOException {
		collector.add(file);
		if((!file.isHidden() && file.isDirectory()) && !isIgnoreFile(file)) {
			File[] subFiles = file.listFiles();
			for(int i = 0; i < subFiles.length; i++) {
				listFiles(subFiles[i],collector);
			}
		}
	}
	
	public static File mkdir(String dir,String file) {
		if(dir == null) throw new IllegalArgumentException("dir must be not null");
		File result = new File(dir,file);
		parnetMkdir(result);
		return result;
	}

	public static void parnetMkdir(File outputFile) {
		if(outputFile.getParentFile() != null) {
			outputFile.getParentFile().mkdirs();
		}
	}
	
	public static File getRsourcesByClassLoader(String resourceName) throws IOException {
		Enumeration<URL> urls = GeneratorProperties.class.getClassLoader().getResources(resourceName);
		while (urls.hasMoreElements()) {
			return new File(urls.nextElement().getFile());
		}
		throw new FileNotFoundException(resourceName);
	}
	
	private static boolean isIgnoreFile(File file) {
		List ignoreList = new ArrayList();
		ignoreList.add(".svn");
		ignoreList.add("CVS");
		ignoreList.add(".cvsignore");
		ignoreList.add(".copyarea.db"); //ClearCase
		ignoreList.add("SCCS");
		ignoreList.add("vssver.scc");
		ignoreList.add(".DS_Store");
		ignoreList.add(".git");
		ignoreList.add(".gitignore");
		for(int i = 0; i < ignoreList.size(); i++) {
			if(file.getName().equals(ignoreList.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public static  Set binaryExtentionsList = new HashSet();
	static {
	    loadBinaryExtentionsList("cn/org/rapid_framework/generator/util/binary_filelist.txt");
	}
	
	public static void loadBinaryExtentionsList(String resourceName) {
	    try {
        File file = FileHelper.getRsourcesByClassLoader(resourceName);
	    binaryExtentionsList.addAll(IOHelper.readLines(new FileReader(file)));
	    }catch(Exception e) {
	        throw new RuntimeException(e);
	    }
    }
	
	/** 检查文件是否是二进制文件 */
    public static boolean isBinaryFile(File file) {
        return isBinaryFile(file.getName());
    }

    public static boolean isBinaryFile(String filename) {
    	if(StringHelper.isBlank(getExtension(filename))) return false;
        return binaryExtentionsList.contains(getExtension(filename));
    }
	
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.indexOf(".");
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
    
	   //-----------------------------------------------------------------------
    /**
     * Deletes a directory recursively. 
     *
     * @param directory  directory to delete
     * @throws IOException in case deletion is unsuccessful
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message =
                "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
     * </ul>
     *
     * @param file  file or directory to delete, can be <code>null</code>
     * @return <code>true</code> if the file or directory was deleted, otherwise
     * <code>false</code>
     *
     * @since Commons IO 1.4
     */
    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                cleanDirectory(file);
            }
        } catch (Exception e) {
        }

        try {
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent){
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                String message =
                    "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }
}
