package de.wwu.pi.statisticsweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	private JmsTemplate jmstemplate;
	
	public void addStatistics(Double x) {
		jmstemplate.convertAndSend("StatisticsQueue", x);
	}
	
	public Double getMedian() {
		return 109.0;
	}
	
	public Double getAverage() {
		return 110.0;
	}
}
