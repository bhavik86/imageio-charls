package com.codecCentral.imageio.charls;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.codecCentral.imageio.generic.DecoderBase;
import org.codecCentral.imageio.generic.GenericImageReaderSpi;



public class JPLSCharlsImageReaderSpi extends GenericImageReaderSpi {

	
    protected boolean registered = false;
    
    static final String[] suffixes = { "jpg", "jpeg"};
     
    static final String[] formatNames = { "jpeg-ls", "JPEG-LS" };
     
    static final String[] MIMETypes = { "image/jpg", "image/jpeg" };

    static final String version = "1.0";

    static final String readerCN = "com.codecCentral.imageio.charls.JPLSCharlsImageReader";

    static final String vendorName = "CodecCentral";

    // writerSpiNames
    static final String[] wSN = { null };

    // StreamMetadataFormatNames and StreamMetadataFormatClassNames
    static final boolean supportsStandardStreamMetadataFormat = false;

    static final String nativeStreamMetadataFormatName = null;

    static final String nativeStreamMetadataFormatClassName = null;

    static final String[] extraStreamMetadataFormatNames = { null };

    static final String[] extraStreamMetadataFormatClassNames = { null };

    // ImageMetadataFormatNames and ImageMetadataFormatClassNames
    static final boolean supportsStandardImageMetadataFormat = false;

    static final String nativeImageMetadataFormatName = null;

    static final String nativeImageMetadataFormatClassName = null;

    static final String[] extraImageMetadataFormatNames = { null };

    static final String[] extraImageMetadataFormatClassNames = { null };

    public JPLSCharlsImageReaderSpi() {
        super(
                vendorName,
                version,
                formatNames,
                suffixes,
                MIMETypes,
                readerCN, // readerClassName
                new Class[] { File.class, byte[].class, ImageInputStream.class, URL.class, List.class },
                wSN, // writer Spi Names
                supportsStandardStreamMetadataFormat,
                nativeStreamMetadataFormatName,
                nativeStreamMetadataFormatClassName,
                extraStreamMetadataFormatNames,
                extraStreamMetadataFormatClassNames,
                supportsStandardImageMetadataFormat,
                nativeImageMetadataFormatName,
                nativeImageMetadataFormatClassName,
                extraImageMetadataFormatNames,
                extraImageMetadataFormatClassNames);
        
    	libraries = Arrays.asList("CharLS", "CharLS_JNI");
        _utilities.loadLibraries(libraries);
        format = "jpeg-ls";
    }
    
	protected DecoderBase CreateDecoder()
	{
		return new CharlsDecoder();
	}


    /**
     * Returns an instance of the {@link JPLSCharlsImageReader}
     * 
     * @see javax.imageio.spi.ImageReaderSpi#createReaderInstance(java.lang.Object)
     */
    public ImageReader createReaderInstance(Object source) throws IOException {
        return new JPLSCharlsImageReader(this);
    }

    /**
     * @see javax.imageio.spi.IIOServiceProvider#getDescription(java.util.Locale)
     */
    public String getDescription(Locale locale) {
        return new StringBuffer("JPEG-LS Image Reader, version ").append(version).toString();
    }

  
}
