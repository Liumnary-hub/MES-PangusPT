//package com.mes.config;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.info.License;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.servers.Server;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import org.springdoc.core.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Arrays;
//
//// SwaggerConfig.java
//@Configuration
//@OpenAPIDefinition(
//        info = @Info(
//                title = "MES质量管理系统API文档",
//                version = "1.0.0",
//                description = "MES质量管理模块接口文档",
//                contact = @Contact(
//                        name = "开发团队",
//                        email = "dev@mes.com",
//                        url = "https://mes.com"
//                ),
//                license = @License(
//                        name = "Apache 2.0",
//                        url = "https://www.apache.org/licenses/LICENSE-2.0"
//                )
//        ),
//        servers = {
//                @Server(url = "http://localhost:8080", description = "开发环境"),
//                @Server(url = "https://test.mes.com", description = "测试环境")
//        }
//)
//@SecurityScheme(
//        name = "Authorization",
//        type = SecuritySchemeType.HTTP,
//        scheme = "bearer",
//        bearerFormat = "JWT"
//)
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("Authorization",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")
//                        )
//                )
//                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
//                .tags(Arrays.asList(
//                        new Tag().name("质量管理").description("质量检验相关接口"),
//                        new Tag().name("检验标准").description("检验标准管理接口"),
//                        new Tag().name("检验计划").description("检验计划管理接口"),
//                        new Tag().name("检验任务").description("检验任务管理接口"),
//                        new Tag().name("统计分析").description("质量统计分析接口")
//                ));
//    }
//
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("quality")  // 分组名称
//                .pathsToMatch("/api/quality/**")  // 匹配路径
//                .pathsToExclude("/api/quality/internal/**")  // 排除路径
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .group("admin")
//                .pathsToMatch("/api/admin/**")
//                .build();
//    }
//}