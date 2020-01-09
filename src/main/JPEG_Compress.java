package main;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class JPEG_Compress {
	
	boolean YType = true;
	boolean NoneYType = false;
	char Type_Y = 1, Type_Cb = 2, Type_Cr = 3;
	
	int R[][];
	int G[][];
	int B[][];
	
	float Y[][];
	float Cb[][];
	float Cr[][];
	
	float currentBlock_Y[][];
	float currentBlock_Cb[][];
	float currentBlock_Cr[][];
	
	int currentQuantizedBlock_Y[][];
	int currentQuantizedBlock_Cb[][];
	int currentQuantizedBlock_Cr[][];

	int currentVector_Y_debug88[] = 
		  {-8     , -16  ,   -34 ,    0 ,      0   ,    0 ,      0,       0,
			0  ,     -3,      0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0 ,      0};
	
	int currentVector_Cb_debug88[] = 
		  {4   ,     5,       8,    0 ,      0   ,    0 ,      0,       0,
			0  ,     1,      0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0 ,      0};
	
	int currentVector_Cr_debug88[] = 
		  {-1     , -16  ,   10 ,    0 ,      0   ,    0 ,      0,       0,
			0  ,     1,      0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0,       0,
			0  ,     0,       0,       0,       0,       0,       0 ,      0};
	
	int currentVector_Y_debug168_1[] = {
			-16,    -8,     -34,    0,      0,      0,      0,      0,
			0,      -3,     0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
	};
	
	int currentVector_Cb_debug168_1[] = {
			8,      3,      8,      0,      0,      0,      0,      0,
			0,      1,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
	};
	int currentVector_Cr_debug168_1[] = {
			-14,    -8,     10,     0,      0,      0,      0,      0,
			0,      1,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
	};
	
	int currentVector_Y_debug168_2[] = {
			3,      -8,     -34,    0,      0,      0,      0,      0,
			0,      -3,     0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
	};
	int currentVector_Cb_debug168_2[] = {
			-2,     3,      8,      0,      0,      0,      0,      0,
			0,      1,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
	};
	int currentVector_Cr_debug168_2[] = {
			16,     -8,     10,     0,      0,      0,      0,      0,
			0,      1,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
			0,      0,      0,      0,      0,      0,      0,      0,
	};

	int currentVector_Y[];
	int currentVector_Cb[];
	int currentVector_Cr[];
	
	Bitcode huffmanTable_Y_DC[];
	Bitcode huffmanTable_Y_AC[];
	Bitcode huffmanTable_CbCr_DC[];
	Bitcode huffmanTable_CbCr_AC[];
	
	Bitcode codeWordFoQuantizedValuePositive[], codeWordFoQuantizedValueNagative[];
	
	BitBuffer bitBuffer;
	char specialByte = 0xFF;

	BufferedImage img_buff = null;
	BufferedImage img_buff_out = null;
	char imgWidth;
	char imgHeight;
	char paddingWidth = 0;
	char paddingHeight = 0;
	char imgWidthWithPadding;
	char imgHeightWithPadding;
    
	int horizontalIndex[];
	int verticalIndex[];

    public static int n = 8,m = 8; 
    public static double pi = 3.142857; 
    
    //entropy coding
    int last_DC_Y = 0, last_DC_Cb = 0, last_DC_Cr = 0;
    char pos_NonZero_Y = 9, pos_NonZero_Cb = 9, pos_NonZero_Cr = 9;
    
    
    //write to file
    FileOutputStream f2 = null, f_debug = null;
	DataOutputStream writer =null;

	//CONSTANT
	
	//QUAlity
	char quality = 90;
    
    //HEADER
    char header[] = {0xFF, 0xD8, 
    				 0xFF, 0xE0,
    				 0x00, 0x10,
    				 'J','F','I','F',0x00,
    				 1,1,
    				 0,
    				 0,1,0,1,
    				 0,0};
    //QUANTUM
    	//quatum marker
    char quantum_marker[] = {0xFF, 0xDB, 0x00, 0x84};
    char quantum_entry_Y = 0x00;
    char quantum_entry_CbCr = 0x01;
    
    //SOF
    char SOF_marker[] = {0xFF, 0xC0, 0x00, 0x11};
    char SOF_numBitPerColor = 0x08;
    char SOF_numOfColor = 0x03;
    char SOF_metadataForYCbCr[] = {0x01, 0x11, 0x00, 0x02, 0x11, 0x01, 0x03, 0x11, 0x01};
    
    //Huffman Tables
    char HUFFMAN_marker[] = {0xFF, 0xC4, 0x01, 0xA2};
    char HUFFMAN_entry_Y_DC = 0x00;
    char HUFFMAN_entry_Y_AC = 0x10;
    char HUFFMAN_entry_CbCr_DC = 0x01;
    char HUFFMAN_entry_CbCr_AC = 0x11;
    
    //SOS
    char SOS_marker[] = {0xFF, 0xDA, 0x00, 0x0C};
    char SOS_metadata[] = {0x03, 0x01, 0x00, 0x02, 0x11, 0x03, 0x11, 0x00, 0x3F, 0x00};
    
    //EOI
    char EOI_marker[] = {0xFF, 0x09};
    //QUANTUM
	char Q_matrix_Y[][] = {
								{16, 11, 10, 16, 24, 40, 51, 61},
								{12, 12, 14, 19, 26, 58, 60, 55},
								{14, 13, 16, 24, 40, 57, 69, 56},
								{14, 17, 22, 29, 51, 87, 80, 62},
								{18, 22, 37, 56, 68, 109, 103, 77},
								{24, 35, 55, 64, 81, 104, 113, 92},
								{49, 64, 78, 87, 103, 121, 120, 101},
								{72, 92, 95, 98, 112, 100, 103, 99}
							};
	
	char Q_matrix_CbCr[][] ={
								{17, 18, 24, 47, 99, 99, 99, 99},
								{18, 21, 26, 66, 99, 99, 99, 99},
								{24, 26, 56, 99, 99, 99, 99, 99},
								{47, 66, 99, 99, 99, 99, 99, 99},
								{99, 99, 99, 99, 99, 99 ,99 ,99},
								{99, 99, 99, 99, 99, 99 ,99 ,99},
								{99, 99, 99, 99, 99, 99 ,99 ,99},
								{99, 99, 99, 99, 99, 99 ,99 ,99},
							};
	 char ZigZag[][] = new char[8][8];
//	    {  
//	      {0, 1, 5, 6,14,15,27,28},   // ZigZag[] =  0, 1, 5, 6,14,15,27,28,
//	      {2, 4, 7,13,16,26,29,42},   //             2, 4, 7,13,16,26,29,42,
//	      {3, 8,12,17,25,30,41,43},   //             3, 8,12,17,25,30,41,43,
//	      {9,11,18,24,31,40,44,53},   //             9,11,18,24,31,40,44,53,
//	      {10,19,23,32,39,45,52,54},   //            10,19,23,32,39,45,52,54,
//	      {20,22,33,38,46,51,55,60},   //            20,22,33,38,46,51,55,60,
//	      {21,34,37,47,50,56,59,61},   //            21,34,37,47,50,56,59,61,
//	      {35,36,48,49,57,58,62,63} };
	 
	//HUFFMAN
	//Y_DC
	char Huffman_Y_DC_CodesPerBitSize[] = { 0,1,5,1,1,1,1,1,1,0,0,0,0,0,0,0 }; 
	char Huffman_Y_DC_CodeValue[] = { 0,1,2,3,4,5,6,7,8,9,10,11 };
	//Y_AC
	char Huffman_Y_AC_CodesPerBitsize[]   = { 0,2,1,3,3,2,4,3,5,5,4,4,0,0,1,125 }; // sum = 162
	char Huffman_Y_AC_Values        []   =                                        // => 162 codes
	    { 0x01,0x02,0x03,0x00,0x04,0x11,0x05,0x12,0x21,0x31,0x41,0x06,0x13,0x51,0x61,0x07,0x22,0x71,0x14,0x32,0x81,0x91,0xA1,0x08, // 16*10+2 symbols because
	      0x23,0x42,0xB1,0xC1,0x15,0x52,0xD1,0xF0,0x24,0x33,0x62,0x72,0x82,0x09,0x0A,0x16,0x17,0x18,0x19,0x1A,0x25,0x26,0x27,0x28, // upper 4 bits can be 0..F
	      0x29,0x2A,0x34,0x35,0x36,0x37,0x38,0x39,0x3A,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x53,0x54,0x55,0x56,0x57,0x58,0x59, // while lower 4 bits can be 1..A
	      0x5A,0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x6A,0x73,0x74,0x75,0x76,0x77,0x78,0x79,0x7A,0x83,0x84,0x85,0x86,0x87,0x88,0x89, // plus two special codes 0x00 and 0xF0
	      0x8A,0x92,0x93,0x94,0x95,0x96,0x97,0x98,0x99,0x9A,0xA2,0xA3,0xA4,0xA5,0xA6,0xA7,0xA8,0xA9,0xAA,0xB2,0xB3,0xB4,0xB5,0xB6, // order of these symbols was determined empirically by JPEG committee
	      0xB7,0xB8,0xB9,0xBA,0xC2,0xC3,0xC4,0xC5,0xC6,0xC7,0xC8,0xC9,0xCA,0xD2,0xD3,0xD4,0xD5,0xD6,0xD7,0xD8,0xD9,0xDA,0xE1,0xE2,
	      0xE3,0xE4,0xE5,0xE6,0xE7,0xE8,0xE9,0xEA,0xF1,0xF2,0xF3,0xF4,0xF5,0xF6,0xF7,0xF8,0xF9,0xFA };
	
	//CbCr_DC
	char Huffman_CbCr_DC_CodesPerBitsize[] = { 0,3,1,1,1,1,1,1,1,1,1,0,0,0,0,0 };   // sum = 12
	char Huffman_CbCr_DC_Values         [] = { 0,1,2,3,4,5,6,7,8,9,10,11 };         // => 12 codes (identical to DcLuminanceValues)
	//CbCr_AC
	char Huffman_CbCr_AC_CodesPerBitsize[] = { 0,2,1,2,4,4,3,4,7,5,4,4,0,1,2,119 }; // sum = 162
	char Huffman_CbCr_AC_Values        [] =                                        // => 162 codes
	    { 0x00,0x01,0x02,0x03,0x11,0x04,0x05,0x21,0x31,0x06,0x12,0x41,0x51,0x07,0x61,0x71,0x13,0x22,0x32,0x81,0x08,0x14,0x42,0x91, // same number of symbol, just different order
	      0xA1,0xB1,0xC1,0x09,0x23,0x33,0x52,0xF0,0x15,0x62,0x72,0xD1,0x0A,0x16,0x24,0x34,0xE1,0x25,0xF1,0x17,0x18,0x19,0x1A,0x26, // (which is more efficient for AC coding)
	      0x27,0x28,0x29,0x2A,0x35,0x36,0x37,0x38,0x39,0x3A,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x53,0x54,0x55,0x56,0x57,0x58,
	      0x59,0x5A,0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x6A,0x73,0x74,0x75,0x76,0x77,0x78,0x79,0x7A,0x82,0x83,0x84,0x85,0x86,0x87,
	      0x88,0x89,0x8A,0x92,0x93,0x94,0x95,0x96,0x97,0x98,0x99,0x9A,0xA2,0xA3,0xA4,0xA5,0xA6,0xA7,0xA8,0xA9,0xAA,0xB2,0xB3,0xB4,
	      0xB5,0xB6,0xB7,0xB8,0xB9,0xBA,0xC2,0xC3,0xC4,0xC5,0xC6,0xC7,0xC8,0xC9,0xCA,0xD2,0xD3,0xD4,0xD5,0xD6,0xD7,0xD8,0xD9,0xDA,
	      0xE2,0xE3,0xE4,0xE5,0xE6,0xE7,0xE8,0xE9,0xEA,0xF2,0xF3,0xF4,0xF5,0xF6,0xF7,0xF8,0xF9,0xFA };
	
	final int CODE_WORD_LIMIT = 2048; // +/-2^11, maximum value after DCT

	
	public boolean Compress(String input_url, String output_url) {
		// System.out.println("Anh chuan bi nen: " + input_url);
		readImage(input_url);
//		createExampleImage();
		genHuffmanTables();
		try {
			 f2 = new FileOutputStream(output_url);
			 writer = new DataOutputStream(f2);
			 
			 
//			 //debug
//			imgHeightWithPadding = 8;
//			imgWidthWithPadding = 16;
			 
			writeAllMetadata(writer);
			genCodeWordForQuantizedValue();
			
			bitBuffer  = new BitBuffer(0, (char) 0);
			
			for(int i=0; i< imgHeightWithPadding; i+=8) {
				for(int j=0; j< imgWidthWithPadding; j+=8) {
					
					currentBlock_Y  = getCurrentBlock(i, j, Y);
					currentBlock_Cb = getCurrentBlock(i, j, Cb);
					currentBlock_Cr = getCurrentBlock(i, j, Cr);
					
					currentVector_Y  = new int[64];
					currentVector_Cb  = new int[64];
					currentVector_Cr  = new int[64];
					// System.out.println("DCT");
					currentBlock_Y = DCT_2D(currentBlock_Y);
					log2DBlock(currentBlock_Y);
					currentBlock_Cb = DCT_2D(currentBlock_Cb);
					log2DBlock(currentBlock_Cb);
					currentBlock_Cr = DCT_2D(currentBlock_Cr);
					log2DBlock(currentBlock_Cr);

					// System.out.println("quantization");
					currentQuantizedBlock_Y = Quantization(currentBlock_Y, YType);
					log2DBlock(currentQuantizedBlock_Y);
					currentQuantizedBlock_Cb = Quantization(currentBlock_Cb, NoneYType);
					log2DBlock(currentQuantizedBlock_Cb);
					currentQuantizedBlock_Cr = Quantization(currentBlock_Cr, NoneYType);
					log2DBlock(currentQuantizedBlock_Cr);
					
					
					currentVector_Y = assignVector(currentQuantizedBlock_Y, Type_Y);
					currentVector_Cb = assignVector(currentQuantizedBlock_Cb, Type_Cb);
					currentVector_Cr = assignVector(currentQuantizedBlock_Cr, Type_Cr);
					//debug:data after zigzag from toolJpeg.cpp
					

					last_DC_Y = EntropyEncode(currentVector_Y, last_DC_Y, Type_Y);
					last_DC_Cb = EntropyEncode(currentVector_Cb, last_DC_Cb, Type_Cb);
					last_DC_Cr = EntropyEncode(currentVector_Cr, last_DC_Cr, Type_Cr);


				}
			}
			
//			//debug
//			
//			last_DC_Y = EntropyEncode(currentVector_Y_debug168_1, last_DC_Y, Type_Y);
//			last_DC_Cb = EntropyEncode(currentVector_Cb_debug168_1, last_DC_Cb, Type_Cb);
//			last_DC_Cr = EntropyEncode(currentVector_Cr_debug168_1, last_DC_Cr, Type_Cr);
//			
//			last_DC_Y = EntropyEncode(currentVector_Y_debug168_2, last_DC_Y, Type_Y);
//			last_DC_Cb = EntropyEncode(currentVector_Cb_debug168_2, last_DC_Cb, Type_Cb);
//			last_DC_Cr = EntropyEncode(currentVector_Cr_debug168_2, last_DC_Cr, Type_Cr);
			
			writeEOI(writer);
			 
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		finally {
				try {
					if(f2!=null)f2.close();
					if(writer!=null)writer.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
		return true;
	
	}
	
	private void createExampleImage() {
		
		
		imgHeightWithPadding = 256;
		imgWidthWithPadding = 512;
		R = new int[imgHeightWithPadding][imgWidthWithPadding]; 
		G = new int[imgHeightWithPadding][imgWidthWithPadding];
		B = new int[imgHeightWithPadding][imgWidthWithPadding];
		
		
		//YCbCr has bigger size after adding padding pixel
		Y = new float[imgHeightWithPadding][imgWidthWithPadding];
		Cr = new float[imgHeightWithPadding][imgWidthWithPadding];
		Cb = new float[imgHeightWithPadding][imgWidthWithPadding];
		
		
		for (char y = 0; y < imgHeightWithPadding; y++)
		    for (char x = 0; x < imgWidthWithPadding; x++)
		    {
		      // memory location of current pixel
//		      char offset = (char) ((y * imgHeightWithPadding + x) * 8);
		      // red and green fade from 0 to 255, blue is always 127
		      R[y][x] = 255 * x / imgWidthWithPadding;
		      G[y][x] = 255 * y / imgHeightWithPadding;
		      B[y][x] = 127;
		      
		  	// RGB to YCbCr
				RGB2YCbCr(y, x);
		// SHIFT VALUE
				shiftValue(y,x);
		    }
		// System.out.println("RGB");
		log2DArr(R);
		log2DArr(G);
		log2DArr(B);
		// System.out.println("YCbCr");
		log2DArr(Y);
		log2DArr(Cb);
		log2DArr(Cr);

	}
	
	
	private void writeAllMetadata(DataOutputStream writer) {
		writeHeader(writer);
		writeQuantum(writer);
		writeSOF(writer);
		writeHuffmanTables(writer);
		writeSOS(writer);
	}
	
	private void writeHeader(DataOutputStream writer) {
		//1.write header
		writeArr(writer, header);
	}
	private void writeQuantum(DataOutputStream writer) {
	
		try {
			//2.write quantum 
			 //2.1.quantum marker
			 writeArr(writer, quantum_marker);
		 
			 //2.2.quantum tables
			 //2.2.1.Y table
			 writer.writeByte(quantum_entry_Y);
			 writeMatrix(writer, Q_matrix_Y);
			 
			 //2.2.2.CbCr table
			 writer.writeByte(quantum_entry_CbCr);
			 writeMatrix(writer, Q_matrix_CbCr);
			 
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		 
	}
	private void writeSOF(DataOutputStream writer) {
		try {
			
			 writeArr(writer, SOF_marker);
			 writer.writeByte(SOF_numBitPerColor);
			 //3.2.h1,h2,w1,w2
			 writer.writeByte(imgHeightWithPadding>>8);
			 writer.writeByte(imgHeightWithPadding&0xFF);
			 writer.writeByte(imgWidthWithPadding>>8);
			 writer.writeByte(imgWidthWithPadding&0xFF);
			 //3.2 num of color
			 writer.writeByte(SOF_numOfColor);
			 //metadata for each color
			 writeArr(writer, SOF_metadataForYCbCr);
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		//3.Write SoF
		 //3.1. marker
		 
	}
	private void writeHuffmanTables(DataOutputStream writer) {
		try {
			 //4.huffman tables
			 //4.1. marker
			writeArr(writer, HUFFMAN_marker);
			 //4.2. entry Y-DC
			 //4.3. table Y-DC
			writer.writeByte(HUFFMAN_entry_Y_DC);
			writeArr(writer, Huffman_Y_DC_CodesPerBitSize);
			writeArr(writer, Huffman_Y_DC_CodeValue);
			 //4.4 table Y-AC
			writer.writeByte(HUFFMAN_entry_Y_AC);
			writeArr(writer, Huffman_Y_AC_CodesPerBitsize);
			writeArr(writer, Huffman_Y_AC_Values);
			
			 //4.5 table CbCr-DC
			writer.writeByte(HUFFMAN_entry_CbCr_DC);
			writeArr(writer, Huffman_CbCr_DC_CodesPerBitsize);
			writeArr(writer, Huffman_CbCr_DC_Values);
			 //4.6 table CbCr-AC
			writer.writeByte(HUFFMAN_entry_CbCr_AC);
			writeArr(writer, Huffman_CbCr_AC_CodesPerBitsize);
			writeArr(writer, Huffman_CbCr_AC_Values);

			
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private void writeSOS(DataOutputStream writer) {
			writeArr(writer, SOS_marker);
			writeArr(writer, SOS_metadata);
	}
	private void writeEOI(DataOutputStream writer) {
		writeArr(writer, EOI_marker);;
	}
	
	private void writeArr(DataOutputStream writer, char[] arr) {
		try {
			for(char c: arr) writer.writeByte(c);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeMatrix(DataOutputStream writer, char[][] arr) {
		try {
			for(int i=0; i<m; i++)
				for(int j=0; j<n; j++) writer.writeByte(arr[i][j]);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	private float[][] getCurrentBlock(int startI,int startJ, float arr[][]) {
	  float[][] currentArr = new float[8][8];
	  for(int i = 0; i < 8; i++) {
		for(int j = 0; j < 8; j++) {
			currentArr[i][j] = arr[i+startI][j+startJ];
		}
	  }
	  return currentArr;
	}
	private void readImage(String url) {
		/*
		 * in this function, we do: readImage -> DownSpamling--> RGB to YCbCr -> shift value
		 * but because we don't know much about down sampling, then ignore it in this version
		 * 
		 */
		
		
		//READ IMAGE
//		 try {
//			 
//			 int matrix[][] = { 
//			    		{ -76, -73, -67, -62, -58, -67, -64, -55 }, 
//			            { -65, -69, -76, -38, -19, -43, -59, -56 }, 
//			            { -66, -69, -60, -15, 16, -24, -62, -55 }, 
//			            { -65, -70, -57, -6, 26, -22, -58, -59 }, 
//			            { -61, -67, -60, -24, -2, -40, -60, -58 }, 
//			            { -49, -63, -68, -58, -51, -60, -70, -53 }, 
//			            { -43, -57, -64, -69, -73, -67, -63, -45 }, 
//			            { -41, -49, -59, -60, -63, -52, -50, -34 } };
//			 int checkMatrix[][] = DCT_2D(matrix);
//			 checkMatrix = Quantization(checkMatrix, true);
//			 // log2DArr(checkMatrix);

//
//				img_buff =  ImageIO.read(getClass().getResource(url));
//			} catch (IOException e) {
//				// System.out.println(e.getMessage());
//			}
		 // get vector index vertical and horizontal
		 horizontalIndex = getIndexArr(0);
		 verticalIndex = getIndexArr(1);
		 for(int i = 0; i < 64; i++) {
			 // System.out.print(horizontalIndex[i] + " ");
		 }
		 // System.out.println();
		 for(int i = 0; i < 64; i++) {
		    // System.out.print(verticalIndex[i] + " ");
		 }
		 // System.out.println();
		 // assign to vector
		 char num = 0;
		 for(int i = 0; i < 64; i++) {
			 ZigZag[horizontalIndex[i]][verticalIndex[i]] = num;
			 num++;
		 }
		 for(int i = 0; i < 8; i++) {
			 for(int j = 0; j < 8; j++) {
				 // System.out.print( (int)ZigZag[i][j] + " ");
			 }
			 // System.out.println();
		 }
		 
			try {
				img_buff =  ImageIO.read(getClass().getResource(url));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(img_buff!=null) {
//				for(int y=0; y<img_buff.getHeight(); y++) {
//					for(int x=0; x<img_buff.getWidth(); x++) // System.out.printf("%d",img_buff.getRGB(y, x));
//					// System.out.println();
//				}
				imgHeight = (char) img_buff.getHeight();
				imgWidth = (char) img_buff.getWidth();
				// System.out.println("height:"+(int)imgHeight+" width:"+(int)imgWidth);
				
				//calculate padding need to add to make width and height mod 8 == 0;
				paddingWidth = (char) (((imgWidth%8)==0)? 0 : (8 - imgWidth%8)); 
				paddingHeight = (char) (((imgHeight%8)==0)? 0 : (8 - imgHeight%8)); 
				
				//image size with padding pixels
				imgWidthWithPadding = (char) (imgWidth + paddingWidth);
				imgHeightWithPadding = (char) (imgHeight + paddingHeight);
				
				R = new int[imgHeight][imgWidth];
				G = new int[imgHeight][imgWidth];
				B = new int[imgHeight][imgWidth];
				
				
				//YCbCr has bigger size after adding padding pixel
				Y = new float[imgHeightWithPadding][imgWidthWithPadding];
				Cr = new float[imgHeightWithPadding][imgWidthWithPadding];
				Cb = new float[imgHeightWithPadding][imgWidthWithPadding];

				//get RGB value of each pixel then convert from RGB to YCbCr
				//note: i - horizontal, j - vertical 
				// // System.out.println((img_buff.getRGB(14,3))&0xff);
				for(int x=0; x<imgWidth; x++) {
					for(int y=0; y<imgHeight; y++) {
						int p = img_buff.getRGB(x,y);
						R[y][x] = (p>>16)&0xff;
						G[y][x] = (p>>8)&0xff;
						B[y][x] = (p)&0xff;
						
//						// System.out.print(i+":"+j+":"+R[i][j]+"\t");

		// RGB to YCbCr
				RGB2YCbCr(y, x);
		// SHIFT VALUE
				shiftValue(y,x);

					}
					// System.out.println();
				}
				
				//**add value to padding pixels by cloning the value of nearest pixel
				// System.out.println(".............Padding");
				addPaddingPixels(imgWidth, imgHeight, imgWidthWithPadding, imgHeightWithPadding);
				
			}
	}
	private void RGB2YCbCr(int i, int j) {
			
		int r = R[i][j] ;
		int g = G[i][j] ;
		int b = B[i][j] ;

		
		  Y[i][j] = (float)(0.299f*r + 0.587f*g + 0.114f*b);
		  Cb[i][j] = (float)(-0.16874f*r - 0.33126f*g + 0.5f*b +128);
		  Cr[i][j] = (float)(0.5f*r - 0.41869f*g - 0.08131f*b +128);

		
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
			// System.out.println(".............Width Greater");
			for(int y=0; y<imgHeight;y++){
				 for(int x=imgWidth; x< imgWidthWithPadding; x++){
					Y[y][x] = Y[y][x-1];
					Cb[y][x] = Cb[y][x-1];
					Cr[y][x] = Cr[y][x-1];
				}
			}
		}
	
		//add value to padding pixels for the bottom edge
		if(imgHeightWithPadding > imgHeight) {
			for(int y=height; y<imgHeightWithPadding; y++) {
				for(int x=0; x< imgWidthWithPadding; x++){
					Y[y][x] = Y[y-1][x];
					Cb[y][x] = Cb[y-1][x];
					Cr[y][x] = Cr[y-1][x];
				}
			}
		}
	}

	private float[][] DCT_2D(float matrix[][]) {
		//apply dct 2d for currentBlock_Y, currentBlock_Cb, currentBlock_Cr
		 int u, v, x, y; 
		   
	        // dct will store the discrete cosine transform 
	        float[][] dct = new float[m][n]; 
	        double cu, cv, dct1, sum; 
	   
	        for (u = 0; u < n; u++)  
	        { 
	        	for (v = 0; v < m; v++) 
	            {  
	            	
	            	if (u == 0) 
	                    cu = 1 / Math.sqrt(2); 
	                else
	                    cu = 1; 
	                      
	                if (v == 0) 
	                    cv = 1 / Math.sqrt(2); 
	                else
	                    cv = 1; 
	   
	                // sum will temporarily store the sum of  
	                // cosine signals 
	                sum = 0; 
	                
	                for (x = 0; x < n; x++)  
	                { 
	                	for (y = 0; y < m; y++)  
	                    { 
	                        dct1 = matrix[x][y] *  
	                               Math.cos((2 * x + 1) * u * pi / (16)) *  
	                               Math.cos((2 * y + 1) * v * pi / (16)); 
	                        sum = sum + dct1; 
	                    } 
	                } 
	                dct[u][v] = (float)(cu * cv * sum/4); 
	              //  // System.out.print( " " + dct[i][j] + " ");
	            } 
	            // // System.out.println();
	        } 
	        return dct;
	}
	private int[][] Quantization(float matrix[][], boolean checkYType) {
		//apply quantization for currentBlock_Y, currentBlock_Cb, currentBlock_Cr
		int[][] newMatrix = new int[8][8];
		if(checkYType) {
			for(int i = 0; i< 8; i++) {
				for(int j = 0; j <8 ; j++) {
//					newMatrix[i][j] = Math.round((matrix[i][j] + Q_matrix_Y[i][j]/2 )/ Q_matrix_Y[i][j]);
					newMatrix[i][j] = Math.round(matrix[i][j]/ Q_matrix_Y[i][j]);

				}
			}
		}else {
			for(int i = 0; i< m; i++) {
				for(int j = 0; j <n ; j++) {
//					newMatrix[i][j] =  Math.round((matrix[i][j] + Q_matrix_CbCr[i][j]/2 )/ Q_matrix_CbCr[i][j]);
					newMatrix[i][j] = Math.round(matrix[i][j]/ Q_matrix_CbCr[i][j]);

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

	private int[] assignVector(int arr[][], char TYPE) {
		
		//hoang
		int[] vector = new int[64];
		char posNoneZero = 0;

		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++) {
				vector[ZigZag[i][j]] = arr[i][j];
				if(vector[ZigZag[i][j]]!=0 && ZigZag[i][j]>posNoneZero) posNoneZero = ZigZag[i][j];
			}
		
		if(TYPE == Type_Y) pos_NonZero_Y = posNoneZero;
		else if(TYPE == Type_Cb) pos_NonZero_Cb = posNoneZero;
		else pos_NonZero_Cr = posNoneZero;
		
		// System.out.println("Pos non-zero:"+(int)posNoneZero);

		return vector;
		
		//khang
//		int[] vector = new int[64];
//		int count = 0;
//		char posNoneZero = 0;
//		for(char i = 0; i< 64; i++) {
//			vector[count++] = arr[verticalIndex[i]][horizontalIndex[i]];
//			if(vector[count-1]!=0) posNoneZero = i;
//		}
//		
//		if(TYPE == Type_Y) pos_NonZero_Y = posNoneZero;
//		else if(TYPE == Type_Cb) pos_NonZero_Cb = posNoneZero;
//		else pos_NonZero_Cr = posNoneZero;
//		return vector;
		
	}
	private int EntropyEncode(int[] vector, int lastDC, char TYPE) {
		logVector(vector);
		// System.out.println("\nEncoding DC:.......");
		Bitcode[] huffmanTableDC, huffmanTableAC;
		if(TYPE == Type_Y) {
			huffmanTableDC = huffmanTable_Y_DC;
			huffmanTableAC = huffmanTable_Y_AC;
		}else {
			huffmanTableDC = huffmanTable_CbCr_DC;
			huffmanTableAC = huffmanTable_CbCr_AC;
		}
		//encode DC
		// System.out.println("DC:"+vector[0]);
		int diff =  (vector[0] - lastDC);
		// System.out.println("DC diff:"+diff);
		if(diff==0) {
			writeBits(huffmanTableDC[0x00]);
		}else {
			Bitcode codeword;
			if(diff>0) 	codeword = codeWordFoQuantizedValuePositive[diff];
			else  codeword = codeWordFoQuantizedValueNagative[-diff];
			codeword.myPrintCodeWord();
			writeBits(huffmanTableDC[codeword.getNumOfBit()]);//write the code word
			writeBits(codeword);
		}
		
		
		//encode A
		// System.out.println("\nEncoding AC:.......");
		char posNoneZero = 0;
		char numOfSequenceZero = 0;
		
		if(TYPE == Type_Y) posNoneZero = pos_NonZero_Y ;
		else if(TYPE == Type_Cb) posNoneZero = pos_NonZero_Cb;
		else posNoneZero = pos_NonZero_Cr;
		// System.out.println("Last none zero:"+(int)posNoneZero);
		
		for(int i=1; i<= (int) posNoneZero; i++) {
			if(vector[i]==0) numOfSequenceZero+=0x10;
			else {
				// System.out.println("\n***AC values: "+vector[i]);
				if(numOfSequenceZero>0xF0) {
					writeBits(huffmanTableAC[0xF0]);
					numOfSequenceZero = 0;
				}
				Bitcode codeword;
				if(vector[i]>0) 	codeword = codeWordFoQuantizedValuePositive[vector[i]];
				else  codeword = codeWordFoQuantizedValueNagative[-vector[i]];
				codeword.myPrintCodeWord();
				//// System.out.println("numOfSequence ** "+ (int)numOfSequenceZero +"numofbit"+ (int)codeword.getNumOfBit());
				writeBits(huffmanTableAC[numOfSequenceZero+codeword.getNumOfBit()]);//TODO: or +
				//// System.out.println(huffmanTableAC[numOfSequenceZero+codeword.getNumOfBit()].getCode());
				writeBits(codeword);
				numOfSequenceZero = 0;
			}
		}
		
		if(posNoneZero<63) {
			// System.out.println(">>>>>write 0x00");
			writeBits(huffmanTableAC[0x00]);
		}
		return vector[0];
	}
	private void writeBits(Bitcode codeword) {
		// System.out.println("CALL method WRITEBITS");	
		codeword.myPrintCodeWord();
		// System.out.println();
//		// System.out.println((int)codeword.getCode());
//		// System.out.printf("buffer : add : before : data %s numbits: %d\n",Integer.toBinaryString(bitBuffer.getData()), (int)bitBuffer.getNumbits());
		// System.out.print("before : ");
		bitBuffer.printBufferData();

		bitBuffer.setNumbits((char) (bitBuffer.getNumbits()+codeword.getNumOfBit()));
		bitBuffer.setData((bitBuffer.getData()<<codeword.getNumOfBit())|codeword.getCode());
		
//		// System.out.printf("buffer : add : after  : data %s numbits: %d\n",Integer.toBinaryString(bitBuffer.getData()), (int)bitBuffer.getNumbits());
		// System.out.print("after : ");
		bitBuffer.printBufferData();
		
		while(bitBuffer.getNumbits()>=8) {
			bitBuffer.setNumbits((char) (bitBuffer.getNumbits()-8));
			char firstByte = (char) (bitBuffer.getData()>>bitBuffer.getNumbits());
			// System.out.println("***write out byte***:"+Integer.toHexString((int)(firstByte&0xFF)));
			try {
				writer.writeByte(firstByte);
				if((firstByte&0xFF) == 0xFF) {
					writer.writeByte(0x00);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			// System.out.printf("buffer : write out : after : data %s numbits: %d\n",Integer.toBinaryString(bitBuffer.getData()), (int)bitBuffer.getNumbits());
			// System.out.print("*after write byte: ");
			bitBuffer.printBufferData();
		}
	}
	private void genHuffmanTables() {
		huffmanTable_Y_DC = generateHuffmanTable(Huffman_Y_DC_CodesPerBitSize, Huffman_Y_DC_CodeValue);
		huffmanTable_Y_AC = generateHuffmanTable(Huffman_Y_AC_CodesPerBitsize, Huffman_Y_AC_Values);
		huffmanTable_CbCr_DC = generateHuffmanTable(Huffman_CbCr_DC_CodesPerBitsize, Huffman_CbCr_DC_Values);
		huffmanTable_CbCr_AC = generateHuffmanTable(Huffman_CbCr_AC_CodesPerBitsize, Huffman_CbCr_AC_Values);
		
		// System.out.println("***Y_DC***");
		logBitCodeArr(huffmanTable_Y_DC);
		// System.out.println("***Y_AC***");
		logBitCodeArr(huffmanTable_Y_AC);
		// System.out.println("***CbCr_DC***");
		logBitCodeArr(huffmanTable_CbCr_DC);
		// System.out.println("***CbCr_AC***");
		logBitCodeArr(huffmanTable_CbCr_AC);
	
	}
	private Bitcode[] generateHuffmanTable(char[] CodesPerBitSizeArr, char[] CodeValuesArr) {
		Bitcode[] result = new Bitcode[256];
		char huffmanCode = 0;
		char indexarr=0;
		char indexOfCurrentCode = CodeValuesArr[indexarr];
		for(char size = 1; size<=16; size++) {
			for(int i = 0; i< CodesPerBitSizeArr[size - 1]; i++) {
				indexOfCurrentCode = CodeValuesArr[indexarr++];
				result[indexOfCurrentCode] = new Bitcode(huffmanCode++, indexOfCurrentCode, size);
			}
			huffmanCode<<=1;
		}
		
		return result;
	}
	private void genCodeWordForQuantizedValue() {
		codeWordFoQuantizedValuePositive = new Bitcode[2048];
		codeWordFoQuantizedValueNagative = new Bitcode[2048];

		char numbits = 1;
		int mask = 1;
		for(char codevalue=1; codevalue< 2048; codevalue++) {
			if(codevalue>mask) {
				numbits++;
				mask =  ((mask<<1)|1);
			}
			codeWordFoQuantizedValuePositive[codevalue] = new Bitcode(codevalue, codevalue, numbits);
			codeWordFoQuantizedValueNagative[codevalue] = new Bitcode((char)(mask-codevalue), -codevalue, numbits);
		}
		
	
//		for(char codevalue=1; codevalue< 2048; codevalue++) {
//			codeWordFoQuantizedValuePositive[codevalue].printCodeWord();
//			codeWordFoQuantizedValueNagative[codevalue].myPrintCodeWord();
//			// System.out.println();
//		}
	
	}

	

	
	private void logBitCodeArr(Bitcode bitcodeArr[]) {
		for(int i=0; i<bitcodeArr.length;i++) {
			if(bitcodeArr[i]!=null)bitcodeArr[i].myPrintCodeWord(i);
		}
		// System.out.println();
	}
	private void log2DArr(int matrix[][]) {
		for(int i = 0; i< imgHeightWithPadding; i++) {
			for(int j = 0;j< imgWidthWithPadding; j++) {
				// System.out.print(" " + matrix[i][j] + " ");
			}
			// System.out.println();
		}
		// System.out.println();
	}
	private void log2DArr(float matrix[][]) {
		for(int i = 0; i< imgHeightWithPadding; i++) {
			for(int j = 0;j< imgWidthWithPadding; j++) {
				// System.out.print(matrix[i][j] + "\t");
			}
			// System.out.println();
		}
		// System.out.println();
	}
	private void log2DBlock(float matrix[][]) {
		for(int i = 0; i< 8; i++) {
			for(int j = 0;j< 8; j++) {
				// System.out.print(matrix[i][j] + "\t");
			}
			// System.out.println();
		}
		// System.out.println();
	}
	private void log2DBlock(int matrix[][]) {
		for(int i = 0; i< 8; i++) {
			for(int j = 0;j< 8; j++) {
				// System.out.print(matrix[i][j] + "\t");
			}
			// System.out.println();
		}
		// System.out.println();
	}
	private void logVector(int vector[]) {
		for(int i:vector) {
			// System.out.print(i+" ");
		}
		// System.out.println();
	}
}
