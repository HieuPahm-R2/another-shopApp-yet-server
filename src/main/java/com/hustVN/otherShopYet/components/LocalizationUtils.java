package com.hustVN.otherShopYet.components;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public String getLocalizedMessage(String code, Object... args) {
        HttpServletRequest req = WebUtils.getCurrentRequest();
        Locale locale = localeResolver.resolveLocale(req);
        return messageSource.getMessage(code, args,locale);
    }
}
