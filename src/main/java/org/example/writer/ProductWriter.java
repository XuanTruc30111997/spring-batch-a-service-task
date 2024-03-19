package org.example.writer;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Product;
import org.example.properties.LiquibaseProperties;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ProductWriter implements Tasklet, StepExecutionListener {
    private List<Product> productsTransformed;

    @Autowired
    DataSource dataSource;

    @Autowired
    LiquibaseProperties liquibaseProperties;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Start writing");

        SimpleJdbcInsert simpleJdbcInsert =
                new SimpleJdbcInsert(dataSource).withTableName("product_task");
        simpleJdbcInsert.setSchemaName(liquibaseProperties.getSchema());

        this.productsTransformed.forEach(prod -> {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("id", prod.getId());
            parameters.put("name", prod.getName());
            parameters.put("price", prod.getPrice());

            simpleJdbcInsert.execute(parameters);
        });

        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("ProductWriter Before writing");
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();
        this.productsTransformed = (List<Product>) executionContext.get("productsTransformed");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("End writing Product");

        return ExitStatus.COMPLETED;
    }
}
