package omar.cucumber.ogc.util

import com.sun.istack.internal.NotNull
import org.apache.commons.io.FileUtils

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.DataBuffer
import static java.lang.Math.sqrt

class FileCompare
{
   FileCompare() {}

   static boolean checkImages(URL filePath1, URL filePath2, String image_type=null)
   {
      println "\n### FileCompare::checkImages(URL1, URL2)"
      println "### URL1   ${filePath1}"
      println "### URL2   ${filePath2}"
      String suffix = image_type ? ".${image_type}" : ""
      File file1 = File.createTempFile("tempImage1", suffix)
      File file2 = File.createTempFile("tempImage2", suffix)

      FileUtils.copyURLToFile(filePath1, file1)
      FileUtils.copyURLToFile(filePath2, file2)

      boolean imagesEqual = checkImages(file1, file2)

      if (imagesEqual)
      {
         file1.deleteOnExit()
         file2.deleteOnExit()
      }
      else
      {
         println "### Failed test -- preserving temporary files on server."
      }

      return imagesEqual
   }

   static boolean checkImages(File file1, File file2)
   {
      double FLT_EPSILON=0.000000001;
      if (!file1.exists() || !file2.exists())
         return 0

      println "\n@@@ FileCompare::checkImages(File1, File2)"
      println "@@@ File1   ${file1}"
      println "@@@ File2   ${file2}"
      double correlation = 0
      try
      {
         // Take buffer data from both image files.
         BufferedImage biA = ImageIO.read(file1)
         DataBuffer dbA = biA.getData().getDataBuffer()
         int sizeA = dbA.getSize()
         BufferedImage biB = ImageIO.read(file2)
         DataBuffer dbB = biB.getData().getDataBuffer()
         int sizeB = dbB.getSize()
         if (sizeA != sizeB)
         {
            System.out.println("Images are not of same size")
            return 0
         }

         // Compare data-buffer objects by computing normalized cross-correlation:
         double a=0, b=0, sumA2=0, sumB2=0, sumAB=0
         for (int i = 0; i < sizeA; i++)
         {
            a = dbA.getElem(i);
            b = dbB.getElem(i);
            sumA2 += a * a;
            sumB2 += b * b;
            sumAB += a * b;
         }
         double denom = sqrt(sumA2 * sumB2);
         if (denom > FLT_EPSILON)
            correlation = sumAB / denom;
         else if((sumA2 < FLT_EPSILON)&&(sumB2 < FLT_EPSILON)) // if both are zero then correlated
            correlation = 1.0;
         else
            correlation = 0.0;
      }
      catch (Exception e)
      {
         System.out.println("Failed to compare image files ...")
         throw e
      }

      boolean passed = (correlation > 0.99)
      if (passed)
         println "FileCompare::checkImages()  correlation=${correlation}  PASSED"
      else
         println "FileCompare::checkImages()  correlation=${correlation}  FAILED"

      return (passed)
   }
}
