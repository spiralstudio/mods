package com.spiralstudio.mod.core.util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author Leego Yih
 */
public class MethodModifier {
    private CtClass declaring;
    private String methodName;
    private String[] paramTypeNames;
    private String body;
    private String insertBefore;
    private String insertAfter;
    private Integer modifiers;

    public MethodModifier() {
    }

    public MethodModifier declaring(CtClass declaring) {
        this.declaring = declaring;
        return this;
    }

    public MethodModifier methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public MethodModifier paramTypeNames(String... paramTypeNames) {
        this.paramTypeNames = paramTypeNames;
        return this;
    }

    public MethodModifier body(String body) {
        this.body = body;
        return this;
    }

    public MethodModifier insertBefore(String insertBefore) {
        this.insertBefore = insertBefore;
        return this;
    }

    public MethodModifier insertAfter(String insertAfter) {
        this.insertAfter = insertAfter;
        return this;
    }

    public MethodModifier modifiers(int modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public CtMethod build(ClassPool classPool) throws NotFoundException, CannotCompileException {
        CtMethod method;
        if (paramTypeNames != null) {
            if (paramTypeNames.length > 0) {
                method = declaring.getDeclaredMethod(methodName, classPool.get(paramTypeNames));
            } else {
                method = declaring.getDeclaredMethod(methodName, new CtClass[0]);
            }
        } else {
            method = declaring.getDeclaredMethod(methodName);
        }
        if (modifiers != null) {
            method.setModifiers(modifiers);
        }
        if (body != null) {
            method.setBody(body);
        }
        if (insertBefore != null) {
            method.insertBefore(insertBefore);
        }
        if (insertAfter != null) {
            method.insertAfter(insertAfter);
        }
        return method;
    }
}
