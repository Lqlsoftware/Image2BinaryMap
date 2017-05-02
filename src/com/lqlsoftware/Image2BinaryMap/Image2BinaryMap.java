package com.lqlsoftware.Image2BinaryMap;
/*
 *  @author Robin Lu
 *  
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Image2BinaryMap {
	
	public static void main () throws IOException {
		// Image Path
		String ImgPath = "C:\\MAP.png";
		// Output Path
		String OutPath = "C:\\MAP.v";
		
		// Read File
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(ImgPath));
		} catch (Exception e) {
			System.out.println("Cannot find image!");
			System.exit(0);
		}
		File result = new File(OutPath);
		
		// Delete old file
		if (result.exists()) {
			result.delete();
		}
		result.createNewFile();
		FileWriter fw = new FileWriter(result.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		Integer width = image.getWidth();
		Integer height = image.getHeight();
		
		Integer i, j, whiteNum, blackNum;
		
		Color pixel = null;
		Color nextPixel = null;
		Color Black = Color.black;
		
		bw.write("case(rom_address)\n");
		for (j = 0;j < height;j++) {
			whiteNum = 0;
			blackNum = 0;
			bw.write("\t10'd" + j + " : road_rom_data = {");
			pixel = Color.getColor(null, image.getRGB(width - 1, j));
			for (i = width - 1;i >= 0;i--) {
				nextPixel = (i == 0) ? null : Color.getColor(null, image.getRGB(i-1, j));
				// Black
				if (pixel.equals(Black)){
					// When next pixel is black
					if (nextPixel != null && nextPixel.equals(Black)) {
						blackNum += 1;
					} 
					// Other color
					else {
						bw.write("{" + (blackNum+1) + "{1'b1}}");
						bw.write((nextPixel == null) ? "};\n" : "," );
						blackNum = 0;
					}
				}
				// Other color
				else {
					// When next pixel is black
					if (nextPixel != null && !nextPixel.equals(Black)) {
						whiteNum += 1;
					}
					// Other color
					else {
						bw.write((whiteNum+1) + "'b0");
						bw.write((nextPixel == null) ? "};\n" : "," );
						whiteNum = 0;
					}
				}
				pixel = nextPixel;
			}
		}
		bw.write("\tdefault : road_rom_data = 512'b0;\nendcase");
		bw.close();
		System.out.println("Generate Successful!\nSave as " + OutPath);
	}
}
