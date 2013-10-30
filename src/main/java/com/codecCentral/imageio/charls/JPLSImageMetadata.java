package com.codecCentral.imageio.charls;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.w3c.dom.Node;

import com.sun.media.imageioimpl.common.ImageUtil;

public class JPLSImageMetadata extends IIOMetadata {

    public static final String nativeMetadataFormatName = "com_codecCentral_imageio_charls_ImageMetadata_1.0";

    public static final String NUM_COMPONENTS = "NumberOfComponents";

    public static final String MAX_BIT_DEPTH = "MaxBitDepth";

    public static final String WIDTH = "Width";

    public static final String HEIGHT = "Height";

    public static final String TILE_WIDTH = "TileWidth";

    public static final String TILE_HEIGHT = "TileHeight";

    public static final String MAX_QUALITY_LAYERS = "MaxAvailableQualityLayers";

    public static final String DWT_LEVELS = "SourceDWTLevels";

    public static final String IS_SIGNED = "IsSigned";

    public static final String BITS_PER_COMPONENT = "BitsPerComponent";

    public static final String COMPONENT_INDEXES = "ComponentIndexes";

    /** Number of components of the source. */
    private int numComponents;

    /** the whole image width */
    private int width;

    /** the whole image height */
    private int height;

    /** the tile image width */
    private int tileWidth;

    /** the tile image height */
    private int tileHeight;

    /** max number of available quality layers */
    private int maxAvailableQualityLayers = -1;

    /** The source resolution levels. */
    private int sourceDWTLevels;

    private boolean isSigned;

    private int[] bitsPerComponent;

    private int[] componentIndexes;

    public JPLSImageMetadata(JPLSCodestreamProperties properties) {
        numComponents = properties.getNumComponents();
        bitsPerComponent = properties.getBitsPerComponent();
        componentIndexes = properties.getComponentIndexes();
        sourceDWTLevels = properties.getSourceDWTLevels();
        maxAvailableQualityLayers = properties.getMaxAvailableQualityLayers();
        tileHeight = properties.getTileHeight();
        tileWidth = properties.getTileWidth();
        isSigned = properties.isSigned();
        width = properties.getWidth();
        height = properties.getHeight();
    }

    @Override
    public Node getAsTree(String formatName) {
        if (formatName.equalsIgnoreCase(nativeMetadataFormatName))
            return createNativeTree();
        else
            throw new IllegalArgumentException(formatName
                    + " is not a supported format name");
    }

    private Node createNativeTree() {
        IIOMetadataNode rootNode = new IIOMetadataNode(nativeMetadataFormatName);
        IIOMetadataNode child;
        
        child = new IIOMetadataNode(HEIGHT);
        child.setUserObject(Integer.valueOf(height));
        child.setNodeValue(Integer.toString(height));
        rootNode.appendChild(child);
        
        child = new IIOMetadataNode(WIDTH);
        child.setUserObject(Integer.valueOf(width));
        child.setNodeValue(Integer.toString(width));
        rootNode.appendChild(child);
        
        child = new IIOMetadataNode(TILE_HEIGHT);
        child.setUserObject(Integer.valueOf(tileHeight));
        child.setNodeValue(Integer.toString(tileHeight));
        rootNode.appendChild(child);
        
        child = new IIOMetadataNode(TILE_WIDTH);
        child.setUserObject(Integer.valueOf(tileWidth));
        child.setNodeValue(Integer.toString(tileWidth));
        rootNode.appendChild(child);
        
        child = new IIOMetadataNode(NUM_COMPONENTS);
        child.setUserObject(Integer.valueOf(numComponents));
        child.setNodeValue(Integer.toString(numComponents));
        rootNode.appendChild(child);
        
        child = new IIOMetadataNode(BITS_PER_COMPONENT);
        child.setUserObject(bitsPerComponent);
        child.setNodeValue(ImageUtil.convertObjectToString(bitsPerComponent));
        rootNode.appendChild(child);
        
        child = new IIOMetadataNode(COMPONENT_INDEXES);
        child.setUserObject(componentIndexes);
        child.setNodeValue(ImageUtil.convertObjectToString(componentIndexes));
        rootNode.appendChild(child);
        
        child = new IIOMetadataNode(IS_SIGNED);
        child.setUserObject(Boolean.valueOf(isSigned));
        child.setNodeValue(Boolean.toString(isSigned));
        rootNode.appendChild(child);
        
        child = new IIOMetadataNode(MAX_QUALITY_LAYERS);
        child.setUserObject(Integer.valueOf(maxAvailableQualityLayers));
        child.setNodeValue(Integer.toString(maxAvailableQualityLayers));
        rootNode.appendChild(child);
        
        child = new IIOMetadataNode(DWT_LEVELS);
        child.setUserObject(Integer.valueOf(sourceDWTLevels));
        child.setNodeValue(Integer.toString(sourceDWTLevels));
        rootNode.appendChild(child);
        
        return rootNode;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    public void mergeTree(String formatName, Node root)
            throws IIOInvalidTreeException {
        throw new UnsupportedOperationException("MergeTree is unsupported");
    }

    @Override
    public void reset() {

    }

}
