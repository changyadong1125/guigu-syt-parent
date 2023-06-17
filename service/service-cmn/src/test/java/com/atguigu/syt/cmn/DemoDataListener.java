package com.atguigu.syt.cmn;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;

import java.util.List;
import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn
 * class:DemoDataListener
 *
 * @author: smile
 * @create: 2023/5/31-20:00
 * @Version: v1.0
 * @Description:
 */
public class DemoDataListener implements ReadListener<DemoData> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<DemoData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
   // private RegionMapper regionMapper;

    //public DemoDataListener(RegionMapper regionMapper) {

     //   this.regionMapper =regionMapper;
   // }
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        System.out.println("headMap = " + headMap);
    }

    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("demoData = " + demoData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("analysisContext = " + analysisContext);
    }
}
