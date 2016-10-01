package com.krashidbuilt.magic;

import com.icafe4j.image.ImageColorType;
import com.icafe4j.image.ImageParam;
import com.icafe4j.image.options.TIFFOptions;
import com.icafe4j.image.quant.DitherMatrix;
import com.icafe4j.image.quant.DitherMethod;
import com.icafe4j.image.tiff.TIFFTweaker;
import com.icafe4j.image.tiff.TiffFieldEnum.Compression;
import com.icafe4j.io.FileCacheRandomAccessOutputStream;
import com.icafe4j.io.RandomAccessOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by benkauffman on 10/1/16.
 */

public class Pdf2TiffConverter {

    public static void savePdfAsTiff(File sourceFile) throws IOException {

        PDDocument pdf = null;
        try {
            pdf = PDDocument.load(sourceFile);
        } catch (IOException e) {
        }

        BufferedImage[] images = new BufferedImage[pdf.getNumberOfPages()];
        for (int i = 0; i < images.length; i++) {
            PDPage page = (PDPage) pdf.getDocumentCatalog().getAllPages()
                    .get(i);
            BufferedImage image;
            try {
//              image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 288); //works
                image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 300); // does not work
                images[i] = image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String outputFileName = sourceFile.getName().replace(".pdf", "");
        FileOutputStream fos = new FileOutputStream(outputFileName + ".tiff");
        RandomAccessOutputStream rout = new FileCacheRandomAccessOutputStream(
                fos);
        ImageParam.ImageParamBuilder builder = ImageParam.getBuilder();
        ImageParam[] param = new ImageParam[1];
        TIFFOptions tiffOptions = new TIFFOptions();
        tiffOptions.setTiffCompression(Compression.CCITTFAX4);
        builder.imageOptions(tiffOptions);
        builder.colorType(ImageColorType.BILEVEL).ditherMatrix(DitherMatrix.getBayer8x8Diag()).applyDither(true).ditherMethod(DitherMethod.BAYER);
        param[0] = builder.build();
        TIFFTweaker.writeMultipageTIFF(rout, param, images);
        rout.close();
        fos.close();
    }
}