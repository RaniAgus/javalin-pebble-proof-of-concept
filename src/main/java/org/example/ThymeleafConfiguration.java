package org.example;

import lombok.Getter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafConfiguration {
  private static ThymeleafConfiguration INSTANCE;
  private ClassLoaderTemplateResolver templateResolver;
  private @Getter TemplateEngine templateEngine;

  private ThymeleafConfiguration() {
    templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setPrefix("/thymeleaf/");
    templateResolver.setSuffix(".html");
    templateResolver.setCharacterEncoding("UTF-8");

    templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
  }

  public static ThymeleafConfiguration getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ThymeleafConfiguration();
    }
    return INSTANCE;
  }
}
