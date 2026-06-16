package io.github.alexritian.codegen;

import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;

/**
 * @author Too_young
 */
public class NameGeneratorStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
       return switch (mode) {
           case POJO -> super.getJavaClassName(definition, mode) + "Po";
           default -> super.getJavaClassName(definition, mode);
       };
    }


}
