package com.reyesemf.gm.article.infrastructure;


import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class SluggifyTest {
    private final Function<String, String> sluggify = new Sluggify();

    @Test
    public void givenStringWhenApplyThenReturnSlug() {
        assertEquals("nombre-de-producto-promocion", sluggify.apply("Nombre de producto promoción"));
        assertEquals("producto-con-simbolos", sluggify.apply("Producto con símbolos!@#$%"));
        assertEquals("categoria-con-aeiou", sluggify.apply("Categoría con áéíóú"));
        assertEquals("producto-con-n", sluggify.apply("Producto con ñ"));
        assertEquals("producto-con-espacios", sluggify.apply("Producto    con   espacios"));
        assertEquals("producto-con-guiones", sluggify.apply("Producto---con---guiones"));
        assertEquals("", sluggify.apply(""));
        assertEquals("", sluggify.apply("   "));
        assertEquals("", sluggify.apply("!@#$%^&*()"));
        assertEquals("producto-123-version-2", sluggify.apply("Producto 123 versión 2"));
        assertEquals("producto-limpio", sluggify.apply("   ---Producto limpio---   "));
        assertEquals("producto-en-mayusculas", sluggify.apply("PRODUCTO en MaYuScUlAs"));
        assertEquals("producto-con-c", sluggify.apply("Producto con ç"));
        assertEquals("producto-con-u", sluggify.apply("Producto con ü"));
    }

    @Test
    public void givenNullWhenApplyThenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> sluggify.apply(null));
    }

}