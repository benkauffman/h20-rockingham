package com.krashidbuilt.magic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by benkauffman on 10/1/16.
 */
public class ImageManipulation {

    private static Logger logger = LogManager.getLogger();
    
    private BufferedImage image;

    public ImageManipulation(File imageFile) throws IOException {
        image = ImageIO.read(imageFile);
    }

    public void printToConsole(){

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int clr   = image.getRGB(x,y);
                int red   = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue  =  clr & 0x000000ff;

                //(X,Y) = (R,G,B)
                logger.debug("({},{}) = ({},{},{}) ", x, y, red, green, blue);
            }
        }
    }




}
