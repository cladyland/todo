package kovalenko.vika.service.impl;

import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.service.TagService;

public class TagServiceImp implements TagService {
    private final TagDAO tagDAO;
    public TagServiceImp(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }
}
