package kovalenko.vika.service;

import kovalenko.vika.dao.TagDAO;

public class TagService {
    private final TagDAO tagDAO;
    public TagService(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }
}
