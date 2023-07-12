package kovalenko.vika.service.impl;

import kovalenko.vika.command.TagCommand;
import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.model.Tag;
import kovalenko.vika.service.Verifier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImpTest {
    @Mock
    TagDAO tagDAO;
    @Mock
    Session session;
    TagServiceImp service;

    @BeforeEach
    void init() {
        service = new TagServiceImp(tagDAO);
        when(tagDAO.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(Mockito.mock(Transaction.class));
    }

    @Test
    void get_default_tags() {
        service.getDefaultTags();

        Verifier.transactionBegins(session);
        verify(tagDAO, times(1)).getDefaultTags(session);
        Verifier.transactionCommitted(session);
    }

    @Test
    void get_user_tags() {
        service.getUserTags(1L);

        Verifier.transactionBegins(session);
        verify(tagDAO, times(1)).getUserTags(1L);
        Verifier.transactionCommitted(session);
    }

    @Test
    void create_tag() {
        var tagCommand = TagCommand.builder()
                .title("title")
                .color("#00ff1e")
                .userId(1L)
                .build();

        service.createTag(tagCommand);

        Verifier.transactionBegins(session);
        verify(tagDAO, times(1)).save(any(Tag.class));
        Verifier.transactionCommitted(session);
    }
}
