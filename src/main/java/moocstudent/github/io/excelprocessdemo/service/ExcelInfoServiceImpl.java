package moocstudent.github.io.excelprocessdemo.service;

import com.alibaba.fastjson.JSON;
import moocstudent.github.io.excelprocessdemo.mapper.ExcelInfoMapper;
import moocstudent.github.io.excelprocessdemo.model.ExcelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 16:49
 */
@Service
public class ExcelInfoServiceImpl {

//    @Autowired
//    private ExcelInfoMapper excelInfoMapper;

    public Integer insertSelectBatch(List<ExcelInfo> list){
        System.out.println("批量导入数据:"+ JSON.toJSONString(list));
        return 1;
    }

}
