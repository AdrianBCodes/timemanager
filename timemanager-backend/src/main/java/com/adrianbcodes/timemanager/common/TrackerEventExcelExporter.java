package com.adrianbcodes.timemanager.common;

import com.adrianbcodes.timemanager.dto.TrackerEventDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TrackerEventExcelExporter {
    private SXSSFWorkbook workbook;
    private SXSSFSheet sheet;
    private List<TrackerEventDTO> trackerEventDTOList;

    public TrackerEventExcelExporter(List<TrackerEventDTO> trackerEventDTOList){
        this.trackerEventDTOList = trackerEventDTOList;
        workbook = new SXSSFWorkbook(-1);
    }
    private void writeHeaderLine(){
        sheet = workbook.createSheet("Report");
        sheet.trackAllColumnsForAutoSizing();
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);

        createCell(row, 0, "CLIENT", style);
        createCell(row, 1, "USER", style);
        createCell(row, 2, "PROJECT", style);
        createCell(row, 3, "TASK", style);
        createCell(row, 4, "DESCRIPTION", style);
        createCell(row, 5, "DATE", style);
        createCell(row, 6, "DURATION", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style){
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(value.toString());
        cell.setCellStyle(style);
    }

    private void writeDataLines(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int rowCount = 1;

        for(TrackerEventDTO trackerEventDTO : trackerEventDTOList){
            Row row = sheet.createRow(rowCount);
            row.createCell(0).setCellValue(trackerEventDTO.getProject().getClient().getName());
            row.createCell(1).setCellValue(trackerEventDTO.getUser().getFullName());
            row.createCell(2).setCellValue(trackerEventDTO.getProject().getName());
            row.createCell(3).setCellValue(trackerEventDTO.getTask().getName());
            row.createCell(4).setCellValue(trackerEventDTO.getDescription());
            row.createCell(5).setCellValue(trackerEventDTO.getDate().format(formatter));
            row.createCell(6).setCellValue(DurationMapper.durationToString(trackerEventDTO.getDuration()));
            rowCount++;
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
    }

    public void export(HttpServletResponse response){
        writeHeaderLine();
        writeDataLines();

        try{
            workbook.write(response.getOutputStream());
            workbook.dispose();
            workbook.close();
        } catch (IOException eIO){
            throw new RuntimeException(eIO.getMessage());
        }

    }
}
