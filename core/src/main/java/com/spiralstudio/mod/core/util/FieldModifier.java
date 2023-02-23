package com.spiralstudio.mod.core.util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

/**
 * @author Leego Yih
 */
public class FieldModifier {
    private CtClass declaring;
    private String fieldName;
    private Integer modifiers;

    public FieldModifier() {
    }

    public FieldModifier declaring(CtClass declaring) {
        this.declaring = declaring;
        return this;
    }

    public FieldModifier fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public FieldModifier modifiers(int modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public CtField build(ClassPool classPool) throws NotFoundException, CannotCompileException {
        CtField field = declaring.getDeclaredField(fieldName);
        if (modifiers != null) {
            field.setModifiers(modifiers);
        }
        return field;
    }
}
