package kovalenko.vika.service;

import org.hibernate.Session;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class Verifier {
    public static void transactionBegins(@Mock Session session) {
        verify(session.getTransaction(), times(1)).begin();
    }

    public static void transactionCommitted(@Mock Session session) {
        verify(session.getTransaction(), times(1)).commit();
    }

    public static void transactionNotCommitted(@Mock Session session) {
        verify(session.getTransaction(), never()).commit();
    }
}
