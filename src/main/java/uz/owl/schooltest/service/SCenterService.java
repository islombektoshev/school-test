package uz.owl.schooltest.service;

import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.SCenterDao;
import uz.owl.schooltest.entity.SCenter;
import uz.owl.schooltest.entity.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class SCenterService {

    private final SCenterDao sCenterDao;

    public SCenterService(SCenterDao sCenterDao) {
        this.sCenterDao = sCenterDao;
    }

    public SCenter findByAuthorAndName(User author, String name) {
        return sCenterDao.findByAuthorAndName(author, name);
    }

    @Transactional
    public void deleteByAuthorAndName(User author, String name) {
        sCenterDao.deleteByAuthorAndName(author, name);
    }

    public SCenter createSCenterForUser(User user, String name, String caption){
        SCenter center = SCenter.builder()
                .author(user)
                .name(name)
                .caption(caption)
                .build();
        return sCenterDao.save(center);
    }

    public <S extends SCenter> S save(S s) {
        return sCenterDao.save(s);
    }

    public <S extends SCenter> Iterable<S> saveAll(Iterable<S> iterable) {
        return sCenterDao.saveAll(iterable);
    }

    public Optional<SCenter> findById(Long aLong) {
        return sCenterDao.findById(aLong);
    }

    public boolean existsById(Long aLong) {
        return sCenterDao.existsById(aLong);
    }

    public Iterable<SCenter> findAll() {
        return sCenterDao.findAll();
    }

    public Iterable<SCenter> findAllById(Iterable<Long> iterable) {
        return sCenterDao.findAllById(iterable);
    }

    public long count() {
        return sCenterDao.count();
    }

    public void deleteById(Long aLong) {
        sCenterDao.deleteById(aLong);
    }

    public void delete(SCenter sCenter) {
        sCenterDao.delete(sCenter);
    }

    public void deleteAll(Iterable<? extends SCenter> iterable) {
        sCenterDao.deleteAll(iterable);
    }

    public void deleteAll() {
        sCenterDao.deleteAll();
    }
}
