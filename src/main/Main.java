package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
	
	private static String input_url = "test88.tif";
	private static String input_url2 = "image2.png";
	private static String input_url3 = "image.tif";
	private static String input_url4 = "testOrientation.tif";
	private static String input_url5 = "testFinal.jpg";
	private static String input_url6 = "white.tif";
	private static String input_url7 = "ltu15.jpg";
	private static String input_url8 = "MiuAndBon.JPG";
	private static String input_url9 = "bin.JPG";
	private static String input_url_theTestOfHangging = "blue.tif";









	private static String output_url_Mac = "./src/main/image/output.jpg";
	private static String output_url2_Mac = "./src/main/image/output2.jpg";
	private static String output_url_Window = ".\\src\\main\\image\\output.jpg";
	private static String output_url2_Window = ".\\src\\main\\image\\output3.jpg";
	private static String output_url_Window_debug = ".\\src\\main\\image\\debug.txt";



	private static final String OS_NAME_WIN = "Windows 10";
	private final String OS_NAME_MAC = "Mac";
	
	public static void main(String[] args) {
		String test = "";
		JPEG_Compress jpeg_compress = new JPEG_Compress();
		
		//detect if this OS is Window10 or MacOS
		if(System.getProperty("os.name").equals(OS_NAME_WIN)) {
			jpeg_compress.Compress(input_url_theTestOfHangging, output_url_Window);
		}else {
			jpeg_compress.Compress(input_url, output_url_Mac);
		}
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();  
		   System.out.println(dtf.format(now));  	}
	
}
