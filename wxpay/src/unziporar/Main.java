package unziporar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
 
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
 
public class Main {
 
	public void extractZipFiles() {
		String root = "c:/javazip/";
		URL url = Main.class.getResource("/aa.zip");
		String filename = url.getFile();
		try {
			// destination folder to extract the contents
			byte[] buf = new byte[1024];
			ZipInputStream zipinputstream = null;
			ZipEntry zipentry;
			zipinputstream = new ZipInputStream(new FileInputStream(filename));
			zipentry = zipinputstream.getNextEntry();
			while (zipentry != null) {
 
				// for each entry to be extracted
				String entryName = zipentry.getName();
 
				System.out.println(entryName);
 
				int n;
				FileOutputStream fileoutputstream;
				File newFile = new File(entryName);
 
				String directory = newFile.getParent();
 
				// to creating the parent directories
				if (directory == null) {
					if (newFile.isDirectory()) {
						break;
					}
				} else {
					new File(root + directory).mkdirs();
				}
 
				if (!zipentry.isDirectory()) {
					System.out.println("File to be extracted....." + entryName);
					fileoutputstream = new FileOutputStream(root + entryName);
					while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
						fileoutputstream.write(buf, 0, n);
					}
					fileoutputstream.close();
				}
 
				zipinputstream.closeEntry();
				zipentry = zipinputstream.getNextEntry();
			}
			zipinputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	public void extractRarFiles() {
		URL url = Main.class.getResource("D:/abc.rar");
		String filename = url.getFile();
		File archive = new File(filename);
		File destination = new File("D:/abc/");
 
		Archive arch = null;
		try {
			arch = new Archive(archive);
		} catch (RarException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (arch != null) {
			if (arch.isEncrypted()) {
				System.out.println("archive is encrypted cannot extreact");
				return;
			}
			FileHeader fh = null;
			while (true) {
				fh = arch.nextFileHeader();
				if (fh == null) {
					break;
				}
				if (fh.isEncrypted()) {
					System.out.println("file is encrypted cannot extract: "
							+ fh.getFileNameString());
					continue;
				}
				System.out.println("extracting: " + fh.getFileNameString());
				try {
					if (fh.isDirectory()) {
						createDirectory(fh, destination);
					} else {
						File f = createFile(fh, destination);
						OutputStream stream = new FileOutputStream(f);
						arch.extractFile(fh, stream);
						stream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
 
	private File createFile(FileHeader fh, File destination) {
		File f = null;
		String name = null;
		if (fh.isFileHeader() && fh.isUnicode()) {
			name = fh.getFileNameW();
		} else {
			name = fh.getFileNameString();
		}
		f = new File(destination, name);
		if (!f.exists()) {
			try {
				f = makeFile(destination, name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}
 
	private File makeFile(File destination, String name) throws IOException {
		String[] dirs = name.split("\\\\");
		if (dirs == null) {
			return null;
		}
		String path = "";
		int size = dirs.length;
		if (size == 1) {
			return new File(destination, name);
		} else if (size > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				path = path + File.separator + dirs[i];
				new File(destination, path).mkdir();
			}
			path = path + File.separator + dirs[dirs.length - 1];
			File f = new File(destination, path);
			f.createNewFile();
			return f;
		} else {
			return null;
		}
	}
 
	private void createDirectory(FileHeader fh, File destination) {
		File f = null;
		if (fh.isDirectory() && fh.isUnicode()) {
			f = new File(destination, fh.getFileNameW());
			if (!f.exists()) {
				makeDirectory(destination, fh.getFileNameW());
			}
		} else if (fh.isDirectory() && !fh.isUnicode()) {
			f = new File(destination, fh.getFileNameString());
			if (!f.exists()) {
				makeDirectory(destination, fh.getFileNameString());
			}
		}
	}
 
	private void makeDirectory(File destination, String fileName) {
		String[] dirs = fileName.split("\\\\");
		if (dirs == null) {
			return;
		}
		String path = "";
		for (String dir : dirs) {
			path = path + File.separator + dir;
			new File(destination, path).mkdir();
		}
	}
 
	public static void main(String[] args) {
		Main main = new Main();
		// extract zip
		//main.extractZipFiles();
		// extract rar
		main.extractRarFiles();
	}
}
 

