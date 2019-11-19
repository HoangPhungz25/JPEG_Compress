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
				
				currentBlock_Y  = getCurrentBlock(i, j, Y);
				
				currentBlock_Cb = getCurrentBlock(i, j, Cb);
				
				currentBlock_Cr = getCurrentBlock(i, j, Cr);
				
				currentVector_Y  = new int[64];
				currentVector_Cb  = new int[64];
				currentVector_Cr  = new int[64];
				// should be equal
				if(i == 8 && j == 0) {
					System.out.println("mau block:" + currentBlock_Y[1][1]);
					System.out.println("mau goc:" + Y[1 + 8][1]);
				}
				DCT_2D();
				Quantization();
				ZigZagScan_to_CreateVector();
				EntropyEncode();
			}
		}
		
		export_jpeg(output_url);
	
	}
	private int[][] getCurrentBlock(int startI,int startJ, int arr[][]) {
	  int[][] currentArr = new int[8][8];
	  for(int i = 0; i < 8; i++) {
		for(int j = 0; j < 8; j++) {
			currentArr[i][j] = arr[i+startI][j+startJ];
		}
	  }
	  return currentArr;
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
//				shiftValue(i,j);

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
