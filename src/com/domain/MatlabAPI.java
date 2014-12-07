package com.domain;

public interface MatlabAPI {	
	/* Returns 4 statistic featues: mean, variance, skewness, kurtosis */
	Object[] getStatisticalFeatures(String image_path, int window);	
	Object[] getStatisticalFeatures(Object[] image, int window);
	/* Read an image from file */
	Object[] readImage(String image_path);
	/* Applies homomorphic filtering */
	Object[] getHomomorphicFilter(String image_path, double sigma);
	/* Cluster feature vector */
	Object[] getFCMClustrering(Object[] features, int mask,int w,int h);
	Object[] getHomomorphicFilter(Object[] image,double sigma);
	Object[] getDFTFeatures(Object[] image, int window);
	Object[] getDFT(String image_path);
	void writeToXLS(String path, Object[] m,String sheet_name, String cell_num);
	void writeToXLS(String path, String m,String sheet_name, String cell_num);
}
