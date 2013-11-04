package com.codecCentral.imageio.charls;

import java.util.Vector;

import org.codecCentral.imageio.generic.EncoderBase;

/**
 * This class encodes one image into the J2K format, using the OpenJPEG.org
 * library. To be able to log messages, the called must register a
 * IJavaJ2KEncoderLogger object.
 */
public class CharlsEncoder extends EncoderBase {

	public interface IJavaJ2KEncoderLogger {
		public void logEncoderMessage(String message);

		public void logEncoderError(String message);
	}

	// ===== Compression parameters =============>
	// These value may be changed for each image
	private String[] encoder_arguments = null;

	/** number of resolutions decompositions */
	private int nbResolutions = -1;

	/** the quality layers, expressed as compression rate */
	private float[] ratioLayers = null;
	/**
	 * the quality layers, expressed as PSNR values. This variable, if defined,
	 * has priority over the ratioLayers variable
	 */
	private float[] psnrLayers = null;



	/** Holds the compressed version of the index file, returned by the encoder */
	private byte compressedIndex[] = null;

	/**
	 * Tile size. We suppose the same size for the horizontal and vertical
	 * tiles. If size == -1 ==> no tiling
	 */
	private int tileSize = -1;
	// <===== Compression parameters =============

	private Vector<IJavaJ2KEncoderLogger> loggers = new Vector<IJavaJ2KEncoderLogger>();

	public void addLogger(IJavaJ2KEncoderLogger messagesAndErrorsLogger) {
		loggers.addElement(messagesAndErrorsLogger);
	}

	public void removeLogger(IJavaJ2KEncoderLogger messagesAndErrorsLogger) {
		loggers.removeElement(messagesAndErrorsLogger);
	}

	/**
	 * This method compresses the given image.
	 * <P>
	 * It returns the compressed J2K codestream into the compressedStream
	 * byte[].
	 * <P>
	 * It also returns the compression index as a compressed form, into the
	 * compressedIndex byte[].
	 * <P>
	 * One of the image8, image16 or image24 arrays must be correctly
	 * initialized and filled.
	 * <P>
	 * The width, height and depth variables must be correctly filled.
	 * <P>
	 * The nbResolutions, nbLayers and if needed the float[] psnrLayers or
	 * ratioLayers must also be filled before calling this method.
	 */
	public void encode() {
		int comressBufferSize = width * height * getDepth();
		// Need to allocate / reallocate the compressed stream buffer ? (size =
		// max possible size = original image size)
		if (compressedStream == null || compressedStream.length != comressBufferSize) {
			logMessage("OpenJPEGJavaEncoder.encodeImageToJ2K: (re-)allocating "
					+ (width * height * getDepth())
					+ " bytes for the compressedStream");
			compressedStream = new byte[comressBufferSize];
		}
		// Arguments =
		// - number of resolutions "-n 5" : 2
		// - size of tile "-t 512,512" : 2
		//
		// Image width, height, depth and pixels are directly fetched by C from
		// the Java class
		int nbArgs = 2 + (tileSize == -1 ? 0 : 2)
				+ (encoder_arguments != null ? encoder_arguments.length : 0);
		if (psnrLayers != null && psnrLayers.length > 0 && psnrLayers[0] != 0)
			// If psnrLayers is defined and doesn't just express "lossless"
			nbArgs += 2;
		else if (ratioLayers != null && ratioLayers.length > 0
				&& ratioLayers[0] != 0.0)
			nbArgs += 2;
		String[] arguments = new String[nbArgs];
		int offset = 0;
		arguments[offset] = "-n";
		arguments[offset + 1] = "" + nbResolutions;
		offset += 2;
		if (tileSize != -1) {
			arguments[offset++] = "-t";
			arguments[offset++] = "" + tileSize + "," + tileSize;
		}
		// If PSNR layers are defined, use them to encode the images
		if (psnrLayers != null && psnrLayers.length > 0 && psnrLayers[0] != -1) {
			arguments[offset++] = "-q";
			String s = "";
			for (int i = 0; i < psnrLayers.length; i++)
				s += psnrLayers[i] + ",";
			arguments[offset++] = s.substring(0, s.length() - 1);
		} else if (ratioLayers != null && ratioLayers.length > 0
				&& ratioLayers[0] != 0.0) {
			// Specify quality ratioLayers, as compression ratios
			arguments[offset++] = "-r";
			String s = "";
			for (int i = 0; i < ratioLayers.length; i++)
				s += ratioLayers[i] + ",";
			arguments[offset++] = s.substring(0, s.length() - 1);
		}
		if (encoder_arguments != null) {
			for (int i = 0; i < encoder_arguments.length; i++) {
				arguments[i + offset] = encoder_arguments[i];
			}
		}
		logMessage("Encoder additional arguments = " + arrayToString(arguments));
		long startTime = (new java.util.Date()).getTime();
		compressedStreamLength = internalEncode(arguments);
		logMessage("compression time = "
				+ ((new java.util.Date()).getTime() - startTime) + " msec");
	}

	/**
	 * Fills the compressedStream byte[] and the compressedIndex byte[]
	 * 
	 * @return the codestream length.
	 */
	private native long nativeEncode(String[] parameters);
	
	protected long internalEncode(String[] parameters)
	{
		return nativeEncode(parameters);
		
	}

	/**
	 * Return the ratioLayers, i.e. the compression ratio for each quality
	 * layer. If the last value is 0.0, last layer is lossless compressed.
	 */
	public float[] getRatioLayers() {
		return ratioLayers;
	}

	/**
	 * sets the quality layers. At least one level. Each level is expressed as a
	 * compression ratio (float). If the last value is 0.0, the last layer will
	 * be losslessly compressed
	 */
	public void setRatioLayers(float[] layers) {
		this.ratioLayers = layers;
	}

	/**
	 * Return the PSNR Layers, i.e. the target PSNR for each quality layer. If
	 * the last value is -1, last layer is lossless compressed.
	 */
	public float[] getPsnrLayers() {
		return psnrLayers;
	}

	/**
	 * sets the quality layers. At least one level. Each level is expressed as a
	 * target PSNR (float). If the last value is -1, the last layer will be
	 * losslessly compressed
	 */
	public void setPsnrLayers(float[] layers) {
		this.psnrLayers = layers;
	}

	/** Set the number of resolutions that must be created */
	public void setNbResolutions(int nbResolutions) {
		this.nbResolutions = nbResolutions;
	}

	/**
	 * Return the compressed index file. Syntax: TODO PP:
	 */
	public byte[] getCompressedIndex() {
		return compressedIndex;
	}

	public void setCompressedIndex(byte[] index) {
		compressedIndex = index;
	}

	public void reset() {
		super.reset();
		nbResolutions = -1;
		ratioLayers = null;
		psnrLayers = null;
		compressedIndex = null;
	}

	/** Sets the size of the tiles. We assume square tiles */
	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	/**
	 * Contains all the encoding arguments other than the input/output file,
	 * compression ratio, tile size
	 */
	public void setEncoderArguments(String[] argumentsForTheEncoder) {
		encoder_arguments = argumentsForTheEncoder;
	}

	public void logMessage(String message) {
		for (IJavaJ2KEncoderLogger logger : loggers)
			logger.logEncoderMessage(message);
	}

	public void logError(String error) {
		for (IJavaJ2KEncoderLogger logger : loggers)
			logger.logEncoderError(error);
	}

	private String arrayToString(String[] array) {
		if (array == null)
			return "NULL";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++)
			sb.append(array[i]).append(" ");
		sb.delete(sb.length() - 1, sb.length());
		return sb.toString();
	}
}
