package moocstudent.github.io.excelprocessdemo.service;

import com.alibaba.fastjson.JSON;
import moocstudent.github.io.excelprocessdemo.model.ExcelInfo;
import moocstudent.github.io.excelprocessdemo.repository.ExcelInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 16:49
 * 这里数据写入数据库
 */
@Service
public class ExcelInfoServiceImpl {

//    @Autowired
//    private ExcelInfoMapper excelInfoMapper;
    @Autowired
    private ExcelInfoRepository repository;

    public Flux<ExcelInfo> insertSelectBatch(List<ExcelInfo> list){
        System.out.println("批量导入数据:"+ JSON.toJSONString(list));
        Flux<ExcelInfo> excelInfoFlux = repository.saveAll(list);
        return excelInfoFlux;
    }

}
