package moocstudent.github.io.excelprocessdemo.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 14:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelInfo implements Serializable {
    private Long id;

    @ExcelProperty(value="合同编号",index=0)
    private String num;

    @ExcelProperty(value="担保金额",index=1)
    private BigDecimal money;

}
