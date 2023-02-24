package com.spiralstudio.mod.core.util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

/**
 * @author Leego Yih
 */
public class ConstructorModifier {
    private CtClass declaring;
    private boolean first;
    private String[] paramTypeNames;
    private String body;
    private String insertBefore;
    private String insertAfter;
    private Integer modifiers;

    public ConstructorModifier() {
    }

    public ConstructorModifier declaring(CtClass declaring) {
        this.declaring = declaring;
        return this;
    }

    public ConstructorModifier first() {
        this.first = true;
        return this;
    }

    public ConstructorModifier paramTypeNames(String... paramTypeNames) {
        this.paramTypeNames = paramTypeNames;
        return this;
    }

    public ConstructorModifier body(String body) {
        this.body = body;
        return this;
    }

    public ConstructorModifier insertBefore(String insertBefore) {
        this.insertBefore = insertBefore;
        return this;
    }

    public ConstructorModifier insertAfter(String insertAfter) {
        this.insertAfter = insertAfter;
        return this;
    }

    public ConstructorModifier modifiers(int modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public CtConstructor build(ClassPool classPool) throws NotFoundException, CannotCompileException {
        CtConstructor constructor;
        if (first) {
            constructor = declaring.getDeclaredConstructors()[0];
        } else if (paramTypeNames != null) {
            if (paramTypeNames.length > 0) {
                constructor = declaring.getDeclaredConstructor(classPool.get(paramTypeNames));
            } else {
                constructor = declaring.getDeclaredConstructor(new CtClass[0]);
            }
        } else {
            constructor = declaring.getDeclaredConstructor(new CtClass[0]);
        }
        if (modifiers != null) {
            constructor.setModifiers(modifiers);
        }
        if (body != null) {
            constructor.setBody(body);
        }
        if (insertBefore != null) {
            constructor.insertBefore(insertBefore);
        }
        if (insertAfter != null) {
            constructor.insertAfter(insertAfter);
        }
        return constructor;
    }
}
