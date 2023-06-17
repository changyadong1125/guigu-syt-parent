package com.atguigu.syt.cmn;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn
 * class:DemoData
 *
 * @author: smile
 * @create: 2023/5/31-19:49
 * @Version: v1.0
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ColumnWidth(20)
public class DemoData {
    @ExcelProperty("DemoDataName")
    private String name;
    @ExcelProperty("DemoDataPassword")
    private String password;
    @ExcelProperty("DemoDataDescribe")
    private String describe;
    @ExcelIgnore
    private int age;
}
