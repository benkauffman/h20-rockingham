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

        if(args.length < 1){
            throw new Exception("You must at least specifiy the watermark template file in the arguement.");
        }

//        String[] arg = new String[]{"/Users/benkauffman/development/java/h2o-rockingham/images/tiff"};
        args = new String[]{"C:\\Users\\hackur\\Desktop\\watermark.tif",
                "C:\\Users\\hackur\\.IntelliJIdea15\\IdeaProjects\\h2o-rockingham\\images\\tiff"};

        General general = null;
        try {
            general = new General();
        } catch (URISyntaxException e) {
            logger.error("UNABLE TO GET SOURCE DIRECTORY", e);
        }

        File source = general.getSourceDir(args);
        logger.debug("Source directory is set to {}", source);

        File[] images = general.getFilesInSource("tiff");
        if(images.length >= 1){
            ImageManipulation im = new ImageManipulation(args);
            for(File image : images){
                im.removeWaterMark(image);
            }
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