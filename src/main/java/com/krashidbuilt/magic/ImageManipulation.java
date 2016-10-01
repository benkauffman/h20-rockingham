package com.krashidbuilt.magic;

import com.krashidbuilt.model.Coordinate;
import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by benkauffman on 10/1/16.
 */
public class ImageManipulation {

    private static Logger logger = LogManager.getLogger();

    private File watermarkFile = new File(getClass().getClassLoader().getResource("watermark.tif").getFile());

    private List<Coordinate> watermarkCoordinates;

    private BufferedImage image;
    private int white;

    public ImageManipulation() throws IOException {
         white = new Color(255, 255, 255).getRGB();

        //load the watermark and generate the coordinates map
        image = ImageIO.read(watermarkFile);
        watermarkCoordinates = new ArrayList<Coordinate>();
        generateCoordMap();
    }



    public void removeWaterMark(File imageFile) throws Exception {

        logger.debug("REMOVE WATERMARK FROM {}", imageFile.getName());

        image = ImageIO.read(imageFile);

//        logger.debug("LOOPING THROUGH {} WATERMARK COORDINATES", watermarkCoordinates.size());
        for(Coordinate coordinate : watermarkCoordinates){
            if(image.getWidth() < coordinate.getX() || image.getHeight() < coordinate.getY()){
                throw new Exception("WATERMARK COORDINATE IS OUTSIDE IMAGE RANGE");
            }

            cleanPixel(coordinate);
        }

        String output = "clean - " + imageFile.getName();

//        File outputFile =  new File(output);
//        ImageIO.write(image, "TIFF", outputFile);

        TIFFImageWriterSpi tiffspi = new TIFFImageWriterSpi();
        ImageWriter writer = tiffspi.createWriterInstance();

        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

        param.setCompressionType("LZW");
        param.setCompressionQuality(0.5f);


        String absoluteOutputFile = imageFile.getAbsolutePath().replace(imageFile.getName(), output);

        File fOutputFile = new File(absoluteOutputFile);
        ImageOutputStream ios = ImageIO.createImageOutputStream(fOutputFile);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), param);

    }


    private void cleanPixel(Coordinate c){
        int clr   = image.getRGB(c.getX(),c.getY());
        int red   = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue  =  clr & 0x000000ff;

        if(red + green + blue == 0){
//            logger.debug("CLEAN PIXEL AT ({},{})", c.getX(), c.getY());
            image.setRGB(c.getX(), c.getY(), white);
        }

    }

    public void generateCoordMap(){
        logger.debug("READING WATERMARK AND SETTING COORDINATES");
        //get a list of coordinates where the watermark lives
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int clr   = image.getRGB(x,y);
                int red   = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue  =  clr & 0x000000ff;

                if(red + green + blue == 0){
                    //(X,Y) = (R,G,B)
//                    logger.debug("WATERMARK PIXEL ({},{}) = ({},{},{}) ", x, y, red, green, blue);
                    watermarkCoordinates.add(new Coordinate(x,y));
                }

            }
        }

        logger.debug("{} COORDINATES FOUND", watermarkCoordinates.size());

    }


}
