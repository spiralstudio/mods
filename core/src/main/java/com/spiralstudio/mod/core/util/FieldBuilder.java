package com.spiralstudio.mod.core.util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

/**
 * @author Leego Yih
 */
public class FieldBuilder {
    private CtClass declaring;
    private String typeName;
    private String fieldName;
    private Integer modifiers;

    public FieldBuilder() {
    }

    public FieldBuilder declaring(CtClass declaring) {
        this.declaring = declaring;
        return this;
    }

    public FieldBuilder typeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public FieldBuilder fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public FieldBuilder modifiers(int modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public CtField build(ClassPool classPool) throws NotFoundException, CannotCompileException {
        CtField field = new CtField(classPool.get(typeName), fieldName, declaring);
        if (modifiers != null) {
            field.setModifiers(modifiers);
        }
        return field;
    }
}
