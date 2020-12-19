package moocstudent.github.io.excelprocessdemo.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moocstudent.github.io.excelprocessdemo.model.ExcelInfo;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 17:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelProcessEvent{

    private String num;

    private Integer alreadyInSize;

    private Boolean allOk;

    private List<ExcelInfo> listNotValid;

//    private Integer totalSize;


}
