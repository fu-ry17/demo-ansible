package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.dto.Reports.Quote;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class TemplateContentImpl implements TemplateContent {
    private final TemplateEngine templateEngine;

    public TemplateContentImpl(TemplateEngine templateEngine) {

        this.templateEngine = templateEngine;
    }

    @Override
    public String getTemplateContent(Quote quote) {
        Context context = new Context();
        context.setVariable("quote", quote);

        return templateEngine.process("quot_summary", context);
    }

}
