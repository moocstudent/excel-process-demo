package moocstudent.github.io.excelprocessdemo.service;

import moocstudent.github.io.excelprocessdemo.model.ExcelInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: zhangQi
 * @Date: 2020-12-19 14:04
 * 这里进行规则判定
 */
@Service
public class RulesServiceImpl {

    public boolean checkExcelWithRules(ExcelInfo info){
        /*
        如果数据中的money字段超过1000,则允许,否则不允许导入,放入另一个list
         */
        if(info.getMoney().compareTo(new BigDecimal("1345.0"))>0){
            return true;
        }else{
            return false;
        }
    }
}
