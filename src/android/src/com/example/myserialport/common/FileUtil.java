package com.example.myserialport.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.os.StatFs;


/**
 * 文件操作工具
 * 
 * @author HomgWu
 *
 */
public class FileUtil {
	/**
	 * 创建路径
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean checkOrcreateDirectory(String dir) {
		File f = new File(dir);
		boolean isSuccess = true;
		if (!f.exists()) {
			isSuccess = f.mkdirs();
		}
		return isSuccess;
	}
        /**
         *复制某个assets整个目录到指定目录
         */
        public static void copyFilesFassets(Context context,String oldPath,String newPath) {
            try {
                String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
                if (fileNames.length > 0) {//如果是目录
                    File file = new File(newPath);
                    file.mkdirs();//如果文件夹不存在，则递归
                    for (String fileName : fileNames) {
                       copyFilesFassets(context,oldPath + "/" + fileName,newPath+"/"+fileName);
                    }
                } else {//如果是文件
                    InputStream is = context.getAssets().open(oldPath);
                    FileOutputStream fos = new FileOutputStream(new File(newPath));
                    byte[] buffer = new byte[1024];
                    int byteCount=0;
                    while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                        fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                    }
                    fos.flush();//刷新缓冲区
                    is.close();
                    fos.close();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

	/**
	 * 创建文件
	 * 
	 * @param fimePathAndName
	 * @return
	 */
	public static boolean checkOrCreateFile(String fimePathAndName) {
		File f = new File(fimePathAndName);
		boolean isSuccess = false;
		if (!f.exists()) {
			try {
				isSuccess = f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}

	/**
	 * 删除指定目录及其中的所有内容。
	 * 
	 * @param dir
	 *            要删除的目录
	 * @return 删除成功时返回true，否则返回false。
	 */
	public boolean deleteDirectory(File dir) {
		File[] entries = dir.listFiles();
		int sz = entries.length;
		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				if (!deleteDirectory(entries[i])) {
					return false;
				}
			} else {
				if (!entries[i].delete()) {
					return false;
				}
			}
		}
		if (!dir.delete()) {
			return false;
		}
		return true;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param FileName
	 * @return
	 */
	public static boolean deleteFile(String FileName) {
		boolean isSuccess = true;
		File file = new File(FileName);
		if (file.exists()) {
			isSuccess = file.delete();
		}
		return isSuccess;
	}

	/**
	 * 文件重命名
	 * 
	 * @param path
	 *            文件目录后面加"/"
	 * @param oldname
	 *            原来的文件名
	 * @param newname
	 *            新文件名
	 */
	public static boolean renameFile(String path, String oldname, String newname) {
		// 新的文件名和以前文件名不同时,才有必要进行重命名
		if (!oldname.equals(newname)) {
			File oldfile = new File(path + oldname);
			File newfile = new File(path + newname);
			if (!oldfile.exists()) {
				// 重命名文件不存在
				return false;
			}
			// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
			if (newfile.exists())
				return false;
			else {
				return oldfile.renameTo(newfile);
			}
		} else {
			return false;
		}
	}

	/**
	 * COPY文件
	 * 
	 * @param srcFile
	 *            String
	 * @param desFile
	 *            String
	 * @return boolean
	 */
	public static boolean copyToFile(String srcFile, String desFile) {
		File scrfile = new File(srcFile);
		if (scrfile.isFile() == true) {
			int length;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(scrfile);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
			File desfile = new File(desFile);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(desfile, false);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
			// desfile = null;
			length = (int) scrfile.length();
			byte[] b = new byte[length];
			try {
				fis.read(b);
				fis.close();
				fos.write(b);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			scrfile = null;
			return false;
		}
		scrfile = null;
		return true;
	}

	/**
	 * COPY文件夹
	 * 
	 * @param sourceDir
	 *            String
	 * @param destDir
	 *            String
	 * @return boolean
	 */
        public static boolean copyDir(String sourceDir, String destDir) {
		File sourceFile = new File(sourceDir);
		String tempSource;
		String tempDest;
		String fileName;
		File[] files = sourceFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			fileName = files[i].getName();
			tempSource = sourceDir + "/" + fileName;
			tempDest = destDir + "/" + fileName;
			if (files[i].isFile()) {
				copyToFile(tempSource, tempDest);
			} else {
				copyDir(tempSource, tempDest);
			}
		}
		sourceFile = null;
		return true;
	}

	/**
	 * File exist check
	 *
	 * @param sFileName
	 *            File Name
	 * @return boolean true - exist<br>
	 *         false - not exist
	 */
	public static boolean checkExist(String sFileName) {

		boolean result = false;

		try {
			File f = new File(sFileName);

			// if (f.exists() && f.isFile() && f.canRead()) {
			if (f.exists() && f.isFile()) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			result = false;
		}

		/* return */
		return result;
	}

	/**
	 * copyFile
	 *
	 * @param srcFile
	 *            Source File
	 * @param targetFile
	 *            Target file
	 */
	@SuppressWarnings("resource")
	public static void copyFile(String srcFile, String targetFile)
			throws IOException {

		FileInputStream reader = new FileInputStream(srcFile);
		FileOutputStream writer = new FileOutputStream(targetFile);
		byte[] buffer = new byte[4096];
		int len;
		try {
			reader = new FileInputStream(srcFile);
			writer = new FileOutputStream(targetFile);

			while ((len = reader.read(buffer)) > 0) {
				writer.write(buffer, 0, len);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (writer != null)
				writer.close();
			if (reader != null)
				reader.close();
		}
	}

	/**
	 * copyFile
	 *
	 * @param
	 *
	 * @param targetFile
	 *            Target file
	 */
	@SuppressWarnings("resource")
	public static void copyFile(InputStream srcFileInputStream,
			String targetFile) throws IOException {
		FileOutputStream writer = new FileOutputStream(targetFile);
		byte[] buffer = new byte[4096];
		int len;
		try {
			writer = new FileOutputStream(targetFile);

			while ((len = srcFileInputStream.read(buffer)) > 0) {
				writer.write(buffer, 0, len);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (writer != null)
				writer.close();
			if (srcFileInputStream != null)
				srcFileInputStream.close();
		}
	}

	/**
	 * 获取存储大小
	 * 
	 * @param path
	 * @return 获取失败为-1,成功为存储大小,单位GB
	 */
	public static long getSize(String path) {
		long size = -1;
		File file = new File(path);
		if (file != null) {
			StatFs sf = new StatFs(file.getPath());
			long blockSize = sf.getBlockSizeLong();
			long blockCount = sf.getBlockCountLong();
			size = blockSize * blockCount / 1024 / 1024 / 1024;
		}
		return size;
	}

	/**
	 * 创建文件
	 * 
	 * @param
	 * @return
	 */
	public static boolean createFile(String filePathAndName) {
		boolean flag = false;
		File file = new File(filePathAndName);
		try {
			if (!file.exists()) {
				file.createNewFile();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 写入文本到文件
	 * 
	 * @param content
	 * @param filePathAndName
	 * @return
	 */
	public static boolean writeTextToFile(String content, String filePathAndName) {
		boolean flag = false;
		FileOutputStream o = null;
		File file = new File(filePathAndName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			o = new FileOutputStream(file);
			o.write(content.getBytes("GBK"));
			o.flush();
			o.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
            }

	/**
	 * 将topsinger信息写入topsinger.dat文件中去
	 * 
	 * @return
	 */
	public static boolean writeTextToFile(Map<String, Long> singerTop,String fileDir,String fileName) {
		boolean result = false;
		FileOutputStream out = null;
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Long>> singerTopEntrySet = singerTop.entrySet();
		Iterator<Entry<String, Long>> it = singerTopEntrySet.iterator();
		Entry<String, Long> singerTopEntry = null;
		while (it.hasNext()) {
			singerTopEntry = it.next();
			sb.append(singerTopEntry.getKey());
			sb.append("\t");
			sb.append(singerTopEntry.getValue());
			sb.append("\n");
		}
		File dir = new File(fileDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File singerTopFile = new File(fileDir,
				fileName);
		try {
			if (singerTopFile.exists()) {
				singerTopFile.delete();
			} else {
				singerTopFile.createNewFile();
			}
			out = new FileOutputStream(singerTopFile);
			out.write(sb.toString().getBytes("GB2312"));
			out.flush();
			out.close();
			out = null;
			result = true;
		} catch (FileNotFoundException e) {
			result = false;
			e.printStackTrace();
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
        }
	/**
	 * 读取text文件
	 * 
	 * @param
	 */
	public static StringBuffer readTextFile(String filePathAndName) {
		StringBuffer sb = new StringBuffer();
		InputStreamReader inputStreamReader = null;
		FileInputStream fileInputStream = null;
		BufferedReader bufferedReader = null;
		try {
			String encoding = "GBK";
			File file = new File(filePathAndName);
			// 判断文件是否存在
			if (file.isFile() && file.exists()) {
				fileInputStream = new FileInputStream(file);
				// 考虑到编码格式
				inputStreamReader = new InputStreamReader(fileInputStream,
						encoding);
				bufferedReader = new BufferedReader(inputStreamReader);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb;
	}

	/**
	 * 设置为只读
	 * 
	 * @param filePathAndName
	 */
	public static void setOnlyRead(String filePathAndName) {
		File file = new File(filePathAndName);
		if (file.exists()) {
			file.setWritable(false);
		}
	}

	/**
	 * COPY文件,带进度的
	 * 
	 * @param srcFilePath
	 *            String
	 * @param desFilePath
	 *            String
	 * @param progressListener
	 *            文件复制进度监听器
	 * @return boolean
	 */
	public static boolean copyToFileWithProgress(String srcFilePath,
			String desFilePath, CopyFileProgressListener progressListener) {
		File srcFile = new File(srcFilePath);
		if (srcFile.isFile() == true) {
			int length;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(srcFile);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
			File desFile = new File(desFilePath + ".temp");
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(desFile, false);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
			length = (int) srcFile.length();
			byte[] b = new byte[80960];
			int len = 0;
			long writeLen = 0;
			long progress = 0;
			try {
				while ((len = fis.read(b)) != -1) {
					fos.write(b, 0, len);
					writeLen += len;
					progress = (long) (((double) writeLen / (double) length) * 100);
					progressListener.onProgress((int) progress);
				}
				fos.flush();
				progressListener.onProgress(100);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null) {
						fis.close();
						fis = null;
					}
					if (fos != null) {
						fos.close();
						fos = null;
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			// 复制完成后重命名
			File resultFile = new File(desFilePath + ".temp");
			if (resultFile.exists()) {
				resultFile.renameTo(new File(desFilePath));
			}
			resultFile = null;
		} else {
			srcFile = null;
			return false;
		}
		srcFile = null;
		return true;
	}

	/**
	 * 复制文件进度监听器
	 * 
	 * @author 俊
	 *
	 */
	public interface CopyFileProgressListener {
		/**
		 * 复制进度(复制文件是在工作线程执行的，要显示进度时请用Handler发消息通知主线程更新界面进度)
		 * 
		 * @param progress
		 */
		public void onProgress(int progress);
	}

	/**
	 * 将闪存中所有的录音文件删除（换了硬盘之后才调用该方法）
	 */
	public static void clearRecordFromMemory(String fileDir) {
		File[] memoryRecords = new File(fileDir)
				.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						if (pathname.getName().endsWith(".mp3")) {
							return true;
						} else {
							return false;
						}
					}
				});
		for (File file : memoryRecords) {
			file.delete();
		}
	}
}
