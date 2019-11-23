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
	
	double YDct[][];
	double CbDct[][];
	double CrDct[][];
	
	boolean YType = true;
	boolean NoneYType = false;
	
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
	
	int horizontalIndex[];
	int verticalIndex[];

    public static int n = 8,m = 8; 
    public static double pi = 3.142857; 
	
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
				currentBlock_Y = DCT_2D(currentBlock_Y);
				currentBlock_Cb = DCT_2D(currentBlock_Cb);
				currentBlock_Cr = DCT_2D(currentBlock_Cr);
				 
				currentBlock_Y = Quantization(currentBlock_Y, YType);
				currentBlock_Cb = Quantization(currentBlock_Cb, NoneYType);
				currentBlock_Cr = Quantization(currentBlock_Cr, NoneYType);
				
				currentVector_Y = assignVector(currentBlock_Y);
				currentVector_Cb = assignVector(currentBlock_Cb);
				currentVector_Cr = assignVector(currentBlock_Cr);
				
				// ZigZagScan_to_CreateVector();
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
			 // check  DCT and quantum
			 int matrix[][] = { 
			    		{ -76, -73, -67, -62, -58, -67, -64, -55 }, 
			            { -65, -69, -76, -38, -19, -43, -59, -56 }, 
			            { -66, -69, -60, -15, 16, -24, -62, -55 }, 
			            { -65, -70, -57, -6, 26, -22, -58, -59 }, 
			            { -61, -67, -60, -24, -2, -40, -60, -58 }, 
			            { -49, -63, -68, -58, -51, -60, -70, -53 }, 
			            { -43, -57, -64, -69, -73, -67, -63, -45 }, 
			            { -41, -49, -59, -60, -63, -52, -50, -34 } };
			 int checkMatrix[][] = DCT_2D(matrix);
			 checkMatrix = Quantization(checkMatrix, true);
			 // log2DArr(checkMatrix);
			 // get vector index vertical and horizontal
			 horizontalIndex = getIndexArr(0);
			 verticalIndex = getIndexArr(1);
			 for(int i = 0; i < 64; i++) {
			   System.out.print(verticalIndex[i] + " ");
			 }
			 System.out.println();
			 for(int i = 0; i < 64; i++) {
				   System.out.print(horizontalIndex[i] + " ");
				 }
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
	// just log to check
	private void log2DArr(int matrix[][]) {
		for(int i = 0; i< m; i++) {
			for(int j = 0;j< n; j++) {
				System.out.print(" " + matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	private int[][] DCT_2D(int matrix[][]) {
		//apply dct 2d for currentBlock_Y, currentBlock_Cb, currentBlock_Cr
		 int i, j, k, l; 
		   
	        // dct will store the discrete cosine transform 
	        int[][] dct = new int[m][n]; 
	   
//	        for(int p = 0; p < 8; p++) {
//            	for(int a = 0; a<8; a++) {
//            		System.out.print( " " + matrix[p][a] + " ");
//            	}
//            	System.out.println();
//            }
//            
	        double ci, cj, dct1, sum; 
	   
	        for (i = 0; i < m; i++)  
	        { 
	            for (j = 0; j < n; j++)  
	            { 
	                // ci and cj depends on frequency as well as 
	                // number of row and columns of specified matrix 
	                if (i == 0) 
	                    ci = 1 / Math.sqrt(m); 
	                else
	                    ci = Math.sqrt(2) / Math.sqrt(m); 
	                      
	                if (j == 0) 
	                    cj = 1 / Math.sqrt(n); 
	                else
	                    cj = Math.sqrt(2) / Math.sqrt(n); 
	   
	                // sum will temporarily store the sum of  
	                // cosine signals 
	                sum = 0; 
	                
	                for (k = 0; k < m; k++)  
	                { 
	                    for (l = 0; l < n; l++)  
	                    { 
	                        dct1 = matrix[k][l] *  
	                               Math.cos((2 * k + 1) * i * pi / (2 * m)) *  
	                               Math.cos((2 * l + 1) * j * pi / (2 * n)); 
	                        sum = sum + dct1; 
	                    } 
	                } 
	                dct[i][j] = (int) Math.round(ci * cj * sum); 
	              //  System.out.print( " " + dct[i][j] + " ");
	            } 
	            // System.out.println();
	        } 
	        return dct;
	}
	private int[][] Quantization(int matrix[][], boolean checkYType) {
		//apply quantization for currentBlock_Y, currentBlock_Cb, currentBlock_Cr
		int[][] newMatrix = new int[m][n];
		if(checkYType) {
			for(int i = 0; i< m; i++) {
				for(int j = 0; j <n ; j++) {
					newMatrix[i][j] = Math.round((matrix[i][j] + Q_matrix_Y[i][j]/2 )/ Q_matrix_Y[i][j]);
				}
			}
		}else {
			for(int i = 0; i< m; i++) {
				for(int j = 0; j <n ; j++) {
					newMatrix[i][j] =  Math.round((matrix[i][j] + Q_matrix_CbCr[i][j]/2 )/ Q_matrix_CbCr[i][j]);
				}
			}
		}
		
		return newMatrix;
	}
	private int[] getIndexArr(int start) {
		int[] index = new int[64];
		int count = 0;
		int i,j;
		for(int max = start; max < 8; max= max+2) {
			for( i = 0; i< max + 1; i++) {
				index[count++] = i;
			}
			for( j = max - 1; j >= 0; j--) {
				index[count++] = j;
			}	
		};
		for(int min = start; min < 8; min= min+ 2) {
			for( i = min; i< 8; i++) {
				index[count++] = i;
			}
			for( j = 7; j > min; j--) {
				index[count++] = j;
			}
		}
		return index;
	}
	private void ZigZagScan_to_CreateVector() {
		//apply zigzag scan on 3 block currentBlock_Y, currentBlock_Cb, currentBlock_Cr 
		//to assign value for 3 vector currentVector_Y, currentVector_Cb, currentVector_Cr
	}
	private int[] assignVector(int arr[][]) {
		int[] vector = new int[64];
		int count = 0;
		for(int i = 0; i< 64; i++) {
			vector[count++] = arr[verticalIndex[i]][horizontalIndex[i]];
		}
		return vector;
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
