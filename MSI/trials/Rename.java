import java.io.*;
import java.util.*;

public class Rename {

	public static void main(String[] args) {
		try {
			File dir = new File("prying");
			for(int x=30; x < 61; x++) {
				File f = new File(dir.getPath() + "/shot" + x + ".jpg");
				f.renameTo(new File(dir.getPath() + "/shot" + (x-30) + ".jpg"));
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}