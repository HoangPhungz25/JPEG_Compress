![](https://sistenix.com/img/rgb.png "test image RGB")
![](https://sistenix.com/img/ycbcr.jpg "TEST image YCbCr")
# Project Structure
* __Main__:
    *   Init UI 
    *   Call JPEG_Compress class
* __JPEG_Compress__
    *   Read Image from url get from Main
    *   Compress
    *   Export to url get from  Main
* __Image files__
    * image.tif (raw input image file for testing)
    * image2.png (second input image file for testing)
    * other images: They all are output image. Each image is an export for testing : read img, convert color space, split blocks.
# Issue
1. What is the JPEG file format? After doing all processes, how can we write those data to file and which format of file do we chose?
2. Some function depends on JPEG file format to detect its "return" and its output, thus i can't complete those function in source code.
3. UI: we have no UI, this should be consider at last.
# Documents
* Theory
    * [blog](http://www.robertstocker.co.uk/jpeg/jpeg_new_6.htm)
    * [video](https://www.youtube.com/watch?v=Q2aEzeMDHMA)
* Coding
    * [source code C](https://github.com/stbrumme/toojpeg)
![](https://scontent.fhan2-1.fna.fbcdn.net/v/t1.15752-9/75439382_2832649200294713_3042513837023559680_n.png?_nc_cat=103&_nc_oc=AQk4_ExhGKoVkQJo9sG2ORdGGNx9SvVTtUjv6FQuMkr1SkL7mFr-Zwh4fefErbt6SPo&_nc_ht=scontent.fhan2-1.fna&oh=f1213b959aaa560142912433c98680ca&oe=5E467229 "TEST image YCbCr")