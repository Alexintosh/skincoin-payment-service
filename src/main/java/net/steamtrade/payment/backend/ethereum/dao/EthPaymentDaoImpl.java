package net.steamtrade.payment.backend.ethereum.dao;

import com.querydsl.jpa.impl.JPAQuery;
import net.steamtrade.payment.backend.ethereum.dao.model.EthPayment;
import net.steamtrade.payment.backend.ethereum.dao.model.QEthPayment;
import net.steamtrade.payment.backend.ethereum.dao.repository.EthPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 * Created by sasha on 29.06.17.
 */
@Repository
public class EthPaymentDaoImpl implements EthPaymentDao {

    @Autowired
    private EthPaymentRepository repository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public EthPayment save(EthPayment payment) {
        return repository.save(payment);
    }

    @Override
    public EthPayment getPayment(int appId, int type, String requestId) {
        QEthPayment payment = QEthPayment.ethPayment;
        return repository.findOne(payment.id.appId.eq(appId)
                .and(payment.id.type.eq(type))
                .and(payment.id.requestId.eq(requestId)));
    }

    @Override
    public EthPayment getPayment(String hash, boolean forUpdate) {
        QEthPayment payment = QEthPayment.ethPayment;
        JPAQuery<EthPayment> query = new JPAQuery<>(em);
        query.from(payment).where(payment.hash.eq(hash));
        if (forUpdate) {
            query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        }
        return query.fetchOne();
    }
}