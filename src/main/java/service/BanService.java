package service;

import persistence.repo.BanRepository;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
public class BanService {
    @Inject private BanRepository banRepo;
    @Resource private EJBContext ctx;

    @RolesAllowed({"admin"})
    @Transactional
    public int addBan(){return 1;}

    @RolesAllowed({"admin"})
    @Transactional
    public int removeBan(){return 1;}
}
