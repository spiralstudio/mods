package com.spiralstudio.mod.core.util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author Leego Yih
 */
public class MethodBuilder {
    private CtClass declaring;
    private String body;

    public MethodBuilder() {
    }

    public MethodBuilder declaring(CtClass declaring) {
        this.declaring = declaring;
        return this;
    }

    public MethodBuilder body(String body) {
        this.body = body;
        return this;
    }

    public CtMethod build(ClassPool classPool) throws NotFoundException, CannotCompileException {
        return CtMethod.make(body, declaring);
    }
}
