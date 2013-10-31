package com.codecCentral.imageio.charls;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageInputStream;

import org.codecCentral.imageio.generic.NativeUtilities;
import org.codecCentral.imageio.generic.Utils;


public class JPLSCharlsImageReaderSpi extends ImageReaderSpi {


	static List<String> libraries;

	static {
		libraries = Arrays.asList("CharLS", "CharLS_JNI");
        NativeUtilities.loadLibraries(libraries);
    }
	
	
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
    }

    /**
     * This method checks if the provided input can be decoded from this SPI
     */
    public boolean canDecodeInput(Object input) throws IOException {
    	if (input == null)
    		return false;
    	
        boolean isDecodable = true;
    	CharlsDecoder decoder = new CharlsDecoder();

        // Retrieving the File source
        if (input instanceof File) {
            isDecodable =  decoder.canDecode(((File) input).getAbsolutePath());
          
        } else if (input instanceof byte[]) 
        {
           // source = (byte[])input;
        } else if (input instanceof URL)
        {
            final URL tempURL = (URL) input;
            if (tempURL.getProtocol().equalsIgnoreCase("file")) {
                //source = Utils.urlToFile(tempURL);
            }
        } else
            return false;

        return isDecodable;
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
        return new StringBuffer("JP2K Image Reader, version ").append(version)
                .toString();
    }

    /**
     * Upon registration, this method ensures that this SPI is listed at the top
     * of the ImageReaderSpi items, so that it will be invoked before the
     * default ImageReaderSpi
     * 
     * @param registry
     *                ServiceRegistry where this object has been registered.
     * @param category
     *                a Class object indicating the registry category under
     *                which this object has been registered.
     */
    @SuppressWarnings("unchecked")
	public synchronized void onRegistration(ServiceRegistry registry, Class category) {
        super.onRegistration(registry, category);
        if (registered) {
            return;
        }

        registered = true;
        
        
        if (!NativeUtilities.areLibrariesAvailable(libraries)) {
            final IIORegistry iioRegistry = (IIORegistry) registry;
            final Class<ImageReaderSpi> spiClass = ImageReaderSpi.class;
            final Iterator<ImageReaderSpi> iter = iioRegistry.getServiceProviders(spiClass,true);
            while (iter.hasNext()) {
                final ImageReaderSpi provider = (ImageReaderSpi) iter.next();
                if (provider instanceof JPLSCharlsImageReaderSpi) {
                    registry.deregisterServiceProvider(provider);
                }
            }
            return;
        }
        
        
        final List<ImageReaderWriterSpi> readers = Utils.getJDKImageReaderWriterSPI(registry,"jpeg", true);
        for (ImageReaderWriterSpi elem:readers) {
        	if (elem instanceof ImageReaderSpi){
	            final ImageReaderSpi spi = (ImageReaderSpi) elem;;
	            if (spi == this)
	                continue;
	            registry.deregisterServiceProvider(spi);
	            registry.setOrdering(category, this, spi);
        	}

        }
    }
}
