package com.enn.energy.system.common.util;


import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.*;


/**
 * Created by SHH
 */
public class ExcelUtils {

    private static int DATA_IN_MEMORY = 1000;

    private static int XLSX_SHEET_ROWS = 1_000_000;

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    private ExcelUtils() {

    }

    /**
     * @param tableHeaders    表头
     * @param rows            数据行
     * @param exportExcelName 导出文件名
     * @param excelSuffix     文件后缀
     * @return ResponseEntity<byte                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               [                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ]>
     */
    public static ResponseEntity<byte[]> exportExcel(String[] tableHeaders, List<LinkedHashMap<String, String>> rows, String exportExcelName,
                                                     ExcelSuffix excelSuffix) {
        //增加参数校验
        Optional.ofNullable(tableHeaders)
                .filter(ths -> ths.length > 0)
                .orElseThrow(() -> new IllegalArgumentException("headers can't be null"));
        Optional.ofNullable(exportExcelName)
                .filter(name -> name.length() > 0)
                .orElseThrow(() -> new IllegalArgumentException("exportExcelName can't be null"));

        return generateResponse(createWorkBook(tableHeaders, rows), excelSuffix);
    }

    /**
     * 生产成本分析
     *
     * @param tableHeaders    表头
     * @param rows            数据行
     * @param exportExcelName 导出文件名
     * @return Workbook
     */
    public static Workbook productConsumptionExportExcel(String[] tableHeaders, List<LinkedHashMap<String, String>> rows, String exportExcelName) {
        //增加参数校验
        Optional.ofNullable(tableHeaders)
                .filter(ths -> ths.length > 0)
                .orElseThrow(() -> new IllegalArgumentException("headers can't be null"));
        Optional.ofNullable(exportExcelName)
                .filter(name -> name.length() > 0)
                .orElseThrow(() -> new IllegalArgumentException("exportExcelName can't be null"));

        return createWorkBook(tableHeaders, rows);
    }

    /**
     * 生产成本分析
     *
     * @param tableHeaders    表头
     * @param rows            数据行
     * @param exportExcelName 导出文件名
     * @param excelSuffix     文件后缀
     * @param workbook
     * @return
     */
    public static ResponseEntity<byte[]> productConsumptionExportExcelTwo(String[] tableHeaders, List<LinkedHashMap<String, String>> rows, String exportExcelName,
                                                                          ExcelSuffix excelSuffix, Workbook workbook, int index) {
        //增加参数校验
        Optional.ofNullable(tableHeaders)
                .filter(ths -> ths.length > 0)
                .orElseThrow(() -> new IllegalArgumentException("headers can't be null"));
        Optional.ofNullable(exportExcelName)
                .filter(name -> name.length() > 0)
                .orElseThrow(() -> new IllegalArgumentException("exportExcelName can't be null"));

        return generateResponse(productConsumptionCreateWorkBook(tableHeaders, rows, workbook, index), excelSuffix);
    }

    /**
     * 生产成本分析
     *
     * @param headers
     * @param rows
     * @param workbook
     * @param rowIndex 从第几行插入
     * @return
     */
    private static Workbook productConsumptionCreateWorkBook(String[] headers, List<LinkedHashMap<String, String>> rows, Workbook workbook, int rowIndex) {

        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.createRow(rowIndex);
        CellStyle cellStyle = getCellStyle(workbook);
        setTableHeader(row, headers, cellStyle);

        if (CollectionUtils.isNotEmpty(rows)) {
            int index = rowIndex + 1;
            for (Map<String, String> map : rows) {
                row = sheet.createRow(index);
                int columnIndex = 0;
                for (String key : map.keySet()) {
                    CellUtil.createCell(row, columnIndex, map.get(key), cellStyle);
                    columnIndex++;
                }
                index++;
            }
        }
        return workbook;
    }

    /**
     * @param excelSuffix Excel 后缀
     * @param axis        导入方式 x/y
     * @param sheetUnit   sheet信息
     */
    public static ResponseEntity<byte[]> export(ExcelSuffix excelSuffix, Axis axis, SheetUnit sheetUnit) {

        return export(excelSuffix, axis, Collections.singletonList(sheetUnit));
    }

    /**
     * @param excelSuffix Excel 后缀
     * @param axis        导入方式 x/y
     * @param sheetList   sheet信息(多个)
     */
    public static ResponseEntity<byte[]> export(ExcelSuffix excelSuffix, Axis axis, List<SheetUnit> sheetList) {

        return generateResponse(newWorkbook(sheetList, axis), excelSuffix);
    }

