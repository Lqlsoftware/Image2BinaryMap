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

	// ×óÓÒ±ßÔµÑÕÉ«Îª°×É«
	public static void main (String[] argv) throws IOException {
		// Image Path
		String ImgPath = "C:\\Users\\LqlSoft Computer\\Desktop\\MAP.png";
		// Output Path
		String OutPath = "C:\\Users\\LqlSoft Computer\\Desktop\\MAP.v";
		
		// Read File
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(ImgPath));
		} catch (Exception e) {
			System.out.println("Cannot find image");
			System.exit(0);
		}
		File result = new File(OutPath);
		if (!result.exists()) {
			 result.createNewFile();
		}
		FileWriter fw = new FileWriter(result.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		int i, j, whiteNum, blackNum, flagW, flagB;
		Color temp = null;
		for (j = 0;j < 960;j++) {
			whiteNum = 0;
			blackNum = 0;
			flagW = 0;
			flagB = 1;
			bw.write("10'd" + j + " : road_rom_data = {");
			for (i = 511;i > 0;i--) {
				temp = Color.getColor(null, image.getRGB(i, j));
				if (temp.getRed() == 255 && temp.getGreen() == 255 && temp.getBlue() == 255) {
					whiteNum += 1;
					flagW = 0;
					if (flagB  == 0) {
						bw.write("{" + blackNum + "{1'b1}},");
						blackNum = 0;
						flagB = 1;
					}
				}
				else if (temp.getRed() == 0 && temp.getGreen() == 0 && temp.getBlue() == 0){
					blackNum += 1;
					flagB = 0;
					if (flagW == 0) {
						bw.write(whiteNum + "'b0,");
						whiteNum = 0;
						flagW = 1;
					}
				}
			}
			bw.write(whiteNum + "'b0};" + '\n');
		}
		bw.close();
		System.out.println("Generate Successful!\nSave as " + OutPath);
	}
}
