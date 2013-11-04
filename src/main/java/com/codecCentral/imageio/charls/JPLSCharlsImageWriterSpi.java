package com.codecCentral.imageio.charls;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

import org.codecCentral.imageio.generic.GenericImageWriterSpi;

public class JPLSCharlsImageWriterSpi extends GenericImageWriterSpi {

    static final String[] suffixes = { "jpeg", "JPEG", "jpg", "JPG" };

    static final String[] formatNames = { "jpeg-ls", "JPEG-LS"};

    static final String[] MIMETypes = { "image/jpeg" };

    static final String version = "1.0";

    static final String writerCN = "com.codecCentral.imageio.charls.JPLSCharlsImageWriterSpi";

    static final String vendorName = "CodecCentral";

    // ReaderSpiNames
    static final String[] readerSpiName = { "com.codecCentral.imageio.charls.JPLSCharlsImageWriterSpi" };

    // StreamMetadataFormatNames and StreamMetadataFormatClassNames
    static final boolean supportsStandardStreamMetadataFormat = false;

    static final String nativeStreamMetadataFormatName = null;

    static final String nativeStreamMetadataFormatClassName = null;

    static final String[] extraStreamMetadataFormatNames = null;

    static final String[] extraStreamMetadataFormatClassNames = null;

    // ImageMetadataFormatNames and ImageMetadataFormatClassNames
    static final boolean supportsStandardImageMetadataFormat = false;

    static final String nativeImageMetadataFormatName = null;

    static final String nativeImageMetadataFormatClassName = null;

    static final String[] extraImageMetadataFormatNames = { null };

    static final String[] extraImageMetadataFormatClassNames = { null };
    
    static final Class[] OUTPUT_TYPE =  { File.class, ImageOutputStream.class };

    /**
     * Default {@link ImageWriterSpi} constructor for JP2K writers.
     */
    public JPLSCharlsImageWriterSpi() {
        super(vendorName, version, formatNames, suffixes, MIMETypes, writerCN,
                OUTPUT_TYPE, readerSpiName,
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
    }

    /**
     * @see javax.imageio.spi.ImageWriterSpi#createWriterInstance(java.lang.Object)
     */
    public ImageWriter createWriterInstance(Object extension)
            throws IOException {
        return new JPLSCharlsImageWriter(this);
    }

    /**
     * @see javax.imageio.spi.IIOServiceProvider#getDescription(java.util.Locale)
     */
    public String getDescription(Locale locale) {
        return "SPI for JPEG LS ImageWriter based on CharLS JNI";
    }

    /**
     * Refine the check if needed.
     */
    public boolean canEncodeImage(ImageTypeSpecifier type) {
//        final int numBands = type.getNumBands();
//        final int numBits = type.getBitsPerBand(0);
        return true;
    }

}
