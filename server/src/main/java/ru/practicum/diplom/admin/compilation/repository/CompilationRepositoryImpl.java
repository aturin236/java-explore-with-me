package ru.practicum.diplom.admin.compilation.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import ru.practicum.diplom.admin.compilation.Compilation;

import javax.persistence.criteria.*;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CompilationRepositoryImpl implements CompilationRepositoryCustom {
    private final SessionFactory sessionFactory;

    @Override
    public List<Compilation> findCompilationsByParam(Boolean pinned, int from, int size) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Compilation> cq = cb.createQuery(Compilation.class);
        Root<Compilation> root = cq.from(Compilation.class);
        root.fetch("events", JoinType.LEFT);
        cq.select(root);

        Predicate predicate = cb.conjunction();

        if (pinned != null) {
            Predicate p = cb.equal(root.get("pinned"), pinned);
            predicate = cb.and(predicate, p);
        }

        cq.where(predicate);
        List<Compilation> results = session.createQuery(cq)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        session.close();
        return results;
    }
}
