package com.aws.rekognition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.util.IOUtils;

public class RekognitionEngine {

	private AmazonRekognition rekog;
	private Float similarityThreshold = 70F;
	private String sourceImage = "/Users/LIFE/Desktop/input.JPG";
	private String targetImage = "/Users/LIFE/Desktop/target.JPG";
	private ByteBuffer sourceImageBytes=null;
	private ByteBuffer targetImageBytes=null;
	private InputStream inputStream=null;
    		
	
	public void identifier(){
		
		rekog = AmazonRekognitionClientBuilder.defaultClient();
		
		try {
			inputStream = new FileInputStream(new File(sourceImage));
			sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		} catch (FileNotFoundException e) {
			System.out.println("Input file not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error while converting input image into bytes");
			e.printStackTrace();
		}
		
		try {
			inputStream = new FileInputStream(new File(targetImage));
			targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		} catch (FileNotFoundException e) {
			System.out.println("target file not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error while converting target image into bytes");
			e.printStackTrace();
		}
		
	       Image source = new Image()
	    		    .withBytes(sourceImageBytes);
	       Image target = new Image()
	          		.withBytes(targetImageBytes);

	     CompareFacesRequest request = new CompareFacesRequest()
	                  .withSourceImage(source)
	                  .withTargetImage(target)
	                  .withSimilarityThreshold(similarityThreshold);
	     
	  // Call operation
	  CompareFacesResult result = rekog.compareFaces(request);
	  
      // Display results
      List <CompareFacesMatch> faceDetails = result.getFaceMatches();
      for (CompareFacesMatch match: faceDetails){
      	ComparedFace face= match.getFace();
      	BoundingBox position = face.getBoundingBox();
      	System.out.println("Face at " + position.getLeft().toString()
      			+ " " + position.getTop()
      			+ " matches with " + face.getConfidence().toString()
      			+ "% confidence.");
      }
		
	}
 	
}
