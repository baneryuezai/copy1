package com.IT.liuJia.service;

import java.util.List;
import java.util.Map;

public interface ReportService {

    List<Map<String,Object>> getPackageReport();

    Map<String,Object> getBusinessReportData();
}
