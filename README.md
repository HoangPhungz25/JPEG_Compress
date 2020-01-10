# Demo result
Zoom them yourself to see the differences.
* Image before compression: 6000 x 3376 , 11,6MB
![](https://drive.google.com/uc?export=view&id=1arBoacp2fdW32UjjJrwzYtGwSypfqzPs "Image before compression")
* Image after compression:  1,23MB
![](https://drive.google.com/uc?export=view&id=1WFSy39oh4vjQ-_xwbHCiiyVa1Gb2PyoJ "Image after compression")
# Project Structure: classes
* __Main__:
    *   Create path to input image, output location  
    *   Call JPEG_Compress class
* __JPEG_Compress__
    *   Read Image from path get from Main
    *   Compress
    *   Export to output path get from  Main
* __BitCode__
    *   A class for a huffman code, contain 2 atributes: code word and number of bit that demonstrates the code word
* __BitBuffer__
    * A class use to store bit sequence generated in Run Length Encoding and Huffman Encoding processes.
    * It contains 2 atributes: the buffer (a int number), and the number of bit that still in the buffer, waiting there to write out.
# Technical espects
*   Use default quantization table for 50% quality lost.
*   Use standard huffman table that encodes the length of value instead of value it self.
*   Works with: 3 color system image.
*   No subsampling
*   JFIF : 1.1
*   It works well when compress larg size image, bring a high compression ratio, but it has a really little effect on small size image.
*   Works well with non-text image.
# Issues
1. UI: we have no UI, this should be consider at last.
2. Bad performance (comment the console log to avoid IO block, increase performance)
# Documents
* Theory
    * T? tìm hi?u, l?n r?i, không bi?t google à, th?y t? b?o th?
    * [blog 1](http://www.robertstocker.co.uk/jpeg/jpeg_new_6.htm)
    * [blog 2](http://imrannazar.com/Let%27s-Build-a-JPEG-Decoder%3A-Concepts)
    * [video](https://www.youtube.com/watch?v=Q2aEzeMDHMA)
* Coding
    * [Huffman encoding](https://www.impulseadventure.com/photo/jpeg-huffman-coding.html)
    * [source code C++](https://github.com/stbrumme/toojpeg)
