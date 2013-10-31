package com.codecCentral.imageio.charls;

import org.codecCentral.imageio.generic.DecoderBase;

/**
 * This class decodes one  codestream into an image (width + height + depth +
 * pixels[].. To be able to log messages, the
 * called must register a IJavaJ2KDecoderLogger object.
 */
public class CharlsDecoder extends DecoderBase {

	/** the quality layers */
	private int[] layers = null;

	private int reductionIn = 0;
	private int tileIn = -1;
	private int areaX0 = 0;
	private int areaY0 = 0;
	private int areaX1 = 0;
	private int areaY1 = 0;

	private int userChangedTile = 0;
	private int userChangedReduction = 0;
	private int userChangedArea = 0;

	private int maxTiles = 0;
	private int maxReduction = 0;
	
	private static boolean DEBUG_COMPRESS_FROM_BUFFER = false;	
	

	@Override
    protected int internalDecode(String[] parameters)
	{
	   int rc =  	nativeDecode(parameters);
		if (DEBUG_COMPRESS_FROM_BUFFER)
		{
			
			CharlsEncoder encoder = new CharlsEncoder();
			if (image8 != null)
			{
				encoder.setImage8(image8);
				encoder.setDepth( 8);
			}
			else if (image16 != null)
			{
				encoder.setImage16(image16);
				encoder.setDepth( 16);				
				
			}
			else if (image24 != null)
			{
				encoder.setImage24(image24);
				encoder.setDepth( 24);
			}
			encoder.setWidth( width);
			encoder.setHeight(height);
			encoder.setNbResolutions(6);
		    encoder.encode();
		}
		return rc;
	}

	@Override
	protected int internalGetFormat(String[] parameters) {
		return 0;
	}	

	
	//NATIVE METHODS

	/**
	 * Decode the jpeg-ls stream given in the codestream byte[] and fills the
	 * image8, image16 or image24 array, according to the bit depth.
	 */
	/* ================================================================== */
	private native int nativeDecode(String[] parameters);

	/* ================================================================== */

	public void setTileIn(int t) {
		this.tileIn = t;
	}

	public void setReductionIn(int r) {
		this.reductionIn = r;
	}

	public void setAreaIn(int x0, int y0, int x1, int y1) {
		this.areaX0 = x0;
		this.areaY0 = y0;
		this.areaX1 = x1;
		this.areaY1 = y1;
	}

	public void setUserChangedTile(int v) {
		this.userChangedTile = v;
	}

	public void setUserChangedReduction(int v) {
		this.userChangedReduction = v;
	}

	public void setUserChangedArea(int v) {
		this.userChangedArea = v;
	}

	public int getMaxTiles() {
		return maxTiles;
	}

	public int getMaxReduction() {
		return maxReduction;
	}

	public void setMaxTiles(int v) {
		this.maxTiles = v;
	}

	public void setMaxReduction(int v) {
		this.maxReduction = v;
	}

	public void reset() {
		layers = null;
		super.reset();
	}


}
