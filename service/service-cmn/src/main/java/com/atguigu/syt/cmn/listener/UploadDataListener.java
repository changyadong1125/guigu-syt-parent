package com.atguigu.syt.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.vo.cmn.RegionExcelVo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn.listener
 * class:UploadDataListener
 *
 * @author: smile
 * @create: 2023/6/2-16:46
 * @Version: v1.0
 * @Description:
 */
@Slf4j
public class UploadDataListener implements ReadListener<RegionExcelVo> {
    @Resource
    private  RegionService regionService;

    private static final int BATCH_COUNT = 500;

    private List<RegionExcelVo> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    public UploadDataListener(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:每次读取一条数据都会执行一次该方法
     */
    @Override
    public void invoke(RegionExcelVo regionExcelVo, AnalysisContext analysisContext) {
        log.info("读取到一条数据:{}",regionExcelVo);
        cachedDataList.add(regionExcelVo);
        if (cachedDataList.size()>=BATCH_COUNT){
            regionService.batchInsert(cachedDataList);
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        regionService.batchInsert(cachedDataList);
        log.info("===========================================>文件解析完毕<===========================================");
    }
}
