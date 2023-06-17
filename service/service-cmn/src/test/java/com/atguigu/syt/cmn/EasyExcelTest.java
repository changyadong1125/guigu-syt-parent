package com.atguigu.syt.cmn;

import com.alibaba.excel.EasyExcel;
import com.atguigu.syt.cmn.mapper.RegionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn
 * class:EasyExcelTest
 *
 * @author: smile
 * @create: 2023/5/31-19:48
 * @Version: v1.0
 * @Description:
 */
@SpringBootTest
public class EasyExcelTest {
    @Resource
    private RegionMapper regionMapper;
    @Test
    public void testWrite(){
       // List<Region> regionList = regionMapper.selectList(null);
        ArrayList<DemoData> demoData = new ArrayList<>();
       demoData.add(new DemoData("1", "1", "1", 1));
       demoData.add(new DemoData("1", "1", "1", 1));
       demoData.add(new DemoData("1", "1", "1", 1));
       demoData.add(new DemoData("1", "1", "1", 1));
       demoData.add(new DemoData("1", "1", "1", 1));
       demoData.add(new DemoData("1", "1", "1", 1));
       demoData.add(new DemoData("1", "1", "1", 1));
       demoData.add(new DemoData("1", "1", "1", 1));
       demoData.add(new DemoData("1", "1", "1", 1));
       demoData.add(new DemoData("1", "1", "1", 1));
       // EasyExcel.write("D:\\Java\\尚硅谷java_尚医通\\EasyExcel\\regionList.xlsx", Region.class).excelType(ExcelTypeEnum.XLSX).sheet("regionList").doWrite(regionList);
    }
    @Test
    public void testRead(){
        EasyExcel.read("D:\\Java\\尚硅谷java_尚医通\\EasyExcel\\DemoData.xlsx", DemoData.class,new DemoDataListener()).sheet(0).doRead();
    }
}