    /**
     * @param workbook    workbook
     * @param excelSuffix suffix
     * @return response
     */
    @SneakyThrows
    private static ResponseEntity<byte[]> generateResponse(Workbook workbook, ExcelSuffix excelSuffix) {

        @Cleanup
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //参数校验
        String suffix = Optional.ofNullable(excelSuffix).orElse(ExcelSuffix.xlsx).suffix;
        String attachmentHeader = "attachment;filename=" + Instant.now().toEpochMilli() + suffix;
        HttpHeaders headers = new HttpHeaders();
        //返回文件流
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("content-disposition", attachmentHeader);
        workbook.write(bos);
        byte[] bytes = bos.toByteArray();
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    /**
     * 新建一个 workbook
     *
     * @param sheetUnits 多个sheet信息
     * @return Workbook
     */
    @SneakyThrows
    private static Workbook newWorkbook(List<SheetUnit> sheetUnits, final Axis axis) {

        val workbook = new SXSSFWorkbook(DATA_IN_MEMORY);
        try {
            Optional.ofNullable(sheetUnits)
                    .filter(CollectionUtils::isNotEmpty)
                    .ifPresent(ths -> ths.forEach(unit -> fillContent(workbook, unit, axis)));
        } catch (Exception e) {
            logger.info("create workbook params ==> sheetUnits: {}", sheetUnits);
            logger.info("create workbook error ==> " + e.getMessage(), e);
        }
        return workbook;
    }

    /**
     * 填充Excel 内容
     *
     * @param workbook workbook
     * @param unit     一个sheet
     */
    private static void fillContent(Workbook workbook, SheetUnit unit, Axis axis) {

        final Sheet sheet = Optional.ofNullable(unit.getSheetName())
                .filter(StringUtils::isNotEmpty)
                .map(workbook::createSheet)
                .orElseGet(workbook::createSheet);
        //headers
        String[] headers = unit.getHeaders();
        Row headerRow = sheet.createRow(0);
        for (int i = 0, length = headers.length; i < length; i++) {
            CellUtil.createCell(headerRow, i, headers[i]);
        }
        //content
        List<List<String>> data = Optional.ofNullable(unit.getData()).filter(CollectionUtils::isNotEmpty).orElseGet(ArrayList::new);
        val index = new int[]{1}; //计数器
        switch (axis) {
            case x:
                data.forEach(content -> Optional.ofNullable(content).filter(CollectionUtils::isNotEmpty).ifPresent(ths -> {
                    Row row = sheet.createRow(index[0]);
                    for (int i = 0, length = ths.size(); i < length; i++) {
                        CellUtil.createCell(row, i, ths.get(i));
                    }
                    index[0]++;
                }));
                return;
            case y:
                val columnIndex = new int[]{0};
                data.forEach(content -> Optional.ofNullable(content).filter(CollectionUtils::isNotEmpty).ifPresent(ths -> {
                    ths.forEach(value -> {
                        Row row = Optional.ofNullable(sheet.getRow(index[0])).orElseGet(() -> sheet.createRow(index[0]));
                        if (StringUtils.isNotEmpty(value) && !value.equalsIgnoreCase("null")) {
                            CellUtil.createCell(row, columnIndex[0], value);
                        }
                        index[0]++;
                    });
                    index[0] = 1;
                    columnIndex[0]++;
                }));

        }
    }

    private static Workbook createWorkBook(String[] headers, List<LinkedHashMap<String, String>> rows) {

        Workbook workbook = new SXSSFWorkbook(DATA_IN_MEMORY);
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        CellStyle cellStyle = getCellStyle(workbook);
        setTableHeader(row, headers, cellStyle);

        if (CollectionUtils.isNotEmpty(rows)) {
            int index = 1;
            for (Map<String, String> map : rows) {
                row = sheet.createRow(index);
                int columnIndex = 0;
                for (String key : map.keySet()) {
                    CellUtil.createCell(row, columnIndex, map.get(key), cellStyle);
                    columnIndex++;
                }
                index++;
            }
        }
        return workbook;
    }

    /**
     * 设置表格列头
     */
    private static void setTableHeader(Row row, String[] headers, CellStyle cellStyle) {
        for (int i = 0; i < headers.length; i++) {
            CellUtil.createCell(row, i, headers[i], cellStyle);
        }
    }

    /**
     * 设置表格样式
     */
    private static CellStyle getCellStyle(Workbook workbook) {

        return workbook.createCellStyle();
    }

    public enum ExcelSuffix {

        xls(".xls"),
        xlsx(".xlsx");

        public final String suffix;

        ExcelSuffix(String suffix) {
            this.suffix = suffix;
        }
    }

    public enum Axis {
        x, y
    }

    @ToString
    @Getter
    @Builder
    public static class SheetUnit {

        private String sheetName;
        @NonNull
        private String[] headers;
        @NonNull
        private List<List<String>> data;
    }
}
