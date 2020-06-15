package de.wwu.pi.statisticsweb.service;

public interface StatisticsService {

	void addStatistics(Double x);
	
	Double getMedian();		
		
	Double getAverage();
}
