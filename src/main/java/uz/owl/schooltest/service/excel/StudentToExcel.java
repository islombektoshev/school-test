package uz.owl.schooltest.service.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.owl.schooltest.entity.Student;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StudentToExcel {

    @Transactional
    public XSSFWorkbook getStudentsSheet(List<Student> students) {
        if (students.isEmpty()) throw new RuntimeException("Student list cannot be empty");
        XSSFWorkbook xssfSheets = new XSSFWorkbook();
        XSSFSheet sheet = xssfSheets.createSheet("students");
        XSSFRow row = sheet.createRow(0);
        rowInsertString(row, new String[]{"Ism", "Familya", "Guruh", "Result"}, 0);

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            List<Object> list = new ArrayList();
            list.add(student.getFirstname());
            list.add(student.getLastname());
            if (student.getGuruh() != null)
                list.add(student.getGuruh().getName());
            else list.add("NoGroup");
            list.add(1);
            XSSFRow row1 = sheet.createRow(i + 1);
            rowInsert(row1, list, 0);
        }
        return xssfSheets;
    }

    private void rowInsertString(XSSFRow rows, String[] strings, int start) {
        for (int i = start; i < strings.length + start; i++) {
            XSSFCell cell = rows.createCell(i);
            cell.setCellValue(strings[i - start]);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
    }

    private void rowInsert(XSSFRow rows, List<?> cells, int start) {
        for (int i = start; i < cells.size() + start; i++) {
            Object o = cells.get(i - start);
            XSSFCell cell = rows.createCell(i);
            if (o instanceof Number) {
                Number number = (Number) o;
                cell.setCellValue(number.doubleValue());
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            } else if (o instanceof String) {
                cell.setCellValue(o.toString());
                cell.setCellType(Cell.CELL_TYPE_STRING);
            } else if (o instanceof Boolean) {
                cell.setCellValue((Boolean) o);
                cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
            } else if (o instanceof Date) {
                cell.setCellValue((Date) o);
            } else {
                cell.setCellValue(o.toString());
                cell.setCellType(Cell.CELL_TYPE_ERROR);
            }
        }
    }
}
