package com.example.demo.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeServiceExcelFileDownloading;

@Service
public class EmployeeServiceExcelFileDownloadImpl implements EmployeeServiceExcelFileDownloading {

	
   @Override
    public ByteArrayOutputStream exportFilteredEmployees(List<Employee> filteredEmployees) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Filtered Employees");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Name", "Email", "Job Profile", "Mobile No", "Register Date", "Permanent Address", "Gender"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Populate data rows
        int rowNum = 1;
        for (Employee employee : filteredEmployees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getFullName());
            row.createCell(1).setCellValue(employee.getEmail());
            row.createCell(2).setCellValue(employee.getJobProfile());
            row.createCell(3).setCellValue(employee.getMobileNo());
            row.createCell(4).setCellValue(formatDate(employee.getCreationDate()));
            row.createCell(5).setCellValue(employee.getPermanentAddress());
            row.createCell(6).setCellValue(employee.getGender());
        }

        // Write the output to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    private String formatDate(Date date) {
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	    return sdf.format(date);
    }

}
