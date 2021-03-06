package com.krashidbuilt.info;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;

/**
 * Created by benkauffman on 10/1/16.
 */
public class General {

    private File source;

    private static Logger logger = LogManager.getLogger();

    public General() throws URISyntaxException {
        //default to current working directory
        source = new File(General.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    }

    public File getSourceDir(String[] args){
        if(args.length == 2){
            source = new File(args[1]);
        }else{
            logger.debug("Defaulting to current directory. Only 1 argument (the watermark) was supplied.");
        }

        return source;
    }


    public File[] getFilesInSource(final String extension){

        // create new filename filter
        FilenameFilter fileNameFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if(name.lastIndexOf('.')>0)
                {
                    // get last index for '.' char
                    int lastIndex = name.lastIndexOf('.');

                    // get extension
                    String str = name.substring(lastIndex);

                    // match path name extension
                    if(str.equalsIgnoreCase("." + extension))
                    {
                        return true;
                    }
                }
                return false;
            }
        };

        return source.listFiles(fileNameFilter);
    }
}
