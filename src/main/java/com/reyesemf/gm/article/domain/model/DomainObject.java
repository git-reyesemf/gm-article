package com.reyesemf.gm.article.domain.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.hash;

public abstract class DomainObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 731048927361529804L;

    public abstract String comparisonKey();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainObject that = (DomainObject) o;
        return Objects.equals(comparisonKey(), that.comparisonKey());
    }

    @Override
    public int hashCode() {
        return hash(comparisonKey());
    }

}
