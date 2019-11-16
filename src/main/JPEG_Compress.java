package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class JPEG_Compress {
	int R[][];
	int G[][];
	int B[][];
	
	int Y[][];
	int Cb[][];
	int Cr[][];
	
	int currentBlock_Y[][];
	int currentBlock_Cb[][];
	int currentBlock_Cr[][];

	int currentVector_Y[];
	int currentVector_Cb[];
	int currentVector_Cr[];

	BufferedImage img_buff = null;
	BufferedImage img_buff_out = null;
	int imgWidth;
	int imgHeight;
	int paddingWidth = 0;
	int paddingHeight = 0;
	int imgWidthWithPadding;
	int imgHeightWithPadding;
	
	//CONSTANT
	int Q_matrix_Y[][] = {
								{16, 11, 10, 16, 24, 40, 51, 61},
								{12, 12, 14, 19, 26, 58, 60, 55},
								{14, 13, 16, 24, 40, 57, 69, 56},
								{14, 17, 22, 29, 51, 87, 80, 62},
								{18, 22, 37, 56, 68, 109, 103, 77},
								{24, 35, 55, 64, 81, 104, 113, 92},
								{49, 64, 78, 87, 103, 121, 120, 101},
								{72, 92, 95, 98, 112, 100, 103, 99}
							};
	
	int Q_matrix_CbCr[][] ={
								{17, 18, 24, 47, 99, 99, 99, 99},
								{18, 21, 26, 66, 99, 99, 99, 99},
								{24, 26, 56, 99, 99, 99, 99, 99},
								{47, 66, 99, 99, 99, 99, 99, 99},
								{99, 99, 99, 99, 99, 99 ,99 ,99},
								{99, 99, 99, 99, 99, 99 ,99 ,99},
								{99, 99, 99, 99, 99, 99 ,99 ,99},
								{99, 99, 99, 99, 99, 99 ,99 ,99},
							};

	public void Compress(String input_url, String output_url) {
		readImage(input_url);
		
		
		for(int i=0; i< imgWidthWithPadding; i+=8) {
			for(int j=0; j< imgHeightWithPadding; j+=8) {
				
				currentBlock_Y =  new int[][]{
					{Y[i][j], Y[i][j+1], Y[i][j+2], Y[i][j+3], Y[i][j+4], Y[i][j+5], Y[i][6], Y[i][j+7]},
					{Y[i+1][j], Y[i+1][j+1], Y[i+1][j+2], Y[i+1][j+3], Y[i+1][j+4], Y[i+1][j+5], Y[i+1][6], Y[i+1][j+7]},
					{Y[i+2][j], Y[i+2][j+1], Y[i+2][j+2], Y[i+2][j+3], Y[i+2][j+4], Y[i+2][j+5], Y[i+2][6], Y[i+2][j+7]},
					{Y[i+3][j], Y[i+3][j+1], Y[i+3][j+2], Y[i+3][j+3], Y[i+3][j+4], Y[i+3][j+5], Y[i+3][6], Y[i+3][j+7]},
					{Y[i+4][j], Y[i+4][j+1], Y[i+4][j+2], Y[i+4][j+3], Y[i+4][j+4], Y[i+4][j+5], Y[i+4][6], Y[i+4][j+7]},
					{Y[i+5][j], Y[i+5][j+1], Y[i+5][j+2], Y[i+5][j+3], Y[i+5][j+4], Y[i+5][j+5], Y[i+5][6], Y[i+5][j+7]},
					{Y[i+6][j], Y[i+6][j+1], Y[i+6][j+2], Y[i+6][j+3], Y[i+6][j+4], Y[i+6][j+5], Y[i+6][6], Y[i+6][j+7]},
					{Y[i+7][j], Y[i+7][j+1], Y[i+7][j+2], Y[i+7][j+3], Y[i+7][j+4], Y[i+7][j+5], Y[i+7][6], Y[i+7][j+7]}		
				};
				
				currentBlock_Cb =  new int[][]{
					{Cb[i][j], Cb[i][j+1], Cb[i][j+2], Cb[i][j+3], Cb[i][j+4], Cb[i][j+5], Cb[i][6], Cb[i][j+7]},
					{Cb[i+1][j], Cb[i+1][j+1], Cb[i+1][j+2], Cb[i+1][j+3], Cb[i+1][j+4], Cb[i+1][j+5], Cb[i+1][6], Cb[i+1][j+7]},
					{Cb[i+2][j], Cb[i+2][j+1], Cb[i+2][j+2], Cb[i+2][j+3], Cb[i+2][j+4], Cb[i+2][j+5], Cb[i+2][6], Cb[i+2][j+7]},
					{Cb[i+3][j], Cb[i+3][j+1], Cb[i+3][j+2], Cb[i+3][j+3], Cb[i+3][j+4], Cb[i+3][j+5], Cb[i+3][6], Cb[i+3][j+7]},
					{Cb[i+4][j], Cb[i+4][j+1], Cb[i+4][j+2], Cb[i+4][j+3], Cb[i+4][j+4], Cb[i+4][j+5], Cb[i+4][6], Cb[i+4][j+7]},
					{Cb[i+5][j], Cb[i+5][j+1], Cb[i+5][j+2], Cb[i+5][j+3], Cb[i+5][j+4], Cb[i+5][j+5], Cb[i+5][6], Cb[i+5][j+7]},
					{Cb[i+6][j], Cb[i+6][j+1], Cb[i+6][j+2], Cb[i+6][j+3], Cb[i+6][j+4], Cb[i+6][j+5], Cb[i+6][6], Cb[i+6][j+7]},
					{Cb[i+7][j], Cb[i+7][j+1], Cb[i+7][j+2], Cb[i+7][j+3], Cb[i+7][j+4], Cb[i+7][j+5], Cb[i+7][6], Cb[i+7][j+7]}		
				};
				
				currentBlock_Cr =  new int[][]{
					{Cr[i][j], Cr[i][j+1], Cr[i][j+2], Cr[i][j+3], Cr[i][j+4], Cr[i][j+5], Cr[i][6], Cr[i][j+7]},
					{Cr[i+1][j], Cr[i+1][j+1], Cr[i+1][j+2], Cr[i+1][j+3], Cr[i+1][j+4], Cr[i+1][j+5], Cr[i+1][6], Cr[i+1][j+7]},
					{Cr[i+2][j], Cr[i+2][j+1], Cr[i+2][j+2], Cr[i+2][j+3], Cr[i+2][j+4], Cr[i+2][j+5], Cr[i+2][6], Cr[i+2][j+7]},
					{Cr[i+3][j], Cr[i+3][j+1], Cr[i+3][j+2], Cr[i+3][j+3], Cr[i+3][j+4], Cr[i+3][j+5], Cr[i+3][6], Cr[i+3][j+7]},
					{Cr[i+4][j], Cr[i+4][j+1], Cr[i+4][j+2], Cr[i+4][j+3], Cr[i+4][j+4], Cr[i+4][j+5], Cr[i+4][6], Cr[i+4][j+7]},
					{Cr[i+5][j], Cr[i+5][j+1], Cr[i+5][j+2], Cr[i+5][j+3], Cr[i+5][j+4], Cr[i+5][j+5], Cr[i+5][6], Cr[i+5][j+7]},
					{Cr[i+6][j], Cr[i+6][j+1], Cr[i+6][j+2], Cr[i+6][j+3], Cr[i+6][j+4], Cr[i+6][j+5], Cr[i+6][6], Cr[i+6][j+7]},
					{Cr[i+7][j], Cr[i+7][j+1], Cr[i+7][j+2], Cr[i+7][j+3], Cr[i+7][j+4], Cr[i+7][j+5], Cr[i+7][6], Cr[i+7][j+7]}		
				};
				
				currentVector_Y  = new int[64];
				currentVector_Cb  = new int[64];
				currentVector_Cr  = new int[64];

				DCT_2D();
				Quantization();
				ZigZagScan_to_CreateVector();
				EntropyEncode();
			}
		}
		
		export_jpeg(output_url);
	
	}
	
	private void readImage(String url) {
		/*
		 * in this function, we do: readImage -> DownSpamling -> RGB to YCbCr -> shift value
		 * but because we don't know much about down sampling, then ignore it in this version
		 * 
		 */
		
		
		//READ IMAGE
		 try {
				img_buff =  ImageIO.read(getClass().getResource(url));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			 
			if(img_buff!=null) {
				imgHeight = img_buff.getHeight();
				imgWidth = img_buff.getWidth();
				
				//calculate padding need to add to make width and height mod 8 == 0;
				paddingWidth = ((imgWidth%8)==0)? 0 : (8 - imgWidth%8); 
				paddingHeight = ((imgHeight%8)==0)? 0 : (8 - imgHeight%8); 
				
				//image size with padding pixels
				imgWidthWithPadding = imgWidth + paddingWidth;
				imgHeightWithPadding = imgHeight + paddingHeight;
				
				R = new int[imgWidth][imgHeight];
				G = new int[imgWidth][imgHeight];
				B = new int[imgWidth][imgHeight];
				
				
				//YCbCr has bigger size after adding padding pixel
				Y = new int[imgWidthWithPadding][imgHeightWithPadding];
				Cr = new int[imgWidthWithPadding][imgHeightWithPadding];
				Cb = new int[imgWidthWithPadding][imgHeightWithPadding];

				//get RGB value of each pixel then convert from RGB to YCbCr
				//note: i - horizontal, j - vertical 
				for(int i=0; i<imgWidth; i++) {
					for(int j=0; j<imgHeight; j++) {
						int p = img_buff.getRGB(i,j);
						R[i][j] = (p>>16)&0xff;
						G[i][j] = (p>>8)&0xff;
						B[i][j] = (p)&0xff;
		// RGB to YCbCr
				RGB2YCbCr(i, j);
		// SHIFT VALUE
				shiftValue(i,j);

				}
			}
				
				//**add value to padding pixels by cloning the value of nearest pixel
				addPaddingPixels(imgWidth, imgHeight, imgWidthWithPadding, imgHeightWithPadding);
				
			}
	}
	private void RGB2YCbCr(int i, int j) {
			
		int r = R[i][j] ;
		int g = G[i][j] ;
		int b = B[i][j] ;

		
		  Y[i][j] = (int)(0.299*r + 0.587*g + 0.144*b);
		  Cb[i][j] = (int)(-0.1687*r - 0.3313*g + 0.5*b +128);
		  Cr[i][j] = (int)(0.5*r - 0.4187*g - 0.0813*b +128);

		
	}
	private void shiftValue(int i, int j) {
		// TODO Auto-generated method stub
		Y[i][j] -= 128;
		Cb[i][j] -= 128;
		Cr[i][j] -= 128;


	}
	private void addPaddingPixels(int width, int height, int imgWidthWithPadding, int imgHeightWithPadding) {
		//add value to padding pixels for the right edge
		if(imgWidthWithPadding > imgWidth) {
			for(int i=imgWidth; i< imgWidthWithPadding; i++) {
				for(int j=0; j<imgHeight; j++) {
					Y[i][j] = Y[i-1][j];
					Cb[i][j] = Cb[i-1][j];
					Cr[i][j] = Cr[i-1][j];
				}
			}
		}
	
		//add value to padding pixels for the bottom edge
		if(imgHeightWithPadding > imgHeight) {
			for(int i=0; i< imgWidthWithPadding; i++) {
				for(int j=height; j<imgHeightWithPadding; j++) {
					Y[i][j] = Y[i][j-1];
					Cb[i][j] = Cb[i][j-1];
					Cr[i][j] = Cr[i][j-1];
				}
			}
		}
	}
	
	

	private void DCT_2D() {
		//apply dct 2d for currentBlock_Y, currentBlock_Cb, currentBlock_Cr
	}
	private void Quantization() {
		//apply quantization for currentBlock_Y, currentBlock_Cb, currentBlock_Cr
	}
	private void ZigZagScan_to_CreateVector() {
		//apply zigzag scan on 3 block currentBlock_Y, currentBlock_Cb, currentBlock_Cr 
		//to assign value for 3 vector currentVector_Y, currentVector_Cb, currentVector_Cr
	}
	private void EntropyEncode() {
		//apply RLC on AC, DPCM on DC for 3 vector currentVector_Y, currentVector_Cb, currentVector_Cr
		//??? don't know what should i return because it depends on how we write to file jpeg 
		//this will be soon edited.
	}
	

	private void export_jpeg(String output_url) {
		
		// this function is a mystery???????????????
		//???have no idea about jpeg file format.
		//this will be soon edited 
		
		File f = null;
		img_buff_out = new BufferedImage(imgWidthWithPadding, imgHeightWithPadding, BufferedImage.TYPE_INT_RGB);
		
		//test export image with YCbCr color space
		if(img_buff_out!=null) {
			
			for(int i=0; i<imgWidthWithPadding; i++) {
				for(int j=0; j<imgHeightWithPadding; j++) {
					img_buff_out.setRGB(i, j, Y[i][j]<<16|Cb[i][j]<<8|Cr[i][j]);
				}
			}
			
			
		try
		   { 
		            f = new File(output_url); 
		            System.out.println(f.delete());
		            
		            System.out.println(f);
			        ImageIO.write(img_buff_out, "jpg", f); 

		   } 
		catch(IOException e) 
		   { 
		            System.out.println(e); 
		   } 
			
		}
	}
	
	
}