package moocstudent.github.io.excelprocessdemo.repository;

import moocstudent.github.io.excelprocessdemo.model.ExcelInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: zhangQi
 * @Date: 2020-12-18 22:04
 */
@Repository
public interface ExcelInfoRepository extends R2dbcRepository<ExcelInfo, Long> {
}
