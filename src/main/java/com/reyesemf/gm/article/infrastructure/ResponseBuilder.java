package com.reyesemf.gm.article.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class ResponseBuilder {

    public static final String ATTRIBUTES = "attributes";
    public static final String DATA = "data";
    public static final String ERRORS = "errors";
    public static final String ID = "id";
    public static final String INCLUDED = "included";
    public static final String TYPE = "type";

    private final List<ErrorObject> errors = new ArrayList<>();
    private final List<ResourceObject> data = new ArrayList<>();
    private final List<ResourceObject> included = new ArrayList<>();
    private final Map<String, Object> map = new HashMap<>();
    private final Map<String, Object> meta = new HashMap<>();

    public ResponseBuilder data(ResourceObject object) {
        requireNonNull(object, "resourceObject must not be null");
        data.add(object);
        return this;
    }

    public ResponseBuilder errors(ErrorObject object) {
        requireNonNull(object, "resourceObject must not be null");
        errors.add(object);
        return this;
    }

    private void addResource(String key, List<ResourceObject> list) {
        if (!list.isEmpty()) {
            map.put(key, list.stream().map(ResourceObject::map).collect(toList()));
        }
    }

    private void addError(List<ErrorObject> list) {
        if (!list.isEmpty()) {
            map.put(ERRORS, list);
        }
    }

    private void addMeta(Map<String, Object> meta) {
        if (!meta.isEmpty()) {
            map.put("meta", meta);
        }
    }

    public Map<String, Object> build() {
        if (!errors.isEmpty()) {
            addError(errors);
        } else {
            addResource(DATA, data);
            addResource(INCLUDED, included);
            addMeta(meta);
        }
        return map;
    }

    public static class ErrorObject {

        private final String title;
        private final String detail;
        private final int status;

        public ErrorObject(String title, String detail, int status) {
            this.title = title;
            this.detail = detail;
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public String getDetail() {
            return detail;
        }

        public int getStatus() {
            return status;
        }
    }

    public static class ResourceObject {

        private Map<String, Object> attributes = new HashMap<>();
        private String id;
        private String type;

        public ResourceObject() {
            //
        }

        public String getType() {
            return type;
        }

        public ResourceObject setType(String type) {
            this.type = type;
            return this;
        }

        public String getId() {
            return id;
        }

        public ResourceObject setId(String id) {
            this.id = id;
            return this;
        }

        public ResourceObject setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Map<String, Object> map() {
            Map<String, Object> map = new HashMap<>();
            map.put(TYPE, type);
            map.put(ID, id);
            if (!attributes.isEmpty()) {
                map.put(ATTRIBUTES, attributes);
            }
            return map;
        }

    }
}