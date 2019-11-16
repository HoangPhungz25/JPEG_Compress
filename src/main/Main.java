package main;


public class Main {
	
	private static String input_url = "image.tif";
	private static String input_url2 = "image2.png";

	private static String output_url = ".\\src\\main\\output.jpg";
	private static String output_url2 = ".\\src\\main\\output2.jpg";


	
	public static void main(String[] args) {
		
		JPEG_Compress jpeg_compress = new JPEG_Compress();
		jpeg_compress.Compress(input_url, output_url);
				
	}
	
	
}
