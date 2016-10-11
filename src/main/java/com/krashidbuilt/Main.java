package com.krashidbuilt;

import com.krashidbuilt.info.General;
import com.krashidbuilt.magic.ImageManipulation;
import com.krashidbuilt.magic.Pdf2TiffConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Ben Kauffman on 09/30/16.
 */
public class Main {

    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws Exception {

        if(args.length == 0 || (args.length == 1 && args[0].contains("help"))){
            logger.debug("Argument 1 should be the watermark.tif 'template' file.");
            logger.debug("Argument 2 should be the directory path where the watermarked images are stored.");
            logger.debug("A new sub directory will be created in the target directory called 'dry'.");
            logger.debug("The dry images will be copied to the dry directory after the watermark is removed.");
            System.exit(0);
        }

        if(args.length < 2){
            throw new Exception("You must specify the watermark template file in the first argument. " +
                    "You must specify the directory of the images in the 2nd argument.");
        }

        try{

            General general = null;
            try {
                general = new General();
            } catch (URISyntaxException e) {
                logger.error("UNABLE TO GET SOURCE DIRECTORY", e);
            }

            File source = general.getSourceDir(args);
            logger.info("Source directory is set to {}", source);

            File[] images = general.getFilesInSource("tiff");
            if(images != null && images.length >= 1){
                ImageManipulation im = new ImageManipulation(args);
                for(File image : images){
                    im.removeWaterMark(image);
                }
            }

        }catch (Exception ex){
            logger.error("EXCEPTION CAUGHT IN THE MAIN CLASS WRAPPER", ex);
        }

    }

    private void convert(General general) throws IOException {

        File[] pdfs = general.getFilesInSource("pdf");
        logger.debug("{} PDFs found in source matching the filter criteria", pdfs.length);

        for(File pdf : pdfs){
            logger.debug("converting pdf {}", pdf.getName());
            Pdf2TiffConverter.savePdfAsTiff(pdf);
        }
    }
}