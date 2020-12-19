package moocstudent.github.io.excelprocessdemo.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 14:48
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("excel_info")
public class ExcelInfo implements Serializable {

    @Id
    @Column("id")
    private Long id;
    @Column("num")
    @ExcelProperty(value="合同编号",index=0)
    private String num;
    @Column("money")
    @ExcelProperty(value="担保金额",index=1)
    private BigDecimal money;

}
