package com.krashidbuilt.info;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;

/**
 * Created by benkauffman on 10/1/16.
 */
public class General {

    private File source;



    public General() throws URISyntaxException {
        //default to current working directory
        source = new File(General.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    }

    public File getSourceDir(String[] args){
        if(args.length >= 1){
            source = new File(args[0]);
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
