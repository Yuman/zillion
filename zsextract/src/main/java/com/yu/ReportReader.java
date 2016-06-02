package com.yu;

import com.yu.entity.Report;

public interface ReportReader {

	boolean hasNext();

	Report next() throws Exception;

}