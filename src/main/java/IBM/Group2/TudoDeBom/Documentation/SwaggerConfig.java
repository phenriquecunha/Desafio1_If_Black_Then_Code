package IBM.Group2.TudoDeBom.Documentation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("IBM.Group2.TudoDeBom.Controllers"))
          .paths(PathSelectors.any())
          .build()
          .useDefaultResponseMessages(false)
        .globalResponseMessage(RequestMethod.GET, responseMessageForGET())
        .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API Desafio 1 IBM If (black) { code }")
                .description("API para a loja fictícia Tudo De Bom")
                .version("1.0.0")
                .build();
    }

    private List<ResponseMessage> responseMessageForGET()
{
    return new ArrayList<ResponseMessage>() {{
        add(new ResponseMessageBuilder()
            .code(500)
            .message("500 message")
            .responseModel(new ModelRef("Error"))
            .build());
        add(new ResponseMessageBuilder()
            .code(403)
            .message("Forbidden!")
            .build());
    }};
}


}