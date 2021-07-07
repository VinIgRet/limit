package ru.vinigret.limit.service;

import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.context.Context;
import java.util.Map;

public class GenericTemplateEngine {

    private final TemplateEngine templateEngine;

    public GenericTemplateEngine(TemplateMode templateMode) {
        templateEngine = new org.thymeleaf.TemplateEngine();
        ClassLoaderTemplateResolver templateResolver   = new ClassLoaderTemplateResolver(Thread.currentThread().getContextClassLoader());
        templateResolver.setTemplateMode(templateMode);
        templateResolver.setPrefix("/thymeleaf/");
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCacheable(true);
        this.templateEngine.setTemplateResolver(templateResolver);
    }
    public String getTemplate(String templateName, Map<String, Object> parameters) {
        Context ctx = new Context();
        if (parameters != null) {
            parameters.forEach((k, v) -> {
                ctx.setVariable(k, v);
            });
        }
        return this.templateEngine.process(templateName, ctx).trim();
    }

}