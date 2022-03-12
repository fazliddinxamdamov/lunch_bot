package uz.fazliddin.service;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uz.fazliddin.model.UserFood;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Fazliddin Xamdamov
 * @date 11.03.2022  18:26
 * @project New-Lunch-Bot2
 */
public class FileService {

    public void keepToExel(List<UserFood> list){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/royxat.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Post sheet");

            XSSFRow row = sheet.createRow(0);
            XSSFCell cell = row.createCell(0);

            cell.setCellValue("Ism Familya");
            row.createCell(1).setCellValue("Lavozim");
            row.createCell(2).setCellValue("Ovqat");
            row.createCell(3).setCellValue("Vaqt");

            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i+1);
                row.createCell(0).setCellValue(list.get(i).getUserFullName());
                row.createCell(1).setCellValue(list.get(i).getUserPosition());
                row.createCell(2).setCellValue(list.get(i).getFoodName());
                row.createCell(3).setCellValue(list.get(i).getKuni().toString());
            }

            workbook.write(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
