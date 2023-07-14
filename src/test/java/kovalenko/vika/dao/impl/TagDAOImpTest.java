package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.AbstractDAOTest;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kovalenko.vika.dao.H2Env.LAST_ID_TAGS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagDAOImpTest extends AbstractDAOTest {
    TagDAOImp dao;

    @Override
    @BeforeEach
    protected void init() {
        dao = new TagDAOImp(factory);
        session.getTransaction().begin();
    }

    @Test
    void get_default_tags() {
        var expected = Set.of("Work", "JavaRush", "Study", "Sport", "Design");

        Set<String> actual = dao.getDefaultTags(session)
                .stream()
                .map(TagDTO::getTitle)
                .collect(Collectors.toSet());

        assertEquals(expected.size(), actual.size());
        expected.forEach(title -> assertTrue(actual.contains(title)));
    }

    @Test
    void get_user_tags() {
        int expSize = 2;
        String expected1 = "JohnTag";
        String expected2 = "JohnTag2";

        List<TagDTO> userTags = dao.getUserTags(1L);

        assertEquals(expSize, userTags.size());
        assertEquals(expected1, userTags.get(0).getTitle());
        assertEquals(expected2, userTags.get(1).getTitle());
    }

    @Test
    void get_tags_by_ids() {
        int expSize = 2;
        long javaRushId = 2L;
        long designId = 5L;

        Set<String> actual = dao.getTagsByIds(Set.of(javaRushId, designId))
                .stream()
                .map(Tag::getTitle)
                .collect(Collectors.toSet());

        assertEquals(expSize, actual.size());
        assertTrue(actual.contains("JavaRush"));
        assertTrue(actual.contains("Design"));
    }

    @Test
    void save_default_tag() {
        long expId = LAST_ID_TAGS + 1;
        String expTitle = "new-tag";
        String expColor = "#FFA07A";
        boolean expDefault = true;

        var tag = Tag.builder()
                .title(expTitle)
                .color(expColor)
                .isDefault(expDefault)
                .build();

        Tag actual = dao.save(tag);

        assertEquals(expId, actual.getId());
        assertEquals(expTitle, actual.getTitle());
        assertEquals(expColor, actual.getColor());
        assertEquals(expDefault, actual.isDefault());
    }

    @Test
    void update_user_tag() {
        long tagId = LAST_ID_TAGS;
        String expTitle = "MyTag";
        String expColor = "#F08080";
        boolean expDefault = false;

        Tag tag = dao.getTagsByIds(Set.of(tagId))
                .stream()
                .findFirst()
                .orElse(new Tag());

        tag.setTitle(expTitle);
        tag.setColor(expColor);

        Tag actual = dao.update(tag);

        assertEquals(tagId, actual.getId());
        assertEquals(expTitle, actual.getTitle());
        assertEquals(expColor, actual.getColor());
        assertEquals(expDefault, actual.isDefault());
    }
}
