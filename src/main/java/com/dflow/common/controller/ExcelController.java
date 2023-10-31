package com.dflow.common.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@Log4j2
public class ExcelController {

    @PostMapping("/excel")
    public void excel(HttpServletResponse res, @RequestBody HashMap<String, Object> paramMap) throws IOException {

        List<HashMap<String,Object>> data = (List<HashMap<String, Object>>) paramMap.get("excelData");
        log.info(data);


        //excel sheet 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("직원 리스트");                   //엑실 sheet 이름
        sheet.setDefaultColumnWidth(28);                                      //디폴트 너비 설정

        // 엑셀의 행에 해당하는 Row  객체 생성
        Row row = null;
        // 엑셀의 열에 해당하는 cell 객체 생성
        Cell cell = null;

        //header data
        int rowCount = 0;   // 데이터가 저장될 행
        String header[] = new String[]{"번호", "이름", "직책", "부서", "핸드폰 번호","이메일 번호"};

        // 첫 행을 생성
        row = sheet.createRow(0);

        // 헤더의 수(컬럼 이름의 수)만큼 반복해서 열을 생성
        for(int i = 0; i < header.length; i++){

            //열을 만들어준다.
            cell = row.createCell(i);

            // 열에 헤더이름(컬럼 이름)을 넣어준다.
            cell.setCellValue(header[i]);

        }

        // 현재 행의 개수를 가지고 있는 변수 rowCount 선언(header 행잉 있으므로 1부터 시작)
        rowCount = 1;

        //body 내용
        for(int i = 0; i < data.size(); i++){

            row = sheet.createRow(rowCount++);

            cell = row.createCell(0);
            cell.setCellValue(i+1);

            cell = row.createCell(1);
            cell.setCellValue((String) data.get(i).get("memberNameKr"));

            cell = row.createCell(2);
            cell.setCellValue((String) data.get(i).get("staffName"));

            cell = row.createCell(3);
            cell.setCellValue((String) data.get(i).get("departmentName"));

            cell = row.createCell(4);
            cell.setCellValue((String) data.get(i).get("memberPhone"));

            cell = row.createCell(5);
            cell.setCellValue((String) data.get(i).get("memberEmail"));
        }
        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        res.setHeader("content-Disposition","attachment;filename=직원리스트.xlsx");

        try{
            workbook.write(res.getOutputStream());
        }catch (IOException e){
            log.error("Workbook write 수행 중 IOException 발생");
            throw new RuntimeException(e);
        }finally {
            workbook.close();
        }
    }

}
