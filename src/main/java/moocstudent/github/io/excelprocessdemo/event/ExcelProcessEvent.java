package moocstudent.github.io.excelprocessdemo.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 17:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelProcessEvent {

    private Integer alreadyInSize;

    private Boolean allOk;

//    private Integer totalSize;

}
