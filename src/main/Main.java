package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
	
	private static String input_url = "lenna.png";






	private static String output_url_Mac = "./src/main/image/output.jpg";
	private static String output_url_Window = ".\\src\\main\\image\\output.jpg";



	private static final String OS_NAME_WIN = "Windows 10";
	private final String OS_NAME_MAC = "Mac";
	
	public static void main(String[] args) {
		String test = "";
		JPEG_Compress jpeg_compress = new JPEG_Compress();
		
		//detect if this OS is Window10 or MacOS
		if(System.getProperty("os.name").equals(OS_NAME_WIN)) {
			jpeg_compress.Compress(input_url, output_url_Window);
		}else {
			jpeg_compress.Compress(input_url, output_url_Mac);
		}
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();  
		   System.out.println(dtf.format(now));  	}
	
}
