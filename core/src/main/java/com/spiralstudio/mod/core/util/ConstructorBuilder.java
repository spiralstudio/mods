package com.spiralstudio.mod.core.util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

/**
 * @author Leego Yih
 */
public class ConstructorBuilder {
    private CtClass declaring;
    private String[] parameters;
    private String body;
    private Integer modifiers;

    public ConstructorBuilder() {
    }

    public ConstructorBuilder declaring(CtClass declaring) {
        this.declaring = declaring;
        return this;
    }

    public ConstructorBuilder parameters(String... parameters) {
        this.parameters = parameters;
        return this;
    }

    public ConstructorBuilder body(String body) {
        this.body = body;
        return this;
    }

    public ConstructorBuilder modifiers(int modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public CtConstructor build(ClassPool classPool) throws NotFoundException, CannotCompileException {
        CtConstructor constructor;
        if (parameters != null && parameters.length > 0) {
            constructor = new CtConstructor(classPool.get(parameters), declaring);
        } else {
            constructor = new CtConstructor(new CtClass[0], declaring);
        }
        if (modifiers != null) {
            constructor.setModifiers(modifiers);
        }
        if (body != null) {
            constructor.setBody(body);
        }
        return constructor;
    }
}
