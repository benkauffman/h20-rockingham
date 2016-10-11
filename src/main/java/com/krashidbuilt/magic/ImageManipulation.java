package com.krashidbuilt.magic;

import com.krashidbuilt.model.Coordinate;
import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by benkauffman on 10/1/16.
 */
public class ImageManipulation {

    private static Logger logger = LogManager.getLogger();

    private File watermarkFile;

    private List<Coordinate> watermarkCoordinates;

    private BufferedImage image;
    private int white;
    private int black;

    public ImageManipulation(String[] args) throws Exception {
        try{
            watermarkFile = new File(getClass().getClassLoader().getResource("watermark.tif").getFile());

            if(args.length >= 1){
                watermarkFile = new File(args[0]);
                logger.debug("WATERMARK SOURCE FILE IS {}", watermarkFile.getAbsolutePath());
            }

        }catch (Exception ex){
            logger.error("UNABLE TO LOAD WATERMARK IMAGE", ex);
            throw new Exception("UNABLE TO LOAD WATERMARK IMAGE");
        }

        black = new Color(0, 0, 0).getRGB();
        white = new Color(255, 255, 255).getRGB();

        //load the watermark and generate the coordinates map
        image = ImageIO.read(watermarkFile);
        watermarkCoordinates = new ArrayList<Coordinate>();
        generateCoordMap();
    }



    public void removeWaterMark(File imageFile) throws Exception {

        logger.debug("REMOVE WATERMARK FROM {}", imageFile.getName());


        ImageInputStream is = ImageIO.createImageInputStream(imageFile);
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(is);
        ImageReader reader = (ImageReader) iterator.next();
        iterator = null;
        reader.setInput(is);

        image = ImageIO.read(imageFile);


        int pageCount = reader.getNumImages(true);

        for(int i = 0; i <= pageCount - 1; i++){
            image = reader.read(i);

            for(Coordinate coordinate : watermarkCoordinates){
                if(isBlack(coordinate)){
                    smartDelete(coordinate);
                }
            }

            save(imageFile, imageFile.getName().replace(".tiff", "." + String.format("%04d", i + 1)));

        }


    }


    private void save(File imageFile, String rename) throws IOException {
        TIFFImageWriterSpi tiffspi = new TIFFImageWriterSpi();
        ImageWriter writer = tiffspi.createWriterInstance();

        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

        param.setCompressionType("LZW");
        param.setCompressionQuality(0.5f);


        File outputDir = new File(imageFile.getAbsolutePath().replace(imageFile.getName(), "dry"));
        if(!outputDir.exists()){
            outputDir.mkdir();
        }

        String absoluteOutputFile = outputDir.getPath() + File.separator + rename;


        File fOutputFile = new File(absoluteOutputFile);
        ImageOutputStream ios = ImageIO.createImageOutputStream(fOutputFile);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), param);
    }

    private boolean isBlack(Coordinate c){

        if(image.getWidth() < c.getX() || image.getHeight() < c.getY()){
            //out of bounds
            return false;
        }

        return image.getRGB(c.getX(), c.getY()) == black;
    }

    private void smartDelete(Coordinate c){
        //this watermark is only ever 4 pixels wide or 4 pixels tall
        //get the pixel and do some exploratory checks to make sure we should delete this pixel
        int xGridCount = 4;
        int yGridCount = 4;

        int blackCount = 0;
        int startPosition = c.getX() - xGridCount / 2;
        for(int i = 1; i <= xGridCount * 2; i++){

            if(startPosition == c.getX()){
                //this is the origin, skip it
                startPosition++;
            }
            if(isBlack(new Coordinate(startPosition++, c.getY()))){
                blackCount++;
            }
        }


        startPosition = c.getY() - yGridCount / 2;
        for(int i = 1; i <= yGridCount * 2; i++){

            if(startPosition == c.getY()){
                //this is the origin, skip it
                startPosition++;
            }
            if(isBlack(new Coordinate(c.getX(), startPosition++))){
                blackCount++;
            }
        }


        float percentage = (blackCount * 100 / (xGridCount * yGridCount));

        if(percentage <= 75){
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
                    watermarkCoordinates.add(new Coordinate(x,y));
                }

            }
        }

        logger.debug("{} COORDINATES FOUND", watermarkCoordinates.size());

    }


}
